/**
 * GameController.java
 * This file contains the GameController class, which serves as the core engine
 * for Potion Prodigy. It handles the main game loop, user menus, crafting mechanics, 
 * market interactions, and used to file input/output for saving and loading player states.
 * now it returns results to the GUI.
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Manages the primary flow, menus, and state of the game.
 * This class coordinates interactions between the Player, Market, and Recipe Compendium,
 * while processing GUI-based user inputs.
 */
public class GameController {

    private GameView view;

    /** The player entity currently engaged in the game. */
    private Player currentPlayer;

    /** The market instance where the player can buy and sell items. */
    private Market market;

    /** A comprehensive list of all available recipes in the game. */
    private ArrayList<Recipe> recipeCompendium;

    /** Tracks if the login bonus has been claimed during the current session to prevent multiple claims*/
    private boolean loginBonusClaimed;

    /** Tracks the number of potions brewed since the market was last refreshed. */
    private int brewsSinceMarket;

    /**
     * Constructs a new gameController and creates the games' core system
     * <p><b>Pre-conditions:</b> A valid GameView object must be provided.</p>
     * <p><b>Post-conditions:</b> The recipe compendium and market are created, 
     * tracks are reset, listeners are bound, and the compendium is loaded from the external file.</p>
     * 
     * @param view The primary GUI instance for the application.
     */
    public GameController(GameView view) {
        this.view = view;
        this.recipeCompendium = new ArrayList<Recipe>();
        this.market = new Market();
        this.loginBonusClaimed = false;
        this.brewsSinceMarket = 0;
        
        loadCompendium();
        setupListeners();
    }

