package zuulproject.model.innercontroller.battle.innerbattlecontroller;

/**
 * Enumeration class BattleCommandTypes - all possible commands during combat
 * 
 * @author Alok Swamy
 */
public enum BattleCommandTypes {

    FIGHT("fight"), FLEE("flee"), STATUS("status"), HELP("help"), IDLE("idle"), UNKNOWN("?");
    
    private String commandString;
    
    // returns a string represent of the enum
    BattleCommandTypes(String commandString) {
        this.commandString = commandString;
    }
    
    // lets it be able to print the enum
    public String toString() {
        return commandString;
    }
}
