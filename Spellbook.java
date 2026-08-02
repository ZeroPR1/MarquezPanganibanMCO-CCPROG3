/**
 * Spellbook.java
 * This file contains the Spellbook class, which manages the player's collection
 * of discovered potion recipes, allowing for unlocking, sorting, and viewing them.
 */

import java.util.ArrayList;

/**
 * Represents the player's spellbook of unlocked potion recipes.
 * This class handles the addition of newly discovered recipes, checks for 
 * existing unlocks, and outputs the recipes sorted by their unique ID.
 */
public class Spellbook{

  /** A list containing all the recipes the player has successfully discovered. */
  private ArrayList<Recipe> unlockedRecipes;

  /**
   * Constructs a new Spellbook with an empty list of unlocked recipes.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:</b> The unlockedRecipes ArrayList is initialized and empty.</p>
   */
  public Spellbook() {
    this.unlockedRecipes = new ArrayList<Recipe>();
  }

  /**
   * Adds a newly discovered recipe to the spellbook if it hasn't been unlocked yet.
   * @param newRecipe The Recipe object to add.
   * @return A String message indicating success or an empty string if already unlocked.
   * <p><b>Pre-conditions:</b> newRecipe must be a valid, fully populated Recipe object.</p>
   * <p><b>Post-conditions:</b> If the recipe ID is not already in the spellbook, it is added to the 
   * unlockedRecipes list and a success message is returned.</p>
   */
  public String addRecipe(Recipe newRecipe){
    String result = "";
    if (!hasRecipe(newRecipe.getId())){
      this.unlockedRecipes.add(newRecipe);
      result = "Alchemy Success! New recipe added to spellbook: " + newRecipe.getName();
    }
    return result;
  }

  /**
   * Checks if a specific recipe ID has already been unlocked in the spellbook.
   * @param id The integer ID of the recipe to check.
   * @return True if the recipe is in the spellbook, false otherwise.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:</b> Returns true if the ID matches an existing recipe, without modifying state.</p>
   */
  public boolean hasRecipe(int id) {
    boolean found = false;
    for (int i = 0; i < this.unlockedRecipes.size(); i++){
      if (this.unlockedRecipes.get(i).getId() == id){
        found = true;
      }
    }
    return found;
  }

  /**
   * Sorts and formats all currently unlocked recipes in ascending order by their ID.
   * @return A formatted String containing the sorted spellbook contents.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:</b> The unlockedRecipes list is sorted via bubble sort, and the 
   * ID, name, and price of each recipe are formatted into a string and returned.</p>
   */
  public String displaySpellbook() { //returns all unlocked recipes sorted by concoction as a String
    String display = "";
    if (this.unlockedRecipes.isEmpty()) {
      display = "Your spellbook is currently empty.\n";
    } else {
    //bubble sort to arrange by id in ascending order
    for (int i = 0; i < this.unlockedRecipes.size() - 1; i++) {
      for (int j = 0; j < this.unlockedRecipes.size() - i - 1; j++){
          int id1 = this.unlockedRecipes.get(j).getId();
          int id2 = this.unlockedRecipes.get(j + 1).getId();

        if (id1 > id2) {
          Recipe temp = this.unlockedRecipes.get(j);
          this.unlockedRecipes.set(j, this.unlockedRecipes.get(j + 1));
          this.unlockedRecipes.set(j + 1, temp);
        }
      }
    }

    display += "\n=== Your Spellbook ===\n";
    for (int i = 0; i < this.unlockedRecipes.size(); i++) {
        Recipe r = this.unlockedRecipes.get(i);
        display += "ID: " + r.getId() + " | " + r.getName() + " (Sells for " + r.getPrice() + ")\n";
      }  
    }
    return display;
  }

  /**
   * Converts the unlocked recipes into a sorted 2D array for GUI table rendering.
   * @return An Object[][] containing the potion names and their recipe IDs.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:</b> Returns a structured, ID-sorted array of the spellbook's contents.</p>
   */
  public Object[][] getSpellbookTableData() {
      // bubble sort to arrange by id in ascending order to match console output logic
      // The outer loop determines the number of passes required to sort the entire list
      for (int i = 0; i < this.unlockedRecipes.size() - 1; i++) {
        for (int j = 0; j < this.unlockedRecipes.size() - i - 1; j++){
            int id1 = this.unlockedRecipes.get(j).getId();
            int id2 = this.unlockedRecipes.get(j + 1).getId();

          // Swap logic: if the current element's ID is greater than the next, swap their positions
          if (id1 > id2) {
            Recipe temp = this.unlockedRecipes.get(j);
            this.unlockedRecipes.set(j, this.unlockedRecipes.get(j + 1));
            this.unlockedRecipes.set(j + 1, temp);
          }
        }
      }
      
      Object[][] data = new Object[this.unlockedRecipes.size()][2];
      for (int i = 0; i < this.unlockedRecipes.size(); i++) {
          Recipe r = this.unlockedRecipes.get(i);
          data[i][0] = r.getName();
          data[i][1] = r.getId();
      }
      return data;
  }

  /**
   * Compiles the IDs of all unlocked recipes into a formatted string for saving.
   * @return A comma-separated String containing the IDs of all unlocked recipes.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:</b> A formatted data string is returned representing the unlocked recipes.</p>
   */
  public String exportSpellbookData() {
    String data = "";
    for (int i = 0; i < this.unlockedRecipes.size(); i++) {
        data += this.unlockedRecipes.get(i).getId() + ",";
    }
    return data;
  }
}
