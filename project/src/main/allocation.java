import java.util.List;
import java.util.logging.Logger;
import org.hibernate.Session;

public class InventoryAllocation {
    private static final Logger logger = Logger.getLogger(InventoryAllocation.class.getName());

    public static void allocateInventory(Session session, String strategy) {
        logger.info("Starting inventory allocation with strategy: " + strategy);

        // 割り当て対象の注文を取得
        List<Order> orders = session.createQuery("FROM Order WHERE allocated = false", Order.class).list();

        for (Order order : orders) {
            logger.info(String.format("Processing order %d with item code %s and quantity %d", order.getId(), order.getItemCode(), order.getQuantity()));

            // 在庫を取得
            List<Inventory> inventories = session.createQuery("FROM Inventory WHERE itemCode = :itemCode ORDER BY id", Inventory.class)
                    .setParameter("itemCode", order.getItemCode())
                    .list();

            // 割り当て戦略に応じて在庫割り当てを実行
            switch (strategy) {
                case "FIFO":
                    allocateFifo(session, order, inventories);
                    break;
                case "LIFO":
                    allocateLifo(session, order, inventories);
                    break;
                case "AVERAGE":
                    allocateAverage(session, order, inventories);
                    break;
                case "SPECIFIC":
                    allocateSpecific(session, order, inventories);
                    break;
                case "TOTAL_AVERAGE":
                    allocateTotalAverage(session, order, inventories);
                    break;
                case "MOVING_AVERAGE":
                    allocateMovingAverage(session, order, inventories);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown allocation strategy: " + strategy);
            }

            logger.info("Allocation completed for order " + order.getId());
            order.setAllocated(true);
        }

        session.flush();
        logger.info("Inventory allocation completed");
    }

    private static void allocateFifo(Session session, Order order, List<Inventory> inventories) {
        int remainingQuantity = order.getQuantity();
        for (Inventory inventory : inventories) {
            if (remainingQuantity <= 0) {
                break;
            }
            int allocatedQuantity = Math.min(remainingQuantity, inventory.getQuantity());
            remainingQuantity -= allocatedQuantity;
            inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
            createAllocationResult(session, order, allocatedQuantity, allocatedQuantity * inventory.getUnitPrice());
        }
    }

    private static void allocateLifo(Session session, Order order, List<Inventory> inventories) {
        int remainingQuantity = order.getQuantity();
        for (int i = inventories.size() - 1; i >= 0; i--) {
            Inventory inventory = inventories.get(i);
            if (remainingQuantity <= 0) {
                break;
            }
            int allocatedQuantity = Math.min(remainingQuantity, inventory.getQuantity());
            remainingQuantity -= allocatedQuantity;
            inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
            createAllocationResult(session, order, allocatedQuantity, allocatedQuantity * inventory.getUnitPrice());
        }
    }

    private static void allocateAverage(Session session, Order order, List<Inventory> inventories) {
        int totalQuantity = inventories.stream().mapToInt(Inventory::getQuantity).sum();
        double totalPrice = inventories.stream().mapToDouble(inv -> inv.getQuantity() * inv.getUnitPrice()).sum();
        double averagePrice = totalQuantity > 0 ? totalPrice / totalQuantity : 0;
        logger.info("Calculated average price: " + averagePrice);

        int remainingQuantity = order.getQuantity();
        double totalAllocatedPrice = 0;
        for (Inventory inventory : inventories) {
            if (remainingQuantity <= 0) {
                break;
            }
            int allocatedQuantity = Math.min(remainingQuantity, inventory.getQuantity());
            remainingQuantity -= allocatedQuantity;
            inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
            totalAllocatedPrice += allocatedQuantity * averagePrice;
        }

        createAllocationResult(session, order, order.getQuantity(), totalAllocatedPrice);
    }

    private static void allocateSpecific(Session session, Order order, List<Inventory> inventories) {
        for (Inventory inventory : inventories) {
            if (inventory.getQuantity() >= order.getQuantity()) {
                int allocatedQuantity = order.getQuantity();
                inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
                createAllocationResult(session, order, allocatedQuantity, allocatedQuantity * inventory.getUnitPrice());
                break;
            }
        }
    }

    private static void allocateTotalAverage(Session session, Order order, List<Inventory> inventories) {
        int totalQuantity = inventories.stream().mapToInt(Inventory::getQuantity).sum();
        double totalPrice = inventories.stream().mapToDouble(inv -> inv.getQuantity() * inv.getUnitPrice()).sum();
        double totalAveragePrice = totalQuantity > 0 ? totalPrice / totalQuantity : 0;
        logger.info("Calculated total average price: " + totalAveragePrice);

        int remainingQuantity = order.getQuantity();
        for (Inventory inventory : inventories) {
            if (remainingQuantity <= 0) {
                break;
            }
            int allocatedQuantity = Math.min(remainingQuantity, inventory.getQuantity());
            remainingQuantity -= allocatedQuantity;
            inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
        }

        createAllocationResult(session, order, order.getQuantity(), order.getQuantity() * totalAveragePrice);
    }

    private static void allocateMovingAverage(Session session, Order order, List<Inventory> inventories) {
        int windowSize = 3;
        double[] prices = new double[windowSize];
        int priceIndex = 0;
        int remainingQuantity = order.getQuantity();
        for (Inventory inventory : inventories) {
            if (remainingQuantity <= 0) {
                break;
            }
            int allocatedQuantity = Math.min(remainingQuantity, inventory.getQuantity());
            remainingQuantity -= allocatedQuantity;
            inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
            prices[priceIndex] = inventory.getUnitPrice();
            priceIndex = (priceIndex + 1) % windowSize;
            double movingAveragePrice = calculateMovingAverage(prices);
        }

        createAllocationResult(session, order, order.getQuantity(), order.getQuantity() * movingAveragePrice);
    }

    private static double calculateMovingAverage(double[] prices) {
        double sum = 0;
        int count = 0;
        for (double price : prices) {
            if (price > 0) {
                sum += price;
                count++;
            }
        }
        return count > 0 ? sum / count : 0;
    }

    private static void createAllocationResult(Session session, Order order, int allocatedQuantity, double allocatedPrice) {
        AllocationResult allocationResult = new AllocationResult();
        allocationResult.setOrderId(order.getId());
        allocationResult.setAllocatedQuantity(allocatedQuantity);
        allocationResult.setAllocatedPrice(allocatedPrice);
        session.save(allocationResult);
        logger.info(String.format("Created allocation result for order %d with allocated quantity %d and price %.2f", order.getId(), allocatedQuantity, allocatedPrice));
    }
}
