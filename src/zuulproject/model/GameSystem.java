package zuulproject.model;

import zuulproject.event.*;
import zuulproject.event.GameActionFailEvent.FailedAction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.*;

import org.w3c.dom.Document;

/**
 * A game system makes new games, destroys old ones, tells when the game is over, and whether it is still running (or shutdown)
 * It is equivalent to a gaming console (Game class represents each game you put in the console)
 */

public class GameSystem {
	
	public static final String DEFAULT_GAMEFILE = String.format("%s\\%s",
			System.getProperty("user.dir"),
			"src\\zuulproject\\level\\defaultgame.xml");
	
	public static final String DEFAULT_SAVEPATH = String.format("%s\\%s",
			System.getProperty("user.dir"),
			"src\\zuulproject\\model\\saves\\");
	
	private Game game;
	private List<GameChangeListener> listenerList;
	private String savePath;
	
	public GameSystem() {
		game = null;
		listenerList = new ArrayList<GameChangeListener>();
		savePath = DEFAULT_GAMEFILE;
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
		openGame(DEFAULT_GAMEFILE);
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
		if (savePath.equals(DEFAULT_GAMEFILE)) {
			announceGameActionFailed(new GameActionFailEvent(this, FailedAction.SAVEFILE, false));
		} else {
			saveAsGame(savePath);
		}
	}
	
	public void saveAsGameFile(String file) {
		saveAsGame(DEFAULT_SAVEPATH + file);
	}
	
	private void saveAsGame(String path) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			out.write(game.toXML());
			out.close();
			savePath=path;
			announceGameActionFailed(new GameActionFailEvent(this, FailedAction.SAVEFILE, true));			
		} catch (Exception e) {
			System.out.println("Unknown write Error");
		}		
	}
	
	public void openGameFile(String file) {
		openGame(DEFAULT_SAVEPATH + file);
	}
	
	private void openGame(String path) {
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
				if(path.equals(DEFAULT_GAMEFILE)) {
					savePath=DEFAULT_GAMEFILE;
				} else {
					announceGameActionFailed(new GameActionFailEvent(this, FailedAction.OPENFILE, true));
					savePath = path;
				}
			} catch (Exception e) {
				announceGameActionFailed(new GameActionFailEvent(this, FailedAction.PARSEFILE, false));
			}
		} else {
			announceGameActionFailed(new GameActionFailEvent(this, FailedAction.OPENFILE, false));
		}
	}
}