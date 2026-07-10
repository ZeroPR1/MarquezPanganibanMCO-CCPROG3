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
  
  /**
   * Retrieves the players name
   *
   * @return The current name of the player
   * * <p><b>Pre-conditions:</b> The Player object must be created already. </p>
   * <p><b>Post-conditions:</b> The player's name is returned, and the state of the player remains unchanged </p>
   */
  public String getName(){
    return this.name;
  }

  /**
   * Retrieves the players current crystal balance
   *
   * @return The integer value of the players crystals
   * * <p><b>Pre-conditions:</b> The Player object must be created already.</p>
   * <p><b>Post-conditions:</b> The current crystal balance is returned, the state of the player remains unchanged.</p>
   */
  public int getCrystals(){
    return this.crystals;
  }

  /**
   * Retrieves the players current inevntory
   *
   * @return The inventory associated with the player
   * * <p><b>Pre-conditions:</b> The Player object must be created already.</p>
   * <p><b>Post-conditions:</b> The current inventory is returned, the state of the player remains unchanged.</p>
   */  
  public Inventory getInventory(){
    return this.inventory;
  }

 /**
   * Retrieves the players current spellbook
   *
   * @return The spell book associated with the player
   * * <p><b>Pre-conditions:</b> The Player object must be created already.</p>
   * <p><b>Post-conditions:</b> The current spellbook is returned, the state of the player remains unchanged.</p>
   */ 
  public Spellbook getSpellbook(){
    return this.spellbook;
  }

  /**
   * Adds a specified amount of crystals to the players current balance
   *
   * @param amount The integer amount of crystals to add to the balance
   * * <p><b>Pre-conditions:</b> The amount must be a positive integer greater than 0.</p>
   * <p><b>Post-conditions:</b> If the amount is valid, the players crystal balance is increased by the specified amount. Otherwise, it'll remain unchanged.</p>
   */ 
  public void addCrystals(int amount){
    if (amount > 0){
      this.crystals += amount;
    }
  }

  /**
   * Deducts a specified amount of crystals from the players balance, only if they have sufficient funds.
   *
   * @param amount The integer amount of crystals to deduct to the balance
   * @return true if the deduction was successful, false if the amount is invalid or the player has insufficient crystals.
   * * <p><b>Pre-conditions:</b> The amount must be a positive integer thats greater than 0. The players current crystal balance must be greater than or equal to the amount.</p>
   * <p><b>Post-conditions:</b> If the pre-conditions are met, the crystal balance is decreased by the amount and true is returned. Otherwise the balance remains unchanged and false is returned.</p>
   */ 
  public boolean deductCrystals(int amount){
    if (amount > 0 && this.crystals >= amount){ //ensures the deduction amount is positive and the player can afford the transaction
        this.crystals -= amount;
        return true;
    }
    return false;
  }
}
  
