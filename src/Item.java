public class Item {
    private final int id;
    private final String name;
    private final String icon;
    private final Price currentPrice;

    public Item(int id, String name, String icon, Price price)
    {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.currentPrice = price;
    }
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }
    public Price getCurrentPrice()
    {
        return currentPrice;
    }
}
