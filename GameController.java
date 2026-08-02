/**
 * GameController.java
 * This file contains the GameController class, which serves as the core engine
 * for Potion Prodigy. It handles the main game loop, user menus, crafting mechanics, 
 * market interactions, and used to file input/output for saving and loading player states.
 * now it returns results to the GUI.[cite: 1]
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
 * while processing GUI-based user inputs.[cite: 1]
 */
public class GameController {

    private GameView view;

    /** The player entity currently engaged in the game.[cite: 1] */
    private Player currentPlayer;

    /** The market instance where the player can buy and sell items.[cite: 1] */
    private Market market;

    /** A comprehensive list of all available recipes in the game.[cite: 1] */
    private ArrayList<Recipe> recipeCompendium;

    /** Tracks if the login bonus has been claimed during the current session to prevent multiple claims.[cite: 1] */
    private boolean loginBonusClaimed;

    /** Tracks the number of potions brewed since the market was last refreshed.[cite: 1] */
    private int brewsSinceMarket;

    /**
     * Constructs a new gameController and creates the games' core system
     * <p><b>Pre-conditions:</b> None.</p>
     * <p><b>Post-conditions:</b> The scanner, recipe compendium, and market are created
     * Tracks are reset, and the compendium is loaded from the external file.</p>[cite: 1]
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

    // Initializes all UI listeners to bridge the GUI components to the game logic.
    private void setupListeners() {
        //navigation
        view.addNewGameListener(e -> view.showScreen("nameInput"));
        view.addExitMenuListener(e -> System.exit(0));
        
        //new hame creation
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

        // load/save
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

        view.addExitAndSaveListener(e -> {
            saveGame();
            view.showScreen("Menu");
        });

        // dashboard actions
        view.addBrewChoiceListener(e -> view.showScreen("BREW_CHOICE"));
        view.addVisitMarketListener(e -> {
            checkMarketRefresh();
            view.updateMarketTable(market.getMarketTableData());
            view.showScreen("Market");
        });
        view.addClaimBonusListener(e -> claimLoginBonus());
        view.addBlessCauldronListener(e -> blessCauldronLogic());

        // brewing modes
        view.addNavRecipeModeListener(e -> {
            view.updateSpellbookTable(currentPlayer.getSpellbook().getSpellbookTableData());
            view.showScreen("recipeMode");
        });
        
        view.addNavCreativeModeListener(e -> view.showScreen("creativeMode"));
        
        view.addNavBackToDashboardListener(e -> {
            refreshDashboard();
            view.showScreen("Dashboard");
        });
        
        // action executions
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

        view.addActionBrewCreativeListener(e -> {
            String base = view.getCreativeBaseInput().trim().toUpperCase();
            String fruitsInput = view.getCreativeFruitInput().trim().toUpperCase();
            String[] fruits = fruitsInput.isEmpty() ? new String[0] : fruitsInput.split(",");
            
            String result = creativeMode(base, fruits);
            view.displayMessage(result);
            refreshDashboard();
        });

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
    }

    //Refreshes all tables and labels on the dashboard to reflect current player state.
 
    private void refreshDashboard() {
        if (currentPlayer != null) {
            view.updateDashboardStats(currentPlayer.getName(), currentPlayer.getCrystals(), currentPlayer.getInventory().getUsableCauldronCount());
            
            view.updateTableTitles(currentPlayer.getName() + "'s Inventory", currentPlayer.getName() + "'s Spellbook");
            
            view.updateInventoryTable(currentPlayer.getInventory().getInventoryTableData());
            view.updateSpellbookTable(currentPlayer.getSpellbook().getSpellbookTableData());
        }
    }

    private void startNewGame() {
        this.currentPlayer.deductCrystals(this.currentPlayer.getCrystals());
        this.currentPlayer.addCrystals(5000);
        

        // Grant STRAWBERRY SYRUP components
        currentPlayer.getInventory().addBase("SYRUP BASE", 1);
        currentPlayer.getInventory().addFruit("STRAWBERRY", 1);

        // Unlock first recipe if it exists
        if (!recipeCompendium.isEmpty()) {
            currentPlayer.getSpellbook().addRecipe(recipeCompendium.get(0));
        }

        // Random starter ingredients
        String[] randomPool = {"BUBBLE BASE", "PERFUME BASE", "MILK BASE", "APPLE", "ORANGE"};
        Random rand = new Random();
        int numberOfRandomItems = rand.nextInt(3) + 2;
        
        for (int i = 0; i < numberOfRandomItems; i++) {
            String randomItem = randomPool[rand.nextInt(randomPool.length)];
            int randomQty = rand.nextInt(3) + 1; 
            if (randomItem.contains("BASE")) {
                currentPlayer.getInventory().addBase(randomItem, randomQty);
            } else {
                currentPlayer.getInventory().addFruit(randomItem, randomQty);
            }
        }
    }

    /**
     * Loads the recipes from the POTION COMPENDIUM.csv file.
     * <p><b>Pre-conditions:</b> The recipeCompendium list must be created.</p>
     * <p><b>Post-conditions:</b> If the file exists and is formally correctly, recipeCompendium
     * is populated with Recipe objects. If the file is missing, an error is printed and the list remains empty.</p>[cite: 1]
     */
    private void loadCompendium() { //darshan
        try {
            File file = new File("POTION COMPENDIUM.csv");
            Scanner fileScanner = new Scanner(file);

            //parse the csv line by line to extract the recipe components
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",", -1);
                if (data.length >= 4) {
                    Recipe r = new Recipe(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]));

