package ut.ee382n.ds.client;

import ut.ee382n.ds.core.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClientProtocol extends ClientProtocol {
    private Socket tcpSocket;
    private DataOutputStream tcpOutputStream;
    private BufferedReader tcpInputStream;

    private Logger logger;

    public TCPClientProtocol(InetAddress address, int port) throws IOException {
        super(address, port);
        this.tcpSocket = new Socket(serverAddress, port);
        this.tcpOutputStream = new DataOutputStream(this.tcpSocket.getOutputStream());
        this.tcpInputStream = new BufferedReader(new InputStreamReader(this.tcpSocket.getInputStream()));

        logger = new Logger(Logger.LOG_LEVEL.DEBUG);
    }

    public String sendMessageAndReceiveResponse(String message) {
        boolean sendToServerResult = sendMessage(message);
        if(!sendToServerResult) {
            return "Error while sendind message to server";
        }

        String serverReply = "";
        try {
            serverReply = tcpInputStream.readLine();
            logger.log(Logger.LOG_LEVEL.DEBUG, String.format("Received from server: %s", serverReply));
        } catch (IOException e) {
            logger.log(Logger.LOG_LEVEL.INFO, String.format("Error while reading response from TCP"));
            e.printStackTrace();
            serverReply = "Error while waiting response from server";
        } finally {
            return serverReply;
        }
    }

    private boolean sendMessage(String message) {
        try {
            tcpOutputStream.writeBytes(message);
        } catch (IOException e) {
            System.out.println("Error while sending TCP message to " + serverAddress + " at port " + port);
            System.out.println();
            return false;
        }
        return true;
    }

}