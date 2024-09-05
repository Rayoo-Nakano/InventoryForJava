package inventory;

public class Inventory {
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
