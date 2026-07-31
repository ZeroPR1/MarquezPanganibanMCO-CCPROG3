public class ConcoctionBase extends Item {

  public ConcoctionBase(String name, int quantity) {
    super(name, quantity, determineBuyPrice(name), determineSellPrice(name));  
  }

  private static int determineBuyPrice(String name) {
    int price = 0;

    switch(name.toUpperCase()) {
      case "SYRUP BASE":
        price = 50;
        break;
      case "BUBBLE BASE":
        price = 80;
        break;
      case "PERFUME BASE":
        price = 250;
        break;
      case "MILK BASE":
        price = 250;
        break;
      case "LOTION BASE":
        price = 150;
        break;
      default
        price = 0;
        break;
    }
    return price;
  }

  private static int determineSellPrice(String name) {
    int price = 0;

    switch(name.toUpperCase()) {
      case "SYRUP BASE":
        price = 10;
        break;
      case "BUBBLE BASE":
        price = 20;
        break;
      case "PERFUME BASE":
        price = 50;
        break;
      case "MILK BASE":
        price = 15;
        break;
      case "LOTION BASE":
        price = 25;
        break;
      default
        price = 0;
        break;
    }
    return price;
  }

@Override
  public getItemType(){
    return "Concoction Base";
  }
}
