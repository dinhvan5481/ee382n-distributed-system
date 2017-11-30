package Fusion.communication;

import java.rmi.RemoteException;

public class FusedBackupServer implements IFusedBackupServerRMI {
    @Override
    public void printMsg(String msg) throws RemoteException {
        System.out.println("Hello " + msg);
    }
}
