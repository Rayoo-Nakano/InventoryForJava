import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * 在庫引当プログラム
 * 
 * 在庫情報と注文情報から、在庫を注文に割り当てる処理を行う。
 * 割り当て戦略としてFIFO、LIFO、平均単価、特定在庫、総平均単価、移動平均単価の6種類をサポートする。
 */
public class InventoryAllocation {
    public static final Logger logger = Logger.getLogger(InventoryAllocation.class.getName());

    public static void main(String[] args) {
        // 在庫情報を作成
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(new Inventory(1, "A001", 10, 100.0));
        inventories.add(new Inventory(2, "A001", 5, 120.0));
        inventories.add(new Inventory(3, "A001", 8, 110.0));
        inventories.add(new Inventory(4, "B001", 15, 80.0));
        inventories.add(new Inventory(5, "B001", 20, 90.0));

        // 注文情報を作成
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1, "A001", 15));
        orders.add(new Order(2, "B001", 25));

        // 割り当て戦略を指定
        String strategy = "FIFO";

        try {
            allocateInventory(inventories, orders, strategy);
        } catch (IllegalArgumentException e) {
            logger.severe("エラー: 無効な割り当て戦略が指定されました。" + e.getMessage());
        }
    }

    /**
     * 在庫を注文に割り当てる
     *
     * @param inventories 在庫情報のリスト
     * @param orders      注文情報のリスト
     * @param strategy    割り当て戦略
     */
    public static void allocateInventory(List<Inventory> inventories, List<Order> orders, String strategy) {
        logger.info("在庫割り当てを開始します。戦略: " + strategy);

        for (Order order : orders) {
            logger.info(String.format("注文 %d (商品コード: %s, 数量: %d) を処理中...", order.getId(), order.getItemCode(), order.getQuantity()));

            // 在庫を取得
            List<Inventory> itemInventories = getInventoriesByItemCode(inventories, order.getItemCode());

            // 割り当て戦略に応じて在庫割り当てを実行
            switch (strategy) {
                case "FIFO":
                    allocateFifo(order, itemInventories);
                    break;
                case "LIFO":
                    allocateLifo(order, itemInventories);
                    break;
                case "AVERAGE":
                    allocateAverage(order, itemInventories);
                    break;
                case "SPECIFIC":
                    allocateSpecific(order, itemInventories);
                    break;
                case "TOTAL_AVERAGE":
                    allocateTotalAverage(order, itemInventories);
                    break;
                case "MOVING_AVERAGE":
                    allocateMovingAverage(order, itemInventories);
                    break;
                default:
                    throw new IllegalArgumentException("無効な割り当て戦略が指定されました: " + strategy);
            }

            logger.info("注文 " + order.getId() + " の割り当てが完了しました。");
        }

        logger.info("在庫割り当てが完了しました。");
    }

    private static void allocateFifo(Order order, List<Inventory> itemInventories) {
        int remainingQuantity = order.getQuantity();
        for (Inventory inventory : itemInventories) {
            if (remainingQuantity <= 0) {
                break;
            }
            int allocatedQuantity = Math.min(remainingQuantity, inventory.getQuantity());
            remainingQuantity -= allocatedQuantity;
            inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
            logger.info(String.format("注文 %d に在庫 %d (商品コード: %s, 数量: %d, 単価: %.2f) を割り当てました。", order.getId(), inventory.getId(), inventory.getItemCode(), allocatedQuantity, inventory.getUnitPrice()));
        }
    }

    private static void allocateLifo(Order order, List<Inventory> itemInventories) {
        int remainingQuantity = order.getQuantity();
        for (int i = itemInventories.size() - 1; i >= 0; i--) {
            Inventory inventory = itemInventories.get(i);
            if (remainingQuantity <= 0) {
                break;
            }
            int allocatedQuantity = Math.min(remainingQuantity, inventory.getQuantity());
            remainingQuantity -= allocatedQuantity;
            inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
            logger.info(String.format("注文 %d に在庫 %d (商品コード: %s, 数量: %d, 単価: %.2f) を割り当てました。", order.getId(), inventory.getId(), inventory.getItemCode(), allocatedQuantity, inventory.getUnitPrice()));
        }
    }

    private static void allocateAverage(Order order, List<Inventory> itemInventories) {
        int remainingQuantity = order.getQuantity();
        double totalCost = 0.0;
        int totalAllocatedQuantity = 0;
        for (Inventory inventory : itemInventories) {
            if (remainingQuantity <= 0) {
                break;
            }
            int allocatedQuantity = Math.min(remainingQuantity, inventory.getQuantity());
            remainingQuantity -= allocatedQuantity;
            totalCost += allocatedQuantity * inventory.getUnitPrice();
            totalAllocatedQuantity += allocatedQuantity;
            inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
            logger.info(String.format("注文 %d に在庫 %d (商品コード: %s, 数量: %d, 単価: %.2f) を割り当てました。", order.getId(), inventory.getId(), inventory.getItemCode(), allocatedQuantity, inventory.getUnitPrice()));
        }
        double averagePrice = totalCost / totalAllocatedQuantity;
        logger.info(String.format("注文 %d の平均単価は %.2f です。", order.getId(), averagePrice));
    }

    private static void allocateSpecific(Order order, List<Inventory> itemInventories) {
        int remainingQuantity = order.getQuantity();
        for (Inventory inventory : itemInventories) {
            if (remainingQuantity <= 0) {
                break;
            }
            int allocatedQuantity = Math.min(remainingQuantity, inventory.getQuantity());
            remainingQuantity -= allocatedQuantity;
            inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
            logger.info(String.format("注文 %d に在庫 %d (商品コード: %s, 数量: %d, 単価: %.2f) を割り当てました。", order.getId(), inventory.getId(), inventory.getItemCode(), allocatedQuantity, inventory.getUnitPrice()));
        }
    }

    private static void allocateTotalAverage(Order order, List<Inventory> itemInventories) {
        int remainingQuantity = order.getQuantity();
        double totalCost = 0.0;
        int totalQuantity = 0;
        for (Inventory inventory : itemInventories) {
            totalCost += inventory.getQuantity() * inventory.getUnitPrice();
            totalQuantity += inventory.getQuantity();
        }
        double averagePrice = totalCost / totalQuantity;
        for (Inventory inventory : itemInventories) {
            if (remainingQuantity <= 0) {
                break;
            }
            int allocatedQuantity = Math.min(remainingQuantity, inventory.getQuantity());
            remainingQuantity -= allocatedQuantity;
            inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
            logger.info(String.format("注文 %d に在庫 %d (商品コード: %s, 数量: %d, 単価: %.2f) を割り当てました。", order.getId(), inventory.getId(), inventory.getItemCode(), allocatedQuantity, averagePrice));
        }
    }

    private static void allocateMovingAverage(Order order, List<Inventory> itemInventories) {
        int remainingQuantity = order.getQuantity();
        double[] prices = new double[itemInventories.size()];
        int index = 0;
        for (Inventory inventory : itemInventories) {
            prices[index++] = inventory.getUnitPrice();
        }
        double movingAverage = calculateMovingAverage(prices);
        for (Inventory inventory : itemInventories) {
            if (remainingQuantity <= 0) {
                break;
            }
            int allocatedQuantity = Math.min(remainingQuantity, inventory.getQuantity());
            remainingQuantity -= allocatedQuantity;
            inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
            logger.info(String.format("注文 %d に在庫 %d (商品コード: %s, 数量: %d, 単価: %.2f) を割り当てました。", order.getId(), inventory.getId(), inventory.getItemCode(), allocatedQuantity, movingAverage));
        }
    }

    private static double calculateMovingAverage(double[] prices) {
        double sum = 0.0;
        for (double price : prices) {
            sum += price;
        }
        return sum / prices.length;
    }

    /**
     * 商品コードに一致する在庫情報のリストを取得する
     *
     * @param inventories 在庫情報のリスト
     * @param itemCode    商品コード
     * @return 商品コードに一致する在庫情報のリスト
     */
    public static List<Inventory> getInventoriesByItemCode(List<Inventory> inventories, String itemCode) {
        return inventories.stream()
                .filter(inv -> inv.getItemCode().equals(itemCode))
                .collect(Collectors.toList());
    }
}

class Inventory {
    private int id;
    private String itemCode;
    private int quantity;
    private double unitPrice;

    public Inventory(int id, String itemCode, int quantity, double unitPrice) {
        this.id = id;
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
}

class Order {
    private int id;
    private String itemCode;
    private int quantity;

    public Order(int id, String itemCode, int quantity) {
        this.id = id;
        this.itemCode = itemCode;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public int getQuantity() {
        return quantity;
    }
}
