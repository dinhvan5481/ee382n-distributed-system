package Fusion.FusedBackupServer;

import Fusion.core.AuxNodeFusedBackupServer;
import Fusion.core.FusedNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class FusedBackupServer<T> {
    private ArrayList<LinkedList<AuxNodeFusedBackupServer>> auxLinkedList;
    private Stack<FusedBackupServer<T>> dataStack;
    private int numOfPrimaryServer;
    private ArrayList<Integer> tos;

    private FusedBackupServer(){}

    public FusedBackupServer(int numberOfPrimaryServer) {
        this.numOfPrimaryServer = numberOfPrimaryServer;
        tos = new ArrayList<Integer>(numberOfPrimaryServer);
        this.auxLinkedList = new ArrayList<LinkedList<AuxNodeFusedBackupServer>>(numberOfPrimaryServer);
        this.dataStack = new Stack<FusedBackupServer<T>>();
        for (int i = 0; i < numberOfPrimaryServer; i++) {
            auxLinkedList.set(i, new LinkedList<AuxNodeFusedBackupServer>());
            tos.set(i, 0);
        }
    }

    public void insert(int serverIndex, int key, T newValue, T oldValue) {
        LinkedList<AuxNodeFusedBackupServer> auxNodeFusedBackupServerLinkedList = auxLinkedList.get(serverIndex);
        FusedNode<T> fusedNode = new FusedNode<T>(numOfPrimaryServer, key);
        AuxNodeFusedBackupServer auxNodeFusedBackupServer = new AuxNodeFusedBackupServer(fusedNode);
        if(auxNodeFusedBackupServerLinkedList.contains(auxNodeFusedBackupServer)) {
            int fusedIndex = auxNodeFusedBackupServerLinkedList.indexOf(auxNodeFusedBackupServer);
            FusedNode<T> existedFusedNode = auxNodeFusedBackupServerLinkedList.get(fusedIndex).getFusedNode();
            existedFusedNode.updateCode(oldValue, newValue);
        } else {

        }
    }
}
