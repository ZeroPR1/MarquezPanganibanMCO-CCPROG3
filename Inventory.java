import java.util.ArrayList;

public class Inventory {
  private ArrayList<Ingredient> fruits;
  private ArrayList<Ingredient> bases;
  private ArrayList<Cauldron> cauldrons;

  public Inventory() {
    this.fruits = new ArrayList<Ingredient>();
    this.bases = new ArrayList<Ingredient>();
    this.cauldrons = new ArrayList<Cauldron>();
    for(int i = 0; i < 3; i++){
        this.cauldrons.add(new Cauldron());
    }

    public void addFruit(String name, int quantity){
      for (int i = 0; i < this.fruits.size(); i++){
          if (this.fruits.get(i).getname().equals(name)){
              this.fruits.get(i).addQuantity(quantity);
              return;
          }
      }
      this.fruits.add(new Ingredient(name, quantity));
    }

    public boolean removeFruit(String name, int quantity){
        for (int i = 0; i < this.fruits.size(); i++){
          if (this.fruits.get(i).getName().equals(name)){
              return this.fruits.get(i).deductQuantity(quantity);
          }
        }
        return false;
    }

    public void addBase(String name, int quantity){
        for (int i = 0; i < this.bases.size(); i++){
            if (this.bases.get(i).getName().equals(name)){
                this.bases.get(i).addQuantity(quantity);
                return;
            }
        }
        this.bases.add(new Ingredient(name, quantity));
    }
  }
}
