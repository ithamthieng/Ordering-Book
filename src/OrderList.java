import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class OrderList implements Iterable<Order> {
    private long price;
    private long volume;
    private Order head;
    private Order tail;
    private Order last;
    private Map<Integer, Order> idToOrderMap;
    private int size;

    public OrderList(long price) {
        clear();
        this.price = price;
    }

    public void clear() {
        this.price = 0;
        this.volume = 0;
        this.head = null;
        this.tail = null;
        this.last = null;
        this.idToOrderMap = new HashMap<>();
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public long getPrice() {
        return price;
    }

    public long getVolume() {
        return volume;
    }

    public void add(Order order) {
        idToOrderMap.put(order.getOrderId(), order);
        if (size == 0) {
            order.setPrev(null);
            order.setNext(null);
            head = order;
            tail = order;
        } else {
            tail.setNext(order);
            order.setPrev(tail);
            order.setNext(null);
            tail = order;
        }
        volume += order.getQuantity();
    }

    public void delete(Order order) {
        if (idToOrderMap.containsKey(order.getOrderId())) {
            idToOrderMap.remove(order.getOrderId());
            Order prevOrder = order.getPrev();
            Order nextOrder = order.getNext();
            if (prevOrder != null && nextOrder != null) {
                prevOrder.setNext(nextOrder);
                nextOrder.setPrev(prevOrder);
            } else if (prevOrder != null) {
                prevOrder.setNext(null);
                tail = prevOrder;
            } else if (nextOrder != null) {
                nextOrder.setPrev(null);
                head = nextOrder;
            }
            volume -= order.getQuantity();
        }
    }

    public void deleteById(int orderId) {
        if (idToOrderMap.containsKey(orderId)) {
            delete(idToOrderMap.get(orderId));
        }
    }

    @Override
    public Iterator<Order> iterator() {
        Iterator<Order> iterator = new Iterator<Order>() {
            @Override
            public boolean hasNext() {
                return last != null;
            }

            @Override
            public Order next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Order order = last;
                last = last.getNext();
                return order;
            }
        };
        last = head;
        return iterator;
    }
}
