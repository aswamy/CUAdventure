package zuulproject.model.innercontroller.battle;

import zuulproject.model.innercontroller.battle.innerbattlecontroller.*;
import zuulproject.model.innercontroller.*;
import zuulproject.model.itemholder.*;

import java.util.Random;

/**
 * Combat class that makes two creatures fight to the death!!!
 * It takes two creatures, one player and one monster, and allows the player to perform certain commands on the monster defined by Parser
 * E.g. player can fight the monster, flee, etc.
 */

public class Combat {
	
	public static final String FIGHT_WON = "\nYou have won the battle! You continue...";
	public static final String NEW_LINE = "\n";	
	private Parser parser;
    private Player player;
    private Monster monster;

    public Combat(Player p1, Monster m1, Parser p) {
        player = p1;
        monster = m1;
        parser = p;
    }
    
    // processes the user's input and sends it off to a function that processes the command
    public String fight(String userInput) {
    	String fightStatus = "";
        BattleCommand bCommand = parser.getUserBattleCommand(userInput);
        fightStatus += processBattleCmd(bCommand);
        if (monster.isDead()) {
        	fightStatus += "Items dropped by monster:\n" + monster.getAllItems();
        	player.getRoom().removeMonster();
        	fightStatus += FIGHT_WON;
        }
    	return fightStatus;
    }
    
    // reads the command, and depending on the type of command, calls a function do deal with it
    private String processBattleCmd(BattleCommand command) {
    	BattleCommandTypes commandWord = command.getCommandWord();
    	String s = "";
   
        if (commandWord == BattleCommandTypes.UNKNOWN) {
            s += "Use proper battle commands. Type 'help' if you need assistance.";
        } else if (commandWord == BattleCommandTypes.FIGHT) {
            s+= creaturesBattle(true, true);
        } else if (commandWord == BattleCommandTypes.IDLE) {
            s+= "You used Splash. It does nothing." + NEW_LINE;
            s+= creaturesBattle(false, true);
        } else if (commandWord == BattleCommandTypes.STATUS) {
        	s+= player.creatureHP();
        	s+=NEW_LINE;
        	s+= monster.creatureHP();
        } else if (commandWord == BattleCommandTypes.FLEE) {
            s+= playerFlees();
        } else if (commandWord == BattleCommandTypes.HELP) {
            s+= dspBattleHelp();
        }
    	return s;
    }
    
    // when called, gives the user a 1/3 chance of fleeing
    /* if flee successful (does a undo to go back to room - the reason it undoes instead of just "move_back"
     * is because we don't want to undo a fight (so by undoing, it removes it from the stack))
     */
    private String playerFlees() {
    	String playerFleeStatus = "";
        Random generator = new Random();
    	int escape = generator.nextInt(3);
        
        if(escape==0) {
            playerFleeStatus += "You have fled!";
            playerFleeStatus += player.playerMove(new Command(CommandTypes.GO, "back"));
        } else {
            playerFleeStatus += "Your flee attempt was unsuccessful!";
            playerFleeStatus += creaturesBattle(false, true);
        }    	
    	return playerFleeStatus; 
    }
    
    // Takes two creatures, and makes them fight each other
    private String creatureAttacks(Creature attacker, Creature defender) {
        String displayFightStatus = "";
    	Random generator = new Random();
        int damageDone = attacker.totalAttack() + generator.nextInt((2*attacker.getDamageRange())+1) - attacker.getDamageRange();
        defender.reduceHP(damageDone);
        displayFightStatus += attacker.getName() + " has done " + damageDone + " damage!\n";
        displayFightStatus += defender.creatureHP() + NEW_LINE;
        
        return displayFightStatus;
    }
    
    // Takes a boolean value to tell whether player and monster is attacking, or only one, or none
    private String creaturesBattle(boolean playerFights, boolean monsterFights) {
    	String displayFightTurn = "";
        if (playerFights) {
        	displayFightTurn += creatureAttacks(player, monster);
        	if(monster.isDead()) return displayFightTurn;
        }
        if (monsterFights)
        	displayFightTurn += creatureAttacks(monster, player);
        return displayFightTurn;
    }
    
    /**
     * A procedure to help the player decide what to do during a battle 
     */
    private String dspBattleHelp() {
    	String s = "You are currently in a battle.\nYou cannot move past this room until you kill the monster.\n";
    	s+= "Your command words are:\n" + parser.showAllBattleCommands();
    	return s;
    }
}
