
/**
 * This class represents an item which may be put
 * in a room in the game of Zuul.
 * 
 * @author Lynn Marshall
 * @author Ahmed Abdelrazik
 * @version November 9th, 2019
 */
public class Item
{
    // description of the item
    private String description;
    
    private String name;
    
    // weight of the item in kilgrams 
    private double weight;
    
    /**
     * Constructor for objects of class Item.
     * 
     * @param description The description of the item
     * @param name The name of the item
     * @param weight The weight of the item
     */
    public Item(String description, String name, double weight)
    {
        this.description = description;
        this.name = name;
        this.weight = weight;
    }

    /**
     * Returns a description of the item, including its
     * description and weight.
     * 
     * @return  A description of the item
     */
    public String getDescription()
    {
        return description + " that weighs " + weight + "kg.";
    }
    
    /**
     * returns the name of the room
     * 
     * @return the room's name
     */
    public String getName()
    {
        return name;
    }
}
