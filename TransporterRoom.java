import java.util.ArrayList;
import java.util.Random;
/**
 * TransporterRoom is a sub-class from Room class which is part of zull game
 * Special room that allows the player to go to any random room even if there is no exit for it
 *
 * @author Ahmed Abdelrazik
 * @version November 9th, 2019
 */
public class TransporterRoom extends Room 
{
    /**
     * Constructor initalises the tarnsportor room 
     */
    public TransporterRoom(){
        super("in Transporter Room");
    }

    /**
     * Returns a random room, independent of the direction parameter. *
     * @param direction Ignored.
     * @return A randomly selected room.
     */
    public Room getExit(String direction) {
        return findRandomRoom();
    }

    /**
     * Choose a random room.
     *
     * @return The room we end up in upon leaving this one. */
    private Room findRandomRoom()
    {
        Random r = new Random();
        return rooms.get(r.nextInt(rooms.size()));
    }
}
