/**
 * Market.java
 * This file contains the Market class, which manages the dynamic market 
 * slots and handles the generation of random items for the player to buy.
 */

import java.util.Random;

/**
 * Represents the game's marketplace where ingredients and cauldrons are sold.
 * This class manages an array of IngredientSlots, repopulating them randomly
 * upon refresh while ensuring limits like a maximum of one cauldron per restock.
 */
public class Market {

    /** An array of slots representing the available items for sale in the market. */
    private IngredientSlot[] slots;

    /** A flag to track whether a cauldron has already been spawned in the current market refresh. */
    private boolean cauldronInStock;

    /**
     * Constructs a new Market, initializing the slot array and populating it with random items.
     * <p><b>Pre-conditions:</b> None.</p>
     * <p><b>Post-conditions:</b> The slots array is initialized with a size of 8 and 
     * refreshMarket() is called to populate them.</p>
     */
    public Market(){
      this.slots = new IngredientSlot[8];
      refreshMarket();
    }

    /**
     * Randomly populates the market slots with ingredients and a potential cauldron.
     * <p><b>Pre-conditions:</b> The slots array must be initialized.</p>
     * <p><b>Post-conditions:</b> Each slot is filled with a randomly selected item and a random 
     * quantity (1-5). A maximum of one cauldron can spawn per refresh; if a second is rolled, 
     * it defaults to strawberries.</p>
     */
    public void refreshMarket(){ //randomly populates the market slots
      Random rand = new Random();
      this.cauldronInStock = false;
      String[] possibleItems = {"STRAWBERRY", "ORANGE", "LEMON", "BANANA", "MANGO", 
                                "PINEAPPLE", "KIWI", "BLUEBERRY", "COCONUT", "SYRUP BASE",
                                "BUBBLE BASE", "PERFUME BASE", "MILK BASE", "LOTION BASE", "CAULDRON"};

      for (int i = 0; i < this.slots.length; i++){
          String selectedItem = possibleItems[rand.nextInt(possibleItems.length)];

          if (selectedItem.equals("CAULDRON")) {
              if (!this.cauldronInStock) {
                  this.slots[i] = new IngredientSlot("CAULDRON", 1);
                  this.cauldronInStock = true;
              } else {
                    this.slots[i] = new IngredientSlot("STRAWBERRY", rand.nextInt(5) + 1);
                  }
              } else {
                  this.slots[i] = new IngredientSlot(selectedItem, rand.nextInt(5) + 1);
              }
            }
          }

    /**
     * Retrieves the ingredient slot at a specific index in the market.
     * @param index The integer index of the slot to retrieve.
     * @return The IngredientSlot object at the specified index.
     * <p><b>Pre-conditions:</b> The index must be a valid integer within the bounds of the slots array (0-7).</p>
     * <p><b>Post-conditions:</b> The corresponding IngredientSlot is returned without modifying the market state.</p>
     */
    public IngredientSlot getSlot(int index){
        return this.slots[index];
    }

    /**
     * Displays the current stock and quantities available in the market to the console.
     * <p><b>Pre-conditions:</b> None.</p>
     * <p><b>Post-conditions:</b> Iterates through the slots and prints out the item name and 
     * quantity for available items, or "[SOLD OUT]" for empty slots.</p>
     */
    public void displayMarket(){ //displays current stock and prices.
        System.out.println("\n=== Welcome to the Market! ===");
        for (int i = 0; i < this.slots.length; i++){
            IngredientSlot s = this.slots[i];

            if (s.getQuantity() > 0 && !"Empty".equals(s.getItemName())){ //check if quantity is > 0 AND the slot hasnt been flagged as empty
                System.out.println("[" + i + "] " + s.getItemName() + " | Qty: " + s.getQuantity());
            }
            else{
                System.out.println("[" + i + "] [SOLD OUT]");
            }
        }
    }
  }
