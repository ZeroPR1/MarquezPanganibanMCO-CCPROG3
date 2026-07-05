//note: kyle marquezs' laptop was not turning on, in turn, he dictated what to type through discord calls and messenger chats
public class IngredientSlot {
  private String itemName;
  private int quantity;

  public IngredientSlot(String itemName, int quantity) {
    this.itemName = itemName;
    this.quantity = quantity;
  }

  public String getItemName() {
    return this.itemName;
  }

  public int getQuantity() {
    return this.quantity;
  }

  public void emptySlot() {
    this.itemName = "Empty";
    this.quantity = 0;
  }
}
