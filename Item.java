/**
 * Abstract superclass representing a generic item in the game.
 */
public abstract class Item {
    private String name;
    private int quantity;
    private int buyPrice;
    private int sellPrice;

    public Item(String name, int quantity, int buyPrice, int sellPrice) {
        this.name = name;
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public String getName() { 
    	return name; 
    }
    
    public int getQuantity() { 
    	return quantity; 
    }
    
    public int getBuyPrice() { 
    	return buyPrice; 
    }
    
    public int getSellPrice() { 
    	return sellPrice; 
    }

    public void addQuantity(int amount) {
        if (amount > 0) {
            this.quantity += amount;
        }
    }

    public boolean deductQuantity(int amount) {
        boolean success = false;
        if (amount > 0 && this.quantity >= amount) {
            this.quantity -= amount;
            success = true;
        }
        return success;
    }

    public abstract String getItemType();
}
