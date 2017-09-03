package ut.ee382n.ds.core.client;

import java.net.*;

public abstract class ClientProtocol implements IStoreClient {

    protected int port;
    protected InetAddress serverAddress;

    public ClientProtocol(InetAddress address, int port) {
        this.port = port;
        this.serverAddress = address;
    }

    public abstract String sendMessageAndReceiveResponse(String message);

}