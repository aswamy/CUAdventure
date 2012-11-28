package zuulproject.model.innercontroller;

/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * This class holds information about a command that was issued by the user.
 * A command currently consists of two strings: a command word and a second
 * word (for example, if the command was "take map", then the two strings
 * obviously are "take" and "map").
 * 
 * The way this is used is: Commands are already checked for being valid
 * command words. If the user entered an invalid command (a word that is not
 * known) then the command word is <null>.
 *
 * If the command had only one word, then the second word is <null>.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

/**
 * Pretty much the same as the Command in Zuul-better-version2
 * A command is defined by an action (CommandTypes), and what the action is performed on (String)
 * Author: Alok Swamy
 */

public class Command
{
    private CommandTypes commandWord;
    private String secondWord;

    // Command that has 2 words, one is a command enum, other is a string
    public Command(CommandTypes firstWord, String secondWord)
    {
        this.commandWord = firstWord;
        this.secondWord = secondWord;
    }

    // returns the enum type
    public CommandTypes getCommandWord()
    {
        return commandWord;
    }

    // returns the object being affected by the enum 
    public String getSecondWord()
    {
        return secondWord;
    }

    // if command type is unknown, use UNKNOWN enum
    public boolean isUnknown()
    {
        return (commandWord == CommandTypes.UNKNOWN);
    }

    // returns the second word
    public boolean hasSecondWord()
    {
        return (secondWord != null);
    }
    
    // changes the second word to what is sent in (used to forcefully do a command)
    public void setSecondWord(String word)
    {
    	secondWord = word;
    }
}

