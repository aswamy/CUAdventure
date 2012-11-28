package zuulproject.model.innercontroller;

/**
 * Enumeration class CommandTypes - an enum of all the commands in this game
 * 
 * @author Alok Swamy and Eshan
 */
public enum CommandTypes {

    GO("go"), QUIT("quit"), HELP("help"), LOOK("look"), PICKUP("pickup"), CONSUME("consume"), EXAMINE("examine"), UNKNOWN("?"), INVENTORY("inventory"), STATUS("status"), EQUIP("equip"), DEEQUIP("deequip"), APPLY("apply"), UNDO("undo"), REDO("redo");


    private String commandString;
    
    // returns the command type represented by string input
    CommandTypes(String commandString) {
        this.commandString = commandString;
    }
    
    // string representation of enum
    public String toString() {
        return commandString;
    }
}
