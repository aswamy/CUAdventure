package zuulproject.model.item;

/**
 * A type of Item that can heals a Player's health 
 * if he/she uses it
 * 
 * @author Alok Swamy
 */

public class Consumable extends Item{
    protected int regenHP;

    public Consumable(String name, int recoverHP) {
        super(name);
        regenHP = recoverHP;
    }
    
    public Consumable(String name, String desc, int recoverHP) {
    	super(name, desc);
    	regenHP = recoverHP;
    }

    // amount of health the consumable will heal
    public int getHealthHealed() {
        return regenHP;
    }
    
    public String toXML(String itemTag) {
    	return  "<" + itemTag + " type=\"Consumable\">\n"
    			+ "<name>" + itemName + "</name>\n"
    			+ "<description>" + itemDescription + "</description>\n"
    			+ "<regenHP>" + regenHP + "</regenHP>\n"
    			+ "</" + itemTag +">\n";
    }
}
