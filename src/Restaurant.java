import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Restaurant {

    private ArrayList<MenuItem> menu;
    private LinkedList<Order> kitchenQueue;
    private HashMap<Integer, Order> orders;
    private LinkedHashMap<Integer, Order> completedOrders;

    private List<OrderStatusListener> statusListeners;



    public Restaurant() {
        menu = new ArrayList<>();
        kitchenQueue = new LinkedList<>();
        orders = new HashMap<>();
        completedOrders = new LinkedHashMap<>();
        statusListeners = new ArrayList<>();
    }


    public void registerStatusListener(OrderStatusListener listener) {
        statusListeners.add(listener);
    }

    private void changeStatus(Order order, OrderStatus newStatus) {
        OrderStatus oldStatus = order.updateStatus(newStatus);
        statusListeners.forEach(listener -> listener.onStatusChange(order,oldStatus,newStatus));
    }

//    public MenuItem findMenuItem(int id) {
//        for (MenuItem item : menu) {
//            if (item.getId() == id) {
//                return item;
//            }
//        }
//        return null;
//    }

    // 1- Add Menu Item
    public boolean addMenuItem(int id, String name, double price, String category) {
//        if (findMenuItem(id) != null) {
//            System.out.println("A menu item already exists!");
//            return false;
//        }
        boolean alreadyExists = menu.stream().anyMatch(item -> item.getId() == id);
        if (alreadyExists) {
            System.out.println("the menu item already exists");
            return false;
        }
        menu.add(new MenuItem(id, name, price, category));
        System.out.println("Menu item added!");
        return true;
    }

    // 2- Remove Menu Item
    public boolean removeMenuItem(int id) {
//        MenuItem itemToRemove = findMenuItem(id);
//        if (itemToRemove == null) {
//            System.out.println("No menu item found!");
//            return false;
//        }
        boolean itemToRemove = menu.removeIf(item -> item.getId() == id);
        if (itemToRemove) {
            System.out.println("Menu item removed!");
        }else {
            System.out.println("Menu item not found!");
        }
        return itemToRemove;
    }

    // 3- Display Menu
    public void displayMenu() {
//        if (menu.isEmpty()) {
//            System.out.println("No menu items found!");
//            return;
//        }
//        System.out.println("Menu items: ");
//        for (MenuItem item : menu) {
//            System.out.println(item);
//        }
        List<MenuItem> sortedMenu = menu.stream()
                .sorted(Comparator.comparingInt(MenuItem::getId))
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("Menu items: ");
        sortedMenu.forEach(System.out::println);
    }

    public Optional<MenuItem> searchMenu (Predicate<MenuItem> predicate) {
        return menu.stream().filter(predicate).findFirst();
    }
    public Optional<MenuItem> findMenuItem(int id) {
        return searchMenu(item -> item.getId() == id);
    }

    // 4- Search Menu Item
    public void searchMenuItem(int id) {
//        MenuItem itemToSearch = findMenuItem(id);
        Optional<MenuItem> result = findMenuItem(id);
        if (result.isPresent()) {
            System.out.println("Menu item found! "+ result.get());
        }else {
            System.out.println("Menu item not found!");
        }
//        if (itemToSearch == null) {
//            System.out.println("No menu item found!");
//        }else  {
//            System.out.println("Menu item found!");
//        }
    }


    // 5- Create Order
    public boolean createOrder(int orderId, String customerName) {
        if(orders.containsKey(orderId)) {
            System.out.println("Order already exists!");
            return false;
        }
        Order order = new Order(orderId, customerName);
        orders.put(orderId, order);
        System.out.println("Order with id " + orderId + " created for " +  customerName + "  (PENDING)");
        return true;
    }


    // 6- Add Item to Order
    public boolean addItemToOrder(int orderId, int menuItemId, int quantity) {
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order with ID " + orderId + " not found.");
            return false;
        }
//        MenuItem menuItem = findMenuItem(menuItemId);
        Optional<MenuItem> menuItemOptional = findMenuItem(menuItemId);
        if (!menuItemOptional.isPresent()) {
            System.out.println("Menu item with ID " + menuItemId + " does not exist.");
            return false;
        }
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return false;
        }
        boolean added = order.addItem(menuItemOptional.get(), quantity);
        if (added) {
            System.out.println("Added " + quantity + " x " + menuItemOptional.get().getName() + " to order " + orderId );
        }
        return added;
    }


    // 7- Remove Item from Order
    public boolean removeItemFromOrder(int orderId, int menuItemId) {
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order with ID " + orderId + " not found.");
            return false;
        }
        return order.removeItem(menuItemId);
    }

    // 8- Display Order
    public void displayOrder(int orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order with ID " + orderId + " not found.");
            return;
        }
        order.displayOrder();
    }


    // 9- Add Order to Kitchen Queue
    public boolean addOrderToKitchenQueue(int orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("order with id " + orderId + " not found");
            return false;
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            System.out.println("order " + orderId + " cannot be queued because it is " + order.getStatus() );
            return false;
        }
