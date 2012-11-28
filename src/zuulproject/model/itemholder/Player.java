package zuulproject.model.itemholder;

import zuulproject.model.item.*;
import zuulproject.model.innercontroller.*;
import java.util.*;

/**
 * A person that will handle most of the commands given by the user.
 * This class is far the most interactive, and contains many attributes describing the player including combat stats,
 * player location, and a player log that keeps track of where he/she has been (To Undo)
 * 
 * @author Alok Swamy and Eshan
 */
public class Player extends Creature{
		
	private static final String PLAYER_NAME = "Player";
	private static final int ATK_POWER = 5;
	private static final int DMG_RANGE = 2;
	private static final int BONUS_ATK = 0;
	private static final int PLAYER_HP = 20;	
	
    private Room currentRoom;
    private Stack<Room> steps;
    
    protected int bonusAttack; // this attribute tells how much power the equipped item is giving the player
    private Weapon equippedItem;
    
    private Stack<Command> moves; //Keeps track of the Commands that can be undone,
    private Stack<Command> undoMoves;// for redo purposes;
    
    public Player() {
        name = PLAYER_NAME;
        currentRoom = null;
        steps = new Stack<Room>();
        moves = new Stack<Command>();
        undoMoves = new Stack<Command>();

        attackPower = ATK_POWER;
        bonusAttack = BONUS_ATK;
        damageRange = DMG_RANGE;
        maxHP = PLAYER_HP;
        currentHP = PLAYER_HP;
        equippedItem = null;
    }
    
    public int totalAttack() {
        return attackPower + bonusAttack;
    }
    
    public Room getRoom() {
        return currentRoom;
    }

    public void setRoom(Room room) {
        currentRoom = room;
    }
    
    public Weapon getWeapon() {
    	return equippedItem;
    }
    
    /*
     * goRoom allows the user to travel from room to room, but also keeps a log of where he/she has been
     * 'go back' typed in the user input will allow him to return to the previous room (like an UNDO)
     * 
     * Undo portion is written entirely by Ehsan
     */
    
    public String playerMove(Command command) {
        // Try to leave current room.
    	Room nextRoom = null;

        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            return "Go where?";
        }
 
        String room = command.getSecondWord();
        nextRoom = currentRoom.getExitRoom(room);
        
        
        if (nextRoom == null) {
            if(!room.equalsIgnoreCase("back")) // undo command E.K
            {
            	nextRoom = currentRoom;  // for refactoring the Else to get a more efficient Code for adding the Undo Command ( Go back). E.K
                return "There is no door!";
            }
        } 

        //else { else has been refactored.
        if(room.equalsIgnoreCase("back"))
        {
            //if(steps.isEmpty()) for some reason the newly created stack shows elementCount = 1, but there is no elements in it. there fore:   E.K
            if(steps.size()<1)
            {
                nextRoom=currentRoom;
                return "** You are where you started.";
            } else {
                nextRoom = steps.pop();
            }
        }
        else if(currentRoom != nextRoom)//Keeping track of the steps. E.K
        {
            steps.push(currentRoom);
        }
    
