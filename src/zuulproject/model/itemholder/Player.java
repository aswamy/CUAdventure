package zuulproject.model.itemholder;

import zuulproject.event.*;
import zuulproject.model.item.*;
import zuulproject.model.innercontroller.*;
import java.util.*;

/**
 * A person that will handle most of the commands given by the user. This class
 * is far the most interactive, and contains many attributes describing the
 * player including combat stats, player location, and a player log that keeps
 * track of where he/she has been (To Undo)
 * 
 * @author Alok Swamy and Eshan
 */
public class Player extends Creature {

	private static final String PLAYER_NAME = "Player";
	private static final int ATK_POWER = 5;
	private static final int DMG_RANGE = 2;
	private static final int BONUS_ATK = 0;
	private static final int PLAYER_HP = 20;

	private Room currentRoom;
	private Stack<Room> steps;

	protected int bonusAttack; // this attribute tells how much power the
								// equipped item is giving the player
	private Weapon equippedItem;

	private Stack<Command> moves; // Keeps track of the Commands that can be
									// undone,
	private Stack<Command> undoMoves;// for redo purposes;

	private List<GameChangeListener> listenerList;

	public Player() {
		name = PLAYER_NAME;
		currentRoom = null;
		steps = new Stack<Room>();
		moves = new Stack<Command>();
		undoMoves = new Stack<Command>();

		listenerList = new ArrayList<GameChangeListener>();

		attackPower = ATK_POWER;
		bonusAttack = BONUS_ATK;
		damageRange = DMG_RANGE;
		maxHP = PLAYER_HP;
		currentHP = PLAYER_HP;
		equippedItem = null;
	}
	
	public synchronized void addGameListenerList(List<GameChangeListener> g) {
		listenerList.addAll(g);
	}

	// Add people who want to listen to the game
	public synchronized void addGameListener(GameChangeListener g) {
		listenerList.add(g);
	}

	// removes people that don't want to listen to the game
	public synchronized void removeGameListener(GameChangeListener g) {
		listenerList.remove(g);
	}

	// announce the game change has occured to all that want to listen
	protected void announceGameChange(GameChangeEvent e) {
		for (GameChangeListener g : listenerList)
			g.gameCmdProcessed(e);
	}

	// announce that info is requested by the user
	protected void announceGameInfo(GameInfoEvent e) {
		for (GameChangeListener g : listenerList) {
			if (g instanceof GameEventListener)
				((GameEventListener) g).gameInfoRequested(e);
		}
	}

