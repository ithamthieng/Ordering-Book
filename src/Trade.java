
public class Trade {
    private int tradeId;
    private int bidId;
    private int askId;
    private String instrumentName;
    private SIDE side;
    private long quantity;
    private long price;

    public Trade(int tradeId, int bidId, int askId, String instrumentName, SIDE side, long quantity, long price) {
        this.tradeId = tradeId;
        this.bidId = bidId;
        this.askId = askId;
        this.instrumentName = instrumentName;
        this.side = side;
        this.quantity = quantity;
        this.price = price;
    }
}
