public class PriceDetail {
    private int quantity;
    private int unit_price;

    PriceDetail(int quantity, int unit_price)
    {
        this.quantity = quantity;
        this.unit_price = unit_price;
    }
    // Getters and Setters
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnitPrice() {
        return unit_price;
    }

    public void setUnitPrice(int unit_price) {
        this.unit_price = unit_price;
    }
}

