import java.util.List;
import java.util.logging.Logger;
import org.hibernate.Session;

public class InventoryAllocation {
    private static final Logger logger = Logger.getLogger(InventoryAllocation.class.getName());

    public static void allocateInventory(Session session, String strategy) {
        logger.info("Starting inventory allocation with strategy: " + strategy);



        session.flush();
        logger.info("Inventory allocation completed");

    private static void allocateFifo(Session session, List<Inventory> inventories) {
        for (Inventory inventory : inventories) {
            if (remainingQuantity <= 0) {
                break;
            }
            int allocatedQuantity = Math.min(remainingQuantity, inventory.getQuantity());
            remainingQuantity -= allocatedQuantity;
            inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
            createAllocationResult(session, allocatedQuantity, allocatedQuantity * inventory.getUnitPrice());
        }
    }

    private static void allocateLifo(Session session, List<Inventory> inventories) {

        for (int i = inventories.size() - 1; i >= 0; i--) {
            Inventory inventory = inventories.get(i);
            if (remainingQuantity <= 0) {
                break;
            }
            int allocatedQuantity = Math.min(remainingQuantity, inventory.getQuantity());
            remainingQuantity -= allocatedQuantity;
            inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
            createAllocationResult(session, allocatedQuantity, allocatedQuantity * inventory.getUnitPrice());
        }
    }

    private static void allocateAverage(Session session, List<Inventory> inventories) {
        int totalQuantity = inventories.stream().mapToInt(Inventory::getQuantity).sum();
        double totalPrice = inventories.stream().mapToDouble(inv -> inv.getQuantity() * inv.getUnitPrice()).sum();
        double averagePrice = totalQuantity > 0 ? totalPrice / totalQuantity : 0;
        logger.info("Calculated average price: " + averagePrice);


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

        createAllocationResult(session, totalAllocatedPrice);
    }

     private static void allocateTotalAverage(Session session, List<Inventory> inventories) {
        int totalQuantity = inventories.stream().mapToInt(Inventory::getQuantity).sum();
        double totalPrice = inventories.stream().mapToDouble(inv -> inv.getQuantity() * inv.getUnitPrice()).sum();
        double totalAveragePrice = totalQuantity > 0 ? totalPrice / totalQuantity : 0;
        logger.info("Calculated total average price: " + totalAveragePrice);

        for (Inventory inventory : inventories) {
            if (remainingQuantity <= 0) {
                break;
            }
            int allocatedQuantity = Math.min(remainingQuantity, inventory.getQuantity());
            remainingQuantity -= allocatedQuantity;
            inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
        }

    }

    private static void allocateMovingAverage(Session session, List<Inventory> inventories) {
        int windowSize = 3;
        double[] prices = new double[windowSize];
        int priceIndex = 0;

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

    private static void createAllocationResult(Session session, int allocatedQuantity, double allocatedPrice) {
        AllocationResult allocationResult = new AllocationResult();

        allocationResult.setAllocatedQuantity(allocatedQuantity);
        allocationResult.setAllocatedPrice(allocatedPrice);
        session.save(allocationResult);
        logger.info(String.format("Created allocation result for order %d with allocated quantity %d and price %.2f", order.getId(), allocatedQuantity, allocatedPrice));
    }
}
