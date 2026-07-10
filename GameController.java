/**
 * GameController.java
 * This file contains the GameController class, which serves as the core engine
 * for Potion Prodigy. It handles the main game loop, user menus, crafting mechanics, 
 * market interactions, and file input/output for saving and loading player states.
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Manages the primary flow, menus, and state of the game.
 * This class coordinates interactions between the Player, Market, and Recipe Compendium,
 * while processing console-based user inputs.
 */
public class GameController {

  /** The player entity currently engaged in the game. */
  private Player currentPlayer;

  /** The market instance where the player can buy and sell items. */
  private Market market;

  /** A comprehensive list of all available recipes in the game. */
  private ArrayList<Recipe> recipeCompendium;

  /** Tracks the number of potions brewed since the market was last refreshed. */
  private boolean loginBonusClaimed;

  /** Tracks the number of potions brewed since the market was last refreshed. */
  private int brewsSinceMarket;

  /** Scanner object used to read console input from the user. */
  private Scanner scanner;

  /**
   * Constructs a new gameController and creates the games' core system
   * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:</b> The scanner, recipe compendium, and market are created
   * Tracks are reset, and the compendium is loaded from the external file.</p>
   */
  public GameController() {
      this.scanner = new Scanner(System.in);
      this.recipeCompendium = new ArrayList<Recipe>();
      this.market = new Market();
      this.loginBonusClaimed = false;
      this.brewsSinceMarket = 0;
      loadCompendium();
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
        System.out.println("Warning: POTION COMPENDIUM.csv not found.");    
      }
  }

 /**
   * Initiates the game startup sequence, allowing the user to start a new game or load a save
   * <p><b>Pre-conditions:</b> The GameController must be created.</p>
   * <p><b>Post-conditions:</b> A valid Player object is created (either new or loaded)
   * and the game proceeds to the main menu loop.</p>
   */
  public void startGame() {
    boolean gameStarted = false;
    
    System.out.println("Welcome to Potion Prodigy!");

    // Keep prompting until a new game is created or a save is loaded
    while (!gameStarted) {
      System.out.println("[1] New Game");
      System.out.println("[2] Load Existing Save");
      System.out.print("Choose an option: ");
      String choice = scanner.nextLine();
      
      if (choice.equals("1")) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        this.currentPlayer = new Player(name);
        System.out.println("Welcome, " + name + "! Starting a new adventure...");
        gameStarted = true;
      }
      else if (choice.equals("2")) {
        System.out.print("Enter the name of your save file: ");
        String saveName = scanner.nextLine();
        
        boolean loadSuccess = loadSaveFile(saveName);
              
        if (loadSuccess) {
          System.out.println("Welcome back!");
          gameStarted = true;
        } 
        else {System.out.println("Save file not found. Please try again or start a New Game.");
             }
      } else {
        System.out.println("Invalid choice.");
      }
    }
    
      mainMenuLoop();
  }
  
  /**
   * Manages the main interactive loop of the game, displaying the primary options
   * <p><b>Pre-conditions:</b> A valid currentPlayer must exist.</p>
   * <p><b>Post-conditions:</b> The game contninues until the player chooses to exit, at which
   * point the game state is saved and the loop stops.</p>
   */
  public void mainMenuLoop() { //kyle
    boolean playing = true;
    
    while (playing) {
      System.out.println("\n=== Main Menu ===");
      System.out.println("Crystals: " + currentPlayer.getCrystals());
      System.out.println("[1] Brew Concoction");
      System.out.println("[2] Check Inventory");
      System.out.println("[3] Check Spellbook");
      System.out.println("[4] Visit Market");
      System.out.println("[5] Bless Cauldron");
      System.out.println("[6] Login Bonus");
      System.out.println("[7] Exit Game");
      System.out.print("What would you like to do? ");
      
      String choice = scanner.nextLine();

      // Route user to the appropriate sub-menus or actions based on the selection of the user
      if (choice.equals("1")) {
        brewMenu();
      } else if (choice.equals("2")) {
        currentPlayer.getInventory().displayInventory();
      } else if (choice.equals("3")) {
        currentPlayer.getSpellbook().displaySpellbook();
      } else if (choice.equals("4")) {
        visitMarket();
      } else if (choice.equals("5")) {
        blessCauldronLogic();
      } else if (choice.equals("6")) {
        claimLoginBonus();
      } else if (choice.equals("7")) {
        System.out.println("Saving game...");
        saveGame();
        System.out.println("Thank you for playing Potion Prodigy!");
        playing = false;
      } else {
        System.out.println("Invalid choice, please select 1-7.");
      }
    }
  }

  /**
   * Displays and manages the brewing sub menu.
   * <p><b>Pre-conditions:</b> A valid currentPlayer must be present.</p>
   * <p><b>Post-conditions:</b> Routes the user to either Recipe Mode, Creative Mode, or returns them to the main menu.</p>
   */
  private void brewMenu() { //kyle
    boolean brewing = true;
    
    while (brewing) {
      System.out.println("\n=== Brew Concoction ===");
      System.out.println("[1] Recipe Mode");
      System.out.println("[2] Creative Mode");
      System.out.println("[3] Cancel (Return to Main Menu)");
      System.out.print("Select an option: ");
      
      String choice = scanner.nextLine();
      
      if (choice.equals("1")) {
        recipeMode();
        brewing = false;
      }
      else if (choice.equals("2")) {
        creativeMode();
        brewing = false;
      }
      else if (choice.equals("3")) {
        System.out.println("Canceling brew...");
        brewing = false;
      }
      else {
        System.out.println("Invalid option. Please try again.");
      }
    }
  }

   /**
     * Initiates the recipe brewing process by prompting the player for a known recipe ID.
     * <p><b>Pre-conditions:</b> The player must have an initialized spellbook, and the global 
     * recipe compendium must be loaded.</p>
     * <p><b>Post-conditions:</b> If the recipe is valid and ingredients are sufficient, the 
     * ingredients are consumed, the potion is brewed and sold, and crystals are awarded. 
     * If invalid, the game state remains completely unchanged.</p>
     */
    private void recipeMode() {
        currentPlayer.getSpellbook().displaySpellbook();
        System.out.print("Enter Recipe ID to brew: ");
        
        try {
            // Attempt to parse the user input to prevent crashes from letters or blanks
            int id = Integer.parseInt(scanner.nextLine());

            // Validate that the player has actually discovered this recipe
            if (!currentPlayer.getSpellbook().hasRecipe(id)) {
                System.out.println("Error: Recipe not unlocked");
            } else {
                
                // Fetch the full recipe details from the global compendium
                Recipe targetRecipe = null;
                for (int i = 0; i < recipeCompendium.size(); i++) {
                    if (recipeCompendium.get(i).getId() == id) {
                        targetRecipe = recipeCompendium.get(i);
                    }
                }

                if (targetRecipe != null) {
                    boolean canBrew = true;
                    
                    // Verify that the player has the required base before proceeding
                    if (!currentPlayer.getInventory().checkIngredientAvailability(targetRecipe.getBaseName(), 1, true)) {
                        System.out.println("Error: Insufficient base ingredient.");
                        canBrew = false;
                    } else {
                        // Verify that the player has all required fruits
                        for (int i = 0; i < targetRecipe.getRequiredFruits().size(); i++) {
                            if (!currentPlayer.getInventory().checkIngredientAvailability(targetRecipe.getRequiredFruits().get(i), 1, false)) {
                                canBrew = false;
                            }
                        }
                        if (!canBrew) {
                            System.out.println("Error: Insufficient fruit ingredients.");
                        }
                    }

                    // If all ingredient checks pass, execute the transaction
                    if (canBrew) {
                        System.out.print("Confirm Brew? (Y/N): ");
                        if (scanner.nextLine().equalsIgnoreCase("Y")){
                            
                            // Deduct the exact amounts from the player's inventory
                            currentPlayer.getInventory().removeBase(targetRecipe.getBaseName(), 1);
                            for (int i = 0; i < targetRecipe.getRequiredFruits().size(); i++) {
                                currentPlayer.getInventory().removeFruit(targetRecipe.getRequiredFruits().get(i), 1);
                            }
                            
                            // Award the player the selling price of the brewed potion
                            System.out.println("Successfully brewed " + targetRecipe.getName() + " and sold for " + targetRecipe.getPrice() + "!");
                            currentPlayer.addCrystals(targetRecipe.getPrice());
                            brewsSinceMarket++;
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            // Gracefully handle non-integer inputs and return to the menu
            System.out.println("Error: Invalid input. Please enter a valid numeric ID.");
        }
    }

 
   /**
     * Allows the player to experiment with combinations of bases and fruits to discover new recipes.
     * * <p><b>Pre-conditions:</b> The player must have at least 2 usable cauldrons in their inventory.</p>
     * <p><b>Post-conditions:</b> Ingredients are consumed. If a valid recipe is discovered, it is 
     * added to the spellbook, crystals are awarded, and the potion is sold. If invalid, a cauldron is ruined.</p>
     */
    private void creativeMode() {
        // Prevent soft-locking by ensuring the player always keeps at least one cauldron safe
        if (currentPlayer.getInventory().getUsableCauldronCount() <= 1){
            System.out.println("Error: You only have 1 usable cauldron left. Creative mode locked to prevent soft-lock");
        } else {
            System.out.print("Enter Concotion Base: ");
            String base = scanner.nextLine().toUpperCase();
            
            // Validate base existence before prompting for fruits
            if (!currentPlayer.getInventory().checkIngredientAvailability(base, 1, true)){
                System.out.println("Error: Insufficient base.");
            } else {
                System.out.print("Enter fruits (comma seperated, max 3): ");
                String[] fruits = scanner.nextLine().toUpperCase().split(",");
                
                // Enforce the strict maximum of 3 fruits per concoction
                if (fruits.length > 3) {
                    System.out.println("Error: Maximum of 3 fruits allowed per brew.");
                } else {
                    ArrayList<String> fruitList = new ArrayList<>();
                    boolean validFruits = true;

                    // Parse the input array, check for duplicates, and verify inventory counts
                    for (int i = 0; i < fruits.length && validFruits; i++) {
                        String f = fruits[i].trim();
                        if (fruitList.contains(f)) {
                            System.out.println("Error: cannot repeat ingredients.");
                            validFruits = false;
                        } else if (!currentPlayer.getInventory().checkIngredientAvailability(f, 1, false)){
                            System.out.println("Error: Insufficient " + f + ".");
                            validFruits = false;
                        } else {
                            fruitList.add(f);
                        }
                    }
                    
                    if (validFruits) {
                        System.out.print("Confirm brew? (Y/N): ");
                        if (scanner.nextLine().equalsIgnoreCase("Y")) {
                            
                            // Deduct the ingredients from the player's inventory
                            currentPlayer.getInventory().removeBase(base, 1);
                            for(int i = 0; i < fruitList.size(); i++) {
                                currentPlayer.getInventory().removeFruit(fruitList.get(i), 1);
                            }

                            boolean success = false;
                            
                            // Cross-reference the player's ingredients with the global recipe compendium
                            for (int i = 0; i < recipeCompendium.size() && !success; i++) {
                                if (recipeCompendium.get(i).matchesIngredients(base, fruitList)) {
                                    success = true;
                                    Recipe r = recipeCompendium.get(i);
                                    
                                    System.out.println("Success! Brewed " + r.getName() + "!");
                                    currentPlayer.addCrystals(r.getPrice());
                                    currentPlayer.getSpellbook().addRecipe(r);
                                    brewsSinceMarket++;
                                }
                            }

                            // Penalize the player for incorrect combinations
                            if (!success) {
                                System.out.println("Failure! the mixture exploded D: cauldron ruined :<");
                                currentPlayer.getInventory().ruinOneCauldron();
                                brewsSinceMarket++;
                            }
                        }   
                    }
                }
            }
        }
    }
  
 /**
   * Returns the base purchase price for a specific ingredient in the market
   * @param name the name of the ingredient.
   * @return the integer price of the ingredient, or 0 if not found
   * <p><b>Pre-conditions:</b> The name provided is a non-null String.</p>
   * <p><b>Post-conditions:</b> The correct integer price is returned.</p>
   */
  private int getIngredientPrice(String name) {
      int price;
      String upperName;
      price = 1;

      if (name != null) {
        upperName = name.toUpperCase();
        price = 0;

        switch (upperName) {
          case "STRAWBERRY":
            price = 125;
            break;
          case "ORANGE":
            price = 80;
            break;
          case "LEMON":
            price = 50;
            break;
          case "BANANA":
            price = 75;
            break;
          case "MANGO":
            price = 90;
            break;
          case "PINEAPPLE":
            price = 240;
            break;
          case "KIWI":
            price = 200;
            break;
          case "BLUEBERRY":
            price = 120;
            break;
          case "COCONUT":
            price = 180;
            break;
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
            price = 60;
            break;
          case "LOTION BASE":
            price = 150;
            break;
          case "CAULDRON":
            price = 3000;
            break;
          default:
            System.out.println("ERROR: Ingredient " + upperName + " not found in inventory!");
            price = -1;
            break;
        }
      } else {
        System.out.println("ERROR: Null pointer encountered for ingredient name!\n");
      }
      return price;
  }

 /**
   * Manages the player's interactions with the Market (buying and selling).
   * <p><b>Pre-conditions:</b> currentPlayer and market must be properly initialized.</p>
   * <p><b>Post-conditions:</b> Player inventory and crystal balance may be altered. Market slots may be emptied. 
   * If the brew threshold is met, the market is refreshed prior to entry.</p>
   */
private void visitMarket() { 
  // Refresh market if the player has brewed enough potions
      if (brewsSinceMarket >= 3) {
          market.refreshMarket();
          brewsSinceMarket = 0;
      }

      boolean inMarket = true;
      
      while (inMarket) {
          System.out.println("\n=== Welcome to the Market ===");
          System.out.println("[1] Buy Ingredients");
          System.out.println("[2] Sell Ingredients");
          System.out.println("[3] Exit Market");
          System.out.print("Choose an option: ");
          String choice = scanner.nextLine();
          
          if (choice.equals("1")) {
              market.displayMarket();
              System.out.print("Enter slots to buy (comma-separated, example: 1,4) or 'EXIT': ");
              String input = scanner.nextLine();

              if (!input.equalsIgnoreCase("EXIT")) {
                // Process multi-slot purchases
                  String[] choices = input.split(",");
                  for (int i = 0; i < choices.length; i++) {
                      try {
                          int slot = Integer.parseInt(choices[i].trim());
                          IngredientSlot s = market.getSlot(slot);

                          if (s.getQuantity() > 0 && !s.getItemName().equals("Empty")) {
                              int basePrice = getIngredientPrice(s.getItemName());
                              int totalPrice = basePrice * s.getQuantity();
                              String purchasedName = s.getItemName();
                              int purchasedQty = s.getQuantity();

                            // Attempt transaction and route item to correct inventory category
                              if (currentPlayer.deductCrystals(totalPrice)) {
                                  s.emptySlot();
                                  System.out.println("Success! Bought " + purchasedQty + "x " + purchasedName + " for " + totalPrice + " crystals!");

                                  if (purchasedName.equals("CAULDRON")) { currentPlayer.getInventory().addCauldron(); }
                                  else if (purchasedName.contains("BASE")) { currentPlayer.getInventory().addBase(purchasedName, purchasedQty); }
                                  else { currentPlayer.getInventory().addFruit(purchasedName, purchasedQty); }
                              } else {
                                  System.out.println("Error: Insufficient funds. You need " + totalPrice + " crystals for this stack");
                              }
                          } else {
                              System.out.println("Slot [" + slot + "] is already empty");
                          }
                      }  catch (Exception e) {
                          System.out.println("Invalid input skipped");
                      }
                  }
              }
          } else if (choice.equals("2")) {
              currentPlayer.getInventory().displayInventory();
              System.out.print("Enter exactly ONE ingredient name to sell (e.g. MANGO) or 'EXIT': ");
              String sellName = scanner.nextLine().toUpperCase();
              
              if (!sellName.equals("EXIT")) {
                  if (sellName.equals("CAULDRON")) {
                      System.out.println("Error: Cauldrons cannot be sold.");
                  } else {
                      System.out.print("Enter quantity to sell: ");
                      try {
                          int sellQty = Integer.parseInt(scanner.nextLine());
                          int sellPrice = getSellPrice(sellName) * sellQty;

                        // Check which inventory collection to remove the item from
                          boolean hasItem = false;
                          if (sellName.contains("BASE")) {
                              hasItem = currentPlayer.getInventory().removeBase(sellName, sellQty);
                          } else {
                              hasItem = currentPlayer.getInventory().removeFruit(sellName, sellQty);
                          }
                          
                          if (hasItem) {
                              currentPlayer.addCrystals(sellPrice);
                              System.out.println("Success! Sold " + sellQty + "x " + sellName + " for " + sellPrice + " crystals!");
                          } else {
                              System.out.println("Error: You don't have enough of that ingredient to sell.");
                          }
                      } catch (Exception e) {
                          System.out.println("Invalid quantity.");
                      }
                  }
              }
          } else if (choice.equals("3")) {
              System.out.println("Leaving the market...");
              inMarket = false;
          } else {
              System.out.println("Invalid choice. Please select 1, 2, or 3.");
          }
      }
  }
  
 /**
   * Returns the selling price for an ingredient, which is generally lower than the purchase price.
   * @param name The name of the ingredient to sell.
   * @return The integer sell price of the ingredient, or 0 if not found.
   * <p><b>Pre-conditions:</b> The provided name is a non-null String.</p>
   * <p><b>Post-conditions:</b> The correct integer sell price is returned.</p>
   */
  private int getSellPrice(String name) {
      name = name.toUpperCase();
      int price = 0;
      if (name.equals("STRAWBERRY")) { price = 25; }
      if (name.equals("ORANGE")) { price = 40; }
      if (name.equals("LEMON")) { price = 25; }
      if (name.equals("BANANA")) { price = 50; }
      if (name.equals("MANGO")) { price = 30; }
      if (name.equals("PINEAPPLE")) { price = 120; }
      if (name.equals("KIWI")) { price = 80; }
      if (name.equals("BLUEBERRY")) { price = 20; }
      if (name.equals("COCONUT")) { price = 90; }
      if (name.equals("SYRUP BASE")) { price = 10; }
      if (name.equals("BUBBLE BASE")) { price = 20; }
      if (name.equals("PERFUME BASE")) { price = 50; }
      if (name.equals("MILK BASE")) { price = 15; }
      if (name.equals("LOTION BASE")) { price = 25; }
      return price;
  }

  /**
   * Grants the player a random ingredient once per session.
   * <p><b>Pre-conditions:</b> loginBonusClaimed must be tracked for the current session.</p>
   * <p><b>Post-conditions:</b> If unclaimed, a random item is added to the players inventory 
   * and the flag is set to true. If already claimed, no action is taken.</p>
   */
  private void claimLoginBonus() { //kyle
    if (this.loginBonusClaimed == true) {
      System.out.println("You have already claimed your login bonus for this session!");
    } else {
    
    String[] possibleItems = {"STRAWBERRY", "ORANGE", "LEMON", "BANANA", "MANGO", 
                                "PINEAPPLE", "KIWI", "BLUEBERRY", "COCONUT", "SYRUP BASE",
                                "BUBBLE BASE", "PERFUME BASE", "MILK BASE", "LOTION BASE"};
    
    Random rand = new Random();
    int randomIndex = rand.nextInt(possibleItems.length);
    String randomItem = possibleItems[randomIndex];

    // Route to correct inventory type
    if (randomItem.contains("BASE")) {
      currentPlayer.getInventory().addBase(randomItem, 1);
    } else {
      currentPlayer.getInventory().addFruit(randomItem, 1);
    }
    
    this.loginBonusClaimed = true;
    System.out.println("Login Bonus Claimed! You received 1x " + randomItem + ".");
    }
  }

  /**
   * Allows the player to repair a ruined cauldron for a crystal fee.
   * <p><b>Pre-conditions:</b> The player must have at least one unusable cauldron and enough crystals (1000).</p>
   * <p><b>Post-conditions:</b> If conditions are met, 1000 crystals are deducted and one cauldron is restored.</p>
   */
  private void blessCauldronLogic() { //kyle
    int brokenCauldrons = currentPlayer.getInventory().getUnusableCauldronCount();
    
    if (brokenCauldrons == 0) {
      System.out.println("You have no broken cauldrons to bless.");
    } else {
    
    System.out.println("Blessing a cauldron costs 1000 crystals. Proceed? (Y/N)");
    String choice = scanner.nextLine();
    
      if (choice.equalsIgnoreCase("Y")) {
        if (currentPlayer.deductCrystals(1000)) {
          currentPlayer.getInventory().blessOneCauldron();
          System.out.println("Success! A cauldron has been blessed and is ready to use.");
        } else {
          System.out.println("Error: You don't have enough crystals to bless a cauldron.");
        }
      }
    }
  }
  
  /**
   * Saves the current player state to a text file.
   * <p><b>Pre-conditions:</b> currentPlayer must be fully populated with valid state data.</p>
   * <p><b>Post-conditions:</b> A text file named "[PlayerName].txt" is created containing the player's 
   * crystals, inventory data, cauldron counts, and spell book data.</p>
   */
  private void saveGame() { //darshan
      boolean isSuccess = false;
      
      try {
          FileWriter fw = new FileWriter(currentPlayer.getName().trim() + ".txt");
          PrintWriter pw = new PrintWriter(fw);
          
          pw.println("NAME = " + currentPlayer.getName());
          pw.println("CRYSTALS = " + currentPlayer.getCrystals());
          pw.println(); // Empty line for readability
          
          pw.println("[INVENTORY]");
          
          // Fetch the comma-separated fruit string split it and write vertically
          String fruitData = currentPlayer.getInventory().exportFruitData();
          String[] fruits = fruitData.split(",");
          for (int i = 0; i < fruits.length; i++) {
              if (!fruits[i].trim().isEmpty()) {
                  pw.println(fruits[i].trim());
              }
          }
          
          // Fetch the comma-separated base strin split it and write vertically
          String baseData = currentPlayer.getInventory().exportBaseData();
          String[] bases = baseData.split(",");
          for (int i = 0; i < bases.length; i++) {
              if (!bases[i].trim().isEmpty()) {
                  pw.println(bases[i].trim());
              }
          }
          
          // Write cauldrons
          int usable = currentPlayer.getInventory().getUsableCauldronCount();
          int unusable = currentPlayer.getInventory().getUnusableCauldronCount();
          pw.println("TOTAL CAULDRONS = " + (usable + unusable));
          pw.println("USABLE CAULDRONS = " + usable);
          pw.println(); // Empty line for readability
          
    
          pw.println("[SPELLBOOK]");
          pw.println(currentPlayer.getSpellbook().exportSpellbookData());
          
          pw.close();
          isSuccess = true;
          
      } catch (IOException e) {
          System.out.println("Save failed. Error writing file.");
      }
      
      // Print the success message only if everything passed without throwing an error
      if (isSuccess) {
          System.out.println("Game saved successfully :3");
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
          
          // Check existence without an early return
          if (file.exists()) {
              Scanner fileScanner = new Scanner(file);
              String currentSection = "";
              int totalCauldrons = 0;
              int usableCauldrons = 0;

              while (fileScanner.hasNextLine()) {
                  String line = fileScanner.nextLine().trim();

                  // Only process lines that have text (avoids using 'continue')
                  if (!line.isEmpty()) {
                      
                      if (line.startsWith("[") && line.endsWith("]")) {
                          currentSection = line;
                      } else {
                          
                          // Reconstruct basic player state
                          if (currentSection.isEmpty()) {
                              if (line.startsWith("NAME =")) {
                                  String playerName = line.split("=")[1].trim();
                                  this.currentPlayer = new Player(playerName);
                              } else if (line.startsWith("CRYSTALS =")) {
                                  int crystals = Integer.parseInt(line.split("=")[1].trim());
                                  this.currentPlayer.deductCrystals(5000);
                                  this.currentPlayer.addCrystals(crystals);
                              }
                          } 
                          
                          // Parse and populate Inventory
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
                          
                          // Parse and populate Spellbook
                          else if (currentSection.equals("[SPELLBOOK]")) {
                              String[] recipeIds = line.split(",");
                              for (int i = 0; i < recipeIds.length; i++) {
                                  String idStr = recipeIds[i].trim();
                                  
                                  if (!idStr.isEmpty()) {
                                      int id = Integer.parseInt(idStr);
                                      boolean foundRecipe = false;
                                      
                                      // Cross-reference the saved ID without using 'break'
                                      for (int j = 0; j < recipeCompendium.size() && !foundRecipe; j++) {
                                          if (recipeCompendium.get(j).getId() == id) {
                                              currentPlayer.getSpellbook().addRecipe(recipeCompendium.get(j));
                                              foundRecipe = true;
                                          }
                                      }
                                  }
                              }
                          }
                      }
                  }
              }
              
              // Parse and populate Cauldrons
              // Player starts with 3 by default, add only the extras beyond 3
              for (int i = 3; i < totalCauldrons; i++) {
                  currentPlayer.getInventory().addCauldron();
              }
              
              // Ruin the appropriate amount to match the save state
              int unusable = totalCauldrons - usableCauldrons;
              for (int i = 0; i < unusable; i++) {
                  currentPlayer.getInventory().ruinOneCauldron();
              }

              fileScanner.close();
              isSuccess = true;
          }
          
      } catch (Exception e) {
          // Optional: Print the error during testing so it doesn't fail silently
          System.out.println("Error reading save file."); 
      }
      
      return isSuccess;
  }
 }

