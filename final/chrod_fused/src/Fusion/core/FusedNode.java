package Fusion.core;

import java.util.ArrayList;
import java.util.List;

public class FusedNode<T> {
    private List<AuxNode> auxNodes;
    private T value;
    private int numOfPrimaryServer;

    public FusedNode(int numOfPrimaryServer) {
        auxNodes = new ArrayList<AuxNode>(numOfPrimaryServer);
        this.numOfPrimaryServer = numOfPrimaryServer;
    }

    public void updateCode(T oldValue, T newValue) {
        // TODO: added RS code here
        this.value = newValue;
    }
}
