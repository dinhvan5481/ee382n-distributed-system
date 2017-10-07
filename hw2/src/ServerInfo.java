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

    public boolean isMe(int id) {
        return this.id == id;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnlineStatus(boolean status) {
        this.isOnline = status;
    }

}
