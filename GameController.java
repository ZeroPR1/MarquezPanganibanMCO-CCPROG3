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

  private void creativeMode() { //darshan
    
  }

  private void recipeMode() { //darshan
    
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
