/**
 * Player.java
 * * This file serves as the primary entity for a user in the game.
 * It manages the player's identity, currency, items and abilities.
 */

/**
 * Represents a player in the game
 * This class encapsulates player details such as name, crystal balance,
 * inventory of items and their spellbook.
 */
public class Player{
  /** The display name of the player */
  private String name;

  /** The current amount of in-game currency (crystals) the player has. */
  private int crystals;

  /** The inventory system managing the players items */
  private Inventory inventory;

  /** The spellbook managing the learnt spells of the player */
  private Spellbook spellbook;


  /** 
   * Constructs a new player with the specified name
   * Creates the player with a starting balance of 5000 crystals,
   * empty inventory and empty spellbook
   *
   * @param name The chosen name for the player
   * * <p><b>Pre-conditions:</b> The provided name should be a valid, created String. </p>
   * <p><b>Post-conditions:</b> A new Player object is created with the given name, 5000 crystals and a new Inventory and Spellbook
   */
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
  
