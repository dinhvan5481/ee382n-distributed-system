package ut.ee382n.ds.core;

public class StoreOrder {

    private int orderId;
    private String userName;
    private StoreItem item;
    private ORDER_STATUS status;



    public static enum ORDER_STATUS {
        ORDERED,
        CANCELLED
    };

    public StoreOrder(int orderId, String userName, StoreItem item) {
        this.orderId = orderId;
        this.userName = userName;
        this.item = item;
        this.status = ORDER_STATUS.ORDERED;
    }

    public StoreItem getItem() {
        return item;
    }

    public String getUserName() {
        return userName;
    }

    public long getOrderId() {
        return orderId;
    }

    public void cancel() {
        this.status = ORDER_STATUS.CANCELLED;
    }

    public ORDER_STATUS getStatus() {
        return status;
    }

    @Override
    public int hashCode() {
        return orderId;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }

        if(obj == null || obj.getClass() != getClass()) {
            return false;
        }
        return orderId == ((StoreOrder)obj).orderId;
    }

    @Override
    public String toString() {
        return String.format("%d, %s, %d", orderId, item.getName(), item.getQty());
    }
}
