import java.util.ArrayList;

public class Order {

    private int orderId;
    private String customerName;
    private ArrayList<OrderItem> items;
    private double total;
    private OrderStatus status;

    public Order(int orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.total = 0.0;
        this.status = OrderStatus.PENDING;
    }


    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    // I can add items if the order is not completed or cancelled
    public boolean isModifiable() {
        return status != OrderStatus.COMPLETED && status != OrderStatus.CANCELLED;
    }

    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
    }

    public double calculateTotal() {
        double sum = 0.0;
        for (OrderItem item : items) {
            sum+= item.calculateSubtotal();
        }
        this.total = sum;
        return this.total;
    }


    public boolean addItem(MenuItem menuItem, int quantity) {
        if (!isModifiable()) {
            System.out.println("Cannot add items. Order " + orderId + " is " + status);
            return false;
        }
        items.add(new OrderItem(menuItem, quantity));
        calculateTotal();
        return true;
    }


    public boolean removeItem(int menuItemId) {
        if (!isModifiable()) {
            System.out.println("Cannot remove items. Order " + orderId + " is " + status);
            return false;
        }
        for (int i = 0; i < items.size(); i++) {
            if ((items.get(i).getItem().getId() == menuItemId)) {
                items.remove(i);
                calculateTotal();
                return true;
            }
        }
        System.out.println("Item with id " + menuItemId + " not found in order " + orderId);
        return false;
    }


    public void displayOrder() {
        System.out.println("================================");
        System.out.println("Order : " + orderId);
        System.out.println("Customer Name : " + customerName);
        System.out.println("Status : " + status);

        if(items.isEmpty()) {
            System.out.println("No items in order");
        }else  {
            System.out.println("Items in order: ");
            for(OrderItem item : items) {
                System.out.println("  - " + item );
            }
        }

        System.out.println("Total : " + calculateTotal());
        System.out.println("================================");
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerName='" + customerName + '\'' +
                ", total=" + total +
                ", status=" + status +
                '}';
    }
}
