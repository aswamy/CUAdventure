package zuulproject.model;

import zuulproject.event.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A game system makes new games, destroys old ones, tells when the game is over, and whether it is still running (or shutdown)
 * It is equivalent to a gaming console (Game class represents each game you put in the console)
 */

public class GameSystem {
	private Game game;
	private String gameStatus;
	private List<GameListener> listenerList;
	
	public GameSystem() {
		game = null;
		gameStatus = new String();
		listenerList = new ArrayList<GameListener>();
	}
	
	// Add people who want to listen to the game
	public synchronized void addGameListener(GameListener g) {
		listenerList.add(g);
	}
	
	// removes people that don't want to listen to the game
	public synchronized void removeGameListener(GameListener g) {
		listenerList.remove(g);
	}
	
	// process a user input send as a string
	public void processCmd(String s) {
		if (gameFinished()) gameStatus = Game.GAME_END;
		else gameStatus = game.playGame(s);
		announceGameStatus(new GameEvent(this));
		if (gameFinished()) announceGameEnded();
	}
	
	// announce the game status to all that want to listen (GameListeners)
	protected void announceGameStatus(GameEvent e) {
		for (GameListener g : listenerList) g.commandProcessed(e);
	}
	
	// announce the game has ended :(
	protected void announceGameEnded() {
		for (GameListener g : listenerList) g.endGame();
	}
	
	// This is just for checking purposes to see if any of the buttons still interact with the game when this function is true
	private boolean gameFinished() {
		return game.isGameOver();
	}
	
	// Creates a brand new Zuul game and initializes all the rooms, and the player
	public void newGame() {
		Game newGame = new Game();
		newGame.initializeGame();
		game = newGame;
		gameStatus = game.dspWelcome();
		announceGameStatus(new GameEvent(this));
	}
	
	// Determines whether the game console is on, but no game is running
	public boolean gameRunning() {
		if (game != null) return true;
		return false;
	}
	
	// returns the status of the game
	public String getGameStatus() {
		return gameStatus;
	}
	
	// a String that contains the welcome message from the game
	public String dspGameWelcome() {
		return game.dspWelcome();
	}
	
	// returns the game
	public Game getGame()
	{
		return game;
	}
}
