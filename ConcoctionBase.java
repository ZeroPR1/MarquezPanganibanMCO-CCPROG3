/**
 * Represents the base ingredient used in potion concoctions.
 * Extends the abstract Item class.
 */
public class ConcoctionBase extends Item {

  /**
   * Constructs a new ConcoctionBase with a specified name and quantity.
   * @param name The name of the base ingredient.
   * @param quantity The initial quantity of the base ingredient.
   * <p><b>Pre-conditions:</b> name is a valid string, quantity is a valid positive integer.</p>
   * <p><b>Post-conditions:</b> A ConcoctionBase object is instantiated, fetching its respective prices automatically.</p>
   */
  public ConcoctionBase(String name, int quantity) {
    super(name, quantity, determineBuyPrice(name), determineSellPrice(name));  
  }

  /**
   * Determines the buying price of the base ingredient based on its name.
   * @param name The name of the base ingredient.
   * @return The integer buying price.
   * <p><b>Pre-conditions:</b> name is a valid, non-null string.</p>
   * <p><b>Post-conditions:</b> Returns the specific price for the given base, or 0 if it is an unrecognized base.</p>
   */
  private static int determineBuyPrice(String name) {
    int price = 0;
    
    // Evaluates the uppercase version of the name to ensure case-insensitive matching
    switch(name.toUpperCase()) {
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
        price = 250;
        break;
      case "LOTION BASE":
        price = 150;
        break;
      default
        // Acts as a fallback mechanism to prevent null or undefined prices for invalid items
        price = 0;
        break;
    }
    return price;
  }

  /**
   * Determines the selling price of the base ingredient based on its name.
   * @param name The name of the base ingredient.
   * @return The integer selling price.
   * <p><b>Pre-conditions:</b> name is a valid, non-null string.</p>
   * <p><b>Post-conditions:</b> Returns the specific sell price for the given base, or 0 if it is an unrecognized base.</p>
   */
  private static int determineSellPrice(String name) {
    int price = 0;

    switch(name.toUpperCase()) {
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
      default
        price = 0;
        break;
    }
    return price;
  }

  /**
   * Returns the categorization of this item.
   * @return A string representing the item's type.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:</b> Always returns "Concoction Base".</p>
   */
  @Override
  public getItemType() {
    return "Concoction Base";
  }
}
