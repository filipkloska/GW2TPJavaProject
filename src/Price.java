public class Price
{
    private int id;
    private PriceDetail buys;
    private PriceDetail sells;

    Price(PriceDetail buys, PriceDetail sells)
    {
        this.buys = buys;
        this.sells = sells;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PriceDetail getBuys() {
        return buys;
    }

    public void setBuys(PriceDetail buys) {
        this.buys = buys;
    }

    public PriceDetail getSells() {
        return sells;
    }

    public void setSells(PriceDetail sells) {
        this.sells = sells;
    }
}
