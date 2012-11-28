package zuulproject.test;

//import zuulproject.model.*;
import zuulproject.model.innercontroller.*;
//import zuulproject.model.innercontroller.battle.*;
//import zuulproject.model.innercontroller.battle.innerbattlecontroller.*;
import zuulproject.model.item.*;
import zuulproject.model.itemholders.*;
//import zuulproject.event.*;
//import zuulproject.outercontroller.*;
//import zuulproject.view.*;
//import zuulproject.view.graphicalview.*;

import junit.framework.TestCase;

/**
 * A test class that contains all the tests for the player clas
 */

public class PlayerCommandTest extends TestCase{
	private Player player = null;
	private Room room1 = null;
	
	protected void setUp() {
		player = new Player();
	
		room1 = new Room("starting room", "room1");
	
		player.setRoom(room1);
	}
	
	public void testEquip() {
		Weapon weapon = new Weapon("Sword", 3);
		player.insertItem(weapon);
		
		int playerAtkPriorEquip = player.totalAttack();
		
		player.processPlayerCmd(new Command(CommandTypes.EQUIP, weapon.getName()));
		
		assertEquals("The attack of the player should increase by the amount of atk power given by the weapon",
				(playerAtkPriorEquip + weapon.getWeaponAtk()) ,player.totalAttack());
	}
	
	public void testDeequip() {
		Weapon weapon = new Weapon("Dagger", 2);
		player.insertItem(weapon);
		
		player.processPlayerCmd(new Command(CommandTypes.EQUIP, weapon.getName()));
		
		int playerAtkWithWeapon = player.totalAttack();
		
		player.processPlayerCmd(new Command(CommandTypes.DEEQUIP, null));
		
		assertEquals("The attack of the player should decrease by the amount of attack power given by the weapon",
				(playerAtkWithWeapon-weapon.getWeaponAtk()), player.totalAttack());
	}
	
	public void testPlayerPickup() {
		Item item = new Item("item");
		room1.insertItem(item);
		
		int playerInventoryNum = player.numOfItems();
		int roomInventoryNum = room1.numOfItems();
		
		player.processPlayerCmd(new Command(CommandTypes.PICKUP, item.getName()));
		
		assertTrue("Once picked up, the number of items in the player's inventory should go up by 1",
				player.numOfItems()==(playerInventoryNum+1));
		assertTrue("Once picked up, the number of items in the room's inventory should go down by 1",
				room1.numOfItems()==(roomInventoryNum-1));
	}
	
	public void testApply() {
		Powerup powerup = new Powerup("superPill", "gives 5 atk, 0 hp", 5, 0);
		
		player.insertItem(powerup);

		int initialNumOfItems = player.numOfItems();
		int playerAtkPriorPowerup = player.totalAttack();
		
		player.processPlayerCmd(new Command(CommandTypes.APPLY, powerup.getName()));
	
		assertEquals("Must perminently increase the attack/HP of the player",
				(playerAtkPriorPowerup+powerup.getAtkIncrease()), player.totalAttack());
		
		assertTrue("After using powerup, it should disappear from inventory",
				(initialNumOfItems-1)==player.numOfItems());
	}
	
	public void testConsume() {
		Consumable consumable1 = new Consumable("potion", 5);
		Consumable consumable2 = new Consumable("bigPotion", 100);
		player.insertItem(consumable1);
		player.insertItem(consumable2);
		
		player.reduceHP(10);
		
		int playerHPPriorPotion = player.getCurrentHP();
		
		player.processPlayerCmd(new Command(CommandTypes.CONSUME, consumable1.getName()));
		
		assertEquals("When player's missing hp is greater than potion hp recovery power, potion's recovery power = player hp gained",
				(playerHPPriorPotion+consumable1.getHealthHealed()), player.getCurrentHP());
		
		player.processPlayerCmd(new Command(CommandTypes.CONSUME, consumable2.getName()));
		
		assertTrue("When potion power exceeds the amount of hp needed to recover, don't exceed maximum hp",
				player.getCurrentHP()==player.getMaxHP());
	}
}
