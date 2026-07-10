/**
 * Spellbook.java
 * This file contains the Spellbook class, which manages the player's collection
 * of discovered potion recipes, allowing for unlocking, sorting, and viewing them.
 */

import java.util.ArrayList;

/**
 * Represents the player's spellbook of unlocked potion recipes.
 * This class handles the addition of newly discovered recipes, checks for 
 * existing unlocks, and displays the recipes sorted by their unique ID.
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
   * <p><b>Pre-conditions:</b> newRecipe must be a valid, fully populated Recipe object.</p>
   * <p><b>Post-conditions:</b> If the recipe ID is not already in the spellbook, it is added to the 
   * unlockedRecipes list and a success message is printed.</p>
   */
  public void addRecipe(Recipe newRecipe){
    if (!hasRecipe(newRecipe.getId())){
      this.unlockedRecipes.add(newRecipe);
      System.out.println("Alchemy Success! New recipe added to spellbook: " + newRecipe.getName());
    }
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
   * Sorts and displays all currently unlocked recipes in ascending order by their ID.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:</b> The unlockedRecipes list is sorted via bubble sort, and the 
   * ID, name, and price of each recipe are printed to the console.</p>
   */
  public void displaySpellbook() { //displays all unlocked recipes sorted by concotion
    if (this.unlockedRecipes.isEmpty()) {
      System.out.println("Your spellbook is currently empty.");
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

    System.out.println("\n=== Your Spellbook ===");
    for (int i = 0; i < this.unlockedRecipes.size(); i++) {
        Recipe r = this.unlockedRecipes.get(i);
        System.out.println("ID: " + r.getId() + " | " + r.getName() + " (Sells for " + r.getPrice() + ")");
      }  
    }
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