    /**
     * Initializes all UI listeners to bridge the GUI components to the game logic.
     * <p><b>Pre-conditions:</b> The view and core game models must be instantiated.</p>
     * <p><b>Post-conditions:</b> Action listeners are bound to the view's interactive elements to handle routing and logic.</p>
     */
    private void setupListeners() {
        
    	//handle basic navigation by switching between menu screens
        view.addNewGameListener(e -> view.showScreen("nameInput"));
        view.addExitMenuListener(e -> System.exit(0));
        
        //process new game creation by validating the provided character name
        view.addStartJourneyListener(e -> {
            String name = view.getNameInput();
            if(name.trim().isEmpty()) {
                view.displayMessage("Please enter a valid name.");
                return;
            }
            this.currentPlayer = new Player(name);
            startNewGame();
            refreshDashboard();
            view.showScreen("Dashboard");
        });

      //load previous state by asking the user for a save file name
        view.addLoadGameListener(e -> {
            String name = JOptionPane.showInputDialog(null, "Enter the name of your save file:");
            if (name != null && !name.trim().isEmpty()) {
                if (loadSaveFile(name)) {
                    refreshDashboard();
                    view.showScreen("Dashboard");
                    view.displayMessage("Welcome back, " + name + "!");
                } else {
                    view.displayMessage("Save file not found or corrupted.");
                }
            }
        });

        // trigger a game save before routing the user back to the main menu
        view.addExitAndSaveListener(e -> {
            saveGame();
            view.showScreen("Menu");
        });

        //process the dashboard by routing the user to the appropriate sub-menus or triggering actions
        view.addBrewChoiceListener(e -> view.showScreen("BREW_CHOICE"));
        view.addVisitMarketListener(e -> {
            checkMarketRefresh();
            view.updateMarketTable(market.getMarketTableData());
            view.showScreen("Market");
        });
        view.addClaimBonusListener(e -> claimLoginBonus());
        view.addBlessCauldronListener(e -> blessCauldronLogic());

        //manages the specific brewing modes by handling navigation
        view.addNavRecipeModeListener(e -> {
            view.updateSpellbookTable(currentPlayer.getSpellbook().getSpellbookTableData());
            view.showScreen("recipeMode");
        });
        
        view.addNavCreativeModeListener(e -> view.showScreen("creativeMode"));
        
        view.addNavBackToDashboardListener(e -> {
            refreshDashboard();
            view.showScreen("Dashboard");
        });
        
        // start the brew by reading the specific recipe the user inputed
        view.addActionBrewRecipeListener(e -> {
            try {
                int id = Integer.parseInt(view.getRecipeIdInput());
                String result = recipeMode(id);
                view.displayMessage(result);
                refreshDashboard();
            } catch (NumberFormatException ex) {
                view.displayMessage("Please enter a valid numeric ID.");
            }
        });

        //make a custom brew by reading the chosen base and fruit list
        view.addActionBrewCreativeListener(e -> {
            String base = view.getCreativeBaseInput().trim().toUpperCase();
            String fruitsInput = view.getCreativeFruitInput().trim().toUpperCase();
            String[] fruits = fruitsInput.isEmpty() ? new String[0] : fruitsInput.split(",");
            
            String result = creativeMode(base, fruits);
            view.displayMessage(result);
            refreshDashboard();
        });

        //buy an item from the market by checking the chosen slot number
        view.addActionBuyMarketListener(e -> {
            try {
                int slot = Integer.parseInt(view.getMarketSlotInput());
                processMarketPurchase(slot);
                view.updateMarketTable(market.getMarketTableData());
                refreshDashboard();
            } catch (NumberFormatException ex) {
                view.displayMessage("Please enter a valid slot number.");
            }
        });
        
        //make a market sale by checking the item, calculating out the price, and removing it from the inventory
        view.addActionSellListener(e -> {
            try {
                String itemName = view.getSellItemNameInput();
                int qtyToSell = Integer.parseInt(view.getSellQuantityInput());

                if (qtyToSell <= 0) {
                    view.displayMessage("Quantity must be greater than 0.");
                    return;
                }

                boolean isBase = itemName.toUpperCase().contains("BASE");
                int sellPricePerItem = 0;
                
                //finds the price by making a temporary helper to read the standard pricing logic
                if (isBase) {
                    ConcoctionBase tempItem = new ConcoctionBase(itemName, 1);
                    sellPricePerItem = tempItem.getSellPrice(); 
                } else {
                    Fruit tempItem = new Fruit(itemName, 1);
                    sellPricePerItem = tempItem.getSellPrice();
                }

                if (sellPricePerItem == 0) {
                    view.displayMessage("Invalid item name. Cannot determine selling price.");
                    return;
                }

                boolean hasItem = false;
                
                //check and remove the item by following the exact method from inventory.java
                if (currentPlayer.getInventory().checkIngredientAvailability(itemName, qtyToSell, isBase)) {
                    if (isBase) {
                        currentPlayer.getInventory().removeBase(itemName, qtyToSell);
                    } else {
                        currentPlayer.getInventory().removeFruit(itemName, qtyToSell);
                    }
                    hasItem = true;
                }

                if (hasItem) {
                    int totalRevenue = sellPricePerItem * qtyToSell;
                    
                    currentPlayer.addCrystals(totalRevenue);
                    view.displayMessage("Sold " + qtyToSell + "x " + itemName + " for " + totalRevenue + " crystals!");
                    
                    // refresh market elements after a successful transaction
                    view.updateMarketTable(market.getMarketTableData());
                    refreshDashboard(); 
                } else {
                    view.displayMessage("You don't have enough " + itemName + " to sell.");
                }

            } catch (NumberFormatException ex) {
                view.displayMessage("Please enter a valid quantity.");
            }
        });
    }

    /**
     * Refreshes all tables and labels on the dashboard to reflect current player state.
     * <p><b>Pre-conditions:</b> currentPlayer must be fully initialized and the view must be active.</p>
     * <p><b>Post-conditions:</b> The dashboard UI is updated with the latest inventory, crystal balance, and spellbook data.</p>
     */
    private void refreshDashboard() {
        if (currentPlayer != null) {
            view.updateDashboardStats(currentPlayer.getName(), currentPlayer.getCrystals(), currentPlayer.getInventory().getUsableCauldronCount());
            
            view.updateTableTitles(currentPlayer.getName() + "'s Inventory", currentPlayer.getName() + "'s Spellbook");
            
            view.updateInventoryTable(currentPlayer.getInventory().getInventoryTableData());
            view.updateSpellbookTable(currentPlayer.getSpellbook().getSpellbookTableData());
        }
    }

