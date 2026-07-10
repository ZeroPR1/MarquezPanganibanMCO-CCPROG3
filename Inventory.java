/**
 * Inventory.java
 * This file contains the Inventory class, which manages the player's collection
 * of fruits, bases, and cauldrons, providing methods to add, remove, and query items.
 */

import java.util.ArrayList;

/**
 * Represents the player's storage for brewing materials and equipment.
 * This class tracks lists of ingredients and cauldrons, handling the logic for
 * stacking items, checking availability, and managing cauldron states.
 */
public class Inventory {
  
  /** A list of fruit ingredients currently held by the player. */
  private ArrayList<Ingredient> fruits;

  /** A list of base ingredients currently held by the player. */
  private ArrayList<Ingredient> bases;

  /** A list of cauldrons the player owns, tracking both usable and ruined states. */
  private ArrayList<Cauldron> cauldrons;

  /**
   * Constructs a new Inventory with empty ingredient lists and three default cauldrons.
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:<b> Lists for fruits, bases, and cauldrons are instantiated, 
   * and three new usable Cauldron objects are added to the cauldrons list.</p>
   */
  public Inventory() {
    this.fruits = new ArrayList<Ingredient>();
    this.bases = new ArrayList<Ingredient>();
    this.cauldrons = new ArrayList<Cauldron>();
    for(int i = 0; i < 3; i++){
        this.cauldrons.add(new Cauldron());
    }
  }

 /**
   * Adds a specified quantity of a fruit to the inventory, stacking if it already exists.
   * @param name The name of the fruit to add.
   * @param quantity The amount of the fruit to add.
   * <p><b>Pre-conditions:</b> name is a valid non-null String and quantity is greater than 0.</p>
   * <p><b>Post-conditions:<b> If the fruit exists, its quantity is increased. Otherwise, 
   * a new Ingredient object is created and added to the fruits list.</p>
   */
    public void addFruit(String name, int quantity){
      boolean found = false;
      for (int i = 0; i < this.fruits.size(); i++){
          if (this.fruits.get(i).getName().equals(name)){
              this.fruits.get(i).addQuantity(quantity);
              found = true;
          }
      }
      if (!found){
          this.fruits.add(new Ingredient(name, quantity)); 
      }
    }

    /**
     * Removes a specified quantity of a fruit from the inventory.
     * @param name The name of the fruit to remove.
     * @param quantity The amount of the fruit to deduct.
     * @return True if successfully removed, false if insufficient quantity or not found.
     * <p><b>Pre-conditions:</b> name is a valid non-null String and quantity is greater than 0.</p>
     * <p><b>Post-conditions:<b> If the player has enough of the fruit, the quantity is deducted 
     * and true is returned. Otherwise, the inventory is unchanged and false is returned.</p>
     */
    public boolean removeFruit(String name, int quantity){
        boolean success = false;
        for (int i = 0; i < this.fruits.size(); i++){
          if (this.fruits.get(i).getName().equals(name)){
              success = this.fruits.get(i).deductQuantity(quantity);
          }
        }
        return success;
    }

    /**
     * Adds a specified quantity of a base to the inventory, stacking if it already exists.
     * @param name The name of the base to add.
     * @param quantity The amount of the base to add.
     * <p><b>Pre-conditions:</b> name is a valid non-null String and quantity is greater than 0.</p>
     * <p><b>Post-conditions:<b> If the base exists, its quantity is increased. Otherwise, 
     * a new Ingredient object is created and added to the bases list.</p>
     */
    public void addBase(String name, int quantity){
        boolean found = false;
        for (int i = 0; i < this.bases.size(); i++){
            if (this.bases.get(i).getName().equals(name)){
                this.bases.get(i).addQuantity(quantity);
                found = true;
            }
        }
        if (!found) {
            this.bases.add(new Ingredient(name, quantity));
        }
    }


    /**
     * Removes a specified quantity of a base from the inventory.
     * @param name The name of the base to remove.
     * @param quantity The amount of the base to deduct.
     * @return True if successfully removed, false if insufficient quantity or not found.
     * <p><b>Pre-conditions:</b> name is a valid non-null String and quantity is greater than 0.</p>
     * <p><b>Post-conditions:<b> If the player has enough of the base, the quantity is deducted 
     * and true is returned. Otherwise, the inventory is unchanged and false is returned.</p>
     */
    public boolean removeBase(String name, int quantity) {
      boolean success = false;
      for (int i = 0; i < this.bases.size(); i++) {
        if (this.bases.get(i).getName().equals(name)) {
            success = this.bases.get(i).deductQuantity(quantity);
        }
      }
      return success;
    }

