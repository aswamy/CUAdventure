package zuulproject.model;

import zuulproject.event.*;
import zuulproject.event.GameActionFailEvent.FailedAction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.*;

import org.w3c.dom.Document;

/**
 * A game system makes new games, destroys old ones, tells when the game is over, and whether it is still running (or shutdown)
 * It is equivalent to a gaming console (Game class represents each game you put in the console)
 */

public class GameSystem {
	
	public static final String DEFAULT_GAMEPATH = String.format("%s\\%s",
			System.getProperty("user.dir"),
			"src\\zuulproject\\model\\saves\\level1.xml");
	
	private Game game;
	private List<GameChangeListener> listenerList;
	private String savePath;
	
	public GameSystem() {
		game = null;
		listenerList = new ArrayList<GameChangeListener>();
		savePath = "";
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
	
	protected void announceGameActionFailed(GameActionFailEvent e) {
		for (GameChangeListener g : listenerList) {
			if(g instanceof GameEventListener) ((GameEventListener)g).gameActionFailed(e);
		}
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
		openGame(DEFAULT_GAMEPATH);
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
	
	public void saveGame() {
		
	}
	
	public void saveAsGame() {
		
	}
	
	public void openGame(String path) {
		File file = new File(path);
		if (file.exists()) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
				DocumentBuilder d = factory.newDocumentBuilder();
				Document doc = d.parse(file);

				Game newGame = new Game(doc);
				game = newGame;
				announceGameBegins(new GameEvent(this.getGame()));				
				if(!(savePath.equals(DEFAULT_GAMEPATH))) savePath = path;
				
			} catch (Exception e) {
				announceGameActionFailed(new GameActionFailEvent(this, FailedAction.PARSEFILE));
			}
		} else {
			announceGameActionFailed(new GameActionFailEvent(this, FailedAction.OPENFILE));
		}
	}
}