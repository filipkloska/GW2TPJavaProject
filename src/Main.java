
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter the item ID: ");
        Scanner in = new Scanner(System.in);
        int id = 0;
        try
        {
            id = in.nextInt();
            System.out.println("Your input: " + id);
        }
        catch(Exception e)
        {
            System.out.println("Input was not an intiger.");
            e.printStackTrace();
        }
        Item item = ConnectorAPI.retrieveItemFromAPI(id);
        if(item != null)
        {
            DatabaseManager.saveItemToDatabase(item);
            DatabaseManager.saveItemNameToDatabase(item);
        }
    }
}