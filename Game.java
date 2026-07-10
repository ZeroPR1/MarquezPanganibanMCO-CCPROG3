/**
 * The main driver class for the potion-brewing game.
 */

public class Game {

   /**
	 * Default constructor for the Game class.
	 */
	public Game() {
		
	}
	/**
     * The main entry point for the application.
     * @param args Command line arguments (not used).
     */
  public static void main(String[] args){
    GameController gameController = new GameController();
    gameController.startGame();
  }
}
