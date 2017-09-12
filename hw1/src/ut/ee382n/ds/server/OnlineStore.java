package ut.ee382n.ds.server;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by dinhvan5481 on 8/29/17.
 */
public class OnlineStore {
    private HashMap<Integer, StoreItem> inventory;
    private ConcurrentHashMap<Integer, StoreOrder> orders;
    private AtomicInteger storeOrderId;

    public OnlineStore() {
        inventory = new HashMap<>();
        orders = new ConcurrentHashMap<>();
        storeOrderId = new AtomicInteger(1);
    }

    public String placeOrder(String username, String itemName, int qty) {
        if(!this.inventory.containsKey(itemName.hashCode())) {
            return "Not Available - We do not sell this product\n";
        }
        StoreItem purchasedItem = getItemInfo(itemName).placeOrder(qty);
        if(purchasedItem == null) {
            return "Not Available - Not enough items\n";

        }
        int orderId = storeOrderId.getAndIncrement();
        orders.put(orderId, new StoreOrder(orderId, username, purchasedItem));
        return String.format("Your order has been placed, %d %s %s %d\n", orderId, username, itemName, qty);
    }

    public String cancelOrder(int orderId) {
        StoreOrder order = orders.get(orderId);
        if(order == null || order.getStatus() == StoreOrder.ORDER_STATUS.CANCELLED) {
            return String.format("%d not found, no such order\n", orderId);
        }
        order.cancel();
        getItemInfo(order.getItem().getName()).cancelOrder(order.getItem().getQty());
        return String.format("Order %d is cancelled\n", orderId);
    }

    public String search(String userName) {
        Collection<StoreOrder> ordereds = orders.values();
        Collection<StoreOrder> searchOrders = ordereds.parallelStream()
                .filter((o) -> o.getUserName().equals(userName) && o.getStatus() != StoreOrder.ORDER_STATUS.CANCELLED)
                .collect(Collectors.toList());
        if(searchOrders.size() == 0) {
            return String.format("No order found for %s\n", userName);
        }
        StringBuilder sb = new StringBuilder();
        for(StoreOrder order: searchOrders) {
            sb.append(String.format("%s\n", order.toString()));
        }
        return sb.toString();
    }

    public static OnlineStore createOnlineStoreFromFile(String fileName) {
        try {
            Scanner fileIn = new Scanner(new BufferedInputStream(new FileInputStream(fileName)));
            OnlineStore store = new OnlineStore();
            String line;
            System.out.println("Loading inventory... ");

            while (fileIn.hasNextLine()) {
                line = fileIn.nextLine();
                System.out.println(line);
                String[] tokens = line.split(" ");
                String itemName = tokens[0];
                int quantity = Integer.parseInt(tokens[1]);

                StoreItem newItem = new StoreItem(itemName, quantity);
                store.inventory.put(newItem.hashCode(), newItem);
            }

            return store;
        } catch (FileNotFoundException fne){
            System.err.println(fne);
            return null;
        }
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
