package zuulproject.model;

import java.util.ArrayList;
import java.util.List;

import zuulproject.event.*;
import zuulproject.model.innercontroller.*;
import zuulproject.model.item.*;
import zuulproject.model.itemholder.*;
import zuulproject.model.innercontroller.battle.*;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

/**
 * The game class has been modied from the original Zuul
 * 
 * The game processes the commands received from the parsers
 * Currently, the game handles the 'everyday' commands, and the battle commands
 * 
 * Author: Alok Swamy
 */

public class Game {
	private boolean gameOver;
    private Parser parser;
    private Player p1;

    private List<GameChangeListener> listenerList;
    
    /**
     * Create the game and initialise its internal map.
     */
    public Game() {
        parser = new Parser();
        p1 = new Player();
        gameOver = true;
    
        listenerList = new ArrayList<GameChangeListener>();
        initializeGame();
    }

    public synchronized void addGameListenerList(List<GameChangeListener> g) {
    	listenerList.addAll(g);
    	p1.addGameListenerList(g);
    }
    
	public synchronized void addGameListener(GameChangeListener g) {
		listenerList.add(g);
		p1.addGameListener(g);
	}
	
	// removes people that don't want to listen to the game
	public synchronized void removeGameListener(GameChangeListener g) {
		listenerList.remove(g);
		p1.removeGameListener(g);
	}

	// announce the game change has occured to all that want to listen
	protected void announceGameChange(GameChangeEvent e) {
		for (GameChangeListener g : listenerList) g.gameCmdProcessed(e);
	}
	
	// announce that info is requested by the user
	protected void announceGameInfo(GameInfoEvent e) {
		for (GameChangeListener g : listenerList) {
			if (g instanceof GameEventListener) ((GameEventListener)g).gameInfoRequested(e);
		}
	}
	
	protected void announceGameBattle(GameEvent e) {
		for (GameChangeListener g : listenerList) {
			if (g instanceof GameEventListener) ((GameEventListener)g).gameBattleBegins(e);
		}
	}
    
    /**
     * Create all the rooms and link their exits together.
     */    
    public void initializeGame() {
        Room outside, theater, pub, lab, office;
        
        // create the rooms
        outside = new Room("outside the main entrance of the university", "Outside");
        theater = new Room("in a lecture theater", "Theater");
        theater.spawnMonster(new Monster("Vampire"));
        theater.getMonster().insertItem(new Weapon("SuperSword", 3));
        pub = new Room("in the campus pub", "Pub");
        lab = new Room("in a computing lab", "Lab");
        office = new Room("in the computing admin office", "Office");
        
        // initialise room exits

        outside.setExits(Exit.east, theater);
        outside.setExits(Exit.south, lab);
        outside.setExits(Exit.west, pub);
        outside.setExits(Exit.teleporter, office);
        theater.setExits(Exit.west, outside);
        pub.setExits(Exit.east, outside);
        lab.setExits(Exit.north, outside);
        lab.setExits(Exit.east, office);
        office.setExits(Exit.west, lab);

        //outside.insertItem(new Item("GoldenKey"));
        pub.insertItem(new Weapon("Sword", 2));
        lab.insertItem(new Consumable("SmallPotion", 100));
        theater.insertItem(new Powerup("mini_powerup", "Attack Boost: 2, Health Boost: 5", 2, 5));
        
        p1.setRoom(outside); 
        gameOver = false;
    }

    /**
     *  processes user inputs and converts it into a command that the game can read (battle command or regular command)
     */    
    public void playGame(String userInput) {
    	if (!(p1.inBattle())) {
    		Command command = parser.getUserCommand(userInput);
    		processGameCmd(command);
    		if(p1.inBattle()) {
    			announceGameBattle(new GameEvent(p1.getRoom().getMonster()));
    		}
    	} else {
    		Combat combat = new Combat(p1, p1.getRoom().getMonster(), parser);
    		combat.addGameListenerList(listenerList);
    		combat.fight(userInput);
    	}
		if (p1.isDead()) {
            gameOver = true;
            announceGameInfo(new GameInfoEvent(null, CommandTypes.QUIT));
		}
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private void processGameCmd(Command command) {
    	CommandTypes commandWord = command.getCommandWord();
    	
        if (commandWord == CommandTypes.UNKNOWN) {
            announceGameInfo(new GameInfoEvent(null, CommandTypes.UNKNOWN));
        } else if (commandWord == CommandTypes.HELP) {
            announceGameInfo(new GameInfoEvent(this.getParser().getCommandWords().getCommandList(), CommandTypes.HELP));
        } else if (commandWord == CommandTypes.QUIT) {
            gameOver = true;
            announceGameInfo(new GameInfoEvent(null, CommandTypes.QUIT));
        } else {
            p1.processPlayerCmd(command);
        }
    }

    /*
     * returns whether a game is over
     */
    public boolean isGameOver() {
    	return gameOver;
    }
    
    /*
     * returns the player
     */
    public Player getPlayer()
    {
    	return p1;
    }
 
    /*
     * returns the parser
     */
    public Parser getParser() {
    	return parser;
    }
}
