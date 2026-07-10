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
  }

    public void addFruit(String name, int quantity){
      boolean found = false;
      for (int i = 0; i < this.fruits.size(); i++){
          if (this.fruits.get(i).getName().equals(name)){
              this.fruits.get(i).addQuantity(quantity);
              found = true;
          }
      }
      if (!found){
          this.fruits.add(new Ingredient(name, quantity)); 
      }
    }

    public boolean removeFruit(String name, int quantity){
        boolean success = false
        for (int i = 0; i < this.fruits.size(); i++){
          if (this.fruits.get(i).getName().equals(name)){
              success = this.fruits.get(i).deductQuantity(quantity);
          }
        }
        return success;
    }

    public void addBase(String name, int quantity){
        boolean found = false;
        for (int i = 0; i < this.bases.size(); i++){
            if (this.bases.get(i).getName().equals(name)){
                this.bases.get(i).addQuantity(quantity);
                found = true;
            }
        }
        if (!found) {
            this.bases.add(new Ingredient(name, quantity));
        }
    }
    
    public boolean removeBase(String name, int quantity) {
      boolean success = false;
      for (int i = 0; i < this.bases.size(); i++) {
        if (this.bases.get(i).getName().equals(name)) {
            success = this.bases.get(i).deductQuantity(quantity);
        }
      }
      return success;
    }
    
    public boolean checkIngredientAvailability(String name, int requiredQty, boolean isBase) {
      boolean available = false;
      ArrayList<Ingredient> listToCheck = isBase ? this.bases : this.fruits;
      for (int i = 0; i < listToCheck.size(); i++) {
        if (listToCheck.get(i).getName().equals(name) && listToCheck.get(i).getQuantity() >= requiredQty) {
          available = true;
        }
      }
      return available;
    }
    
    public int getUsableCauldronCount() {
      int count = 0;
      for (int i = 0; i < this.cauldrons.size(); i++) {
        if (this.cauldrons.get(i).checkUsability()) count++;
      }
      return count;
    }
    
    public int getUnusableCauldronCount() {
      return this.cauldrons.size() - getUsableCauldronCount();
    }
    
    public void ruinOneCauldron() {
      boolean ruined = false;
      for (int i = 0; i < this.cauldrons.size(); i++) {
        if (this.cauldrons.get(i).checkUsability()) {
          this.cauldrons.get(i).ruinCauldron();
          ruined = true;
        }
      }
    }
    
    public void blessOneCauldron() {
      boolean blessed = false;
      for (int i = 0; i < this.cauldrons.size(); i++) {
        if (!this.cauldrons.get(i).checkUsability()) {
          this.cauldrons.get(i).blessCauldron();
          blessed = true;
        }
      }
    }
    
    public void addCauldron() {
      this.cauldrons.add(new Cauldron());
    }
    
    public void displayInventory() {
      System.out.println("\n--- Fruits ---");
      for (int i = 0; i < this.fruits.size(); i++) {
        System.out.println(this.fruits.get(i).getName() + ": " + this.fruits.get(i).getQuantity());
      }
      System.out.println("--- Bases ---");
      for (int i = 0; i < this.bases.size(); i++) {
        System.out.println(this.bases.get(i).getName() + ": " + this.bases.get(i).getQuantity());
      }
      System.out.println("--- Cauldrons ---");
      System.out.println("Usable: " + getUsableCauldronCount() + " | Unusable: " + getUnusableCauldronCount());
    }
    
    public String exportFruitData() {
      String data = "";
      for (int i = 0; i < this.fruits.size(); i++) {
        data += this.fruits.get(i).getName() + "=" + this.fruits.get(i).getQuantity() + ",";
      }
      return data;
    }
    
    public String exportBaseData() {
      String data = "";
      for (int i = 0; i < this.bases.size(); i++) {
        data += this.bases.get(i).getName() + "=" + this.bases.get(i).getQuantity() + ",";
      }
      return data;
    }
  }