    /**
     * Resets the player's inventory and populates it with starting resources for a new playthrough.
     * <p><b>Pre-conditions:</b> A valid currentPlayer must exist.</p>
     * <p><b>Post-conditions:</b> The player is given 5000 crystals, starting bases, fruits, and basic recipes.</p>
     */
    private void startNewGame() {
        // reset and give a starting capital (based on Sam-Paul.txt)
        this.currentPlayer.deductCrystals(this.currentPlayer.getCrystals());
        this.currentPlayer.addCrystals(5000);
        
        // provide the player with starting amounts of fruits (also based on Sam-Paul.txt)
        this.currentPlayer.getInventory().addFruit("STRAWBERRY", 3);
        this.currentPlayer.getInventory().addFruit("ORANGE", 2);
        this.currentPlayer.getInventory().addFruit("LEMON", 2);
        this.currentPlayer.getInventory().addFruit("BANANA", 3);
        this.currentPlayer.getInventory().addFruit("MANGO", 1);
        this.currentPlayer.getInventory().addFruit("KIWI", 1);
        this.currentPlayer.getInventory().addFruit("BLUEBERRY", 3);
        
        // provide the player with a starting amounts of bases (also also based on Sam-Paul.txt)
        this.currentPlayer.getInventory().addBase("SYRUP BASE", 3);
        this.currentPlayer.getInventory().addBase("BUBBLE BASE", 3);
        this.currentPlayer.getInventory().addBase("PERFUME BASE", 1);
        this.currentPlayer.getInventory().addBase("MILK BASE", 2);
        this.currentPlayer.getInventory().addBase("LOTION BASE", 2);
        
        // Grant the player recipes to start their journey :D (also also also based on Sam-Paul.txt)
        int[] startingRecipeIds = {1, 2, 16, 17, 36, 37, 55, 56};
        for (int recipeId : startingRecipeIds) {
            for (Recipe recipe : recipeCompendium) {
                if (recipe.getId() == recipeId) {
                    this.currentPlayer.getSpellbook().addRecipe(recipe);
                }
            }
        }
    }

    /**
     * Loads the recipes from the POTION COMPENDIUM.csv file.
     * <p><b>Pre-conditions:</b> The recipeCompendium list must be created.</p>
     * <p><b>Post-conditions:</b> If the file exists and is formally correctly, recipeCompendium
     * is populated with Recipe objects. If the file is missing, an error is printed and the list remains empty.</p>
     */
    private void loadCompendium() { //darshan
        try {
            File file = new File("POTION COMPENDIUM.csv");
            Scanner fileScanner = new Scanner(file);

            // get the recipe ingredients by reading the csv line by line
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",", -1);
                if (data.length >= 4) {
                    Recipe r = new Recipe(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]));

