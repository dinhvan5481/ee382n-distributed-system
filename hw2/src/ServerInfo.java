import java.net.Inet4Address;
import java.net.InetAddress;

public class ServerInfo {
    private int id;
    private InetAddress ipAddress;
    private int port;
    private boolean isOnline;

    public ServerInfo(int id, InetAddress ipAddress, int port) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
        //TODO: may be wait for intialization process finished
        this.isOnline = true;
    }

    public int getId() {
        return id;
    }

    public boolean isMe(int requestServerId) {
        return this.id == requestServerId;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnlineStatus(boolean status) {
        this.isOnline = status;
    }

    public boolean sendMessage(int requestServerId, String message) {
        if(isMe(requestServerId)) {
            return true;
        }
        return sendTCPMessageToServer(message);
    }

    private boolean sendTCPMessageToServer(String message) {
        return true;
    }

}
