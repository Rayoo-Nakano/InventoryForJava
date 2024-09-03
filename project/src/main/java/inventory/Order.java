public class Order {
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
