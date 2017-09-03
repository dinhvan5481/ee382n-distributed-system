package ut.ee382n.ds.core.client;

import java.io.IOException;
import java.net.*;

public class UDPClientProtocol extends ClientProtocol {

    DatagramSocket datagramSocket;
    byte[] buffer;

    private final int BUFFER_SIZE = 1024;

    public UDPClientProtocol(InetAddress address, int port) {
        super(address, port);

        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException se) {
            System.err.println(se);
        }

        buffer = new byte[BUFFER_SIZE];
    }

    public String sendMessageAndReceiveResponse(String message) {
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), this.serverAddress, this.port);
        String receivedData = null;
        try {
            datagramSocket.send(packet);

            DatagramPacket receivedPacket = new DatagramPacket(buffer, BUFFER_SIZE);
            datagramSocket.receive(receivedPacket);
            receivedData = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

        } catch (IOException ioe){
            System.err.println(ioe);
        }

        return receivedData;
    }

}