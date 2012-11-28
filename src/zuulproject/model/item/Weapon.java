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

    public int getWeaponAtk() {
        return attackPower;
    }
}
