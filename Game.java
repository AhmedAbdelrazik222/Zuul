import java.util.Stack;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes 
 * @version 2006.03.30
 * 
 * @author Lynn Marshall
 * @author Ahmed Abdelrazik
 * @version November 9th, 2019
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room previousRoom;
    private Stack<Room> previousRoomStack;
    private Item carry;
    private int energy;
    public static final int MAXENERGY = 5;
    private Beamer beamer, beamer2;
    
    /**
     * Create the game and initialise its internal map, as well
     * as the previous room (none) and previous room stack (empty).
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        previousRoom = null;
        previousRoomStack = new Stack<Room>();
        carry  = null;
        energy = 0;
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theatre, pub, lab, office;
        TransporterRoom transporterRoom;
        Item chair, bar, computer, computer2, tree, cookies;
        
        // create some items
        chair = new Item("a wooden chair","chair",5);
        bar = new Item("a long bar with stools","bar",95.67);
        computer = new Item("a PC computer","PC",10);
        computer2 = new Item("a Mac computer","Mac",5);
        tree = new Item("a fir tree","tree",500.5);
        cookies = new Item("delicious cookies","cookies",0.5);
        beamer = new Beamer();
        beamer2 = new Beamer();
       
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        transporterRoom = new TransporterRoom();
        
        // put items in the rooms
        outside.addItem(tree);
        outside.addItem(tree);
        outside.addItem(cookies);
        theatre.addItem(chair);
        theatre.addItem(cookies);
        theatre.addItem(beamer);
        pub.addItem(bar);
        pub.addItem(cookies);
        pub.addItem(beamer2);
        lab.addItem(chair);
        lab.addItem(cookies);
        lab.addItem(computer);
        lab.addItem(chair);
        lab.addItem(computer2);
        office.addItem(chair);
        office.addItem(cookies);
        office.addItem(computer);
        transporterRoom.addItem(cookies);
        
        // initialise room exits
        outside.setExit("east", theatre); 
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        
        theatre.setExit("west", outside);

        pub.setExit("east", outside);
        pub.setExit("south", transporterRoom);
        
        lab.setExit("north", outside);
        lab.setExit("east", office);
        lab.setExit("west", transporterRoom);

        office.setExit("west", lab);
        
        transporterRoom.setExit("east", lab);
        transporterRoom.setExit("north",pub);
        
        currentRoom = outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
        System.out.println("You have to pick cookies and eat the first befor carrying anything");
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            look(command);
        }
        else if (commandWord.equals("eat")) {
            eat(command);
        }
        else if (commandWord.equals("back")) {
            back(command);
        }
        else if (commandWord.equals("stackBack")) {
            stackBack(command);
        }
        else if (commandWord.equals("take")) {
            take(command);
        }
        else if (commandWord.equals("drop")){
            drop(command);
        }
        else if (commandWord.equals("charge")){
            charge();
        }
        else if (commandWord.equals("fire")){
            fire();
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.getCommands());
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * If we go to a new room, update previous room and previous room stack.
     * 
     * @param command The command to be processed
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRoom = currentRoom; // store the previous room
            previousRoomStack.push(currentRoom); // and add to previous room stack
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /** 
     * "Look" was entered. Check the rest of the command to see
     * whether we really want to look.
     * 
     * @param command The command to be processed
     */
    private void look(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Look what?");
        }
        else {
            // output the long description of this room
            System.out.println(currentRoom.getLongDescription());
            if (carry == null){
                System.out.println("You're not carrying anything");
            }else{
                System.out.println("You have " + carry.getName());
            }
        }
    }
    
    /** 
     * "Eat" was entered. Check the rest of the command to see
     * whether we really want to eat.
     * 
     * @param command The command to be processed
     */
    private void eat(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Eat what?");
        }
        else {
            // output that we have eaten
            if(carry != null){
                if(carry.getName().equals("cookies")){
                    System.out.println("You have eaten the cookies");
                    energy = MAXENERGY;
                    carry = null;
                    return;
                }
            }
            System.out.println("You don't have any food");
        }
    }
    
    /** 
     * "Back" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     */
    private void back(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Back what?");
        }
        else {
            // go back to the previous room, if possible
            if (previousRoom==null) {
                System.out.println("No room to go back to.");
            } else {
                // go back and swap previous and current rooms,
                // and put current room on previous room stack
                Room temp = currentRoom;
                currentRoom = previousRoom;
                previousRoom = temp;
                previousRoomStack.push(temp);
                // and print description
                System.out.println(currentRoom.getLongDescription());
            }
        }
    }
    
    /** 
     * "StackBack" was entered. Check the rest of the command to see
     * whether we really want to stackBack.
     * 
     * @param command The command to be processed
     */
    private void stackBack(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("StackBack what?");
        }
        else {
                // step back one room in our stack of rooms history, if possible
            if (previousRoomStack.isEmpty()) {
                System.out.println("No room to go stack back to.");
            } else {
                // current room becomes previous room, and
                // current room is taken from the top of the stack
                previousRoom = currentRoom;
                currentRoom = previousRoomStack.pop();
                // and print description
                System.out.println(currentRoom.getLongDescription());
            }
        }
    }
    
    /**
     * Take item from the room
     * 
     * @param command The command to be processed
     */
    private void take(Command command)
    {
        if(!command.hasSecondWord()){
            System.out.println("take what?");
        }else if((carry != null)){
            System.out.println("You're already carrying somthing"); 
        }else{
            for(Item i : currentRoom.getItemsList()){
                if(command.getSecondWord().equals(i.getName())){      
                    if (i.getName().equals("cookies")){
                        carry = i; // cookies are never done !! don't have to remove them
                        System.out.println("You picked up cookies"); 
                        System.out.println("Eat it to gain energy");
                    }else if(energy < 1){
                        System.out.println("You don't have enough energy");
                        System.out.println("Need to pick up cookies and eat them first");
                    }else{
                        carry = i;
                        System.out.println("You picked up " + carry.getName()); 
                        currentRoom.removeItem(i); // remove the item from the cuurent room
                        energy --;
                    }
                    return;
                }
            }
            System.out.println("That item is not in the room");
        }
    }

    /**
     * Drop item that the player carries 
     * 
     * @param command The command to be processed
     */
    private void drop(Command command)
    {
        if(carry == null){
            System.out.println("You have nothing to drop");
        }else{
            System.out.println("You have dropped " + carry.getName());
            currentRoom.addItem(carry); // place the item in the current room
            carry = null;
        }
    }
    
    /**
     * Charge the beamer if the player has it
     */
    private void charge()
    {
        if(carry == null || !(carry.getName().equals(beamer.getName()))){
            System.out.println("You need to carry a beamer first");
        }else if(carry == beamer  && !beamer.isCharged()){
            beamer.setBeamer();
            beamer.setChargeroom(currentRoom);
            System.out.println("You have charged the beamer");
        }else if(carry == beamer2 && !beamer2.isCharged()){
            beamer2.setBeamer();
            beamer2.setChargeroom(currentRoom);
            System.out.println("You have charged the beamer");
        }else{
            System.out.println("The beamer is already charged");
        }
    }
    
    /**
     * Fire the beamer if the palyer has it and it's charged
     */
    private void fire()
    {
        if(carry == null || !(carry.getName().equals(beamer.getName()))){
            System.out.println("You need to carry a charged beamer");
        }else if(carry == beamer  && beamer.isCharged()){
            System.out.println("FIRE!!!");
            beamer.unsetBeamer();
            previousRoom = currentRoom; // store the previous room
            previousRoomStack.push(currentRoom); // and add to previous room stack
            currentRoom = beamer.backToChargedRoom();// when you fire you go back to the charged room 
            currentRoom.addItem(beamer);             // and beamer is dropped in the charged room to 
            carry = null;                            // and you are now not carrying anything
            System.out.println(currentRoom.getLongDescription());
        }else if(carry == beamer2 && beamer2.isCharged()){
            System.out.println("FIRE!!!");
            beamer2.unsetBeamer();
            previousRoom = currentRoom; // store the previous room
            previousRoomStack.push(currentRoom); // and add to previous room stack
            currentRoom = beamer2.backToChargedRoom();// when you fire you go back to the charged room 
            currentRoom.addItem(beamer2);             // and beamer is dropped in the charged room to 
            carry = null;                            // and you are now not carrying anything
            System.out.println(currentRoom.getLongDescription());
        }else{
            System.out.println("You need to charge the beamer first");
        }
    }
}
