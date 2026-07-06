import java.util.ArrayList;

public class Recipe {
  private int concoctionId;
  private String recipeName;
  private String baseName;
  private ArrayList<String> requiredFruits;

  public Recipe(int conconctionId, String recipeName, String baseName) {
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
  public String getRecipeName() {
    return this.recipeName;
  }

}
