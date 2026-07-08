import java.util.Random;

public class Market {
    private IngredientSlot[] slots;
    private boolean cauldronInStock;

    public Market(){
      this.slots = new IngredientSlot[8]
      refreshMarket();
    }

    public void RefreshMarket(){
      Random rand = new Random();
      this.cauldronInStock = false;
      String[] possibleItems = {}
    }
  
}
