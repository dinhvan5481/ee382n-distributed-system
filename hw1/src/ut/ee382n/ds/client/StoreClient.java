package ut.ee382n.ds.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class StoreClient implements IStoreClient {

    ClientProtocol protocol;
    InetAddress serverAddress;
    int port;

    public StoreClient(InetAddress address, int port) {
        this.serverAddress = address;
        this.port = port;
        setMode("U");
    }


    public void setMode(String mode) {
        // TODO close current connection and change protocol.
        switch (mode) {
            case "U":
                protocol = new UDPClientProtocol(serverAddress, port);
                break;
            case "T":
                protocol = new TCPClientProtocol(serverAddress, port);
                break;
        }
    }

    public String sendMessageAndReceiveResponse(String message) {
        return protocol.sendMessageAndReceiveResponse(message);
    }

    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 2040;

        Scanner stdin = new Scanner(System.in);
        StoreClient client;

        try {
            InetAddress address = InetAddress.getByName(hostname);
            client = new StoreClient(address, port);

            while(true) {
                String command = stdin.nextLine();
                if (command.length() == 0) continue;

                String[] tokens = command.split(" ");
                if (tokens[0] == "setMode") {
                    client.setMode(tokens[1]);
                } else {
                    System.out.println(client.sendMessageAndReceiveResponse(command));
                }
            }

        } catch (UnknownHostException uhe) {
            System.err.println(uhe);
        }
    }

}