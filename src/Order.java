enum SIDE {
    BID, ASK;
}
public class Order {
    private int orderId;
    private String instrumentName;
    private SIDE side;
    private long quantity;
    private long price;
    private Order prev;
    private Order next;

    public Order(int orderId, String instrumentName, SIDE side, long quantity, long price) {
        this.orderId = orderId;
        this.instrumentName = instrumentName;
        this.side = side;
        this.quantity = quantity;
        this.price = price;
        this.prev = null;
        this.next = null;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public SIDE getSide() {
        return side;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public Order getPrev() {
        return prev;
    }

    public void setPrev(Order prev) {
        this.prev = prev;
    }

    public Order getNext() {
        return next;
    }

    public void setNext(Order next) {
        this.next = next;
    }


}
