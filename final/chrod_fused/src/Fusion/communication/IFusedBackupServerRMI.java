package Fusion.communication;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IFusedBackupServerRMI extends Remote {
    void upsert(int serverId, int key, int newValue, int oldValue) throws RemoteException;
    void delete(int serverId, int key, int deleteValue, int tosValue) throws RemoteException;
    List<Integer> retrieveAuxList(int serverId) throws RemoteException;
}
