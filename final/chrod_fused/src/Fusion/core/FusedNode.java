package Fusion.core;

import java.util.ArrayList;
import java.util.List;

public class FusedNode<T> {
    private List<AuxNodeFusedBackupServer> auxNodes;
    private int key;
    private T value;
    private int numOfPrimaryServer;
    private int refCount;

    public FusedNode(int numOfPrimaryServer,int key) {
        this.key = key;
        auxNodes = new ArrayList<AuxNodeFusedBackupServer>(numOfPrimaryServer);
        this.numOfPrimaryServer = numOfPrimaryServer;
        refCount = 0;
    }

    public void updateCode(T oldValue, T newValue) {
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

    @Override
    public int hashCode() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FusedNode<?>) {
            if(((FusedNode<?>)obj).key == this.key) {
                return true;
            }
        }
        return false;
    }
}
