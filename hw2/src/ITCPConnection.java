
public interface ITCPConnection extends Runnable {
    void sendTCPMessage(String message);
    void sendCommand(ServerCommand serverCommand);
}
