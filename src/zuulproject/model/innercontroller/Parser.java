package zuulproject.model.innercontroller;

import zuulproject.model.innercontroller.battle.innerbattlecontroller.*;
import java.util.StringTokenizer;
/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 * 
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

/**
 * Mainly the same parser as the one in Zuul-better_version2
 * author: Alok Swamy
 * 
 */
public class Parser {
    private CommandWords commands;  // holds all valid command words

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser() {
        commands = new CommandWords();
    }
    
    public Command getUserCommand(String s) {
        String word1 = null;
        String word2 = null;

        // Find up to two words on the line.
        StringTokenizer tokenizer = new StringTokenizer(s);
        
        if(tokenizer.hasMoreTokens()) {
            word1 = tokenizer.nextToken();      // get first word
            if(tokenizer.hasMoreTokens()) {
                word2 = tokenizer.nextToken();      // get second word
                // note: we just ignore the rest of the input line.
            }
        }
        return new Command(commands.getCommandWord(word1), word2);
    }

    public BattleCommand getUserBattleCommand(String s) {
        String word1 = null;

        StringTokenizer tokenizer = new StringTokenizer(s);
        
        if(tokenizer.hasMoreTokens())
            word1 = tokenizer.nextToken();

        return new BattleCommand(commands.getBattleCommandWord(word1));
    }
    
    
    /**
     * Show all commands. This is only here to allow Game to know all about the CommandWords
     * but it can find out about them through the Parser
     */
    
    public String showAllCommands() {
    	return commands.dspAllCommands();
    }
    
    public String showAllBattleCommands() {
    	return commands.dspAllBattleCommands();
    }
    
    public CommandWords getCommandWords() {
    	return commands;
    }
}
