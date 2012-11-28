package zuulproject.model.item;

/**
 * A powerup that makes the player perminently stronger
 *
 */
public class Powerup extends Item {
    protected int increaseAttack; //how much attack boost the player gets
    protected int increaseHP;		// how much hp boost the player gets

    public Powerup (String name, String description, int atkIncrease, int hpIncrease) {
    	super(name, description);
    	increaseAttack = atkIncrease;
    	increaseHP = hpIncrease;
    }
    
    // getters of the instance variables
    public int getAtkIncrease() {
    	return increaseAttack;
    }
    
    public int getHPIncrease() {
    	return increaseHP;
    }
}
