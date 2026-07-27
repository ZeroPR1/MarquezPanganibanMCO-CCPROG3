/**
 * Abstract superclass that represents a generic item in game
 * Provides standard properties for inventory and market management
 */

public abstract class Item {
  private String name;
  private int buyPrice;
  private int sellPrice;

/**
 * Constructor for intializing fthe core attributes of the item 
 * 
 * @param name: The name of the item
 * @param buyPrice: The markets' purchase price in crystals
 * @param sellPrice: The markets' selling price in crystals
 */

  public item(String name, int buyPrice, int sellprice) {
    this.name = name;
    this.buyPrice = buyPrice;
    this.sellPrice = sellPrice;
  }

  
}
