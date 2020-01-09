
/**
 * Beamer is a sub class from Item class which represents an item which may be put
 * in a room in the game of Zuul.
 * 
 * @author Ahmed Abdelrazik
 * @version 9th November, 2019
 */
public class Beamer extends Item
{
    private boolean Charged;
    private Room chargedRoom; //room where the beamer was charged
    /**
     * Constructor the intialises the beamer of weight 5 kg and not charged 
     */
    public Beamer()
    {
        super("fire fire beamer","beamer", 5.0);
        Charged = false;
        chargedRoom = null;
    }
    
    /**
     * Charges the beamer
     */
    public void setBeamer()
    {
        Charged = true;
    }
    
    /**
     * Uncharges teh beamer
     */
    public void unsetBeamer()
    {
        Charged = false;
    }
    
    /**
     * Checks if the beamer is cahrged or not
     *
     * @return  true if the beamer is charged, false otherwise 
     */
    public boolean isCharged()
    {
        return Charged;
    }
    
    /**
     * sets chargedRoom to a specified one
     * 
     * @param room the room being charged
     */
    public void setChargeroom(Room room)
    {
        chargedRoom = room;
    }
    
    /**
     * return the cahrgedRoom
     * 
     * @returns chargedRoom of the beamer
     */
    public Room backToChargedRoom()
    {
        return chargedRoom;
    }
}
