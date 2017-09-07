package ut.ee382n.ds.server;

public class StoreItem {
    private String name;
    private int qty;
    public StoreItem(String name, int qty) {
        this.name = name;
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public int getQty() {
        return qty;
    }

    public synchronized StoreItem placeOrder(int qty) {
        if(qty > this.qty) {
            return null;
        }
        this.qty -= qty;
        return createOrderItem(qty);
    }

    public synchronized void cancelOrder(int qty) {
        this.qty += qty;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null || obj.getClass() != getClass()) {
            return false;
        }
        StoreItem obj2 = (StoreItem)obj;
        return obj2.equals(this.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s %d", name, qty);
    }

    private StoreItem createOrderItem(int qty) {
        return new StoreItem(name, qty);
    }
}
