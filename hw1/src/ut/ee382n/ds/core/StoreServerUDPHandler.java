package ut.ee382n.ds.core;

import java.net.*;
import java.io.*;

public class StoreServerUDPHandler implements Runnable {

    private final int BUFFER_SIZE = 1024;
    private final int PORT;

    public StoreServerUDPHandler(int port, OnlineStore store) {
        PORT = port;
    }

    public void run() {
        DatagramPacket dataPacket;
        try {
            DatagramSocket dataSocket = new DatagramSocket(PORT);
            byte[] buffer = new byte[BUFFER_SIZE];
            while (true) {
                dataPacket = new DatagramPacket(buffer, BUFFER_SIZE);
                dataSocket.receive(dataPacket);

                String command = new String(dataPacket.getData());
                // TODO add parser and handler

            }

        } catch (SocketException e) {
            System.err.println(e);
        }

    }
}