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

    // amount of health the consumable will heal
    public int getHealthHealed() {
        return regenHP;
    }
}
