package Fusion.core;

import java.util.ArrayList;
import java.util.List;

public class FusedNode {
    protected List<AuxNodeFusedBackupServer> auxNodes;
    protected int value;
    protected int numOfPrimaryServer;
    protected int refCount;

    public FusedNode(int numOfPrimaryServer) {
        auxNodes = new ArrayList<>(numOfPrimaryServer);
        this.numOfPrimaryServer = numOfPrimaryServer;
        refCount = 0;
    }

    public void updateCode(int oldValue, int newValue) {
        // TODO: added RS code here
        this.value = newValue;
    }

    public void setAuxNode(int serverId, AuxNodeFusedBackupServer auxNode) {
        auxNodes.set(serverId, auxNode);
    }

    public AuxNodeFusedBackupServer getAuxNode(int serverId) {
        return  auxNodes.get(serverId);
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
