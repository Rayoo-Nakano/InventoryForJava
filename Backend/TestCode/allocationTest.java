import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InventoryAllocationTest {
    private Session mockSession;

    @Before
    public void setUp() {
        mockSession = mock(Session.class);
    }

    @Test
    public void testAllocateInventory_FIFO() {
        Order order = new Order(1, "ITEM001", 5, false);
        Inventory inventory1 = new Inventory(1, "ITEM001", 3, 5.0);
        Inventory inventory2 = new Inventory(2, "ITEM001", 2, 5.0);
        List<Order> orders = List.of(order);
        List<Inventory> inventories = List.of(inventory1, inventory2);

        when(mockSession.createQuery("FROM Order WHERE allocated = false", Order.class)).thenReturn(mockQuery(orders));
        when(mockSession.createQuery("FROM Inventory WHERE itemCode = :itemCode ORDER BY id", Inventory.class)).thenReturn(mockQuery(inventories));

        InventoryAllocation.allocateInventory(mockSession, "FIFO");

        verifyAllocationResults(order, 5, 25.0);
        verifyInventoryQuantities(inventories, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllocateInventory_UnknownStrategy() {
        InventoryAllocation.allocateInventory(mockSession, "UNKNOWN");
    }

    // 其他测试用例...

    private <T> Query<T> mockQuery(List<T> results) {
        Query<T> mockQuery = mock(Query.class);
        when(mockQuery.list()).thenReturn(results);
        return mockQuery;
    }

    private void verifyAllocationResults(Order order, int allocatedQuantity, double allocatedPrice) {
        ArgumentCaptor<AllocationResult> captor = ArgumentCaptor.forClass(AllocationResult.class);
        verify(mockSession, times(1)).save(captor.capture());
        AllocationResult result = captor.getValue();
        assertEquals(order.getId(), result.getOrderId());
        assertEquals(allocatedQuantity, result.getAllocatedQuantity());
        assertEquals(allocatedPrice, result.getAllocatedPrice(), 0.001);
    }

    private void verifyInventoryQuantities(List<Inventory> inventories, int... quantities) {
        for (int i = 0; i < inventories.size(); i++) {
            assertEquals(quantities[i], inventories.get(i).getQuantity());
        }
    }
}
