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

  public GameController() {
      this.scanner = new Scanner(System.in);
      this.recipeCompendium = new ArrayList<Recipe>();
      this.market = new Market();
      this.loginBonusClaimed = false;
      this.brewsSinceMarket = 0;
      loadCompendium();
  }

  private void loadCompendium() { //darshan
      try {
          File file = new File("POTION COMPENDIUM.csv");
          Scanner fileScanner = new Scanner(file);
          while (fileScanner.hasNextLine()) {
              String[] data = fileScanner.nextLine().split(",", -1);
              if (data.length >= 4) {
                  Recipe r = new Recipe(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]));
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
  
  public void startGame() { //kyle
    boolean gameStarted = false;
    
    System.out.println("Welcome to Potion Prodigy!");
    
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

  private void recipeMode() { //darshan
      currentPlayer.getSpellbook().displaySpellbook();
      System.out.print("Enter Recipe ID to brew: ");
      int id = Integer.parseInt(scanner.nextLine());

      if (!currentPlayer.getSpellbook().hasRecipe(id)) {
        System.out.println("Error: Recipe not unlocked");
        return;
      }

      Recipe target = null;
      for (int i = 0; i < recipeCompendium.size(); i++) {
          if (recipeCompendium.get(i).getId() == id) {
            target = recipeCompendium.get(i);
          }
      }

    if (target != null) {
        if (!currentPlayer.getInventory().checkIngredientAvailability(target.getBaseName(), 1, true)) {
            System.out.println("Error: Insufficient ingredients.");
            return;
        }
        for (int i = 0; i < target.getRequiredFruits().size(); i++) {
            if (!currentPlayer.getInventory().checkIngredientAvailability(target.getRequiredFruits().get(i), 1, false)) {
                System.out.println("Error: Insufficient ingredients.");
                return;
            }
        }

        System.out.print("Confirm Brew? (Y/N): ");
        if (scanner.nextLine().equalsIgnoreCase("Y")){
            currentPlayer.getInventory().removeBase(target.getBaseName(), 1);
            for (int i = 0; i < target.getRequiredFruits().size(); i++) {
                currentPlayer.getInventory().removeFruit(target.getRequiredFruits().get(i), 1);
            }
            System.out.println("Successfully brewed " + target.getName() + " and sold for " + target.getPrice() + "!");
            currentPlayer.addCrystals(target.getPrice());
            brewsSinceMarket++;
        }
    }
}
  
  private void creativeMode() { //darshan
      if (currentPlayer.getInventory().getUsableCauldronCount() <= 1){
          System.out.println("Error: You only have 1 usable cauldron left. Creative mode locked to prevent soft-lock");
          return;
      }

      System.out.print("Enter Concotion Base: ");
      String base = scanner.nextLine().toUpperCase();
      if (!currentPlayer.getInventory().checkIngredientAvailability(base, 1, true)){
          System.out.println("Error: Insufficient base.");
          return;
      }

      System.out.print("Enter fruits (comma seperated, max 3): ");
      String[] fruits = scanner.nextLine().toUpperCase().split(",");
      ArrayList<String> fruitList = new ArrayList<>();

      for (int i = 0; i < fruits.length && i < 3; i++) {
          String f = fruits[i].trim();
          if (fruitList.contains(f)) {
              System.out.println("Error: cannot repeat ingredients.");
              return;
          }
          if (!currentPlayer.getInventory().checkIngredientAvailability(f, 1, false)){
              System.out.println("Error: Insufficient " + f + ".");
              return;
          }
          fruitList.add(f);
      }

      System.out.print("Confirm brew? (Y/N): ");
      if (scanner.nextLine().equalsIgnoreCase("Y")) {
          currentPlayer.getInventory().removeBase(base, 1);
          for(int i = 0; i < fruitList.size(); i++) {
            currentPlayer.getInventory().removeFruit(fruitList.get(i), 1);
          }

          boolean success = false;
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

        if (!success) {
            System.out.println("Failure! the mixture exploded D: cauldron ruined :<");
            currentPlayer.getInventory().ruinOneCauldron();
            brewsSinceMarket++;
        }
      }   
  }

  private int getIngredientPrice(String name) {//kyle
    name = name.toUpperCase();
    
    if (name.equals("STRAWBERRY")) {
      return 125;
    } else if (name.equals("ORANGE")) {
      return 80;
    } else if (name.equals("LEMON")) {
      return 50;
    } else if (name.equals("BANANA")) {
      return 75;
    } else if (name.equals("MANGO")) {
      return 90;
    } else if (name.equals("PINEAPPLE")) {
      return 240;
    } else if (name.equals("KIWI")) {
      return 200;
    } else if (name.equals("BLUEBERRY")) {
      return 120;
    } else if (name.equals("COCONUT")) {
      return 180;
    } else if (name.equals("SYRUP BASE")) {
      return 50;
    } else if (name.equals("BUBBLE BASE")) {
      return 80;
    } else if (name.equals("PERFUME BASE")) {
      return 250;
    } else if (name.equals("MILK BASE")) {
      return 60;
    } else if (name.equals("LOTION BASE")) {
      return 150;
    } else if (name.equals("CAULDRON")) {
      return 3000;
    }   
      return 0;
  }

private void visitMarket() { 
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

  private int getSellPrice(String name) {
      name = name.toUpperCase();
      if (name.equals("STRAWBERRY")) return 25;
      if (name.equals("ORANGE")) return 40;
      if (name.equals("LEMON")) return 25;
      if (name.equals("BANANA")) return 50;
      if (name.equals("MANGO")) return 30;
      if (name.equals("PINEAPPLE")) return 120;
      if (name.equals("KIWI")) return 80;
      if (name.equals("BLUEBERRY")) return 20;
      if (name.equals("COCONUT")) return 90;
      if (name.equals("SYRUP BASE")) return 10;
      if (name.equals("BUBBLE BASE")) return 20;
      if (name.equals("PERFUME BASE")) return 50;
      if (name.equals("MILK BASE")) return 15;
      if (name.equals("LOTION BASE")) return 25;
      return 0;
  }

  
  private void claimLoginBonus() { //kyle
    if (this.loginBonusClaimed == true) {
      System.out.println("You have already claimed your login bonus for this session!");
      return;
    }
    
    String[] possibleItems = {"STRAWBERRY", "ORANGE", "LEMON", "BANANA", "MANGO", 
                                "PINEAPPLE", "KIWI", "BLUEBERRY", "COCONUT", "SYRUP BASE",
                                "BUBBLE BASE", "PERFUME BASE", "MILK BASE", "LOTION BASE"};
    
    Random rand = new Random();
    int randomIndex = rand.nextInt(possibleItems.length);
    String randomItem = possibleItems[randomIndex];
    
    if (randomItem.contains("BASE")) {
      currentPlayer.getInventory().addBase(randomItem, 1);
    } else {
      currentPlayer.getInventory().addFruit(randomItem, 1);
    }
    
    this.loginBonusClaimed = true;
    System.out.println("Login Bonus Claimed! You received 1x " + randomItem + ".");
  }
  
  private void blessCauldronLogic() { //kyle
    int brokenCauldrons = currentPlayer.getInventory().getUnusableCauldronCount();
    
    if (brokenCauldrons == 0) {
      System.out.println("You have no broken cauldrons to bless.");
      return;
    }
    
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

  private void saveGame() { //darshan
      try {
          FileWriter fw = new FileWriter(currentPlayer.getName() + ".txt");
          PrintWriter pw = new PrintWriter(fw);
          pw.println(currentPlayer.getName());
          pw.println(currentPlayer.getCrystals());
          pw.println("Fruits: " + currentPlayer.getInventory().exportFruitData());
          pw.println("Bases: " + currentPlayer.getInventory().exportBaseData());
          pw.println("Cauldrons:" + currentPlayer.getInventory().getUsableCauldronCount() + "," + currentPlayer.getInventory().getUnusableCauldronCount());
          pw.println("Spellbook:" + currentPlayer.getSpellbook().exportSpellbookData());
          pw.close();
          System.out.println("Game saved successfully :3");
      } catch (IOException e) {
          System.out.println("Save failed.");
      }
  }

  private boolean loadSaveFile(String name) { //darshan
      try {
          File file = new File(name + ".txt");
          if (!file.exists()) { return false; }

          Scanner fileScanner = new Scanner(file);
          String playerName = fileScanner.nextLine();
          this.currentPlayer = new Player(playerName);
          int crystals = Integer.parseInt(fileScanner.nextLine());
          this.currentPlayer.deductCrystals(5000);
          this.currentPlayer.addCrystals(crystals);

          String fruitLine = fileScanner.nextLine().replace("Fruits: ", "");
          if (!fruitLine.isEmpty()) {
              String[] fruits = fruitLine.split(",");
              for (int i = 0; i < fruits.length; i++) {
                  if (!fruits[i].isEmpty()) {
                      String[] parts = fruits[i].split("=");
                      currentPlayer.getInventory().addFruit(parts[0], Integer.parseInt(parts[1]));
                  }
              }
          }

          String baseLine = fileScanner.nextLine().replace("Bases: ", "");
          if (!baseLine.isEmpty()) {
              String[] bases = baseLine.split(",");
              for (int i = 0; i < bases.length; i++) {
                  if (!bases[i].isEmpty()) {
                      String[] parts = bases[i].split("=");
                      currentPlayer.getInventory().addBase(parts[0], Integer.parseInt(parts[1]));
                  }
              }
          }

          String cauldronLine = fileScanner.nextLine().replace("Cauldrons:", "");
          String[] cauldronCounts = cauldronLine.split(",");
          int usable = Integer.parseInt(cauldronCounts[0]);
          int unusable = Integer.parseInt(cauldronCounts[1]);
          int totalCauldrons = usable + unusable;

          for (int i = 3; i < totalCauldrons; i++) {
              currentPlayer.getInventory().addCauldron();
          }
          for (int i = 0; i < unusable; i++) {
              currentPlayer.getInventory().ruinOneCauldron();
          }

          String spellbookLine = fileScanner.nextLine().replace("Spellbook:", "");
          if (!spellbookLine.isEmpty()) {
              String[] recipeIds = spellbookLine.split(",");
              for (int i = 0; i < recipeIds.length; i++) {
                  if (!recipeIds[i].isEmpty()) {
                      int id = Integer.parseInt(recipeIds[i]);
                      for (int j = 0; j < recipeCompendium.size(); j++){
                          if (recipeCompendium.get(j).getId() == id) {
                              currentPlayer.getSpellbook().addRecipe(recipeCompendium.get(j));
                          }
                      }
                  }
              }
          }

          fileScanner.close();
          return true;

      } catch (Exception e) { return false; }
    }
}
