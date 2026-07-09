import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class GameController {
  private Player currentPlayer;
  private Market market;
  private ArrayList<Recipe> recipeCompendium;
  private boolean loginBonusClaimed;
  private int brewsSinceMarket;
  private Scanner scanner;

  public GameController() { //darshan
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
                  if (data.length > 4 && !data[4].isEmpty()) { r.addaddRequiredFruit(data[4]); }
                  if (data.length > 5 && !data[5].isEmpty()) { r.addaddRequiredFruit(data[5]); }
                  if (data.length > 6 && !data[6].isEmpty()) { r.addaddRequiredFruit(data[6]); }
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
    
  }

  public void mainMenuLoop() { //kyle
    
  }

  private void brewMenu() { //kyle
    
  }

  private void recipeMode() { //darshan
      currentPlayer.getSpellbook().displaySpellbook();
      System.out.print("Enter Recipe ID to brew: ");
      String id = scanner.nextLine();

      if (!currentPlayer.getSpellbook().hasRecipe(id)) {
        System.out.println("Error: Recipe not unlocked");
        return;
      }

      Recipe target = null;
      for (int i = 0; i < recipeCompendium.size(); i++) {
          if (recipeCompendium.get(i).getId().equals(id)) {
            target = recipeCompendium.get(i);
          }
      }

    if (target != null) {
        if (!currentPlayer.getInventory().checkIngredientAvailability(target.getBaseRequirement(), 1, true)) {
            System.out.println("Error: Insufficient ingredients.");
            return;
        }
        for (int i = 0; i < target.getFruitRequirements().size(); i++) {
            if (!currentPlayer.getInventory().checkIngredientAvailability(target.getFruitRequirements().get(i), 1, false)) {
                System.out.println("Error: Insufficient ingredients.");
                return;
            }
        }

        System.out.print("Confirm Brew? (Y/N): ");
        if (scanner.nextLine().equalsIgnoreCase("Y")){
            currentPlayer.getInventory().removeBase(target.getBaseRequirement(), 1);
            for (int i = 0; i < target.getFruitRequirements().size(); i++) {
                currentPlayer.getInventory().removeFruit(target.getFruitRequirements().get(i), 1);
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
    
  }

  private void visitMarket() { //darshan
      if (brewsSinceMarket >= 3) {
          market.refreshMarket();
          brewsSinceMarket = 0;
      }

      market.displayMarket();
      System.out.print("Enter slots to buy (comma-seperated, example: 1,4) or 'EXIT': ");
      String input = scanner.nextline();

      if (!input.equalsIgnoreCase("EXIT)) {
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

                      if (purchasedName.equals("CAULDRON)) { currentPlayer.get Inventory().addCauldron(); }
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
  }

  private void blessCauldronLogic() { //kyle
    
  }

  private void claimLoginBonus() { //kyle
    
  }

  private void saveGame() { //darshan
    
  }

  private boolean loadSaveFile(String name) { //darshan
    
  }

  
}
