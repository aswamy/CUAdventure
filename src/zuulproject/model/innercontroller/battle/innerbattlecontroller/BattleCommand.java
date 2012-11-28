package zuulproject.model.innercontroller.battle.innerbattlecontroller;

/**
 * A command readable by the Combat class constructed by the Parser
 * BattleCommand tells the Combat class what the user has typed
 * 
 * (similar to Command class)
 * 
 * author: Alok Swamy
 */
public class BattleCommand {
    private BattleCommandTypes commandWord;

    /*
     * Battle command only has one word of type BattleCommandType
     */
    public BattleCommand(BattleCommandTypes firstWord)
    {
        this.commandWord = firstWord;
    }

    /*
     * returns the enum that represents the command word
     */
    public BattleCommandTypes getCommandWord()
    {
        return commandWord;
    }

    /**
     * @return true if this command was not understood.
     */
    public boolean isUnknown()
    {
        return (commandWord == BattleCommandTypes.UNKNOWN);
    }
}

