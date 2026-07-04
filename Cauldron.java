public class Cauldron { //represents the brewing station
  private boolean isUsable;

  public Cauldron() { //makes a new cauldron, is usable by default ofcourse
    this.isUsable = true;
  }

  public boolean checkUsability() {
    return this.isUsable;
  }

  public void ruinCauldron() {//detects the cauldron as unsable after a failed brew
    this.isUsable = false;
  }

  public void blessCauldron() { //restores the cauldron so it can be usable again
    this.isUsable = true;
  }  
}
