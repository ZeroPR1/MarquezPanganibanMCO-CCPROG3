/**
 * ConcoctionBase.java
 * Represents a base ingredient required for creating potions.
 * This class extends the generic Item class, providing specific pricing 
 * and type identification for base ingredients used in the crafting system.
 */
public class ConcoctionBase extends Item {
    
    /**
     * Constructs a new ConcoctionBase item with the specified name and quantity.
     * 
     * @param name The name of the base ingredient.
     * @param quantity The amount of this base ingredient.
     * 
     * <p><b>Pre-conditions:</b> A valid, non-null String name and a positive integer quantity must be provided.</p>
     * <p><b>Post-conditions:</b> A ConcoctionBase object is instantiated with its correct buy and sell prices automatically determined.</p>
     * 
     */
    public ConcoctionBase(String name, int quantity) { //darshan
        // initialize the parent Item class using dynamically determined prices
        super(name, quantity, determineBuyPrice(name), determineSellPrice(name));
    }

    /**
     * Returns the base purchase price for a specific concoction base in the market.
     * 
     * @param name The name of the base ingredient.
     * @return The integer buy price of the base.
     * 
     * <p><b>Pre-conditions:</b> The provided name must be a non-null String.</p>
     * <p><b>Post-conditions:</b> The correct integer buy price is returned. If the base is not found, 0 is returned.</p>
     * 
     */
    private static int determineBuyPrice(String name) { //kyle
        int price = 0;
        
        // Evaluates the uppercase version of the name to ensure case-insensitive matching
        switch (name.toUpperCase()) {
            case "SYRUP BASE":
                price = 50;
                break;
            case "BUBBLE BASE":
                price = 80;
                break;
            case "PERFUME BASE":
                price = 250;
                break;
            case "MILK BASE":
                price = 60;
                break;
            case "LOTION BASE":
                price = 150;
                break;
            default:
            	// Acts as a fallback mechanism to prevent null or undefined prices for invalid items
                price = 0;
                break;
        }
        return price;
    }

    /**
     * Returns the selling price for a specific concoction base, which is generally lower than the purchase price.
     * 
     * @param name The name of the base ingredient.
     * @return The integer sell price of the base.
     * 
     * <p><b>Pre-conditions:</b> The provided name must be a non-null String.</p>
     * <p><b>Post-conditions:</b> The correct integer sell price is returned. If the base is not found, 0 is returned.</p>
     * 
     */
    private static int determineSellPrice(String name) { //darshan
        int price = 0;
        
        // route to the correct sell price based on the base's name
        switch (name.toUpperCase()) {
            case "SYRUP BASE":
                price = 10;
                break;
            case "BUBBLE BASE":
                price = 20;
                break;
            case "PERFUME BASE":
                price = 50;
                break;
            case "MILK BASE":
                price = 15;
                break;
            case "LOTION BASE":
                price = 25;
                break;
            default:
                price = 0;
                break;
        }
        return price;
    }

    /**
     * Identifies the specific category of this item within the inventory system.
     * 
     * @return A String representing the item's type classification.
     * 
     * <p><b>Pre-conditions:</b> The ConcoctionBase object must be instantiated.</p>
     * <p><b>Post-conditions:</b> Returns the string literal identifying it as a base.</p>
     * 
     */
    @Override
    public String getItemType() { //darshan
        return "Concoction Base"; 
    }
}
