import java.sql.Timestamp;

public class ItemRecord {

    private final Timestamp timestamp;
    private final int supplyPrice;
    private final int demandPrice;
    private final int supplyQuantity;
    private final int demandQuantity;

    public ItemRecord(Timestamp timestamp, int supplyPrice, int supplyQuantity, int demandPrice, int demandQuantity) {
        this.timestamp = timestamp;
        this.supplyPrice = supplyPrice;
        this.demandPrice = demandPrice;
        this.supplyQuantity = supplyQuantity;
        this.demandQuantity = demandQuantity;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getSupplyPrice() {
        return supplyPrice;
    }

    public int getDemandPrice() {
        return demandPrice;
    }

    public int getSupplyQuantity() {
        return supplyQuantity;
    }

    public int getDemandQuantity() {
        return demandQuantity;
    }
}