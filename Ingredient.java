public class Ingredient{
  private String name;
  private int quantity;

// @param name = Name of the item
// @param quantity = starting quantity
  
  public Ingredient(String name, int quantity){
    this.name = name;
    this.quantity = quantity;
  }

  public String getName(){
    return this.name;
  }

  public int getQuantity(){
    return this.quantity;
  }

  public void addQuantity(int amount){
    if (amount > 0) this.quantity += amount;
  }

  public boolean deductQuantity(int amount){]
    boolean success = false;
    
    if (amount > 0 && this.quantity >= amount){
      this.quantity -= amount;
      success = true;
    }
    return success;
  }
}
