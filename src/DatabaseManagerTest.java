import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {

    @org.junit.jupiter.api.Test
    void saveItemToDatabase()
    {
        PriceDetail priceDetail = new PriceDetail(10,10);
        Price price = new Price(priceDetail,priceDetail);
        Item item = new Item(1, "Test Item", "dsa", price);

        DatabaseManager.saveItemToDatabase(item);
        DatabaseManager.saveItemNameToDatabase(item);

        assertTrue(DatabaseManager.itemExistsInDatabase(item), "Item should exist in the database after saving");

        DatabaseManager.deleteItemFromDatabase(item.getId());

        assertFalse(DatabaseManager.itemExistsInDatabase(item), "Item should not exist in the database after deletion");
    }

    @org.junit.jupiter.api.Test
    void getPriceData()
    {
        String tableName = "test_item";
        List<ItemRecord> priceData = DatabaseManager.getPriceData(tableName);

        assertNotNull(priceData, "List of price data should not be null");
    }

    @org.junit.jupiter.api.Test
    void getItemIdFromTableName() {
        // Given
        String tableName = "test_item";
        int itemId = DatabaseManager.getItemIdFromTableName(tableName);

        assertTrue(itemId > 0, "Item ID should be greater than 0");
    }
}