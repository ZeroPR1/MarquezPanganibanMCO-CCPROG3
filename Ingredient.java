/**
 * Ingredient.java
 * This file contains the Ingredient class, which represents a fundamental
 * component used in the game's brewing system, tracking its name and quantity.
 */

/**
 * Represents an individual ingredient in the player's inventory or the market.
 * This class manages the name and current stock level of the item, allowing 
 * for the safe addition and deduction of quantities.
 */
public class Ingredient{
  
  /** The name of the ingredient. */
  private String name;

  /** The current quantity of this ingredient available. */
  private int quantity;

  /**
   * Constructs a new Ingredient with a specified name and initial quantity.
   * @param name Name of the item.
   * @param quantity Starting quantity.
   * <p><b>Pre-conditions:</b> name must be a valid String and quantity is a valid integer.</p>
   * <p><b>Post-conditions:<b> A new Ingredient object is instantiated with the provided data.</p>
   */
  public Ingredient(String name, int quantity){
    this.name = name;
    this.quantity = quantity;
  }
  
  /**
   * Retrieves the name of the ingredient.
   * @return The String name of the ingredient.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:<b> The name attribute is returned.</p>
   */
  public String getName(){
    return this.name;
  }

  /**
   * Retrieves the current quantity of the ingredient.
   * @return The integer amount of this ingredient currently held.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:<b> The quantity attribute is returned.</p>
   */
  public int getQuantity(){
    return this.quantity;
  }

  /**
   * Increases the quantity of the ingredient by a specified amount.
   * @param amount The positive integer amount to add to the total.
   * <p><b>Pre-conditions:</b> amount must be greater than 0.</p>
   * <p><b>Post-conditions:<b> If the amount is positive, the ingredient's quantity is increased.</p>
   */
  public void addQuantity(int amount){
    if (amount > 0) this.quantity += amount;
  }

  /**
   * Decreases the quantity of the ingredient by a specified amount.
   * @param amount The positive integer amount to deduct from the total.
   * @return A boolean representing true if the deduction was successful, or false if it failed.
   * <p><b>Pre-conditions:</b> amount must be greater than 0, and current quantity must be sufficient.</p>
   * <p><b>Post-conditions:<b> If valid, the quantity is decreased by the amount and true is returned. 
   * Otherwise, the state remains unchanged and false is returned.</p>
   */
  public boolean deductQuantity(int amount){
    boolean success = false;
    
    if (amount > 0 && this.quantity >= amount){
      this.quantity -= amount;
      success = true;
    }
    return success;
  }
}