                    //Dynamically adds fruits if they exist in the CSV columns
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
     * Initiates the recipe brewing process by prompting the player for a known recipe ID.
     * <p><b>Pre-conditions:</b> The player must have an initialized spellbook, and the global 
     * recipe compendium must be loaded.</p>
     * <p><b>Post-conditions:</b> If the recipe is valid and ingredients are sufficient, the 
     * ingredients are consumed, the potion is brewed and sold, and crystals are awarded. 
     * If invalid, the game state remains completely unchanged.</p>
     *
     * @param id: The ID of the recipe the player is trying to brew
     * @return String: containg the success message or the specific error encountered[cite: 1]
     */
    public String recipeMode(int id) {
        String statusMessage = "";

        if (!currentPlayer.getSpellbook().hasRecipe(id)) {
            statusMessage = "Error: Recipe not unlocked";
        } else {
            Recipe targetRecipe = null;
            for (int i = 0; i < recipeCompendium.size(); i++) {
                if (recipeCompendium.get(i).getId() == id) {
                    targetRecipe = recipeCompendium.get(i);
                }
            }

            if (targetRecipe != null) {
                boolean canBrew = true;

                if (!currentPlayer.getInventory().checkIngredientAvailability(targetRecipe.getBaseName(), 1, true)) {
                    statusMessage = "Error: Insufficient base ingredient.";
                    canBrew = false;
                } else {
                    for (int i = 0; i < targetRecipe.getRequiredFruits().size(); i++) {
                        if (!currentPlayer.getInventory().checkIngredientAvailability(targetRecipe.getRequiredFruits().get(i), 1, false)) {
                            canBrew = false;
                        }
                    }
                    if (!canBrew) {
                        statusMessage = "Error: Insufficient fruit ingredients.";
                    }
                }

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
     * added to the spellbook, crystals are awarded, and the potion is sold. If invalid, a cauldron is ruined.</p>[cite: 1]
     * 
     * @param base: The selected base ingredient
     * @param fruits: The array of selected fruit ingredients
     * @return String: containing the success message or the specific error encountered
     */
    private String creativeMode(String base, String[] fruits) { //darshan
        if (currentPlayer.getInventory().getUsableCauldronCount() <= 1){
            return "Error: You only have 1 usable cauldron left. Creative mode locked to prevent soft-lock.";
        } 
        
        if (!currentPlayer.getInventory().checkIngredientAvailability(base, 1, true)){
            return "Error: Insufficient base.";
        } 
        
        if (fruits.length > 3) {
            return "Error: Maximum of 3 fruits allowed per brew.";
        } 

        ArrayList<String> fruitList = new ArrayList<>();
        boolean validFruits = true;

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
        
        currentPlayer.getInventory().removeBase(base, 1);
        for(int i = 0; i < fruitList.size(); i++) {
            currentPlayer.getInventory().removeFruit(fruitList.get(i), 1);
        }

        boolean success = false;
        String statusMessage = "";
        
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

        if (!success) {
            statusMessage = "Failure! the mixture exploded D: cauldron ruined :<";
            currentPlayer.getInventory().ruinOneCauldron();
            brewsSinceMarket++;
        }
        
        return statusMessage;
    }

    /**
     * Checks if the market needs to be refreshed based on brews.
     */
    private void checkMarketRefresh() {
        if (brewsSinceMarket >= 3) {
            market.refreshMarket();
            brewsSinceMarket = 0;
            view.displayMessage("The market has refreshed its stock!");
        }
    }

    /**
     * Manages the player's interactions with the Market (buying and selling).
     * <p><b>Pre-conditions:</b> currentPlayer and market must be properly initialized.</p>
     * <p><b>Post-conditions:</b> Player inventory and crystal balance may be altered. Market slots may be emptied. 
     * If the brew threshold is met, the market is refreshed prior to entry.</p>[cite: 1]
     */
    private void processMarketPurchase(int slot) { 
        try {
            IngredientSlot s = market.getSlot(slot);
            int qty = s.getQuantity();

            if (qty > 0 && !s.getItemName().equals("Empty")) {
                String purchasedName = s.getItemName();
                int basePrice = 0;
                
                if (purchasedName.equals("CAULDRON")) {
                    basePrice = new Cauldron().getBuyPrice();
                } else if (purchasedName.contains("BASE")) {
                    basePrice = new ConcoctionBase(purchasedName, 1).getBuyPrice();
                } else {
                    basePrice = new Fruit(purchasedName, 1).getBuyPrice();
                }
                
                int totalPrice = basePrice * qty;

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
     * Grants the player a random ingredient once per session.
     * <p><b>Pre-conditions:</b> loginBonusClaimed must be tracked for the current session.</p>
     * <p><b>Post-conditions:</b> If unclaimed, a random item is added to the players inventory 
     * and the flag is set to true. If already claimed, no action is taken.</p>[cite: 1]
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
     * Allows the player to repair a ruined cauldron for a crystal fee.
     * <p><b>Pre-conditions:</b> The player must have at least one unusable cauldron and enough crystals (1000).</p>
     * <p><b>Post-conditions:</b> If conditions are met, 1000 crystals are deducted and one cauldron is restored.</p>[cite: 1]
     */
    private void blessCauldronLogic() { //kyle
        int brokenCauldrons = currentPlayer.getInventory().getUnusableCauldronCount();
        
        if (brokenCauldrons == 0) {
            view.displayMessage("You have no broken cauldrons to bless.");
        } else {
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
     * crystals, inventory data, cauldron counts, and spell book data.</p>[cite: 1]
     */
    private void saveGame() { //darshan
        try {
            FileWriter fw = new FileWriter(currentPlayer.getName().trim() + ".txt");
            PrintWriter pw = new PrintWriter(fw);
            
            pw.println("NAME = " + currentPlayer.getName());
            pw.println("CRYSTALS = " + currentPlayer.getCrystals());
            pw.println(); 
            pw.println("[INVENTORY]");
            
            String[] fruits = currentPlayer.getInventory().exportFruitData().split(",");
            for (int i = 0; i < fruits.length; i++) {
                if (!fruits[i].trim().isEmpty()) pw.println(fruits[i].trim());
            }
            
            String[] bases = currentPlayer.getInventory().exportBaseData().split(",");
            for (int i = 0; i < bases.length; i++) {
                if (!bases[i].trim().isEmpty()) pw.println(bases[i].trim());
            }
            
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
     * @return true if the save file was successfully loaded, false otherwise.[cite: 1]
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
                            if (currentSection.isEmpty()) {
                                if (line.startsWith("NAME =")) {
                                    this.currentPlayer = new Player(line.split("=")[1].trim());
                                } else if (line.startsWith("CRYSTALS =")) {
                                    int crystals = Integer.parseInt(line.split("=")[1].trim());
                                    this.currentPlayer.deductCrystals(this.currentPlayer.getCrystals());
                                    this.currentPlayer.addCrystals(crystals);
                                }
                            } 
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
                            else if (currentSection.equals("[SPELLBOOK]")) {
                                String[] recipeIds = line.split(",");
                                for (int i = 0; i < recipeIds.length; i++) {
                                    String idStr = recipeIds[i].trim();
                                    if (!idStr.isEmpty()) {
                                        int id = Integer.parseInt(idStr);
                                        boolean recipeFound = false; 
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
                
                for (int i = 3; i < totalCauldrons; i++) {
                    currentPlayer.getInventory().addCauldron();
                }
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
