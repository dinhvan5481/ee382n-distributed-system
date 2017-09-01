package ut.ee382n.ds.core;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class StoreServer {

    private final int UDP_PORT = 2040;

    private ExecutorService threadPool;

    public static void main(String[] args) {

        StoreServerUDPHandler udpHandler = new StoreServerUDPHandler();
        // TODO: TCP Handler

        Thread udpHandlerThread = new Thread(udpHandler);
        try {
            udpHandlerThread.start();
            this.join(udpHandlerThread);
        } finally {

        }
    }
}