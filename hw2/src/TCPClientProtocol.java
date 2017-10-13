import Server.Utils.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class  TCPClientProtocol{


    private int serverId;
    private ArrayList<String> servers;
    private Socket tcpSocket;
    private PrintStream tcpOutputStream;
    private Scanner tcpInputStream;
    private final int TIME_OUT = 100;

    private Logger logger;

    public TCPClientProtocol(ArrayList<String> servers) {
        this.servers = servers;
        serverId = 0;
        logger = new Logger(Logger.LOG_LEVEL.DEBUG);
    }

    private void ensureActiveConnection() {
        serverId = 0;
        while (tcpSocket == null) {
            String[] serverInfo = servers.get(serverId).split(":");
            long currentTimeMillis = System.currentTimeMillis();
            while (System.currentTimeMillis() - currentTimeMillis < TIME_OUT) {
                try {
                    InetAddress serverAddress = InetAddress.getByName(serverInfo[0]);
                    int port = Integer.parseInt(serverInfo[1]);
                    tcpSocket = new Socket(serverAddress, port);
                    tcpOutputStream = new PrintStream(this.tcpSocket.getOutputStream());
                    tcpInputStream = new Scanner(this.tcpSocket.getInputStream());
                } catch (IOException ioe) {
                    System.err.printf("Error connecting to: %s:%s\n", serverInfo);
                    System.err.println(ioe);
                    clearConnection();
                }
            }
            serverId = ++serverId % servers.size();
        }
    }

    private void clearConnection() {
        tcpSocket = null;
        tcpOutputStream = null;
        tcpInputStream = null;
    }

    public String sendMessageAndReceiveResponse(String message) {
        String response = null;

        while (response == null) {
            ensureActiveConnection();
            sendMessage(message);

            try {
                response = tcpInputStream.nextLine();
            } catch (NoSuchElementException nse) {
                System.err.println(nse);
                clearConnection();
            } catch (IllegalStateException ise) {
                System.err.println(ise);
                clearConnection();
            }
        }

        return response;
    }

    public void sendMessage(String message) {
        tcpOutputStream.println(message);
        tcpOutputStream.flush();
    }

}