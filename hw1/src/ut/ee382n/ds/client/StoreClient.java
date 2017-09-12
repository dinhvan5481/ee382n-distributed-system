package ut.ee382n.ds.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class StoreClient implements IStoreClient {

    ClientProtocol udpClient;
    ClientProtocol tcpClient;
    ClientProtocol protocolChosen;
    InetAddress serverAddress;
    int udpPort;
    int tcpPort;

    public StoreClient(InetAddress address, int udpPort, int tcpPort) throws IOException {
        this.serverAddress = address;
        this.udpPort = udpPort;
        this.tcpPort = tcpPort;
        udpClient = new UDPClientProtocol(address, udpPort);
        tcpClient = new TCPClientProtocol(address, tcpPort);
        protocolChosen = tcpClient;
    }


    public void setMode(String type) {
        if(type.equals("U")) {
            protocolChosen = udpClient;
        } else if (type.equals("T")){
            protocolChosen = tcpClient;
        }
    }

    public String sendMessageAndReceiveResponse(String message) {
        return protocolChosen.sendMessageAndReceiveResponse(message);
    }

    public static void main(String[] args) {
        String hostname = "localhost";
        final int UDP_PORT = 2040;
        final int TCP_PORT = 2050;

        Scanner stdin = new Scanner(System.in);
        StoreClient client;

        try {
            InetAddress address = InetAddress.getByName(hostname);
            client = new StoreClient(address, UDP_PORT, TCP_PORT);

            while(true) {
                String command = stdin.nextLine();
                if (command.length() == 0) continue;

                String[] tokens = command.split(" ");
                if (tokens[0].equals("setmode")) {
                    client.setMode(tokens[1]);
                } else if(tokens[0].equals("exit")) {
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