//        if (kitchenQueue.contains(order)) {
//            System.out.println("order " + orderId + " is already in the kitchen queue");
//            return false;
//        }
        boolean aleadyQueued = kitchenQueue.stream().anyMatch( i -> i.getOrderId() == orderId);
        if (aleadyQueued) {
            System.out.println("order with id " + orderId + " is already queued" );
            return false;
        }
//        order.updateStatus(OrderStatus.IN_KITCHEN);
        changeStatus(order, OrderStatus.IN_KITCHEN);
        kitchenQueue.addLast(order);
        System.out.println("order " + orderId + " added to the kitchen queue (IN KITCHEN)");
        return true;
    }

    // 10- Process Next Order
    public boolean processNextOrder() {
        if (kitchenQueue.isEmpty()) {
            System.out.println("The kitchen queue is empty. Nothing to process.");
            return false;
        }

        Order order = kitchenQueue.removeFirst();
//        order.updateStatus(OrderStatus.COMPLETED);
        changeStatus(order, OrderStatus.COMPLETED);
        completedOrders.put(order.getOrderId(), order);
        System.out.println("order " + order.getOrderId() + " processed and marked COMPLETED.");
        return true;
    }

    // 11- Search Order
    public Order searchOrder(int orderId) {
//        Order order = orders.get(orderId);
//        if (order == null) {
//            System.out.println("order with id " + orderId + " not found.");
//        } else {
//            System.out.println("found: " + order);
//        }
//        return order;
        Optional <Order> result = Optional.ofNullable(orders.get(orderId));
        if (result.isPresent()) {
            System.out.println("found: " + result.get());
        }else {
            System.out.println("order with id " + orderId + " not found.");
        }
        return result.orElse(null);
    }

    // 12- Check Order Status
    public void checkOrderStatus(int orderId) {
//        Order order = orders.get(orderId);
//        if (order == null) {
//            System.out.println("order with id " + orderId + " not found");
//            return;
//        }
//        System.out.println("order " + orderId + " status: " + order.getStatus());
        Optional<Order> result = Optional.ofNullable(orders.get(orderId));
        if (result.isPresent()) {
            System.out.println("order " + orderId + " status: " + result.get().getStatus());
        } else {
            System.out.println("order with id " + orderId + " not found");
        }
    }

    // 13- Display Completed Orders
    public void displayCompletedOrders() {
//        if (completedOrders.isEmpty()) {
//            System.out.println("no orders have been completed yet");
//            return;
//        }
//        System.out.println("===== COMPLETED ORDERS =====");
//        for (Order order : completedOrders.values()) {
//            System.out.println(order);
//        }
        List<Order> completedList = completedOrders.values().stream()
                .collect(Collectors.toCollection(ArrayList::new));
        ReportGenerator.generateReport("Completed Orders", completedList);
    }
}
