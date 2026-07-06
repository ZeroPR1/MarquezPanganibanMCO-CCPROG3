import java.util.ArrayList;

public class Recipe {
  private int concoctionId;
  private String recipeName;
  private String baseName;
  private int price;
  private ArrayList<String> requiredFruits;

  public Recipe(int concoctionId, String recipeName, String baseName) {
    this.concoctionId = concoctionId;
    this.recipeName = recipeName;
    this.baseName = baseName;
    this.requiredFruits = new ArrayList<>();
  }
  
  public void addRequiredFruit(String fruitName) {
    this.requiredFruits.add(fruitName);
  }
  
  public int getConcoctionId() {
    return this.concoctionId;
  }

  public int getBaseName(){
    return this.baseName
  }
  
  public String getRecipeName() {
    return this.recipeName;
  }

  public boolean matchesIngredients(String inputBase, ArrayList<String> inputFruits) {
    if (!this.baseName.equals(inputBase) || this.requiredFruits.size() != inputFruits.size()) {
      return false;
    }
    for (int i = 0; i < this.requiredFruits.size(); i++) {
      String required = this.requiredFruits.get(i);
      if (!inputFruits.contains(required)) {
        return false;
      }
    }
    return true;
  }
}
