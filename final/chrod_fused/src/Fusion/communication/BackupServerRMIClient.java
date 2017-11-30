package Fusion.communication;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BackupServerRMIClient {
    private Registry registry;
    private IFusedBackupServerRMI stub;

    public BackupServerRMIClient() throws RemoteException {
        registry = LocateRegistry.getRegistry();
    }

    public void connectToServer() {
        try {
            stub = (IFusedBackupServerRMI) registry.lookup("b2/fused");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg() {
        try {
            stub.printMsg("hello from client");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
