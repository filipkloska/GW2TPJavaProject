import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/itemdatabase";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void saveItemToDatabase(Item item) {
        String tableName = item.getName().replace(' ', '_');

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             CallableStatement stmt = conn.prepareCall("{CALL insertIntoTable(?, ?, ?, ?, ?, ?)}")) {

            stmt.setString(1, tableName);
            stmt.setInt(2, item.getCurrentPrice().getBuys().getQuantity());
            stmt.setInt(3, item.getCurrentPrice().getBuys().getUnitPrice());
            stmt.setInt(4, item.getCurrentPrice().getSells().getQuantity());
            stmt.setInt(5, item.getCurrentPrice().getSells().getUnitPrice());
            stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<ItemRecord> getPriceData(String tableName) {
        List<ItemRecord> priceData = new ArrayList<>();
        String query = "SELECT fetch_timestamp, supply_price, supply_quantity, demand_price, demand_quantity FROM " + tableName;

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("fetch_timestamp");
                int supplyPrice = rs.getInt("supply_price");
                int demandPrice = rs.getInt("demand_price");
                int supplyQuantity = rs.getInt("supply_quantity");
                int demandQuantity = rs.getInt("demand_quantity");
                priceData.add(new ItemRecord(timestamp, supplyPrice, supplyQuantity, demandPrice, demandQuantity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return priceData;
    }
}