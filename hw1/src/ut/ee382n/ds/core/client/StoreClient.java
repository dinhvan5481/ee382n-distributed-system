

public class StoreClient implements IStoreClient {

    ClientProtocol protocol = new TCPClientProtocol();

    public void setMode(String mode) {
        switch (mode) {
            case "U":
                protocol = new UDPClientProtocol();
                break;
            case "T":
                protocol = new TCPClientProtocol();
                break;
            default:
                throw new Exception("Unknown protocol:" + mode);
        }
    }

    public String purchase(String username, String productName, int quantity){

    }

    public String cancel(int orderId) {

    }

    public String search(String userName) {

    }

    public String list() {

    }

}