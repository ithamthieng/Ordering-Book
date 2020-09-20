public class Main {

    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();
        orderBook.addOrder("GOOG", SIDE.BID, 100, 100.00);
        orderBook.addOrder("GOOG", SIDE.BID, 100, 99.00);
        orderBook.addOrder("GOOG", SIDE.BID, 100, 98.00);
        orderBook.addOrder("GOOG", SIDE.BID, 100, 97.00);
        orderBook.addOrder("GOOG", SIDE.ASK, 300, 98.99);
        orderBook.getSpread("GOOG");
        orderBook.getVolumeAtPrice("GOOG", 100.00);
    }
}
