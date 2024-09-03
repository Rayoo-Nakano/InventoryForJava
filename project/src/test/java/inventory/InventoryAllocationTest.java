import inventory.Inventory;
import inventory.Order;
import inventory.InventoryAllocation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class InventoryAllocationTest {

    @Test
    void allocateInventory_FIFO() {
        List<InventoryAllocation.Inventory> inventories = new ArrayList<>();
        inventories.add(new InventoryAllocation.Inventory(1, "A001", 10, 100.0));
        inventories.add(new InventoryAllocation.Inventory(2, "A001", 5, 120.0));
        inventories.add(new InventoryAllocation.Inventory(3, "A001", 8, 110.0));

        List<InventoryAllocation.Order> orders = new ArrayList<>();
        orders.add(new InventoryAllocation.Order(1, "A001", 15));

        InventoryAllocation.allocateInventory(inventories, orders, "FIFO");

        assertEquals(0, inventories.get(0).getQuantity());
        assertEquals(0, inventories.get(1).getQuantity());
        assertEquals(3, inventories.get(2).getQuantity());
    }

    @Test
    void allocateInventory_LIFO() {
        List<InventoryAllocation.Inventory> inventories = new ArrayList<>();
        inventories.add(new InventoryAllocation.Inventory(1, "B001", 15, 80.0));
        inventories.add(new InventoryAllocation.Inventory(2, "B001", 20, 90.0));

        List<InventoryAllocation.Order> orders = new ArrayList<>();
        orders.add(new InventoryAllocation.Order(1, "B001", 25));

        InventoryAllocation.allocateInventory(inventories, orders, "LIFO");

        assertEquals(5, inventories.get(0).getQuantity());
        assertEquals(10, inventories.get(1).getQuantity());
    }

    @Test
    void allocateInventory_Average() {
        List<InventoryAllocation.Inventory> inventories = new ArrayList<>();
        inventories.add(new InventoryAllocation.Inventory(1, "C001", 10, 200.0));
        inventories.add(new InventoryAllocation.Inventory(2, "C001", 20, 150.0));
        inventories.add(new InventoryAllocation.Inventory(3, "C001", 15, 180.0));

        List<InventoryAllocation.Order> orders = new ArrayList<>();
        orders.add(new InventoryAllocation.Order(1, "C001", 30));

        InventoryAllocation.allocateInventory(inventories, orders, "AVERAGE");

        assertEquals(0, inventories.get(0).getQuantity());
        assertEquals(5, inventories.get(1).getQuantity());
        assertEquals(0, inventories.get(2).getQuantity());
    }

    @Test
    void allocateInventory_Specific() {
        List<InventoryAllocation.Inventory> inventories = new ArrayList<>();
        inventories.add(new InventoryAllocation.Inventory(1, "D001", 5, 100.0));
        inventories.add(new InventoryAllocation.Inventory(2, "D001", 20, 120.0));
        inventories.add(new InventoryAllocation.Inventory(3, "D001", 10, 110.0));

        List<InventoryAllocation.Order> orders = new ArrayList<>();
        orders.add(new InventoryAllocation.Order(1, "D001", 15));

        InventoryAllocation.allocateInventory(inventories, orders, "SPECIFIC");

        assertEquals(0, inventories.get(1).getQuantity());
        assertEquals(5, inventories.get(0).getQuantity());
        assertEquals(10, inventories.get(2).getQuantity());
    }

    @Test
    void allocateInventory_TotalAverage() {
        List<InventoryAllocation.Inventory> inventories = new ArrayList<>();
        inventories.add(new InventoryAllocation.Inventory(1, "E001", 10, 200.0));
        inventories.add(new InventoryAllocation.Inventory(2, "E001", 20, 150.0));
        inventories.add(new InventoryAllocation.Inventory(3, "E001", 15, 180.0));

        List<InventoryAllocation.Order> orders = new ArrayList<>();
        orders.add(new InventoryAllocation.Order(1, "E001", 30));

        InventoryAllocation.allocateInventory(inventories, orders, "TOTAL_AVERAGE");

        assertEquals(0, inventories.get(0).getQuantity());
        assertEquals(5, inventories.get(1).getQuantity());
        assertEquals(0, inventories.get(2).getQuantity());
    }

    @Test
    void allocateInventory_MovingAverage() {
        List<InventoryAllocation.Inventory> inventories = new ArrayList<>();
        inventories.add(new InventoryAllocation.Inventory(1, "F001", 10, 200.0));
        inventories.add(new InventoryAllocation.Inventory(2, "F001", 20, 150.0));
        inventories.add(new InventoryAllocation.Inventory(3, "F001", 15, 180.0));
        inventories.add(new InventoryAllocation.Inventory(4, "F001", 5, 250.0));

        List<InventoryAllocation.Order> orders = new ArrayList<>();
        orders.add(new InventoryAllocation.Order(1, "F001", 40));

        InventoryAllocation.allocateInventory(inventories, orders, "MOVING_AVERAGE");

        assertEquals(0, inventories.get(0).getQuantity());
        assertEquals(5, inventories.get(1).getQuantity());
        assertEquals(0, inventories.get(2).getQuantity());
        assertEquals(0, inventories.get(3).getQuantity());
    }

    @Test
    void allocateInventory_InvalidStrategy() {
        List<InventoryAllocation.Inventory> inventories = new ArrayList<>();
        List<InventoryAllocation.Order> orders = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> InventoryAllocation.allocateInventory(inventories, orders, "INVALID"));
    }

    @Test
    void allocateFifo() {
        List<InventoryAllocation.Inventory> inventories = new ArrayList<>();
        inventories.add(new InventoryAllocation.Inventory(1, "A001", 10, 100.0));
        inventories.add(new InventoryAllocation.Inventory(2, "A001", 5, 120.0));
        inventories.add(new InventoryAllocation.Inventory(3, "A001", 8, 110.0));

        InventoryAllocation.Order order = new InventoryAllocation.Order(1, "A001", 15);

        InventoryAllocation.allocateFifo(order, inventories);

        assertEquals(0, inventories.get(0).getQuantity());
        assertEquals(0, inventories.get(1).getQuantity());
        assertEquals(3, inventories.get(2).getQuantity());
    }

    @Test
    void allocateLifo() {
        List<InventoryAllocation.Inventory> inventories = new ArrayList<>();
        inventories.add(new InventoryAllocation.Inventory(1, "B001", 15, 80.0));
        inventories.add(new InventoryAllocation.Inventory(2, "B001", 20, 90.0));

        InventoryAllocation.Order order = new InventoryAllocation.Order(1, "B001", 25);

        InventoryAllocation.allocateLifo(order, inventories);

        assertEquals(5, inventories.get(0).getQuantity());
        assertEquals(10, inventories.get(1).getQuantity());
    }

    @Test
    void allocateAverage() {
        List<InventoryAllocation.Inventory> inventories = new ArrayList<>();
        inventories.add(new InventoryAllocation.Inventory(1, "C001", 10, 200.0));
        inventories.add(new InventoryAllocation.Inventory(2, "C001", 20, 150.0));
        inventories.add(new InventoryAllocation.Inventory(3, "C001", 15, 180.0));

        InventoryAllocation.Order order = new InventoryAllocation.Order(1, "C001", 30);

        InventoryAllocation.allocateAverage(order, inventories);

        assertEquals(0, inventories.get(0).getQuantity());
        assertEquals(5, inventories.get(1).getQuantity());
        assertEquals(0, inventories.get(2).getQuantity());
    }

    @Test
    void allocateSpecific() {
        List<InventoryAllocation.Inventory> inventories = new ArrayList<>();
        inventories.add(new InventoryAllocation.Inventory(1, "D001", 5, 100.0));
        inventories.add(new InventoryAllocation.Inventory(2, "D001", 20, 120.0));
        inventories.add(new InventoryAllocation.Inventory(3, "D001", 10, 110.0));

        InventoryAllocation.Order order = new InventoryAllocation.Order(1, "D001", 15);

        InventoryAllocation.allocateSpecific(order, inventories);

        assertEquals(0, inventories.get(1).getQuantity());
        assertEquals(5, inventories.get(0).getQuantity());
        assertEquals(10, inventories.get(2).getQuantity());
    }

    @Test
    void allocateTotalAverage() {
        List<InventoryAllocation.Inventory> inventories = new ArrayList<>();
        inventories.add(new InventoryAllocation.Inventory(1, "E001", 10, 200.0));
        inventories.add(new InventoryAllocation.Inventory(2, "E001", 20, 150.0));
        inventories.add(new InventoryAllocation.Inventory(3, "E001", 15, 180.0));

        InventoryAllocation.Order order = new InventoryAllocation.Order(1, "E001", 30);

        InventoryAllocation.allocateTotalAverage(order, inventories);

        assertEquals(0, inventories.get(0).getQuantity());
        assertEquals(5, inventories.get(1).getQuantity());
        assertEquals(0, inventories.get(2).getQuantity());
    }

    @Test
    void allocateMovingAverage() {
        List<InventoryAllocation.Inventory> inventories = new ArrayList<>();
        inventories.add(new InventoryAllocation.Inventory(1, "F001", 10, 200.0));
        inventories.add(new InventoryAllocation.Inventory(2, "F001", 20, 150.0));
        inventories.add(new InventoryAllocation.Inventory(3, "F001", 15, 180.0));
        inventories.add(new InventoryAllocation.Inventory(4, "F001", 5, 250.0));

        InventoryAllocation.Order order = new InventoryAllocation.Order(1, "F001", 40);

        InventoryAllocation.allocateMovingAverage(order, inventories);

        assertEquals(0, inventories.get(0).getQuantity());
        assertEquals(5, inventories.get(1).getQuantity());
        assertEquals(0, inventories.get(2).getQuantity());
        assertEquals(0, inventories.get(3).getQuantity());
    }

    @Test
    void calculateMovingAverage() {
        double[] prices = {200.0, 150.0, 180.0};
        double movingAverage = InventoryAllocation.calculateMovingAverage(prices);
        assertEquals(176.67, movingAverage, 0.01);
    }

    @Test
    void getInventoriesByItemCode() {
        List<InventoryAllocation.Inventory> inventories = new ArrayList<>();
        inventories.add(new InventoryAllocation.Inventory(1, "A001", 10, 100.0));
        inventories.add(new InventoryAllocation.Inventory(2, "B001", 15, 80.0));
        inventories.add(new InventoryAllocation.Inventory(3, "A001", 5, 120.0));

        List<InventoryAllocation.Inventory> itemInventories = InventoryAllocation.getInventoriesByItemCode(inventories, "A001");

        assertEquals(2, itemInventories.size());
        assertEquals("A001", itemInventories.get(0).getItemCode());
        assertEquals("A001", itemInventories.get(1).getItemCode());
    }
}
