package Fusion.communication;

import Fusion.FusedBackupServer.FusedBackupServer;
import com.sun.tools.corba.se.idl.InvalidArgument;

import java.rmi.RemoteException;
import java.util.List;

public class FusedBackupServerRMI implements IFusedBackupServerRMI {
    private FusedBackupServer fusedBackupServer;

    public FusedBackupServerRMI() {}

    public FusedBackupServerRMI(int numberOfBackupServers, List<Integer> coeff) throws InvalidArgument {
        fusedBackupServer = new FusedBackupServer(numberOfBackupServers, coeff);
    }

    @Override
    public void upsert(int serverId, int key, int newValue, int oldValue) throws RemoteException {
        System.out.println(String.format("Upsert: receive from server id %d: key - %d, new value - %d, old value - %d",
                serverId, key, newValue, oldValue));
        fusedBackupServer.upsert(serverId, key, newValue, oldValue);
    }

    @Override
    public void delete(int serverId, int key, int deleteValue, int endValue) throws RemoteException {
        System.out.println(String.format("Delete: receive from server id %d: key - %d, new value - %d, old value - %d",
                serverId, key, deleteValue, endValue));
        fusedBackupServer.delete(serverId, key, deleteValue, endValue);
    }

    @Override
    public List<Integer> retrieveAuxList(int serverId) {
        return fusedBackupServer.retrieveFusedLinkedList(serverId);
    }
}
