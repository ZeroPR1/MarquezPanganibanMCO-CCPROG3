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
     * Compiles the current stock and quantities available in the market into a string.
     * @return A formatted String representing the current market inventory.
     * <p><b>Pre-conditions:</b> None.</p>
     * <p><b>Post-conditions:</b> Iterates through the slots and returns a formatted string of the item name and 
     * quantity for available items, or "[SOLD OUT]" for empty slots.</p>
     */
    public String displayMarket(){ //returns current stock and prices as a String.
        String display = "\n=== Welcome to the Market! ===\n";
        for (int i = 0; i < this.slots.length; i++){
            IngredientSlot s = this.slots[i];

            if (s.getQuantity() > 0 && !"Empty".equals(s.getItemName())){ //check if quantity is > 0 AND the slot hasnt been flagged as empty
                display += "[" + i + "] " + s.getItemName() + " | Qty: " + s.getQuantity() + "\n";
            }
            else{
                display += "[" + i + "] [SOLD OUT]\n";
            }
        }
        return display;
    }

    /**
     * Converts the current market slots into a 2D array for GUI table rendering.
     * @return An Object[][] containing slot ID, ingredient name, quantity, and price.
     * <p><b>Pre-conditions:</b> None.</p>
     * <p><b>Post-conditions:</b> Returns a structured array of the market's current state.</p>
     */
    public Object[][] getMarketTableData() {
        Object[][] data = new Object[this.slots.length][4];
        for (int i = 0; i < this.slots.length; i++) {
            IngredientSlot s = this.slots[i];
            data[i][0] = i; 
            
            if (s.getQuantity() > 0 && !"Empty".equals(s.getItemName())) {
                String name = s.getItemName();
                data[i][1] = name;
                data[i][2] = s.getQuantity();
                
                int price = 0;
                if (name.equals("CAULDRON")) {
                    price = new Cauldron().getBuyPrice();
                } else if (name.contains("BASE")) {
                    price = new ConcoctionBase(name, 1).getBuyPrice();
                } else {
                    price = new Fruit(name, 1).getBuyPrice();
                }
                data[i][3] = price;
            } else {
                data[i][1] = "[SOLD OUT]";
                data[i][2] = 0;
                data[i][3] = "-";
            }
        }
        return data;
    }
  }
