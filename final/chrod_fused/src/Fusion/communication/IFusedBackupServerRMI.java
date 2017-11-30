package Fusion.communication;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFusedBackupServerRMI extends Remote {
    void printMsg(String msg) throws RemoteException;
}