                    // add extra fruits by checking if they are listed in the CSV
                    if (data.length > 4 && !data[4].isEmpty()) { r.addRequiredFruit(data[4]); }
                    if (data.length > 5 && !data[5].isEmpty()) { r.addRequiredFruit(data[5]); }
                    if (data.length > 6 && !data[6].isEmpty()) { r.addRequiredFruit(data[6]); }
                    this.recipeCompendium.add(r);
                }
            }
            fileScanner.close();
        }  
        catch (Exception e) {
            System.out.println("Error loading compendium.");
        }
    }

    /**
     * Initiates the recipe brewing process by checking the player's spellbook and inventory.
     * <p><b>Pre-conditions:</b> The player must have an initialized spellbook, and the global 
     * recipe compendium must be loaded.</p>
     * <p><b>Post-conditions:</b> If the recipe is valid and ingredients are sufficient, the 
     * ingredients are consumed, the potion is brewed and sold, and crystals are awarded. 
     * If invalid, the game state remains completely unchanged.</p>
     *
     * @param id The ID of the recipe the player is trying to brew
     * @return A String containing the success message or the specific error encountered
     */
    public String recipeMode(int id) {
        String statusMessage = "";

        // confirm the player can make this by checking if they have actually learned the recipe
        if (!currentPlayer.getSpellbook().hasRecipe(id)) {
            statusMessage = "Error: Recipe not unlocked";
        } else {
            
            // get the full recipe details by looking it up in the potion compendium provided
            Recipe targetRecipe = null;
            for (int i = 0; i < recipeCompendium.size(); i++) {
                if (recipeCompendium.get(i).getId() == id) {
                    targetRecipe = recipeCompendium.get(i);
                }
            }

            if (targetRecipe != null) {
                boolean canBrew = true;

                // verify that the player has the required base before proceeding
                if (!currentPlayer.getInventory().checkIngredientAvailability(targetRecipe.getBaseName(), 1, true)) {
                    statusMessage = "Error: Insufficient base ingredient.";
                    canBrew = false;
                } else {
                    
                    // verify that the player has all required fruits
                    for (int i = 0; i < targetRecipe.getRequiredFruits().size(); i++) {
                        if (!currentPlayer.getInventory().checkIngredientAvailability(targetRecipe.getRequiredFruits().get(i), 1, false)) {
                            canBrew = false;
                        }
                    }
                    if (!canBrew) {
                        statusMessage = "Error: Insufficient fruit ingredients.";
                    }
                }

                // if all ingredient checks, complete the process
                if (canBrew) {
                    currentPlayer.getInventory().removeBase(targetRecipe.getBaseName(), 1);
                    for (int i = 0; i < targetRecipe.getRequiredFruits().size(); i++) {
                        currentPlayer.getInventory().removeFruit(targetRecipe.getRequiredFruits().get(i), 1);
                    }
                    
                    currentPlayer.addCrystals(targetRecipe.getPrice());
                    brewsSinceMarket++;
                    statusMessage = "Successfully brewed " + targetRecipe.getName() + " and sold for " + targetRecipe.getPrice() + "!";
                }
            } else {
                statusMessage = "Error: Recipe ID not found in compendium.";
            }
        }
        return statusMessage;
    }

    /**
     * Allows the player to experiment with combinations of bases and fruits to discover new recipes.
     * <p><b>Pre-conditions:</b> The player must have at least 2 usable cauldrons in their inventory.</p>
     * <p><b>Post-conditions:</b> Ingredients are consumed. If a valid recipe is discovered, it is 
     * added to the spellbook, crystals are awarded, and the potion is sold. If invalid, a cauldron is ruined.</p>
     * 
     * @param base The selected base ingredient
     * @param fruits The array of selected fruit ingredients
     * @return A String containing the success message or the specific error encountered
     */
    private String creativeMode(String base, String[] fruits) { //darshan
        
        // prevent soft-locking by ensuring the player always keeps at least one cauldron safe
        if (currentPlayer.getInventory().getUsableCauldronCount() <= 1){
            return "Error: You only have 1 usable cauldron left. Creative mode locked to prevent soft-lock.";
        } 
        
        // validate base exist before checking the fruits
        if (!currentPlayer.getInventory().checkIngredientAvailability(base, 1, true)){
            return "Error: Insufficient base.";
        } 
        
        // makes sure it only has the maximum of 3 fruits per concoction
        if (fruits.length > 3) {
            return "Error: Maximum of 3 fruits allowed per brew.";
        } 

        ArrayList<String> fruitList = new ArrayList<>();
        boolean validFruits = true;

        // review the requested items by reading the list, check for duplicates, and verify inventory counts
        for (int i = 0; i < fruits.length && validFruits; i++) {
            String f = fruits[i].trim();
            if (f.isEmpty()) continue;
            
            if (fruitList.contains(f)) {
                return "Error: cannot repeat ingredients.";
            } else if (!currentPlayer.getInventory().checkIngredientAvailability(f, 1, false)){
                return "Error: Insufficient " + f + ".";
            } else {
                fruitList.add(f);
            }
        }
        
        // deduct the ingredients from the player's inventory
        currentPlayer.getInventory().removeBase(base, 1);
        for(int i = 0; i < fruitList.size(); i++) {
            currentPlayer.getInventory().removeFruit(fruitList.get(i), 1);
        }

        boolean success = false;
        String statusMessage = "";
        
        // cross-reference the player's ingredients with the global recipe compendium
        for (int i = 0; i < recipeCompendium.size() && !success; i++) {
            if (recipeCompendium.get(i).matchesIngredients(base, fruitList)) {
                success = true;
                Recipe r = recipeCompendium.get(i);
                
                statusMessage = "Success! Brewed " + r.getName() + "!";
                currentPlayer.addCrystals(r.getPrice());
                currentPlayer.getSpellbook().addRecipe(r);
                brewsSinceMarket++;
            }
        }

        // punish the player for wrong comvos
        if (!success) {
            statusMessage = "Failure! the mixture exploded D: cauldron ruined :<";
            currentPlayer.getInventory().ruinOneCauldron();
            brewsSinceMarket++;
        }
        
        return statusMessage;
    }

    /**
     * Checks if the market needs to be refreshed based on the player's brewing activity.
     * <p><b>Pre-conditions:</b> The global market and views must be instantiated.</p>
     * <p><b>Post-conditions:</b> If the threshold is met, the market is refreshed and a message is displayed.</p>
     */
    private void checkMarketRefresh() {
        if (brewsSinceMarket >= 3) {
            market.refreshMarket();
            brewsSinceMarket = 0;
            view.displayMessage("The market has refreshed its stock!");
        }
    }

    /**
     * Processes a player's purchase from a specific market slot.
     * <p><b>Pre-conditions:</b> currentPlayer and market must be properly initialized.</p>
     * <p><b>Post-conditions:</b> Player inventory and crystal balance are altered. Market slot is emptied on success.</p>
     * 
     * @param slot The integer index of the market slot the player is buying from.
     */
    private void processMarketPurchase(int slot) { 
        try {
            IngredientSlot s = market.getSlot(slot);
            int qty = s.getQuantity();

            if (qty > 0 && !s.getItemName().equals("Empty")) {
                String purchasedName = s.getItemName();
                int basePrice = 0;
                
                // determine the correct object class to pull the base buy price from
                if (purchasedName.equals("CAULDRON")) {
                    basePrice = new Cauldron().getBuyPrice();
                } else if (purchasedName.contains("BASE")) {
                    basePrice = new ConcoctionBase(purchasedName, 1).getBuyPrice();
                } else {
                    basePrice = new Fruit(purchasedName, 1).getBuyPrice();
                }
                
                int totalPrice = basePrice * qty;

                // attempt transaction and route item to correct inventory category
                if (currentPlayer.deductCrystals(totalPrice)) {
                    s.emptySlot(); // Completely empties the slot enforcing the buy-out
                    
                    view.displayMessage("Success! Bought the entire stock of " + qty + "x " + purchasedName + " for " + totalPrice + " crystals!");

                    if (purchasedName.equals("CAULDRON")) { 
                        for(int i=0; i<qty; i++) currentPlayer.getInventory().addCauldron(); 
                    } else if (purchasedName.contains("BASE")) { 
                        currentPlayer.getInventory().addBase(purchasedName, qty); 
                    } else { 
                        currentPlayer.getInventory().addFruit(purchasedName, qty); 
                    }
                } else {
                    view.displayMessage("Error: Insufficient funds. You need " + totalPrice + " crystals to buy out this slot.");
                }
            } else {
                view.displayMessage("Invalid slot or empty stock.");
            }
        } catch (Exception e) {
            view.displayMessage("Error processing purchase.");
        }
    }

    /**
     * Grants the player a random ingredient once per session via the GUI.
     * <p><b>Pre-conditions:</b> loginBonusClaimed must be tracked for the current session.</p>
     * <p><b>Post-conditions:</b> If unclaimed, a random item is added to the players inventory 
     * and the flag is set to true. If already claimed, no action is taken.</p>
     */
    private void claimLoginBonus() { //kyle
        if (this.loginBonusClaimed) {
            view.displayMessage("You have already claimed your login bonus for this session!");
        } else {
            String[] possibleItems = {"STRAWBERRY", "ORANGE", "LEMON", "BANANA", "MANGO", 
                                      "PINEAPPLE", "KIWI", "BLUEBERRY", "COCONUT", "SYRUP BASE",
                                      "BUBBLE BASE", "PERFUME BASE", "MILK BASE", "LOTION BASE"};
            
            Random rand = new Random();
            String randomItem = possibleItems[rand.nextInt(possibleItems.length)];

            // route to correct inventory type
            if (randomItem.contains("BASE")) {
                currentPlayer.getInventory().addBase(randomItem, 1);
            } else {
                currentPlayer.getInventory().addFruit(randomItem, 1);
            }
            
            this.loginBonusClaimed = true;
            view.displayMessage("Login Bonus Claimed! You received 1x " + randomItem + ".");
            refreshDashboard();
        }
    }

    /**
     * Triggers a GUI dialogue allowing the player to repair a ruined cauldron for a crystal fee.
     * <p><b>Pre-conditions:</b> The player must have at least one unusable cauldron and enough crystals (1000).</p>
     * <p><b>Post-conditions:</b> If conditions are met and confirmed, 1000 crystals are deducted and one cauldron is restored.</p>
     */
    private void blessCauldronLogic() { //kyle
        int brokenCauldrons = currentPlayer.getInventory().getUnusableCauldronCount();
        
        if (brokenCauldrons == 0) {
            view.displayMessage("You have no broken cauldrons to bless.");
        } else {
            
            // format a multi-line confirmation prompt for the GUI dialof
            String prompt = "Bless a cauldron? Price: 1000 crystals\n"
                          + "Usable cauldrons: " + currentPlayer.getInventory().getUsableCauldronCount() + "\n"
                          + "Ruined cauldrons: " + brokenCauldrons + "\n"
                          + "Current Crystals: " + currentPlayer.getCrystals();
                          
            int choice = JOptionPane.showConfirmDialog(null, prompt, "Bless Cauldron", JOptionPane.YES_NO_OPTION);
            
            if (choice == JOptionPane.YES_OPTION) {
                if (currentPlayer.deductCrystals(1000)) {
                    currentPlayer.getInventory().blessOneCauldron();
                    view.displayMessage("Success! A cauldron has been blessed and is ready to use.");
                    refreshDashboard();
                } else {
                    view.displayMessage("Error: You don't have enough crystals to bless a cauldron.");
                }
            }
        }
    }
    
    /**
     * Saves the current player state to a text file.
     * <p><b>Pre-conditions:</b> currentPlayer must be fully populated with valid state data.</p>
     * <p><b>Post-conditions:</b> A text file named "[PlayerName].txt" is created containing the player's 
     * crystals, inventory data, cauldron counts, and spellbook data.</p>
     */
    private void saveGame() { //darshan
        try {
            FileWriter fw = new FileWriter(currentPlayer.getName().trim() + ".txt");
            PrintWriter pw = new PrintWriter(fw);
            
            pw.println("NAME = " + currentPlayer.getName());
            pw.println("CRYSTALS = " + currentPlayer.getCrystals());
            pw.println(); 
            pw.println("[INVENTORY]");
            
            // fetch the comma-separated fruit string, split it, and write vertically
            String[] fruits = currentPlayer.getInventory().exportFruitData().split(",");
            for (int i = 0; i < fruits.length; i++) {
                if (!fruits[i].trim().isEmpty()) pw.println(fruits[i].trim());
            }
            
            // fetch the comma-separated base string, split it, and write vertically
            String[] bases = currentPlayer.getInventory().exportBaseData().split(",");
            for (int i = 0; i < bases.length; i++) {
                if (!bases[i].trim().isEmpty()) pw.println(bases[i].trim());
            }
            
            // write total and usable cauldrons
            int usable = currentPlayer.getInventory().getUsableCauldronCount();
            int unusable = currentPlayer.getInventory().getUnusableCauldronCount();
            pw.println("TOTAL CAULDRONS = " + (usable + unusable));
            pw.println("USABLE CAULDRONS = " + usable);
            pw.println(); 
            
            pw.println("[SPELLBOOK]");
            pw.println(currentPlayer.getSpellbook().exportSpellbookData());
            
            pw.close();
            view.displayMessage("Game saved successfully :3");
        } catch (IOException e) {
            view.displayMessage("Save failed. Error writing file.");
        }
    }
    
    /**
     * Loads a saved player state from a specified text file.
     * <p><b>Pre-conditions:</b> A text file matching the provided name must exist in the project root directory.</p>
     * <p><b>Post-conditions:</b> If the file is found and successfully parsed, the currentPlayer object is 
     * instantiated and populated with the saved crystals, inventory data, cauldron counts, and spellbook data. 
     * Returns true if the load is successful, or false if the file does not exist or fails to read.</p>
     *
     * @param name The name of the save file to load (excluding the .txt extension).
     * @return true if the save file was successfully loaded, false otherwise.
     */
    private boolean loadSaveFile(String name) { //darshan
        boolean isSuccess = false;
        try {
            File file = new File(name.trim() + ".txt");
            
            if (file.exists()) {
                Scanner fileScanner = new Scanner(file);
                String currentSection = "";
                int totalCauldrons = 0;
                int usableCauldrons = 0;

                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine().trim();

                    if (!line.isEmpty()) {
                        if (line.startsWith("[") && line.endsWith("]")) {
                            currentSection = line;
                        } else {
                            
                            // reconstruct basic player state
                            if (currentSection.isEmpty()) {
                                if (line.startsWith("NAME =")) {
                                    this.currentPlayer = new Player(line.split("=")[1].trim());
                                } else if (line.startsWith("CRYSTALS =")) {
                                    int crystals = Integer.parseInt(line.split("=")[1].trim());
                                    this.currentPlayer.deductCrystals(this.currentPlayer.getCrystals());
                                    this.currentPlayer.addCrystals(crystals);
                                }
                            } 
                            
                            // parse and populate Inventory
                            else if (currentSection.equals("[INVENTORY]")) {
                                if (line.contains("=")) {
                                    String[] parts = line.split("=");
                                    String itemName = parts[0].trim();
                                    int quantity = Integer.parseInt(parts[1].trim());

                                    if (itemName.endsWith("BASE")) {
                                        currentPlayer.getInventory().addBase(itemName, quantity);
                                    } else if (itemName.equals("TOTAL CAULDRONS")) {
                                        totalCauldrons = quantity;
                                    } else if (itemName.equals("USABLE CAULDRONS")) {
                                        usableCauldrons = quantity;
                                    } else {
                                        currentPlayer.getInventory().addFruit(itemName, quantity);
                                    }
                                }
                            } 
                            
                            // parse and populate Spellbook
                            else if (currentSection.equals("[SPELLBOOK]")) {
                                String[] recipeIds = line.split(",");
                                for (int i = 0; i < recipeIds.length; i++) {
                                    String idStr = recipeIds[i].trim();
                                    if (!idStr.isEmpty()) {
                                        int id = Integer.parseInt(idStr);
                                        boolean recipeFound = false; 
                                        
                                        // cross-reference the saved ID with the global compendium
                                        for (int j = 0; j < recipeCompendium.size() && !recipeFound; j++) {
                                            if (recipeCompendium.get(j).getId() == id) {
                                                currentPlayer.getSpellbook().addRecipe(recipeCompendium.get(j));
                                                recipeFound = true; 
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                // parse and populate Cauldrons
                for (int i = 3; i < totalCauldrons; i++) {
                    currentPlayer.getInventory().addCauldron();
                }
                
                // ruin the appropriate amount to match the save state
                int unusable = totalCauldrons - usableCauldrons;
                for (int i = 0; i < unusable; i++) {
                    currentPlayer.getInventory().ruinOneCauldron();
                }

                fileScanner.close();
                isSuccess = true;
            }
        } catch (Exception e) {
            System.out.println("Error reading save file."); 
        }
        return isSuccess;
    }
}
