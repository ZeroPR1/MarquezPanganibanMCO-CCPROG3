/**
 * Abstract superclass representing a generic item in the game.
 */
public abstract class Item {
    private String name;
    private int quantity;
    private int buyPrice;
    private int sellPrice;

    /**
     * Constructs a new Item with a specified name, quantity, buy price, and sell price.
     * @param name The name of the item.
     * @param quantity The initial quantity of the item.
     * @param buyPrice The base buying price of the item in the market.
     * @param sellPrice The base selling price of the item in the market.
     * <p><b>Pre-conditions:</b> name is a valid string; quantity, buyPrice, and sellPrice are valid non-negative integers.</p>
     * <p><b>Post-conditions:</b> An Item object is instantiated with the provided values.</p>
     */
    public Item(String name, int quantity, int buyPrice, int sellPrice) {
        this.name = name;
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    /**
     * Constructs a new Item with a specified name, quantity, buy price, and sell price.
     * @param name The name of the item.
     * @param quantity The initial quantity of the item.
     * @param buyPrice The base buying price of the item in the market.
     * @param sellPrice The base selling price of the item in the market.
     * <p><b>Pre-conditions:</b> name is a valid string; quantity, buyPrice, and sellPrice are valid non-negative integers.</p>
     * <p><b>Post-conditions:</b> An Item object is instantiated with the provided values.</p>
     */
    public String getName() { 
    	return name; 
    }

    /**
     * Retrieves the current quantity of the item.
     * @return The integer amount of this item.
     * <p><b>Pre-conditions:</b> None.</p>
     * <p><b>Post-conditions:</b> The quantity attribute is returned.</p>
     */
    public int getQuantity() { 
    	return quantity; 
    }

    /**
     * Retrieves the base buying price of the item.
     * @return The integer buy price.
     * <p><b>Pre-conditions:</b> None.</p>
     * <p><b>Post-conditions:</b> The buyPrice attribute is returned.</p>
     */
    public int getBuyPrice() { 
    	return buyPrice; 
    }

    /**
     * Retrieves the base selling price of the item.
     * @return The integer sell price.
     * <p><b>Pre-conditions:</b> None.</p>
     * <p><b>Post-conditions:</b> The sellPrice attribute is returned.</p>
     */
    public int getSellPrice() { 
    	return sellPrice; 
    }

    /**
     * Increases the quantity of the item by a specified amount.
     * @param amount The positive integer amount to add.
     * <p><b>Pre-conditions:</b> amount must be greater than 0.</p>
     * <p><b>Post-conditions:</b> The item's quantity is increased by the amount.</p>
     */
    public void addQuantity(int amount) {
        if (amount > 0) {
            this.quantity += amount;
        }
    }

    /**
     * Decreases the quantity of the item by a specified amount.
     * @param amount The positive integer amount to deduct.
     * @return true if the deduction was successful, false if there was insufficient quantity.
     * <p><b>Pre-conditions:</b> amount must be greater than 0, and current quantity must be >= amount.</p>
     * <p><b>Post-conditions:</b> The item's quantity is decreased by the amount if sufficient stock exists.</p>
     */
    public boolean deductQuantity(int amount) {
        boolean success = false;
        if (amount > 0 && this.quantity >= amount) {
            this.quantity -= amount;
            success = true;
        }
        return success;
    }
    
    /**
     * Identifies the specific categorization type of the item.
     * @return A String representing the item type (e.g., "Fruit", "Equipment").
     * <p><b>Pre-conditions:</b> None.</p>
     * <p><b>Post-conditions:</b> Returns a string defining the child class's item type.</p>
     */
    public abstract String getItemType();
}
