package ut.ee382n.ds.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Created by dinhvan5481 on 8/29/17.
 */
public class OnlineStore {
    private HashMap<Integer, StoreItem> inventory;
    private ConcurrentHashMap<Integer, StoreOrder> orders;
    private AtomicInteger storeOrderId;

    public OnlineStore() {
        inventory = new HashMap<Integer, StoreItem>();
        orders = new ConcurrentHashMap<Integer, StoreOrder>();
        storeOrderId = new AtomicInteger(0);
    }

    public String placeOrder(String username, String itemName, int qty) {
        if(!this.inventory.containsKey(itemName.hashCode())) {
            return "`Not Available - We do not sell this product";
        }
        StoreItem purchasedItem = getItemInfo(itemName).placeOrder(qty);
        if(purchasedItem == null) {
            return "`Not Available - Not enough items";
        }
        int orderId = storeOrderId.getAndIncrement();
        orders.put(orderId, new StoreOrder(orderId, username, purchasedItem));
        return String.format("`Your order has been placed, %d %s %s %d", orderId, username, itemName, qty);
    }

    public String cancelOrder(int orderId) {
        StoreOrder order = orders.get(orderId);
        if(order == null || order.getStatus() == StoreOrder.ORDER_STATUS.CANCELLED) {
            return String.format("%d not found, no such order'.", orderId);
        }
        order.cancel();
        getItemInfo(order.getItem().getName()).cancelOrder(order.getItem().getQty());
        return String.format("Order % is cancelled", orderId);
    }

    public String search(String userName) {
        Collection<StoreOrder> ordereds = orders.values();
        Collection<StoreOrder> searchOrders = ordereds.parallelStream()
                .filter((o) -> o.getUserName() == userName && o.getStatus() != StoreOrder.ORDER_STATUS.CANCELLED)
                .collect(Collectors.toList());
        if(searchOrders.size() < 1) {
            return String.format("No order found for %s", userName);
        }
        StringBuilder sb = new StringBuilder();
        for(StoreOrder order: searchOrders) {
            sb.append(String.format("%s\n", order.toString()));
        }
        return sb.toString();
    }

    public static OnlineStore createOnlineStoreFromFile(String fileName) {
        // process files, and load items
        return new OnlineStore();
    }

    private StoreItem getItemInfo(String itemName) {
        return inventory.get(itemName.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(StoreItem item: inventory.values()) {
            sb.append(String.format("%s\n", item.toString()));
        }
        return sb.toString();
    }


}
