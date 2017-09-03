package ut.ee382n.ds.core;

import java.io.File;
import java.util.concurrent.*;

public class StoreServer {

    private static final int UDP_PORT = 2040;

    private ExecutorService threadPool;

    public static void main(String[] args) {

        String inventoryFile = "inventory.txt";

        OnlineStore store = OnlineStore.createOnlineStoreFromFile(inventoryFile);

        if (store == null) {
            return;
        }

        StoreServerUDPHandler udpHandler = new StoreServerUDPHandler(UDP_PORT, store);
        // TODO: TCP Handler

        Thread udpHandlerThread = new Thread(udpHandler);
        try {
            udpHandlerThread.start();
        } finally {

        }
    }
}