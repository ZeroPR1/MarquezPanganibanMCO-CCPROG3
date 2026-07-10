  /**
   * Cauldron.java
   * This file contains the cauldron class, which represents a brewing station
   * in the game. It manages the operational state of the cauldron for crafting.
   */ 

  /**
   * Represents a brewing station used by players to create potions or spells.
   * This class tracks wheter the cauldron is currently in a usable state
   * or if it requires restoration after a failed brewing attempt.
   */ 
public class Cauldron {
  /** Tracks the current operational status of the cauldron (true if usable, false if ruined). */
  private boolean isUsable;

  /**
   * Constructs a new Cauldron instance.
   * By default, a newly created cauldron is in a usable state.
   * * <p><b>Pre-conditions:</b> None.</p>
   * <p><b>Post-conditions:</b> A new cauldron object is instantiated with its isUsable attribute set to true.</p>
   */ 
  public Cauldron() {
    this.isUsable = true;
  }

  /**
   * Checks the current operational status of the cauldron.
   * * @return true if the cauldron can be used for brewing, false if otherwise
   * <p><b>Pre-conditions:</b> The cauldron object must be created.</p>
   * <p><b>Post-conditions:</b> The current usability state is returned. The state of the cauldron is unchanged</p>
   */ 
  public boolean checkUsability() {
    return this.isUsable;
  }

  /**
   * Marks the cauldron as unusable, typically triggered after a failed brewing attempt
   * * <p><b>Pre-conditions:</b> The cauldron object must be created.</p>
   * <p><b>Post-conditions:</b> The cauldron will be unusable, its' isUsable attribute will be set to false.</p>
   */   
  public void ruinCauldron() {
    this.isUsable = false;
  }

  /**
   * Restores a ruined cauldron back to a functional, usable state.
   * * <p><b>Pre-conditions:</b> The cauldron object must be created.</p>
   * <p><b>Post-conditions:</b> The cauldrons' isUsable attribute will be reset to true.</p>
   */ 
  public void blessCauldron() {
    this.isUsable = true;
  }  
}
