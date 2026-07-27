/**
 * Abstract superclass that represents a generic item in game
 */

public abstract class Item {
  private String name;
  private int quantity;
  private int buyPrice;
  private int sellPrice;

/**
 * Constructor for intializing fthe core attributes of the item 
 * 
 * @param name: The name of the item
 * @param buyPrice: The markets' purchase price in crystals
 * @param sellPrice: The markets' selling price in crystals
 */

  public item(String name, int buyPrice, int sellPrice) {
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
