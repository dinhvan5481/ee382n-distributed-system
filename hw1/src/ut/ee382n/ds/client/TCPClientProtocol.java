package ut.ee382n.ds.client;

import ut.ee382n.ds.core.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class  TCPClientProtocol extends ClientProtocol {
    private Socket tcpSocket;
    private PrintStream tcpOutputStream;
    private Scanner tcpInputStream;

    private Logger logger;

    public TCPClientProtocol(InetAddress address, int port) throws IOException {
        super(address, port);
        this.tcpSocket = new Socket(serverAddress, port);
        this.tcpOutputStream = new PrintStream(this.tcpSocket.getOutputStream());
        this.tcpInputStream = new Scanner(this.tcpSocket.getInputStream());

        logger = new Logger(Logger.LOG_LEVEL.DEBUG);
    }

    public String sendMessageAndReceiveResponse(String message) {
        boolean sendToServerResult = sendMessage(message);
        if(!sendToServerResult) {
            return "Error while sending message to server";
        }

        StringBuilder sb = new StringBuilder();
        while (tcpInputStream.hasNextLine()) {
            String line = tcpInputStream.nextLine();
            if(line.length() == 0) {
                break;
            }
            sb.append(line);
            sb.append("\n");
        }
        return sb.toString();
    }

    private boolean sendMessage(String message) {
//        try {
            tcpOutputStream.println(message);
            tcpOutputStream.flush();
//        } catch (IOException e) {
//            System.out.println("Error while sending TCP message to " + serverAddress + " at port " + port);
//            System.out.println();
//            return false;
//        }
        return true;
    }

}