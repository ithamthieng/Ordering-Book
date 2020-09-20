import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderBook {
    private Map<String, PriceTree> bidMap;
    private Map<String, PriceTree> askMap;
    private Map<Integer, PriceTree> idToPriceTreeMap;
    private int orderId;
    private int tradeId;

    public OrderBook() {
        clear();
    }

    private void clear() {
        this.bidMap = new HashMap<>();
        this.askMap = new HashMap<>();
        this.idToPriceTreeMap = new HashMap<>();
        this.orderId = 0;
        this.tradeId = 0;
    }

    public List<Trade> addOrder(String instrumentName, SIDE side, double quantity, double price) {
        List<Trade> trades = new ArrayList<>();
        long incomingQuantity = doubleToLong(quantity);
        long incomingPrice = doubleToLong(price);
        if (incomingPrice < 0 || incomingQuantity < 0) {
            throw new IllegalArgumentException();
        }
        PriceTree priceTree = (side == SIDE.BID)
                ? askMap.getOrDefault(instrumentName, new PriceTree())
                : bidMap.getOrDefault(instrumentName, new PriceTree());
        while (priceTree.getOrderNum() > 0
                    && incomingQuantity > 0
                    && ((side == SIDE.BID && priceTree.getMinPrice() <= incomingPrice)
                        || (side == SIDE.ASK && priceTree.getMaxPrice() >= incomingPrice))) {
            OrderList orderList = (side == SIDE.BID)
                    ? priceTree.getOrderList(priceTree.getMinPrice())
                    : priceTree.getOrderList(priceTree.getMaxPrice());
            for (Order currOrder: orderList) {
                if (currOrder.getQuantity() <= incomingQuantity) {
                    incomingQuantity -= currOrder.getQuantity();
                    if (side == SIDE.ASK) {
                        trades.add(new Trade(tradeId++, currOrder.getOrderId(), orderId, instrumentName, side, currOrder.getQuantity(), currOrder.getPrice()));
                    } else {
                        trades.add(new Trade(tradeId++, orderId, currOrder.getOrderId(), instrumentName, side, currOrder.getQuantity(), currOrder.getPrice()));
                    }
                    removeOrder(currOrder.getOrderId());
                    idToPriceTreeMap.remove(currOrder.getOrderId());
                } else {
                    currOrder.setQuantity(currOrder.getQuantity() - incomingQuantity);
                    if (side == SIDE.ASK) {
                        trades.add(new Trade(tradeId++, currOrder.getOrderId(), orderId, instrumentName, side, incomingQuantity, currOrder.getPrice()));
                    } else {
                        trades.add(new Trade(tradeId++, orderId, currOrder.getOrderId(), instrumentName, side, incomingQuantity, currOrder.getPrice()));
                    }
                    incomingQuantity = 0;
                    break;
                }
            }
        }
        if (incomingQuantity > 0) {
            Map<String, PriceTree> map = (side == SIDE.BID) ? bidMap : askMap;
            if (!map.containsKey(instrumentName)) {
                map.put(instrumentName, new PriceTree());
            }
            map.get(instrumentName).addOrder(new Order(orderId, instrumentName, side, incomingQuantity, incomingPrice));
            idToPriceTreeMap.put(orderId, map.get(instrumentName));
        }
        orderId++;
        return trades;
    }

    public void removeOrder(int orderId) {
        if (idToPriceTreeMap.containsKey(orderId)) {
            idToPriceTreeMap.get(orderId).removeOrderById(orderId);
            idToPriceTreeMap.remove(orderId);
        }
    }

    public double getSpread(String instrumentName) {
        if (askMap.containsKey(instrumentName) && bidMap.containsKey(instrumentName)) {
            if (askMap.get(instrumentName).getOrderNum() > 0 && bidMap.get(instrumentName).getOrderNum() > 0) {
                return longToDouble(askMap.get(instrumentName).getMinPrice() - bidMap.get(instrumentName).getMaxPrice());
            }
        }
        throw new IllegalStateException();
    }

    public long getVolumeAtPrice(String instrumentName, double price) {
        long incomingPrice = doubleToLong(price);
        long volumeAtPrice = 0;
        if (askMap.containsKey(instrumentName)) {
            volumeAtPrice += askMap.get(instrumentName).getVolumeAtPrice(incomingPrice);
        }
        if (bidMap.containsKey(instrumentName)) {
            volumeAtPrice += bidMap.get(instrumentName).getVolumeAtPrice(incomingPrice);
        }
        return volumeAtPrice / 100;
    }

    private long doubleToLong(double money) {
        return (long) (money * 100);
    }

    private double longToDouble(long money) {
        return (double) (money) / 100;
    }
}
