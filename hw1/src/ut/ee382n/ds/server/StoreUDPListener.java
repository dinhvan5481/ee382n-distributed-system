package ut.ee382n.ds.server;

import ut.ee382n.ds.core.Helper;

import java.net.*;
import java.io.*;

public class StoreUDPListener implements Runnable {

    private final int BUFFER_SIZE = 1024;
    private final int PORT;
    private final OnlineStore store;

    public StoreUDPListener(int port, OnlineStore store) {
        PORT = port;
        this.store = store;
    }

    public void run() {
        DatagramPacket dataPacket;
        System.out.printf("[UDP] Listening on port %d\n", PORT);
        try {
            DatagramSocket dataSocket = new DatagramSocket(PORT);
            byte[] buffer = new byte[BUFFER_SIZE];
            while (true) {
                dataPacket = new DatagramPacket(buffer, BUFFER_SIZE);
                dataSocket.receive(dataPacket);

                String command = new String(dataPacket.getData(), 0, dataPacket.getLength());
                String result = Helper.parseServerInput(store, command);
                DatagramPacket response = new DatagramPacket(result.getBytes(), result.length(), dataPacket.getAddress(), dataPacket.getPort());
                dataSocket.send(response);
            }
        } catch (SocketException e) {
            System.err.println(e);
        } catch (IOException ioe) {
            System.err.println(ioe);
        }

    }
}