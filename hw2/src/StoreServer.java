import java.io.IOException;

public class StoreServer {

    private static final int UDP_PORT = 2040;
    private static final int TCP_WELCOME_PORT = 2050;

    public static void main(String[] args) {

        String inventoryFile = "inventory.txt";

        OnlineStore store = OnlineStore.createOnlineStoreFromFile(inventoryFile);

        if (store == null) {
            return;
        }

        StoreUDPListener udpHandler = new StoreUDPListener(UDP_PORT, store);

        StoreServerTCPListener tcpHandler;
        try {
            tcpHandler = new StoreServerTCPListener(TCP_WELCOME_PORT, store);
        } catch (IOException e) {
            System.out.println("Cannot initialize TCP Handler. Exit store");
            e.printStackTrace();
            return;
        }

        Thread udpHandlerThread = new Thread(udpHandler);
        Thread tcpHandlerThread = new Thread(tcpHandler);
        try {
            udpHandlerThread.start();
        } catch (Exception e) {
            System.out.println("Cannot run store UDP handler thread. Exit store");
            e.printStackTrace();
            return;
        }

        try {
            tcpHandlerThread.start();
        } catch (Exception e) {
            System.out.println("Cannot run store TCP handler thread. Exit store");
            e.printStackTrace();
            udpHandlerThread.interrupt();
            return;
        }

        try {
            udpHandlerThread.join();
            tcpHandlerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}