import static org.junit.jupiter.api.Assertions.*;

class ConnectorAPITest {

    @org.junit.jupiter.api.Test
    void retrieveItemFromAPI_ValidItemId_ReturnsItem()
    {
        int itemId = 19721; // ID of Glob of Ectoplasm
        Item actualItem = ConnectorAPI.retrieveItemFromAPI(itemId);
        assertNotNull(actualItem, "Item should not be null");
        assertEquals("Glob of Ectoplasm", actualItem.getName(), "Retrieved item does not match expected item");
    }

    @org.junit.jupiter.api.Test
    void retrieveItemFromAPI_NonexistentItemId_ReturnsNull()
    {
        int itemId = -1; //no item with -1 id exists
        Item actualItem = ConnectorAPI.retrieveItemFromAPI(itemId);
        assertNull(actualItem, "Item should be null");
    }
}