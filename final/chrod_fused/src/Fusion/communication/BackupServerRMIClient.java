package Fusion.communication;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BackupServerRMIClient {
    private String serverRMIID;
    private Registry registry;
    private IFusedBackupServerRMI stub;

    public BackupServerRMIClient(String serverRMIID) throws RemoteException {
        this.serverRMIID = serverRMIID;
        registry = LocateRegistry.getRegistry();
    }

    public void connectToServer() {
        System.out.println("Try connecting to server " + serverRMIID);
        try {
            stub = (IFusedBackupServerRMI) registry.lookup(serverRMIID + "/fused");
        } catch (RemoteException e) {
            System.out.println("Fail to connect to server " + serverRMIID);
            e.printStackTrace();
        } catch (NotBoundException e) {
            System.out.println("Fail to connect to server " + serverRMIID);
            e.printStackTrace();
        }
    }

    public IFusedBackupServerRMI getRMIClient() {
        return stub;
    }

    public String getServerRMIID() {
        return serverRMIID;
    }
}
