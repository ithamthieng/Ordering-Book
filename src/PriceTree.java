import java.util.HashMap;
import java.util.TreeMap;

public class PriceTree {
    private TreeMap<Long, OrderList> priceToOrderTree;
    private HashMap<Long, OrderList> priceToOrderMap;
    private HashMap<Integer, OrderList> idToOrderMap;
    private int orderNum;

    public PriceTree() {
        clear();
    }

    public void clear() {
        this.priceToOrderTree = new TreeMap<>();
        this.priceToOrderMap = new HashMap<>();
        this.idToOrderMap = new HashMap<>();
        this.orderNum = 0;
    }

    public void addOrder(Order order) {
        if (!priceToOrderMap.containsKey(order)){
            OrderList orderList = new OrderList(order.getPrice());
            priceToOrderTree.put(order.getPrice(), orderList);
            priceToOrderMap.put(order.getPrice(), orderList);
        }
        OrderList orderList = priceToOrderMap.get(order.getPrice());
        orderList.add(order);
        idToOrderMap.put(order.getOrderId(), orderList);
        orderNum++;
    }

    public void removeOrderById(int orderId) {
        if (idToOrderMap.containsKey(orderId)) {
            OrderList orderList = idToOrderMap.get(orderId);
            orderList.deleteById(orderId);
            idToOrderMap.remove(orderId);
            orderNum--;
            if (orderList.size() == 0) {
                priceToOrderTree.remove(orderList.getPrice());
                priceToOrderMap.remove(orderList.getPrice());
            }
        }
    }

    public long getMinPrice() {
        if (orderNum > 0)
            return priceToOrderTree.firstKey();
        throw new IllegalStateException();
    }

    public long getMaxPrice() {
        if (orderNum > 0)
            return priceToOrderTree.lastKey();
        throw new IllegalStateException();
    }

    public OrderList getOrderList(long price) {
        if (priceToOrderMap.containsKey(price))
            return priceToOrderMap.get(price);
        return null;
    }

    public long getVolumeAtPrice(long price) {
        return priceToOrderMap.getOrDefault(price, new OrderList(price)).getVolume();
    }

    public int getOrderNum() {
        return orderNum;
    }
}
