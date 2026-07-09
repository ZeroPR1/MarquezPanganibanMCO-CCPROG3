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
                  Recipe r = new Recipe(data[0], data[1], data[2], Integer.parseInt(data[3]));
                  if (data.length > 4 && !data[4].isEmpty()) { r.addFruitRequirement(data[4]); }
                  if (data.length > 5 && !data[5].isEmpty()) { r.addFruitRequirement(data[5]); }
                  if (data.length > 6 && !data[6].isEmpty()) { r.addFruitRequirement(data[6]); }
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
            currentPlayer.getInventory().removeFruit(fruitList.get(i), quantity: 1);
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
