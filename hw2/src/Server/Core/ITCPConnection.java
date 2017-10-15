package Server.Core;

import Server.Command.Server.ServerCommand;

public interface ITCPConnection extends Runnable {
    void sendTCPMessage(String message);
    void sendCommand(ServerCommand serverCommand);
}
