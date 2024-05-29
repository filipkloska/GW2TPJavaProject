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
             CallableStatement stmt = conn.prepareCall("{CALL insertIntoTable(?, ?, ?, ?, ?, ?, ?)}")) {

            stmt.setString(1, tableName);
            stmt.setInt(2, item.getId());
            stmt.setInt(3, item.getCurrentPrice().getBuys().getQuantity());
            stmt.setInt(4, item.getCurrentPrice().getBuys().getUnitPrice());
            stmt.setInt(5, item.getCurrentPrice().getSells().getQuantity());
            stmt.setInt(6, item.getCurrentPrice().getSells().getUnitPrice());
            stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void saveItemNameToDatabase(Item item) {
        String query = "INSERT INTO items (id, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE name = VALUES(name)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, item.getId());
                stmt.setString(2, item.getName());
                stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<String> getItemNamesFromDatabase() {
        List<String> itemNames = new ArrayList<>();
        String query = "SELECT name FROM items";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                itemNames.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemNames;
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

    public static int getItemIdFromTableName(String tableName) {
        String query = "SELECT item_id FROM " + tableName + " LIMIT 1";
        int itemId = -1;

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                itemId = rs.getInt("item_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemId;
    }
    public static boolean itemExistsInDatabase(Item item) {
        String query = "SELECT COUNT(*) AS count FROM items WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, item.getId());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int count = rs.getInt("count");
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void deleteItemFromDatabase(int itemId) {
        String query = "DELETE FROM items WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
