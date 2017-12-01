package Fusion.core;

import java.util.ArrayList;
import java.util.List;

public class FusedNode {
    protected AuxNodeFusedBackupServer[] auxNodes;
    protected int value;
    protected int numOfPrimaryServer;
    protected int refCount;

    public FusedNode(int numOfPrimaryServer) {
        auxNodes = new AuxNodeFusedBackupServer[numOfPrimaryServer];
        this.numOfPrimaryServer = numOfPrimaryServer;
        refCount = 0;
    }

    public void updateCode(int oldValue, int newValue) {
        this.value += newValue - oldValue;
    }

    public int getValue() {
        return value;
    }

    public void setAuxNode(int serverId, AuxNodeFusedBackupServer auxNode) {
        auxNodes[serverId] = auxNode;
    }

    public AuxNodeFusedBackupServer getAuxNode(int serverId) {
        return  auxNodes[serverId];
    }

    public void increaseRefCount() {
        this.refCount++;
    }

    public void decreaseRefCount() {
        this.refCount--;
    }

    public int getRefCount() {
        return refCount;
    }

    public boolean isFusedEmpty() {
        return refCount == 0;
    }
}
