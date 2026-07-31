/**
 * Represents the fruit ingredient used in potion concotions
 */
public class Fruit extends Item {

  public Fruit(String name, int quantity) {
    super(name, quantity, determineBuyPrice(name), determineSellPrice(name));
  }
  
  
}
