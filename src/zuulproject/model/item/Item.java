package zuulproject.model.item;

/**
 * An item that any ItemHolder can carry
 * 
 * @author Alok Swamy
 */
public class Item {
	
	private static final String default_description = "An ordinary item";
	
    protected String itemName;
    protected String itemDescription;

    public Item(String name, String description){
    	itemName = name;
    	itemDescription = description;
    }
    
    public Item(String name) {
        this(name, default_description);
    }
    
    public Item() {
        this("", default_description);
    }

    // following get the item instance variable values
    public String getName() {
        return itemName;
    }
    
    public String getDescription() {
        return itemDescription;
    }

    // following set item instance variables
    public void setName (String s) {
        itemName = s;
    }
    
    public void setDescriptiong(String s) {
        itemDescription = s;
    }
    
    // string representation of the item
    public String toString() {
        return itemName;
    }
    
    // checks to see if the item is the same
    public boolean equals(Object obj)
    {
    	if( !(obj instanceof Item) ) return false;
    	if(this == obj) return true;
    	Item itm = (Item)obj;
    	return this.getName().equalsIgnoreCase(itm.getName());
    }
    
    public String toXML(String itemTag) {
    	return  "<" + itemTag + " type=\"Item\">\n"
    			+ "<name>" + itemName + "</name>\n"
    			+ "<description>" + itemDescription + "</description>\n"
    			+ "</" + itemTag +">\n";
    }
}
