package Fusion.core;

public class PrimaryNode<T> {
    private int key;
    private T value;
    private AuxNodePrimaryServer auxNode;

    private PrimaryNode(){}
    public PrimaryNode(int key, T value) {
        this.key = key;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public AuxNodePrimaryServer getAuxNode() {
        return auxNode;
    }

    public void setAuxNode(AuxNodePrimaryServer auxNode) {
        this.auxNode = auxNode;
    }
}
