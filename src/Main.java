import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        int[] itemIds = {19976, 19721};  // Dodano ID przedmiotu 19721

        try {
            // Tworzymy instancję Gson
            Gson gson = new Gson();

            for (int itemId : itemIds) {
                //URL do pobrania informacji o przedmiocie
                String itemApiUrl = "https://api.guildwars2.com/v2/items?ids=" + itemId;
                // URL do pobrania informacji o cenie przedmiotu
                String priceApiUrl = "https://api.guildwars2.com/v2/commerce/prices?id=" + itemId;

                // Pobieramy dane o przedmiocie
                String itemJson = fetchData(itemApiUrl);
                Item[] items = gson.fromJson(itemJson, Item[].class);

                // Pobieramy dane o cenie przedmiotu
                String priceJson = fetchData(priceApiUrl);
                // Parsujemy JSON do obiektu Price
                Price price = gson.fromJson(priceJson, Price.class);

                if (items.length == 0)
                {
                    System.out.println("No items found");
                    return;
                }
                Item item = items[0];
                item = new Item(item.getId(), item.getName(), item.getIcon(), price);

                // Wyświetlamy połączone informacje
                System.out.println("Name: " + item.getName());
                System.out.println("ID: " + item.getId());
                System.out.println("Icon: " + item.getIcon());
                System.out.println("Buy Quantity: " + item.getCurrentPrice().getBuys().getQuantity());
                System.out.println("Buy Unit Price: " + item.getCurrentPrice().getBuys().getUnitPrice());
                System.out.println("Sell Quantity: " + item.getCurrentPrice().getSells().getQuantity());
                System.out.println("Sell Unit Price: " + item.getCurrentPrice().getSells().getUnitPrice());
                System.out.println("----------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String fetchData(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        conn.disconnect();

        return sb.toString();
    }
}
