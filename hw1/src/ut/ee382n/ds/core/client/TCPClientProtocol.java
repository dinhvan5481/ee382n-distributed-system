package ut.ee382n.ds.core.client;

import java.net.InetAddress;

public class TCPClientProtocol extends ClientProtocol {

    public TCPClientProtocol(InetAddress address, int port) {
        super(address, port);

    }

    public String sendMessageAndReceiveResponse(String message) {
        return "";
    }

}