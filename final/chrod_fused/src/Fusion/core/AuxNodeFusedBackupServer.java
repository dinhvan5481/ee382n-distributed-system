package Fusion.core;

public class AuxNodeFusedBackupServer {
    private int serverId;
    private int key;
    private FusedNode fusedNode;

    private AuxNodeFusedBackupServer() {}
    public AuxNodeFusedBackupServer(int serverId, int key) {
        this.serverId = serverId;
        this.key = key;
    }

    public FusedNode getFusedNode() {
        return fusedNode;
    }

    public void setFusedNode(FusedNode fusedNode) {
        this.fusedNode = fusedNode;
    }

    public int getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        return serverId + serverId;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AuxNodeFusedBackupServer) {
            AuxNodeFusedBackupServer obj2 = (AuxNodeFusedBackupServer)obj;
            if(this.serverId == obj2.serverId && this.key == obj2.key) {
                return true;
            }
        }
        return false;
    }
}
