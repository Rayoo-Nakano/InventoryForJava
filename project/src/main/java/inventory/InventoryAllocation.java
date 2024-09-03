package inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class InventoryAllocation {
    public static final Logger logger = Logger.getLogger(InventoryAllocation.class.getName());

    public static void main(String[] args) {
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(new Inventory(1, "A001", 10, 100.0));
        inventories.add(new Inventory(2, "A001", 5, 120.0));
        inventories.add(new Inventory(3, "A001", 8, 110.0));
        inventories.add(new Inventory(4, "B001", 15, 80.0));
        inventories.add(new Inventory(5, "B001", 20, 90.0));

        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1, "A001", 15));
        orders.add(new Order(2, "B001", 25));

        String strategy = "FIFO";

        try {
            allocateInventory(inventories, orders, strategy);
        } catch (IllegalArgumentException e) {
            logger.severe("エラー: 無効な割り当て戦略が指定されました。" + e.getMessage());
        }
    }

    public static void allocateInventory(List<Inventory> inventories, List<Order> orders, String strategy) {
        logger.info("在庫割り当てを開始します。戦略: " + strategy);

        for (Order order : orders) {
            logger.info("注文 " + order.getId() + " (商品コード: " + order.getItemCode() + ", 数量: " + order.getQuantity() + ") を処理中...");

            List<Inventory> allocatedInventories;
            switch (strategy) {
                case "FIFO":
                    allocatedInventories = allocateFifo(inventories, order);
                    break;
                // 他の戦略の処理は省略
                default:
                    throw new IllegalArgumentException("無効な割り当て戦略が指定されました: " + strategy);
            }

            if (allocatedInventories.stream().mapToInt(Inventory::getQuantity).sum() == order.getQuantity()) {
                logger.info("注文 " + order.getId() + " の割り当てが完了しました。");
            } else {
                logger.warning("注文 " + order.getId() + " の割り当てが不完全です。");
            }
        }

        logger.info("在庫割り当てが完了しました。");
    }

    private static List<Inventory> allocateFifo(List<Inventory> inventories, Order order) {
        List<Inventory> allocatedInventories = new ArrayList<>();
        int remainingQuantity = order.getQuantity();

        List<Inventory> sortedInventories = inventories.stream()
                .filter(inv -> inv.getItemCode().equals(order.getItemCode()))
                .sorted((inv1, inv2) -> Integer.compare(inv1.getId(), inv2.getId()))
                .collect(Collectors.toList());

        for (Inventory inventory : sortedInventories) {
            if (remainingQuantity > 0) {
                int allocatedQuantity = Math.min(remainingQuantity, inventory.getQuantity());
                remainingQuantity -= allocatedQuantity;
                inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
                logger.info("注文 " + order.getId() + " に在庫 " + inventory.getId() + " (商品コード: " + inventory.getItemCode() + ", 数量: " + allocatedQuantity + ", 単価: " + inventory.getUnitPrice() + ") を割り当てました。");
                allocatedInventories.add(inventory);
            }
        }

        return allocatedInventories;
    }
}
