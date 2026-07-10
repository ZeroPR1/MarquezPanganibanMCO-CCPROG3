//note: kyle marquezs' laptop was not turning on, in turn, he dictated what to type through discord calls and messenger chats

/**
 * IngredientSlot.java
 * This file contains the IngredientSlot class, which represents a single
 * slot within the market, holding a specific item and its available quantity.
 */

/**
 * Represents a designated slot in the market containing an ingredient for sale.
 * This class tracks the name of the item in the slot and the current stock available,
 * allowing the market to manage its inventory slots dynamically.
 */
public class IngredientSlot {
  
  /** The name of the item currently occupying this slot. */
  private String itemName;

  /** The number of this item currently available in this slot. */
  private int quantity;


  /**
   * Constructs a new IngredientSlot with a specified item name and initial quantity.
   * @param itemName The name of the item to place in the slot.
   * @param quantity The initial quantity of the item available.
   * <p><b>Pre-conditions:</b> itemName must be a valid String and quantity is a valid integer.</p>
   * <p><b>Post-conditions:<b> A new IngredientSlot object is instantiated with the provided data.</p>
   */
  public IngredientSlot(String itemName, int quantity) {
    this.itemName = itemName;
    this.quantity = quantity;
  }

  /**
   * Retrieves the name of the item currently in this slot.
   * @return The String name of the item.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:<b> The itemName attribute is returned.</p>
   */
  public String getItemName() {
    return this.itemName;
  }

  /**
   * Retrieves the current quantity of the item in this slot.
   * @return The integer amount of the item currently available.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:<b> The quantity attribute is returned.</p>
   */
  public int getQuantity() {
    return this.quantity;
  }

  /**
   * Clears the current slot, removing the item and resetting the quantity to zero.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:<b> The itemName is set to "Empty" and the quantity is set to 0.</p>
   */
  public void emptySlot() {
    this.itemName = "Empty";
    this.quantity = 0;
  }
}
