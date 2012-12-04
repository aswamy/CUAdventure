package zuulproject.model.item;

/**
 * A type of Item that can boost a Player's attack power 
 * if he/she wields it
 * 
 * @author Alok Swamy
 */

public class Weapon extends Item{
    protected int attackPower;

    public Weapon(String name, int atk) {
        super(name);
        attackPower = atk;
    }
    
    public Weapon(String name, String desc, int atk) {
    	super(name, desc);
    	attackPower = atk;
    }

    public int getWeaponAtk() {
        return attackPower;
    }
    
    public String toXML(String itemTag) {
    	return  "<" + itemTag + " type=\"Weapon\">\n"
    			+ "<name>" + itemName + "</name>\n"
    			+ "<description>" + itemDescription + "</description>\n"
    			+ "<attack>" + attackPower + "</attack>\n"
    			+ "</" + itemTag +">\n";
    }
}
