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
	private List<GameChangeListener> listenerList;
	
	public GameSystem() {
		game = null;
		listenerList = new ArrayList<>();
	}
	
	// Add people who want to listen to the game
	public synchronized void addGameListener(GameChangeListener g) {
		listenerList.add(g);
	}
	
	// removes people that don't want to listen to the game
	public synchronized void removeGameListener(GameChangeListener g) {
		listenerList.remove(g);
	}
	
	// announce the game has started
	protected void announceGameBegins(GameEvent e) {
		for (GameChangeListener g : listenerList) g.gameBegins(e);
	}

	// process a user input send as a string
	public void processCmd(String s) {
		if (!(gameFinished())) game.playGame(s);
	}
		
	// This is just for checking purposes to see if any of the buttons still interact with the game when this function is true
	private boolean gameFinished() {
		return game.isGameOver();
	}
	
	// Creates a brand new Zuul game and initializes all the rooms, and the player
	public void newGame() {
		Game newGame = new Game();
		game = newGame;
		announceGameBegins(new GameEvent(this.getGame()));
	}
	
	// Determines whether the game console is on, but no game is running
	public boolean gameRunning() {
		if (game != null) return true;
		return false;
	}
	
	// returns the game
	public Game getGame() {
		return game;
	}
}