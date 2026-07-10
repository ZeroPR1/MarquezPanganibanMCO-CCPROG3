/**
 * Recipe.java
 * This file contains the Recipe class, which defines the required ingredients
 * and properties for a specific potion concoction in the game.
 */
import java.util.ArrayList;

/**
 * Represents a potion recipe containing a base ingredient and a list of required fruits.
 * This class manages the recipe's identification, naming, pricing, and validates
 * whether a given set of ingredients matches its specific requirements.
 */
public class Recipe {

  /** The unique identifier for this recipe. */
  private int concoctionId;

  /** The display name of the concocted potion. */
  private String recipeName;

  /** The name of the base ingredient required for this recipe. */
  private String baseName;

  /** The base selling price of the brewed potion. */
  private int price;

  /** A list of fruit names required to complete this recipe. */
  private ArrayList<String> requiredFruits;

  /**
   * Constructs a new Recipe with a specified ID, name, base, and price.
   * @param concoctionId The unique integer ID of the recipe.
   * @param recipeName The name of the completed potion.
   * @param baseName The name of the required base ingredient.
   * @param price The integer selling price of the potion.
   * <p><b>Pre-conditions:</b> All string parameters must be valid non-null Strings.</p>
   * <p><b>Post-conditions:<b> A new Recipe object is instantiated and the requiredFruits list is initialized.</p>
   */
  public Recipe(int concoctionId, String recipeName, String baseName, int price) {
    this.concoctionId = concoctionId;
    this.recipeName = recipeName;
    this.baseName = baseName;
    this.price = price;
    this.requiredFruits = new ArrayList<>();
  }

  /**
   * Adds a specified fruit to the list of required ingredients for this recipe.
   * @param fruitName The name of the fruit to add.
   * <p><b>Pre-conditions:</b> fruitName must be a valid non-null String.</p>
   * <p><b>Post-conditions:<b> The fruit name is appended to the requiredFruits list.</p>
   */
  public void addRequiredFruit(String fruitName) {
    this.requiredFruits.add(fruitName);
  }

  /**
   * Retrieves the unique identifier of the recipe.
   * @return The integer ID of the recipe.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:<b> The concoctionId attribute is returned.</p>
   */
  public int getId() {
    return this.concoctionId;
  }

  /**
   * Retrieves the required base ingredient of the recipe.
   * @return The String name of the base ingredient.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:<b> The baseName attribute is returned.</p>
   */
  public String getBaseName(){
    return this.baseName;
  }

  /**
   * Retrieves the name of the completed potion.
   * @return The String name of the recipe.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:<b> The recipeName attribute is returned.</p>
   */
  public String getName() {
    return this.recipeName;
  }

  /**
   * Retrieves the list of fruits required to brew this recipe.
   * @return An ArrayList containing the names of the required fruits.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:<b> The requiredFruits ArrayList is returned.</p>
   */
  public ArrayList<String> getRequiredFruits() {
    return this.requiredFruits;
  }

  /**
   * Retrieves the selling price of the completed potion.
   * @return The integer price of the recipe.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:<b> The price attribute is returned.</p>
   */
  public int getPrice() {
    return this.price;
  }

  /**
   * Checks if a provided base and set of fruits match this recipe's requirements.
   * @param inputBase The name of the base ingredient provided by the player.
   * @param inputFruits A list of fruit names provided by the player.
   * @return True if the ingredients perfectly match the recipe, false otherwise.
   * <p><b>Pre-conditions:</b> inputBase is a valid String and inputFruits is a valid ArrayList.</p>
   * <p><b>Post-conditions:<b> Returns true if the base matches and the fruits array contains all 
   * required items without evaluating state changes.</p>
   */
  public boolean matchesIngredients(String inputBase, ArrayList<String> inputFruits) {
    boolean isMatch = true;
    
    if (!this.baseName.equals(inputBase) || this.requiredFruits.size() != inputFruits.size()) {
      isMatch = false;
    } else {
      for (int i = 0; i < this.requiredFruits.size() && isMatch; i++) {
        String required = this.requiredFruits.get(i);
        if (!inputFruits.contains(required)) {
          isMatch = false;
        }
      }
    }
    return isMatch;
  }
}
