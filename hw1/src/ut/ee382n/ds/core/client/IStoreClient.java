

public interface IStoreClient {

    String purchase(String username, String productName, int quantity);

    String cancel(int orderId);

    String search(String userName);

    String list();
}