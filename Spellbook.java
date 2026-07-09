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

    
  }
  
}
