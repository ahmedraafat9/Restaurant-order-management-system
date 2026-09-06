import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Restaurant restaurant = new Restaurant();



    public static void main(String[] args) {

        loadSampleMenu();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> handleAddMenuItem();
                case 2 -> handleRemoveMenuItem();
                case 3 -> restaurant.displayMenu();
                case 4 -> handleSearchMenuItem();
                case 5 -> handleCreateOrder();
                case 6 -> handleAddItemToOrder();
                case 7 -> handleRemoveItemFromOrder();
                case 8 -> handleDisplayOrder();
                case 9 -> handleAddOrderToKitchenQueue();
                case 10 -> restaurant.processNextOrder();
                case 11 -> handleSearchOrder();
                case 12 -> handleCheckOrderStatus();
                case 13 -> restaurant.displayCompletedOrders();
                case 14 -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please select 1-14.");
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void registerStatusListener() {
        restaurant.registerStatusListener((order, oldStatus, newStatus) ->
                System.out.println("order " + order.getOrderId() + ":" + oldStatus +" -> " + newStatus));
    }

    private static void printMenu() {
        System.out.println("========= RESTAURANT ORDER MANAGER =========");
        System.out.println(" 1. Add Menu Item");
        System.out.println(" 2. Remove Menu Item");
        System.out.println(" 3. Display Menu");
        System.out.println(" 4. Search Menu Item");
        System.out.println(" 5. Create Order");
        System.out.println(" 6. Add Item to Order");
        System.out.println(" 7. Remove Item from Order");
        System.out.println(" 8. Display Order");
        System.out.println(" 9. Add Order to Kitchen Queue");
        System.out.println("10. Process Next Order");
        System.out.println("11. Search Order");
        System.out.println("12. Check Order Status");
        System.out.println("13. Display Completed Orders");
        System.out.println("14. Exit");
        System.out.println("==============================================");
    }

    private static void handleAddMenuItem() {
        int id = readInt("Enter item ID: ");
        String name = readLine("Enter item name: ");
        double price = readDouble();
        String category = readLine("Enter item category: ");
        restaurant.addMenuItem(id, name, price, category);
    }

    private static void handleRemoveMenuItem() {
        int id = readInt("Enter item ID to remove: ");
        restaurant.removeMenuItem(id);
    }

    private static void handleSearchMenuItem() {
        int id = readInt("Enter item ID to search: ");
        restaurant.searchMenuItem(id);
    }

    private static void handleCreateOrder() {
        int orderId = readInt("Enter new order ID: ");
        String customerName = readLine("Enter customer name: ");
        restaurant.createOrder(orderId, customerName);
    }

    private static void handleAddItemToOrder() {
        int orderId = readInt("Enter order ID: ");
        int menuItemId = readInt("Enter menu item ID: ");
        int quantity = readInt("Enter quantity: ");
        restaurant.addItemToOrder(orderId, menuItemId, quantity);
    }

    private static void handleRemoveItemFromOrder() {
        int orderId = readInt("Enter order ID: ");
        int menuItemId = readInt("Enter menu item ID to remove: ");
        restaurant.removeItemFromOrder(orderId, menuItemId);
    }

    private static void handleDisplayOrder() {
        int orderId = readInt("Enter order ID: ");
        restaurant.displayOrder(orderId);
    }

    private static void handleAddOrderToKitchenQueue() {
        int orderId = readInt("Enter order ID to send to kitchen: ");
        restaurant.addOrderToKitchenQueue(orderId);
    }

    private static void handleSearchOrder() {
        int orderId = readInt("Enter order ID to search: ");
        restaurant.searchOrder(orderId);
    }

    private static void handleCheckOrderStatus() {
        int orderId = readInt("Enter order ID: ");
        restaurant.checkOrderStatus(orderId);
    }

    //==============================================================================

    private static void loadSampleMenu() {
        restaurant.addMenuItem(1, "Burger", 150, "Main Course");
        restaurant.addMenuItem(2, "Pizza", 200, "Main Course");
        restaurant.addMenuItem(3, "Pasta", 180, "Main Course");
        restaurant.addMenuItem(4, "Cola", 40, "Drinks");
        System.out.println();
    }

    //================================================================================


    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    private static double readDouble() {
        while (true) {
            System.out.print("Enter item price: ");
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

}