	public String getName() {
		return name;
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
	
	public int getBonusAttack() {
		return bonusAttack;
	}
	
	public void setWeapon(Weapon w) {
		equippedItem = w;
		bonusAttack = w.getWeaponAtk();
	}

	/*
	 * goRoom allows the user to travel from room to room, but also keeps a log
	 * of where he/she has been 'go back' typed in the user input will allow him
	 * to return to the previous room (like an UNDO)
	 * 
	 * Undo portion is written entirely by Ehsan
	 */

	public boolean playerMove(Command command) {
		// Try to leave current room.
		Room nextRoom = null;

		if (!command.hasSecondWord()) {
			return false;
		}

		String room = command.getSecondWord();
		nextRoom = currentRoom.getExitRoom(room);

		if (nextRoom == null) {
			if (!room.equalsIgnoreCase("back")) // undo command E.K
			{
				nextRoom = currentRoom; // for refactoring the Else to get a
										// more efficient Code for adding the
										// Undo Command ( Go back). E.K
				return false;
			}
		}

		// else { else has been refactored.
		if (room.equalsIgnoreCase("back")) {
			// if(steps.isEmpty()) for some reason the newly created stack shows
			// elementCount = 1, but there is no elements in it. there fore: E.K
			if (steps.size() < 1) {
				nextRoom = currentRoom;
				return false;
			} else {
				nextRoom = steps.pop();
			}
		} else if (currentRoom != nextRoom)// Keeping track of the steps. E.K
		{
			steps.push(currentRoom);
		}

		if (nextRoom == null) {
			return false;
		} else {
			if (nextRoom.isEnterable())
				currentRoom = nextRoom;
			else
				return false;
			return true;
		}
	}

	/**
	 * Checks to see if the pickUp command is in proper format This might be
	 * moved to player class soon
	 */

	public boolean playerPickup(Command command) {
		Item removingItem = null;

		if (!command.hasSecondWord()) {
			return false;
		}

		String itemName = command.getSecondWord();

		for (Item item : currentRoom.getItemList())
			if (item.getName().equalsIgnoreCase(itemName)) // changing equals()
															// to
															// equalsIgnoreCase()
															// to make the game
															// easier. E.K
				removingItem = item;

		if (!(removingItem == null)) {
			currentRoom.removeItem(removingItem);
			insertItem(removingItem);
			return true;
		} else
			return false;
	}

	/**
	 * This method is the reverse of playerPickup() it drops an Item in to the
	 * currentRoom
	 * 
	 * @param command
	 * @return String results
	 * @author Ehsan Karami
	 */
	public void playerDrop(Command command) { // For undo purposes, E.K
		Item removingItem = null;
		String itemName = command.getSecondWord();
		if (this.getItemList().contains(new Item(itemName)))
			removingItem = getItemList().get(
					getItemList().indexOf(new Item(itemName)));
		if (!(removingItem == null)) {
			this.removeItem(removingItem);
			currentRoom.insertItem(removingItem);
		}
	}

	/*
	 * Player wields and item to become more powerful (but the item will still
	 * be in the inventory)
	 */
	public boolean playerEquip(Command command) {
		Weapon newWeapon = null;
		String itemName;

		if (!command.hasSecondWord()) {
			return false;
		}

		itemName = command.getSecondWord();
		for (Item item : items) {
			if (item.getName().equals(itemName)) {
				if (item instanceof Weapon) {
					newWeapon = (Weapon) item;
				}
			}
		}
		if (!(newWeapon == null)) {
			setWeapon(newWeapon);
			return true;
		} else
			return false;
	}

	public boolean playerDeequip() {
		if (!(equippedItem == null)) {
			bonusAttack = 0;
			equippedItem = null;
			return true;
		}
		return false;
	}

	public boolean playerConsume(Command command) {
		Consumable consume = null;
		String itemName;

		if (!command.hasSecondWord()) {
			return false;
		}

		itemName = command.getSecondWord();
		for (Item item : items)
			if (item.getName().equals(itemName))
				if (item instanceof Consumable)
					consume = (Consumable) item;

		if (!(consume == null)) {
			int healthHealed;
			if (consume.getHealthHealed() > (maxHP - currentHP))
				healthHealed = maxHP - currentHP;
			else
				healthHealed = consume.getHealthHealed();
			currentHP += healthHealed;
			removeItem(consume);
			return true;
		}
		return false;
	}

	public boolean playerApply(Command command) {
		Powerup powerup = null;
		String itemName;

		if (!command.hasSecondWord()) {
			return false;
		}

		itemName = command.getSecondWord();
		for (Item item : items)
			if (item.getName().equals(itemName))
				if (item instanceof Powerup)
					powerup = (Powerup) item;

		if (powerup != null) {
			int healthBoosted = powerup.getHPIncrease();
			int attackBoosted = powerup.getAtkIncrease();

			attackPower += attackBoosted;
			maxHP += healthBoosted;
			currentHP += healthBoosted;
			removeItem(powerup);
			return true;
		}
		return false;
	}

	public void playerUndoApply(Command command) {
		Powerup powerup = null;
		Item itm;

		itm = new Item(command.getSecondWord());
		if (removedItems.contains(itm)) {
			Item item = removedItems.get(removedItems.indexOf(itm));
			if (item instanceof Powerup)
				powerup = (Powerup) item;
		}
		if (powerup != null) {
			attackPower -= powerup.getAtkIncrease();
			maxHP -= powerup.getHPIncrease();
			currentHP -= powerup.getHPIncrease();
			this.insertItem(powerup);
		}
	}

	public void playerUndoConsume(Command command) {
		Consumable potion = null;
		Item itm;

		itm = new Item(command.getSecondWord());
		if (removedItems.contains(itm)) {
			Item item = removedItems.get(removedItems.indexOf(itm));
			if (item instanceof Consumable)
				potion = (Consumable) item;
		}
		if (potion != null) {
			currentHP -= potion.getHealthHealed();
			this.insertItem(potion);
		}
	}

	public Item playerExamine(Command command) {
		Item examine = null;
		String itemName;
		List<Item> tempList = new ArrayList<Item>();

		if (!command.hasSecondWord()) {
			return null;
		}

		itemName = command.getSecondWord();

		tempList.addAll(items);
		tempList.addAll(currentRoom.getItemList());

		for (Item item : tempList) {
			if (item.getName().equals(itemName)) {
				examine = item;
			}
		}
		return examine;
		/*
		 * if (!(examine==null)) { return true; } return false;
		 */
	}

	public boolean inBattle() {
		return currentRoom.hasMonster();
	}

	/*
	 * returns true if a move that can be undone using "undo" command is completed
	 */
	public boolean processPlayerCmd(Command command) {
		CommandTypes commandWord = command.getCommandWord();
		boolean canUndo = false;
		
		if (commandWord == CommandTypes.LOOK) {
			announceGameInfo(new GameInfoEvent(this.getRoom(),
					CommandTypes.LOOK));
		} else if (commandWord == CommandTypes.PICKUP) {
			boolean commandSuccess = playerPickup(command);
			announceGameChange(new GameChangeEvent(this, CommandTypes.PICKUP,
					commandSuccess));
			if (commandSuccess)
				canUndo = true;
		} else if (commandWord == CommandTypes.INVENTORY) {
			announceGameInfo(new GameInfoEvent(this, CommandTypes.INVENTORY));
		} else if (commandWord == CommandTypes.CONSUME) {
			boolean commandSuccess = playerConsume(command);
			announceGameChange(new GameChangeEvent(this, CommandTypes.CONSUME,
					commandSuccess));
			if (commandSuccess)
				canUndo = true;
		} else if (commandWord == CommandTypes.APPLY) {
			boolean commandSuccess = playerApply(command);
			announceGameChange(new GameChangeEvent(this, CommandTypes.APPLY,
					commandSuccess));
			if (commandSuccess)
				canUndo = true;
		} else if (commandWord == CommandTypes.STATUS) {
			announceGameInfo(new GameInfoEvent(this, CommandTypes.STATUS));
		} else if (commandWord == CommandTypes.EQUIP) {
			boolean commandSuccess = playerEquip(command);
			announceGameChange(new GameChangeEvent(this, CommandTypes.EQUIP,
					commandSuccess));
			if (commandSuccess)
				canUndo = true;
		} else if (commandWord == CommandTypes.DEEQUIP) {
			boolean commandSuccess = playerDeequip();
			announceGameChange(new GameChangeEvent(this, CommandTypes.DEEQUIP,
					commandSuccess));
			if (commandSuccess) {
				canUndo = true;
				command.setSecondWord(equippedItem.getName());
			}
		} else if (commandWord == CommandTypes.EXAMINE) {
			Item examinedItem = playerExamine(command);
			announceGameInfo(new GameInfoConditionalEvent(this,
					CommandTypes.EXAMINE, examinedItem));
		} else if (commandWord == CommandTypes.GO) {
			boolean commandSuccess = playerMove(command);
			announceGameChange(new GameChangeEvent(this, CommandTypes.GO,
					commandSuccess));
			if (commandSuccess)
				canUndo = true;
		} else if (commandWord == CommandTypes.UNDO) {
			boolean commandSuccess = undo();
			announceGameChange(new GameChangeEvent(this, CommandTypes.UNDO,
					commandSuccess));
		} else if (commandWord == CommandTypes.REDO) {
			boolean commandSuccess = redo();
			announceGameChange(new GameChangeEvent(this, CommandTypes.REDO,
					commandSuccess));
		}
		if (canUndo) {
			moves.push(command);
			undoMoves.clear();
		}
		return canUndo;
	}

	public boolean undo() {
		if (moves.empty())
			return false;
		Command cmd = moves.pop();
		switch (cmd.getCommandWord()) {
		case APPLY:
			playerUndoApply(cmd);
			break;
		case CONSUME:
			playerUndoConsume(cmd);
			break;
		case GO:
			playerMove(new Command(CommandTypes.GO, "back"));
			break;
		case EQUIP:
			playerDeequip();
			break;
		case DEEQUIP:
			playerEquip(cmd);
			break;
		case PICKUP:
			playerDrop(cmd);
			break;
		default:
			return false;
		}
		undoMoves.push(cmd);

		return true;
	}

	public boolean redo() {
		if (undoMoves.empty())
			return false;
		Command cmd = undoMoves.pop();
		switch (cmd.getCommandWord()) {
		case APPLY:
			playerApply(cmd);
			break;
		case CONSUME:
			playerConsume(cmd);
			break;
		case GO:
			playerMove(cmd);
			break;
		case EQUIP:
			playerEquip(cmd);
			break;
		case DEEQUIP:
			playerDeequip();
			break;
		case PICKUP:
			playerPickup(cmd);
			break;
		default:
			return false;
		}
		moves.push(cmd);

		return true;
	}
	
	public String toXML() {
		String temp = "";
		temp += "<player pname=\"" + getName() + "\">\n"
				+ "<currentroom>" + getRoom() + "</currentroom>\n"
				+ itemsToXML("playeritem")
				+ "<attackpower>" + attackPower + "</attackpower>\n"
				+ "<currentHP>" + currentHP + "</currentHP>\n"
				+ "<maxHP>" + maxHP + "</maxHP>\n";
				if(equippedItem!=null) temp+= "<equippeditem>" + equippedItem.toString() + "</equippeditem>\n";
		temp+= "</player>\n";
		return temp;
	}
}
