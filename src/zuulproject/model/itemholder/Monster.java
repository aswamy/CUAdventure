package zuulproject.model.itemholder;

/**
 * The monster class is the Player's most annoying obsticle that dwells
 * within a room. We are still debating if it should move from room to room
 * or spawn magically into a room
 * 
 * The monster has similarities between player, but cannot be controlled
 * 
 * @author: Alok Swamy
 */

public class Monster extends Creature{
    
	private static final int DEFAULT_ATK = 10;
	private static final int DEFAULT_RANGE = 1;
	private static final int DEFAULT_MAXHP = 10;
	
    public Monster(String name) {
        this(name, DEFAULT_ATK, DEFAULT_RANGE, DEFAULT_MAXHP, DEFAULT_MAXHP);
    }
    
    public Monster(String name, int attackPower, int damageRange, int maxHP, int currentHP) {
    	this.name = name;
    	this.attackPower = attackPower;
    	this.damageRange = damageRange;   // the mosnter does a damage of 3 +/- 1
    	this.maxHP = maxHP;
    	this.currentHP = currentHP;
    }
    
    public int totalAttack() {
    	return attackPower;
    }
    
    public String toXML() {
    	String temp = "";	
    	temp+= "<monster mname=\"" + this.getName() + "\">\n"
    	+ "<attack>" + attackPower +"</attack>\n"
    	+ "<damagerange>" + damageRange + "</damagerange>\n"
    	+ "<maxHP>" + maxHP + "</maxHP>\n"
    	+ "<currentHP>" + currentHP + "</currentHP>\n"
    	+ itemsToXML("monsteritem")
    	+ "</monster>\n";
    	return temp;
    }
}
