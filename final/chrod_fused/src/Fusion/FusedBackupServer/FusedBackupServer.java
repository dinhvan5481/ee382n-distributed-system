package Fusion.FusedBackupServer;

import Fusion.core.AuxNodeFusedBackupServer;

import java.util.ArrayList;
import java.util.LinkedList;

public class FusedBackupServer<T> {
    private ArrayList<LinkedList<AuxNodeFusedBackupServer>> auxLinkedList;
    private int numOfPrimaryServer;

    private FusedBackupServer(){}

    public FusedBackupServer(int numberOfPrimaryServer) {
        this.numOfPrimaryServer = numberOfPrimaryServer;
        this.auxLinkedList = new ArrayList<LinkedList<AuxNodeFusedBackupServer>>(numberOfPrimaryServer);
        for (int i = 0; i < numberOfPrimaryServer; i++) {
            auxLinkedList.set(i, new LinkedList<AuxNodeFusedBackupServer>());
        }
    }

    public void insert(int serverIndex, int key, T newValue, T oldValue) {

    }
}