        if (currentRoom.isExitable()) {
            if (nextRoom == null) {
                return "There is no door!\n";
            } else {
                if(nextRoom.isEnterable()) currentRoom = nextRoom;
                else return Room.unenterable;
                return playerLook();
            }
        } else {
            return Room.unexitable;
        }
    }   
    
    
    
    /**
     * Checks to see if the pickUp command is in proper format
     * This might be moved to player class soon
     */
    
    public String playerPickup (Command command) {
        Item removingItem = null;
        
        if(!command.hasSecondWord()) {
        	return "Pickup what?";
        }
        
        String itemName = command.getSecondWord();
        
        for (Item item : currentRoom.getItemList())
            if (item.getName().equalsIgnoreCase(itemName)) // changing equals() to equalsIgnoreCase() to make the game easier. E.K
                removingItem = item;
            
        if (!(removingItem==null)) {
            currentRoom.removeItem(removingItem);
            insertItem(removingItem);
            return removingItem + " has been picked up";
        } else return "Item doesn't exist";
   }
   /**
    * This method is the reverse of playerPickup()
    * it drops an Item in to the currentRoom 
    * @param command
    * @return String results
    * @author Ehsan Karami 
    */
   public String playerDrop (Command command) { // For undo purposes, E.K
        Item removingItem = null;
        if(!command.hasSecondWord())
        	return "Drop what?";
        String itemName = command.getSecondWord();        
        /*for (Item item : this.getItemList())
            if (item.getName().equalsIgnoreCase(itemName)) // changing equals() to equalsIgnoreCase() to make the game easier. E.K
                removingItem = item;
          */ // using the items equals method; E.K
        if(this.getItemList().contains(new Item(itemName)))
        	removingItem = getItemList().get(getItemList().indexOf(new Item(itemName)));
        if (!(removingItem==null)) {
            this.removeItem(removingItem);
            currentRoom.insertItem(removingItem);
            return removingItem + " has been droped";
        } else return "You are not carrying a " + itemName;
   }

    public String dspPlayerInventory() {
    	String s = "Inventory:\n";
        s+= getAllItems();	
    	return s;
    }
    
    /*
     * Player wields and item to become more powerful (but the item will still be in the inventory)
     */
    
    public String playerEquip(Command command) {
        Weapon newWeapon = null;
        String itemName;
        
        if(!command.hasSecondWord()) {
            return "Equip what?";
        }
        
        itemName = command.getSecondWord();
        for (Item item : items) {
            if (item.getName().equals(itemName)) {
                if (item instanceof Weapon) {
                    newWeapon = (Weapon)item;
                }
            }
        }
        if (!(newWeapon==null)) {
            equippedItem = newWeapon;
            bonusAttack = newWeapon.getWeaponAtk();
            return itemName + " has been equipped!";
        } else return "Item cannot be equipped or weapon doesn't exist!";
   	
    }
    
    public String playerDeequip() {
    	bonusAttack = 0;
        equippedItem = null;
        return "Item has been deequipped.";
    }

    public String playerUse(Command command) {
    	String s = "";
        Consumable consume = null;
        String itemName;
        
        if(!command.hasSecondWord()) {
            return "Use what?";
        }
        
        itemName = command.getSecondWord();
        for (Item item : items)
            if (item.getName().equals(itemName)) 
                if (item instanceof Consumable) 
                    consume = (Consumable)item;            
        
        if (!(consume==null)) {
            int healthHealed;
            if (consume.getHealthHealed()>(maxHP - currentHP))healthHealed = maxHP-currentHP;
            else healthHealed = consume.getHealthHealed();            
            currentHP += healthHealed;
            
            s = itemName + " has healed " + healthHealed +"HP!\n";
            s += creatureHP();
            removeItem(consume);
        } else s= "Item cannot be consumed or consumable doesn't exist!";
    	
    	return s;
    }

    public String playerApply(Command command) {
    	String s = "";
        Powerup powerup = null;
        String itemName;
        
        if(!command.hasSecondWord()) {
            return "Apply what?";
        }
        
        itemName = command.getSecondWord();
        for (Item item : items)
            if (item.getName().equals(itemName)) 
                if (item instanceof Powerup) 
                    powerup = (Powerup)item;            
        
        if (powerup != null) {
            int healthBoosted = powerup.getHPIncrease();
            int attackBoosted = powerup.getAtkIncrease();
            
            attackPower += attackBoosted;
            maxHP += healthBoosted;
            currentHP += healthBoosted;
            
            s = itemName + " has boosted " + attackBoosted + " attack and  " + healthBoosted +"HP!\n";
            s += creatureStatus();
            removeItem(powerup);
        } else s= "Item cannot be applied or Powerup doesn't exist!";
    	
    	return s;
    }
    /**
     * this method is the reverse of the apply method
     * @param command
     * @return String results
     * @author Ehsan Karami
     */
    public String playerUndoApply(Command command) {
    	String s = "";
        Powerup powerup = null;
        Item itm;
        
        if(!command.hasSecondWord()) {
            return "Apply what?";
        }
        
        itm = new Item(command.getSecondWord());
        if(removedItems.contains(itm))
        {
            Item item = removedItems.get(removedItems.indexOf(itm)); 
            if (item instanceof Powerup) 
                powerup = (Powerup)item;            
        }
        if (powerup != null) {
            attackPower -= powerup.getAtkIncrease();
            maxHP -= powerup.getHPIncrease();
            currentHP -= powerup.getHPIncrease();
            
            s = itm.getName() + " has returned to your inventory and your attack reduced by " + powerup.getAtkIncrease() + " and your HP reduced by  " + powerup.getHPIncrease() +"!\n";
            s += creatureStatus();
            this.insertItem(powerup);
        } else s= "Item was not found or was not applied";
    	
    	return s;
    }
        
    public String playerExamine(Command command) {
        Item examine = null;
        String itemName;
        List<Item> tempList = new ArrayList<Item>();
        
        if(!command.hasSecondWord()) {
            return "Item doesn't exist in your inventory or the room";
        }
        
        itemName = command.getSecondWord();
        
        tempList.addAll(items);
        tempList.addAll(currentRoom.getItemList());
        
        for (Item item : tempList) {
            if (item.getName().equals(itemName)) {
                examine = item;
            }
        }
        if (!(examine==null)) {
            return itemName + ": " + examine.getDescription();
        } else return "Item cannot be examined or item doesn't exist!";
    }
    
    /**
     * See what the room is about (its description), what it has, and what are it's exits
     */
    
    public String playerLook() {
    	String s = "";
    	
    	s += "You are " + currentRoom.getDescription() + "\n"
    			+ "Items:\n";
        s+= currentRoom.getAllItems() + "\n";
        s+= "Exits:\n" +
        		currentRoom.getAllExits();
    	return s;
    }

    public boolean inBattle() {
    	return currentRoom.hasMonster();
    }
    
    public String processPlayerCmd(Command command) {
    	String temp = "";
    	CommandTypes commandWord = command.getCommandWord();
    	boolean canUndo = false; // E.K
    	
        if (commandWord == CommandTypes.LOOK) {
            temp = playerLook();
        } else if (commandWord == CommandTypes.PICKUP) {
            temp = playerPickup(command);
        	canUndo = true;
        } else if (commandWord == CommandTypes.INVENTORY) { 
            temp = dspPlayerInventory();
        } else if (commandWord == CommandTypes.CONSUME) { 
            temp = playerUse(command);
        } else if (commandWord == CommandTypes.APPLY) { 
            temp = playerApply(command);
        	canUndo = true;
        } else if (commandWord == CommandTypes.STATUS) {
            temp = creatureStatus();
        } else if (commandWord == CommandTypes.EQUIP) {
            temp = playerEquip(command);
        	canUndo = true;
        } else if (commandWord == CommandTypes.DEEQUIP) {
        	if(equippedItem != null) // E.K
        	{
        		command.setSecondWord(equippedItem.getName());
        		temp = playerDeequip();
        		canUndo = true;
        	}
        	else temp = "You dont have anything to deequip";
        } else if (commandWord == CommandTypes.EXAMINE) {
            temp = playerExamine(command);
        } else if (commandWord == CommandTypes.GO) {
        	temp = playerMove(command);
        	canUndo = true;
        } else if (commandWord == CommandTypes.UNDO)
        	temp = undo();
        else if (commandWord == CommandTypes.REDO)
        	temp = redo();
        if(canUndo)
        {
        	moves.push(command);
        	undoMoves.clear();
        }
        
    	return temp;
    }
    
    public String undo()
    {
    	if(moves.empty()) return "There is nothing to Undo.";
    	String temp = "";
    	Command cmd = moves.pop();
    	switch(cmd.getCommandWord())
    	{
    	case APPLY:
			temp = playerUndoApply(cmd);
			break;
    	case GO:
    		temp = playerMove(new Command(CommandTypes.GO, "back"));
    		break;
    	case EQUIP:
    		temp = playerDeequip();
    		break;
    	case DEEQUIP:
    		temp = playerEquip(cmd);
    		break;
    	case PICKUP:
    		temp = playerDrop(cmd);
    		break;
    	default: 
    		temp = "Cannot Undo!";
    		break;
    	}
    	undoMoves.push(cmd);
    	
    	return temp;
    }
    
    public String redo()
    {
    	if(undoMoves.empty()) return "There is nothing to Redo.";
    	String temp = "";
    	Command cmd = undoMoves.pop();
    	switch(cmd.getCommandWord())
    	{
    	case APPLY:
			temp = playerApply(cmd);
			break;
    	case GO:
    		temp = playerMove(cmd);
    		break;
    	case EQUIP:
    		temp = playerEquip(cmd);
    		break;
    	case DEEQUIP:
    		temp = playerDeequip();
    		break;
    	case PICKUP:
    		temp = playerPickup(cmd);
    		break;
    	default: 
    		temp = "Cannot Redo!";
    		break;
    	}
    	
    	moves.push(cmd);
    	
    	return temp;
    }
    
    public String creatureStatus() {
    	return creatureHP() + "\n" + name + "'s attack power is " + attackPower + "+" + bonusAttack;
    }
    
}
