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
        try {
            stub = (IFusedBackupServerRMI) registry.lookup(serverRMIID + "/fused");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
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
