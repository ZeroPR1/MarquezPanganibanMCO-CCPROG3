/**
 * Fruit.java
 * Represents a fruit ingredient used in alchemy concoctions.
 * This class extends the generic Item class, providing specific pricing 
 * and type identification for fruit ingredients used in the crafting system.
 */
public class Fruit extends Item {
    
    /**
     * Constructs a new ConcoctionBase with a specified name and quantity.
     * 
     * @param name The name of the fruit ingredient.
     * @param quantity The amount of this fruit ingredient.
     * 
     * <p><b>Pre-conditions:</b> name is a valid string, quantity is a valid positive integer.</p>
     * <p><b>Post-conditions:</b> A Fruit object is instantiated, fetching its respective prices automatically.</p>
     * 
     */
    public Fruit(String name, int quantity) { //kyle
        // initialize the parent Item class using dynamically determined prices
        super(name, quantity, determineBuyPrice(name), determineSellPrice(name));
    }

    /**
     * Returns the base purchase price for a specific fruit in the market.
     * 
     * @param name The name of the fruit ingredient.
     * @return The integer buy price of the fruit.
     * 
     * <p><b>Pre-conditions:</b> The provided name must be a non-null String.</p>
     * <p><b>Post-conditions:</b> The correct integer buy price is returned. If the fruit is not found, 0 is returned.</p>
     * 
     */
    private static int determineBuyPrice(String name) { //kyle
        int price = 0;
        
        // Evaluates the uppercase version of the name to ensure case-insensitive matching
        switch (name.toUpperCase()) {
            case "STRAWBERRY":
                price = 125;
                break;
            case "ORANGE":
                price = 80;
                break;
            case "LEMON":
                price = 50;
                break;
            case "BANANA":
                price = 75;
                break;
            case "MANGO":
                price = 90;
                break;
            case "PINEAPPLE":
                price = 240;
                break;
            case "KIWI":
                price = 200;
                break;
            case "BLUEBERRY":
                price = 120;
                break;
            case "COCONUT":
                price = 180;
                break;
            default:
            	// Acts as a fallback mechanism to prevent null or undefined prices for invalid items
                price = 0;
                break;
        }
        return price;
    }

    /**
     * Determines the selling price of the base ingredient based on its name.
     * 
     * @param name The name of the base ingredient.
     * @return The integer selling price.
     * 
     * <p><b>Pre-conditions:</b> name is a valid, non-null string.</p>
     * <p><b>Post-conditions:</b> Returns the specific sell price for the given base, or 0 if it is an unrecognized base.</p>
     */
    
    private static int determineSellPrice(String name) { //darshan
        int price = 0;
        
        // Evaluates the uppercase version of the name to ensure case-insensitive matching
        switch (name.toUpperCase()) {
            case "STRAWBERRY":
                price = 25;
                break;
            case "ORANGE":
                price = 40;
                break;
            case "LEMON":
                price = 25;
                break;
            case "BANANA":
                price = 50;
                break;
            case "MANGO":
                price = 30;
                break;
            case "PINEAPPLE":
                price = 120;
                break;
            case "KIWI":
                price = 80;
                break;
            case "BLUEBERRY":
                price = 20;
                break;
            case "COCONUT":
                price = 90;
                break;
            default:
            	// Acts as a fallback mechanism to prevent null or undefined prices for invalid items
                price = 0;
                break;
        }
        return price;
    }

    /**
     * Returns the categorization of this item.
     * 
     * @return A string representing the item's type.
     * 
     * <p><b>Pre-conditions:</b> None.</p>
     * <p><b>Post-conditions:</b> Always returns "Concoction Base".</p>
     */
    
    @Override
    public String getItemType() { //darshan
        return "Fruit"; 
    }
}
