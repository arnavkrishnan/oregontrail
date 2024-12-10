import java.util.*;
import java.lang.*;

public class Game {
    private Player player;
    private ArrayList<Companion> companions;
    private ArrayList<Location> locations;
    private ArrayList<Oxen> oxen;
    private Location currentLocation;
    private boolean gameRunning;

    public Game() {
        this.companions = new ArrayList<>();
        this.locations = new ArrayList<>();
        this.oxen = new ArrayList<>();
        this.gameRunning = true;

        init();

        Terminal.clean();
        initLocations();
        Terminal.clean();
        createPlayer();
        createCompanions();
        Terminal.clean();
        chooseDepartureMonth();
        Terminal.clean();
        buySupplies();
        Terminal.clean();
    }

    public void init(){
        Terminal.clean();
        Terminal.print("***THE OREGON TRAIL***\n");
        Terminal.print("***starting game***");
        Terminal.sleep(1000);
       // Terminal.load(100, 100);
    }

    public void start() {
        int choice;
        int milesTraveled = 0;
        while (gameRunning) {
            showMainMenu();
            choice = TextIO.getInt();
            switch (choice) {
                case 1:
                    continueOnTrail();
                    break;
                case 2:
                    checkSupplies();
                    break;
                case 3:
                    lookAtMap();
                    break;
                case 4:
                    changePace();
                    break;
                case 5:
                    changeFoodRations();
                    break;
                case 6:
                    stopToRest();
                    break;
                case 7:
                    attemptToTrade();
                    break;
                case 8:
                    talkToLocals();
                    break;
                default:
                    Terminal.print("Invalid choice, please try again.");
                    break;
            }
            
        }
    }

    public void continueOnTrail() {
    }

    public void checkSupplies() {
    }

    public void lookAtMap() {
    }

    public void changePace() {
    }

    public void changeFoodRations() {
    }

    public void stopToRest() {
    }

    public void attemptToTrade() {
    }

    public void talkToLocals() {
    }

    private void showMainMenu() {
        Terminal.println("1. Continue on Trail");
        Terminal.println("2. Check supplies");
        Terminal.println("3. Look at Map");
        Terminal.println("4. Change Pace");
        Terminal.println("5. Change food rations");
        Terminal.println("6. Stop to Rest");
        Terminal.println("7. Attempt to Trade");
        Terminal.println("8. Talk to Locals");

    }

    private void createPlayer() {
        Terminal.print("Choose your profession: (1) Banker from Boston (2) Carpenter from Ohio (3) Farmer from Illinois: ");
        int professionChoice = TextIO.getInt();
        String profession = "";
        int startingMoney = 0;

        switch (professionChoice) {
            case 1: 
                profession = "Banker from Boston"; 
                startingMoney = 1600;
                break;
            case 2: 
                profession = "Carpenter from Ohio"; 
                startingMoney = 800;
                break;
            case 3: 
                profession = "Farmer from Illinois"; 
                startingMoney = 400;
                break;
        }

        Terminal.print("Enter your name: ");
        String playerName = TextIO.getlnString();

        this.player = new Player(playerName, startingMoney, profession);
    }

    private void createCompanions() {
        for (int i = 0; i < 4; i++) {
            Terminal.print("Enter name for companion " + (i + 1) + ": ");
            String name = TextIO.getlnString();
            companions.add(new Companion(name));
        }
    }

    private void chooseDepartureMonth() {
        Terminal.print("When would you like to leave? (1) March (2) April (3) May (4) June (5) July: ");
        int monthChoice = TextIO.getInt();
    }

    private void initLocations() {
        locations.add(new Fort("Independence", "A trading post and starting point.", 0));
    }

    private void buySupplies() {
        Terminal.println("You have $" + String.valueOf(player.getMoney()) + " to spend. You can get what you need at Matt's general store.");
        Terminal.sleep(2000);
        Terminal.clean();
        Terminal.println("Hello, I'm Matt! So you're going to Oregon! I can fix you up with what you need:");
        Terminal.println("- a team of oxen to pull your wagon");
        Terminal.println("- clothing for both summer and winter");
        Terminal.println("- plenty of food for the trip");
        Terminal.println("- ammunition for your rifles");
        Terminal.println("- spare parts for your wagon");
        Terminal.sleep(6000);
        Terminal.clean();

        int yoke = MattsGeneralStore.buyOxen(player);
        player.makeCharge(40*yoke);
        player.addItem(new Item("Oxen", yoke*2));
        oxen.add(new Oxen());

        Terminal.clean();

        int food = MattsGeneralStore.buyFood(player);
        player.makeCharge(0.20*food);
        player.addItem(new Item("Food", food));

        Terminal.clean();

        int clothes = MattsGeneralStore.buyClothes(player);
        player.makeCharge(10*clothes);
        player.addItem(new Item("Clothes", clothes));

        Terminal.clean();

        int ammo = MattsGeneralStore.buyAmmo(player);
        player.makeCharge(2*ammo);
        player.addItem(new Item("Bullets", 20*ammo));

        Terminal.clean();

        int[] parts = MattsGeneralStore.buyParts(player);
        player.makeCharge(10*(parts[0] + parts[1] + parts[2]));
        player.addItem(new Item("Wheels", parts[0]));
        player.addItem(new Item("Axels", parts[1]));
        player.addItem(new Item("Tongues", parts[2]));

        Terminal.clean();

        player.inventoryToString();

        Terminal.sleep(100000);
    }
}
