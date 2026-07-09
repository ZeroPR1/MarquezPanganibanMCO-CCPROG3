import java.util.ArrayList;

public class Spellbook{
  private ArrayList<Recipe> unlockedRecipes;

  public Spellbook() {
    this.unlockedRecipes = new ArrayList<Recipe>();
  }

  public void addRecipe(Recipe newRecipe){ //adds a recipe to the spell book if it wasnt already unlocked
    if (!hasRecipe(newRecipe.getId())){
      this.unlockedRecipes.add(newRecipe);
      System.out.println("Alchemy Success! New recipe added to spellbook: " + newRecipe.getName());
    }
  }

  public boolean hasRecipe(String id) {
    for (int i = 0; i < this.unlockedRecipes.size(); i++){
      if (this.unlockedRecipes.get(i).getId().equals(id)){
        return true;
      }
    }
    return false;
  }

  public void displaySpellbook() {
    if (this.unlockedRecipes.isEmpty()) {
      System.out.println("Your spellbook is currently empty.");
      return;
    }

    for (int i = 0; i < this.unlockedRecipes.size() - 1; i++) {
      for (int j = 0; j < this.unlockedRecipes.size() - i - 1; j++){
          int id1 = Integer.parseInt(this.unlockedRecipes.get(j).getId());
          int id2 = Integer.parseInt(this.unlockedRecipes.get(j + 1).getId());

        if (id1 > id2) {
          Recipe temp = this.unlockedRecipes.get(j);
          this.unlockedRecipes.set(j, this.unlockedRecipes.get(j + 1));
          this.unlockedRecipes.set(j + 1, temp);
        }
      }
    }

    System.out.println("\n=== Your Spellbook ===");
    for (int i = 0; i < this.unlockedRecipes.size(); i++) {
        Recipe r = this.unlockedRecipes.get(i);
        System.out.println("ID: " + r.getId() + " | " + r.getName() + " (Sells for " + r.getPrice() + ")");
    }  
  }
  
}
