@FunctionalInterface
public interface OrderStatusListener {
    void onStatusChange(Order order,OrderStatus oldStatus,OrderStatus newStatus);
}
