import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectorAPI {

    public static String fetchDataFromAPI(String apiUrl) throws Exception
    {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode() + "\nItem Not Found in API");
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

    public static Item retrieveItemFromAPI(int itemId)
    {
        Item item = null;
        try {
            Gson gson = new Gson();
            // URL do pobrania informacji o przedmiocie
            String itemApiUrl = "https://api.guildwars2.com/v2/items/" + itemId;
            // URL do pobrania informacji o cenie przedmiotu
            String priceApiUrl = "https://api.guildwars2.com/v2/commerce/prices/" + itemId;
            String itemJson = ConnectorAPI.fetchDataFromAPI(itemApiUrl);
            item = gson.fromJson(itemJson, Item.class);
            String priceJson = ConnectorAPI.fetchDataFromAPI(priceApiUrl);
            Price price = gson.fromJson(priceJson, Price.class);
            if (item.getName() == null)
            {
                System.out.println("Item not found");
                return null;
            }
            item = new Item(item.getId(), item.getName(), item.getIcon(), price);
            System.out.println("Name: " + item.getName());
            System.out.println("ID: " + item.getId());
            System.out.println("Icon: " + item.getIcon());
            System.out.println("Buy Quantity: " + item.getCurrentPrice().getBuys().getQuantity());
            System.out.println("Buy Unit Price: " + item.getCurrentPrice().getBuys().getUnitPrice());
            System.out.println("Sell Quantity: " + item.getCurrentPrice().getSells().getQuantity());
            System.out.println("Sell Unit Price: " + item.getCurrentPrice().getSells().getUnitPrice());
            System.out.println("----------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }
}
