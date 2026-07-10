/**
 * Player.java
 * * This file serves as the primary entity for a user in the game.
 * It manages the player's identity, currency, items and abilities.
 */
public class Player{
  private String name;
  private int crystals;
  private Inventory inventory;
  private Spellbook spellbook;

  public Player(String name){
    this.name = name;
    this.crystals = 5000;
    this.inventory = new Inventory();
    this.spellbook = new Spellbook();
  }

  public String getName(){
    return this.name;
  }

  public int getCrystals(){
    return this.crystals;
  }

  public Inventory getInventory(){
    return this.inventory;
  }

  public Spellbook getSpellbook(){
    return this.spellbook;
  }

  public void addCrystals(int amount){
    if (amount > 0){
      this.crystals += amount;
    }
  }

  public boolean deductCrystals(int amount){
    if (amount > 0 && this.crystals >= amount){
        this.crystals -= amount;
        return true;
    }
    return false;
  }
}
  
