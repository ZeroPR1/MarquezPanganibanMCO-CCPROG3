import java.util.Random;

public class Market { //manages the dynamic market slots and transactions
    private IngredientSlot[] slots;
    private boolean cauldronInStock;

    public Market(){
      this.slots = new IngredientSlot[8];
      refreshMarket();
    }

    public void refreshMarket(){ //randomly populates the market slots
      Random rand = new Random();
      this.cauldronInStock = false;
      String[] possibleItems = {"STRAWBERRY", "ORANGE", "LEMON", "BANANA", "MANGO", 
                                "PINEAPPLE", "KIWI", "BLUEBERRY", "COCONUT", "SYRUP BASE",
                                "BUBBLE BASE", "PERFUME BASE", "MILK BASE", "LOTION BASE", "CAULDRON"};

      for (int i = 0; i < this.slots.length; i++){
          String selectedItem = possibleItems[rand.nextInt(possibleItems.length)];

          if (selectedItem.equals("CAULDRON")){
              if (!this.cauldronInStock){
                  this.slots[i] = new IngredientSlot("CAULDRON", 1);
                  this.cauldronInStock = true;
              } else {
                    this.slots[i] = new IngredientSlot("STRAWBERRY", rand.nextInt(5) + 1);
              } else {
                  this.slots[i] = new IngredientSlot(selectedItem, rand.nextInt(5) + 1);
              }
            }
          }

    public IngredientSlot getSlot(int index){
        return this.slots[index];
    }

    public void displayMarket(){ //displays current stock and prices.
        System.out.println("\n=== Welcome to the Market! ===");
        for (int i = 0; i < this.slots.length; i++){
            IngredientSlot s = this.slots[i];

            if (s.getQuantity() > 0 && !"Empty".equals(s.getItemName())){ //check if quantity is > 0 AND the slot hasnt been flagged as empty
                System.out.println("[" + i + "] " + s.getItemName() + " | Qty: " + s.getQuantity());
            }
            else{
                System.out.println("[" + i + "] [SOLD OUT]");
            }
        }
    }
  }
