public class Player{
  private String name;
  private int crystals;
  private Inventory inventory;
  private Spellbook spellbook;

  public Player(string name){
    this.name = name;
    this.crystals = 5000;
    this.inventory = new Inventory();
    this.spellbook = new Spellbook();
  }

  public String getName(){
    return this.name;
  }
  
}
