package zuulproject.model.itemholder;

/**
 * Creature a superclass for the monster and player and whoever that will be "living" in the world of Zuul
 *
 */
public abstract class Creature extends ItemHolder {
   
    protected String name;
    protected int attackPower;
    protected int damageRange;
    protected int maxHP;
    protected int currentHP;

    
    // returns the name of the creature
    public String getName() {
        return name;
    }
    
    public void setName(String s) {
    	name = s;
    }
    
    // returns the total atk power of the creature
    public abstract int totalAttack();
    
    public int getAttackPower() {
    	return attackPower;
    }
    
    public void setAttackPower(int a) {
    	attackPower = a;
    }
    
    //returns weather the creature is dead
    public boolean isDead() {
        if (currentHP <= 0) return true;
        return false;
    }
    
    
    // retuns the damage range of the attacker
    public int getDamageRange() {
        return damageRange;
    }
    
    // reduces the creatures HP
    public void reduceHP(int damage) {
        if (damage < currentHP) currentHP = currentHP - damage;
        else currentHP = 0;
    }
    
    // returns the hp of the creature
    public int getCurrentHP() {
    	return currentHP;
    }
    
    public void setCurrentHP(int hp) {
    	currentHP = hp;
    }
    
    // returns the maximum hp the creature can have
    public int getMaxHP() {
    	return maxHP;
    }
    
    public void setMaxHP(int hp) {
    	maxHP = hp;
    }
}
