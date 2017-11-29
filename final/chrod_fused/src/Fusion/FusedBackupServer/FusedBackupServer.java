package Fusion.FusedBackupServer;

import Fusion.core.AuxNodeFusedBackupServer;
import Fusion.core.FusedNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class FusedBackupServer {
    private ArrayList<LinkedList<AuxNodeFusedBackupServer>> auxLinkedList;
    private ArrayList<FusedNode> dataStack;
    private int numOfPrimaryServer;
    private AtomicInteger[] tos;
    private AtomicInteger dataStackTOS;

    private FusedBackupServer(){}

    public FusedBackupServer(int numberOfPrimaryServer) {
        this.numOfPrimaryServer = numberOfPrimaryServer;
        tos = new AtomicInteger[numberOfPrimaryServer];
        this.auxLinkedList = new ArrayList<LinkedList<AuxNodeFusedBackupServer>>(numberOfPrimaryServer);
        this.dataStack = new ArrayList<FusedNode>();
        for (int i = 0; i < numberOfPrimaryServer; i++) {
            auxLinkedList.set(i, new LinkedList<AuxNodeFusedBackupServer>());
        }
    }

    public void upsert(int serverId, int key, int newValue, int oldValue) {
        LinkedList<AuxNodeFusedBackupServer> auxNodeFusedBackupServerLinkedList = auxLinkedList.get(serverId);
        AuxNodeFusedBackupServer auxNodeFusedBackupServer = checkAuxListContainsKey(auxNodeFusedBackupServerLinkedList, key);
        if(auxNodeFusedBackupServer != null) {
            FusedNode existedFusedNode = auxNodeFusedBackupServer.getFusedNode();
            existedFusedNode.updateCode(oldValue, newValue);
        } else {
            FusedNode fusedNode = null;
            int tosAuxList = getTOSOf(serverId).incrementAndGet();
            if(tosAuxList < dataStack.size()) {
                fusedNode = dataStack.get(tosAuxList);
            } else {
                fusedNode = new FusedNode(numOfPrimaryServer);
                dataStack.add(fusedNode);
            }
            fusedNode.updateCode(oldValue, newValue);
            fusedNode.increaseRefCount();
            AuxNodeFusedBackupServer auxNodeFusedBackupServer1 = new AuxNodeFusedBackupServer(serverId, key);
            fusedNode.setAuxNode(serverId, auxNodeFusedBackupServer1);
            auxNodeFusedBackupServerLinkedList.add(auxNodeFusedBackupServer1);
        }
    }

    public void delete(int serverId, int key, int deleteValue, int endValue) {
        LinkedList<AuxNodeFusedBackupServer> auxNodeFusedBackupServerLinkedList = auxLinkedList.get(serverId);
        AuxNodeFusedBackupServer auxNodeFusedBackupServer = checkAuxListContainsKey(auxNodeFusedBackupServerLinkedList, key);
        if(auxNodeFusedBackupServer == null) {
            return;
        }
        FusedNode tosFusedNode = dataStack.get(getTOSOf(serverId).getAndDecrement());
        auxNodeFusedBackupServerLinkedList.remove(auxNodeFusedBackupServer);
        FusedNode fusedNode = auxNodeFusedBackupServer.getFusedNode();
        fusedNode.updateCode(deleteValue, endValue);
        tosFusedNode.updateCode(endValue, 0);
        tosFusedNode.decreaseRefCount();
        tosFusedNode.getAuxNode(serverId).setFusedNode(fusedNode);
        if(tosFusedNode.isFusedEmpty()) {
            dataStack.remove(dataStack.size() - 1);
        }
    }

    private AtomicInteger getTOSOf(int serverId) {
        if(tos[serverId] == null) {
            tos[serverId] = new AtomicInteger(0);
        }
        return tos[serverId];
    }

    private AtomicInteger getTOSOfDataStack() {
        if(dataStackTOS == null) {
            dataStackTOS = new AtomicInteger(0);
        }
        return dataStackTOS;
    }


    private AuxNodeFusedBackupServer checkAuxListContainsKey(LinkedList<AuxNodeFusedBackupServer> auxNodeFusedBackupServerLinkedList, final int key) {
        Optional<AuxNodeFusedBackupServer> resultOpt = auxNodeFusedBackupServerLinkedList.stream().filter(auxNode -> auxNode.getKey() == key)
                .findFirst();
        if(resultOpt.isPresent()) {
            return resultOpt.get();
        } else {
            return null;
        }
    }
}
