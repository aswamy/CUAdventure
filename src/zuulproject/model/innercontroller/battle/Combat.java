package zuulproject.model.innercontroller.battle;

import zuulproject.event.GameBattleChangeEvent;
import zuulproject.event.GameBattleInfoEvent;
import zuulproject.event.GameChangeListener;
import zuulproject.event.GameEvent;
import zuulproject.event.GameEventListener;
import zuulproject.model.innercontroller.battle.innerbattlecontroller.*;
import zuulproject.model.innercontroller.*;
import zuulproject.model.itemholder.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Combat class that makes two creatures fight to the death!!!
 * It takes two creatures, one player and one monster, and allows the player to perform certain commands on the monster defined by Parser
 * E.g. player can fight the monster, flee, etc.
 */

public class Combat {
	
	private Parser parser;
    private Player player;
    private Monster monster;

    private List<GameChangeListener> listenerList;
    
    public Combat(Player p1, Monster m1, Parser p) {
        player = p1;
        monster = m1;
        parser = p;
    
        listenerList = new ArrayList<GameChangeListener>();
    }

	public synchronized void addGameListener(GameChangeListener g) {
		listenerList.add(g);
	}
	
	// removes people that don't want to listen to the game
	public synchronized void removeGameListener(GameChangeListener g) {
		listenerList.remove(g);
	}
	
	public synchronized void addGameListenerList(List<GameChangeListener> g) {
		listenerList.addAll(g);
	}
    
	protected void announceGameBattleInfo(GameBattleInfoEvent e) {
		for (GameChangeListener g : listenerList) {
			if (g instanceof GameEventListener) ((GameEventListener)g).gameBattleInfoRequested(e);
		}
	}
	
	protected void announceGameBattleChange(GameBattleChangeEvent e) {
		for (GameChangeListener g : listenerList)
			g.gameBattleCmdProcessed(e);
	}
	
	protected void announceGameBattleEnded(GameEvent e) {
		for (GameChangeListener g : listenerList)
			g.gameBattleEnded(e);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Monster getMonster() {
		return monster;
	}
	
    // processes the user's input and sends it off to a function that processes the command
    public void fight(String userInput) {
        BattleCommand bCommand = parser.getUserBattleCommand(userInput);
        processBattleCmd(bCommand);
        if (monster.isDead()) {
        	player.getRoom().removeMonster();
        	announceGameBattleEnded(new GameEvent(this));
        }
    }
    
    // reads the command, and depending on the type of command, calls a function do deal with it
    private void processBattleCmd(BattleCommand command) {
    	BattleCommandTypes commandWord = command.getCommandWord();
   
        if (commandWord == BattleCommandTypes.UNKNOWN) {
        	announceGameBattleInfo(new GameBattleInfoEvent(this, BattleCommandTypes.UNKNOWN));
        } else if (commandWord == BattleCommandTypes.FIGHT) {
        	creaturesBattle(true, true);
            announceGameBattleChange(new GameBattleChangeEvent(this, BattleCommandTypes.FIGHT, true));
        } else if (commandWord == BattleCommandTypes.IDLE) {
        	creaturesBattle(false, true);
            announceGameBattleChange(new GameBattleChangeEvent(this, BattleCommandTypes.IDLE, true));
        } else if (commandWord == BattleCommandTypes.STATUS) {
        	announceGameBattleInfo(new GameBattleInfoEvent(this, BattleCommandTypes.STATUS));
        } else if (commandWord == BattleCommandTypes.FLEE) {
            boolean commandStatus = playerFlees();
            announceGameBattleChange(new GameBattleChangeEvent(this, BattleCommandTypes.FLEE, commandStatus));
        } else if (commandWord == BattleCommandTypes.HELP) {
            announceGameBattleInfo(new GameBattleInfoEvent(this.parser.getCommandWords(), BattleCommandTypes.HELP));
        }
    }
    
    // when called, gives the user a 1/3 chance of fleeing
    /* if flee successful (does a undo to go back to room - the reason it undoes instead of just "move_back"
     * is because we don't want to undo a fight (so by undoing, it removes it from the stack))
     */
    private boolean playerFlees() {
        Random generator = new Random();
    	int escape = generator.nextInt(3);
        
        if(escape==0) {
            player.processPlayerCmd(new Command(CommandTypes.GO, "back"));
            return true;
        }
        creaturesBattle(false, true);
        return false;
    }
    
    // Takes two creatures, and makes them fight each other
    private void creatureAttacks(Creature attacker, Creature defender) {
    	Random generator = new Random();
        int damageDone = attacker.totalAttack() + generator.nextInt((2*attacker.getDamageRange())+1) - attacker.getDamageRange();
        defender.reduceHP(damageDone);
    }
    
    // Takes a boolean value to tell whether player and monster is attacking, or only one, or none
    private void creaturesBattle(boolean playerFights, boolean monsterFights) {
        if (playerFights) {
        	creatureAttacks(player, monster);
        	if(monster.isDead()) return;
        }
        if (monsterFights) creatureAttacks(monster, player);
    }
}
