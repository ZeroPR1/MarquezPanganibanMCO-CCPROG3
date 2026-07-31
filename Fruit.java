/**
 * Represents the fruit ingredient used in potion concotions
 */
public class Fruit extends Item {

  public Fruit(String name, int quantity) {
    super(name, quantity, determineBuyPrice(name), determineSellPrice(name));
  }

  private static int determineBuyPrice(String name) {
    int price = 0;

    switch (name.toUpperCase()){
      case "STRAWBERRY":
        price = 120;
        break;
      case "ORANGE":
        price = 80;
        break;
      case "LEMON":
        price = 50;
        break;
      case "BANANA":
        price = 75;
        break;
      case "MANGO":
        price = 90;
        break;
      case "PINEAPPLE":
        price = 240;
        break;
      case "KIWI":
        price = 200;
        break;
      case "BLUEBERRY":
        price = 200;
        break;
      case "COCONUT":
        price = 180;
        break;
      default:
        price = 0;
        break;
    }
    return price;
  }

    private static int determineSellPrice(String name) {
    int price = 0;

    switch (name.toUpperCase()){
      case "STRAWBERRY":
        price = 25;
        break;
      case "ORANGE":
        price = 40;
        break;
      case "LEMON":
        price = 25;
        break;
      case "BANANA":
        price = 50;
        break;
      case "MANGO":
        price = 30;
        break;
      case "PINEAPPLE":
        price = 120;
        break;
      case "KIWI":
        price = 80;
        break;
      case "BLUEBERRY":
        price = 20;
        break;
      case "COCONUT":
        price = 90;
        break;
      default:
        price = 0;
        break;
    }
    return price;
  }
    @Override
      public String getItemTyipe() {
      return "Fruit";
      }
  }
