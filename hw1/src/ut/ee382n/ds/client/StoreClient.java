package ut.ee382n.ds.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class StoreClient implements IStoreClient {

    ClientProtocol udpClient;
    ClientProtocol tcpClient;
    InetAddress serverAddress;
    ProtocolEnum protocolType;
    int port;

    public enum ProtocolEnum {
        TCP,
        UDP
    }

    public StoreClient(InetAddress address, int port) throws IOException {
        this.serverAddress = address;
        this.port = port;
        this.protocolType = ProtocolEnum.TCP;
        udpClient = new UDPClientProtocol(address, port);
        tcpClient = new TCPClientProtocol(address, port);
    }


    public void setMode(String type) {
        if(type == "U") {
            this.protocolType = ProtocolEnum.UDP;
        } else {
            this.protocolType = ProtocolEnum.TCP;
        }
    }

    public String sendMessageAndReceiveResponse(String message) {
        switch (this.protocolType) {
            case TCP:
                return tcpClient.sendMessageAndReceiveResponse(message);
            case UDP:
                return udpClient.sendMessageAndReceiveResponse(message);
            default:
                return "";
        }
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
                if (tokens[0] == "setmode") {
                    client.setMode(tokens[1]);
                } else if(tokens[0] == "exit") {
                    break;
                } else {
                    System.out.println(client.sendMessageAndReceiveResponse(command));
                }
            }

        } catch (UnknownHostException uhe) {
            System.err.println(uhe);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Cannot create TCP/UDP client protocol");
        }
    }

}