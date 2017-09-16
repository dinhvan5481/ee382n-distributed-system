
/**
 * Created by dinhvan5481 on 8/29/17.
 */
public class Helper {
    public static String parseServerInput(OnlineStore store, String input) {
        String[] tokens = input.split(" ");
        String command = tokens[0];
        String userName, productName;

        switch (command) {
            case "purchase":
                if (tokens.length != 4) {
                    return "Usage:\npurchase <user-name> <product-name> <quantity>\n";
                }
                userName = tokens[1];
                productName = tokens[2];
                int quantity = Integer.parseInt(tokens[3]);
                return store.placeOrder(userName, productName, quantity);
            case "cancel":
                if (tokens.length != 2) {
                    return "Usage:\ncancel <order-id>\n";
                }
                int orderId = Integer.parseInt(tokens[1]);
                return store.cancelOrder(orderId);
            case "search":
                if (tokens.length != 2) {
                    return "Usage:\nsearch <user-name>\n";
                }
                userName = tokens[1];
                return store.search(userName);
            case "list":
                return store.toString();
            default:
                return String.format("Unknown command %s\n", command);
        }
    }
}
