package zuulproject.model.itemholder;

import zuulproject.model.innercontroller.*;
import java.util.*;
/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  The exits are labelled north, 
 * east, south, west.  For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

/**
 * This class possibly has a monster. It can be blocked so others cannot enter, or leave.
 * The room doesn't know about the Player, but the room is where the Player will be throughout the game
 * @author: Alok Swamy
 */

public class Room extends ItemHolder {
    
    public String description;
    private Map<Exit, Room> exits;
    private Monster monster;
    private boolean enterable;
    private String roomName;
    
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description, String roomName) {
    	this.roomName = roomName;
        this.description = description;
        exits = new HashMap<Exit, Room>();
        enterable = true;
        monster = null;
    }

    public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public boolean hasMonster() {
        if(!(monster==null)) return true;
        return false;
    }
    
    public void removeMonster() {
        items.addAll(monster.getItemList());
        monster = null;
    }
    
    /*
     * Puts a monster in the room when the game is being initiated.
     * We could allow the monsters to spawn in the middle of the game later on.
     */
    public void spawnMonster(Monster mon) {
        monster = mon;
        //exitable = false;
    }
    
    public Monster getMonster() {
        return monster;
    }
    
    /**
     * Define the exits of this room.  Every direction either leads
     * to another room
     */
    public void setExits(Exit roomName, Room roomType) {
        exits.put(roomName, roomType);
    }
    
    public Room getExitRoom(String exitString) {
    	Exit exitDoor;
    	try {
    		exitDoor = Exit.valueOf(exitString);
    	} catch(IllegalArgumentException e) {
    		exitDoor = null;
    	}
    	return exits.get(exitDoor);
    }
    public Room getExitRoom(Exit exit) {
        return exits.get(exit);
    }
    
    public List<String> getExitList() {
    	List<String> temp = new ArrayList<String>();
    	for(Exit e : exits.keySet()) temp.add(e.toString());
    	return temp;
    }
    
    // sees if this room in entered (currently not used)
    public Room roomEntered(String room) {
        return this;
    }
        
    // sets wheter you can enter this room
    public void setEnterable(boolean canEnter) {
        enterable = canEnter;
    }
    
    // sees if you can enter this room
    public boolean isEnterable() {
        return enterable;
    }

    /**
     * @return The description of the room.
     */
    public String getDescription()
    {
        return description;
    }
    
    public String toString()
    {
    	return this.roomName;
    }

}