    /**
     * Checks if the inventory contains a sufficient quantity of a specific ingredient.
     * @param name The name of the ingredient to check.
     * @param requiredQty The minimum quantity required.
     * @param isBase True if checking the bases list, false if checking the fruits list.
     * @return True if the ingredient exists in sufficient quantity, false otherwise.
     * <p><b>Pre-conditions:</b> name is a valid non-null String and requiredQty is greater than 0.</p>
     * <p><b>Post-conditions:<b> Returns true if the condition is met without altering the inventory state.</p>
     */
    public boolean checkIngredientAvailability(String name, int requiredQty, boolean isBase) {
      boolean available = false;
      ArrayList<Ingredient> listToCheck = isBase ? this.bases : this.fruits;
      for (int i = 0; i < listToCheck.size(); i++) {
        if (listToCheck.get(i).getName().equals(name) && listToCheck.get(i).getQuantity() >= requiredQty) {
          available = true;
        }
      }
      return available;
    }

    /**
     * Calculates the number of cauldrons currently available for brewing.
     * @return The integer count of usable cauldrons.
     * <p><b>Pre-conditions:</b> The cauldrons list is initialized.</p>
     * <p><b>Post-conditions:<b> The total count of usable cauldrons is returned.</p>
     */
    public int getUsableCauldronCount() {
      int count = 0;
      for (int i = 0; i < this.cauldrons.size(); i++) {
        if (this.cauldrons.get(i).checkUsability()) count++;
      }
      return count;
    }

    /**
     * Calculates the number of cauldrons that are ruined and cannot be used.
     * @return The integer count of unusable cauldrons.
     * <p><b>Pre-conditions:</b> The cauldrons list is initialized.</p>
     * <p><b>Post-conditions:<b> The total count of unusable cauldrons is returned.</p>
     */
    public int getUnusableCauldronCount() {
      return this.cauldrons.size() - getUsableCauldronCount();
    }


    /**
     * Changes the state of one usable cauldron to ruined.
     * <p><b>Pre-conditions:</b> There must be at least one usable cauldron in the inventory.</p>
     * <p><b>Post-conditions:<b> The first encountered usable cauldron is set to a ruined state.</p>
     */
    public void ruinOneCauldron() {
      boolean ruined = false;
      for (int i = 0; i < this.cauldrons.size() && !ruined; i++) {
        if (this.cauldrons.get(i).checkUsability()) {
          this.cauldrons.get(i).ruinCauldron();
          ruined = true;
        }
      }
    }

    /**
     * Restores one ruined cauldron back to a usable state.
     * <p><b>Pre-conditions:</b> There must be at least one unusable cauldron in the inventory.</p>
     * <p><b>Post-conditions:<b> The first encountered ruined cauldron is restored to usability.</p>
     */
    public void blessOneCauldron() {
      boolean blessed = false;
      for (int i = 0; i < this.cauldrons.size() && !blessed; i++) {
        if (!this.cauldrons.get(i).checkUsability()) {
          this.cauldrons.get(i).blessCauldron();
          blessed = true;
        }
      }
    }

    /**
     * Adds a new, usable cauldron to the player's inventory.
     * <p><b>Pre-conditions:</b> None.</p>
     * <p><b>Post-conditions:<b> A newly instantiated Cauldron object is appended to the cauldrons list.</p>
     */
    public void addCauldron() {
      this.cauldrons.add(new Cauldron());
    }

    /**
     * Prints the current state of the inventory to the console.
     * <p><b>Pre-conditions:</b> None.</p>
     * <p><b>Post-conditions:<b> The names and quantities of all fruits and bases, along with 
     * the counts of usable and unusable cauldrons, are output to the console.</p>
     */
    public void displayInventory() {
      System.out.println("\n--- Fruits ---");
      for (int i = 0; i < this.fruits.size(); i++) {
        System.out.println(this.fruits.get(i).getName() + ": " + this.fruits.get(i).getQuantity());
      }
      System.out.println("--- Bases ---");
      for (int i = 0; i < this.bases.size(); i++) {
        System.out.println(this.bases.get(i).getName() + ": " + this.bases.get(i).getQuantity());
      }
      System.out.println("--- Cauldrons ---");
      System.out.println("Usable: " + getUsableCauldronCount() + " | Unusable: " + getUnusableCauldronCount());
    }

    /**
     * Compiles the player's fruit inventory into a formatted string for saving.
     * @return A comma-separated String containing fruit names and quantities.
     * <p><b>Pre-conditions:</b> None.</p>
     * <p><b>Post-conditions:<b> A formatted data string is returned representing the fruits list.</p>
     */
    public String exportFruitData() {
      String data = "";
      for (int i = 0; i < this.fruits.size(); i++) {
        data += this.fruits.get(i).getName() + "=" + this.fruits.get(i).getQuantity() + ",";
      }
      return data;
    }

    /**
     * Compiles the player's base inventory into a formatted string for saving.
     * @return A comma-separated String containing base names and quantities.
     * <p><b>Pre-conditions:</b> None.</p>
     * <p><b>Post-conditions:<b> A formatted data string is returned representing the bases list.</p>
     */
    public String exportBaseData() {
      String data = "";
      for (int i = 0; i < this.bases.size(); i++) {
        data += this.bases.get(i).getName() + "=" + this.bases.get(i).getQuantity() + ",";
      }
      return data;
    }
  }
