import java.util.*;
import java.lang.*;

public class Game {
    private Player player;
    private ArrayList<Companion> companions;
    private ArrayList<Landmark> locations;
    private ArrayList<Oxen> oxen;
    private boolean gameRunning;
    private int rations;
    private int pace;
    private int milesTraveled;
    private int daysTraveled;
    private int month;
    private int weather;
    private HashMap<Integer, String> paceEffectMap;
    private HashMap<Integer, String> healthEffectMap;
    private HashMap<Integer, String> rationEffectMap;
    private HashMap<Integer, String> weatherEffectMap;

    public Game() {
        this.companions = new ArrayList<>();
        this.locations = new ArrayList<>();
        this.oxen = new ArrayList<>();
        this.gameRunning = true;
        this.rations = 1;
        this.pace = 1;
        this.weather = 5;
        this.milesTraveled = 0;
        this.daysTraveled = 0;

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
        setupPaceEffects();
        setupHealthEffects();
        setupRationEffects();
        setupWeatherEffects();
    }

    public void continueOnTrail() {
        milesTraveled += pace * 5;
        daysTraveled++;
        player.subtractItem(new Item("Food", rations*companions.size()*3));
        calculateWeather();
    }

    public void calculateWeather() {
    Random rand = new Random();
    int currentMonth = Integer.parseInt(Date.calculate(daysTraveled, month, "month"));
    boolean isColdMonth = (currentMonth == 11 || currentMonth == 12 || currentMonth == 1 || currentMonth == 2);
    boolean isHotMonth = (currentMonth == 6 || currentMonth == 7 || currentMonth == 8);

    if (weather == 8 && rand.nextInt(4) == 1) {
        weather = 3;
    } else if (weather == 1 && rand.nextInt(4) == 1) {
        weather = 2;
    } else if (!(weather == 1 || weather == 7)) {
        int seasonFactor = currentMonth % 6;

        if (rand.nextInt(seasonFactor + 1) == 1) {
            if (isColdMonth) {
                if (weather > 1) weather--;
            } else if (isHotMonth) {
                if (weather > 3) weather--;
            } else {
                weather--;
            }
        } else if (rand.nextInt(6 - seasonFactor) == 1) {
            if (isHotMonth) {
                if (weather < 7) weather++;
            } else if (isColdMonth) {
                if (weather < 6) weather++;
            } else {
                weather++;
            }
        }
    } else if (weather == 7 && rand.nextInt(3) == 1) {
        weather--;
    } else if (rand.nextInt(3) == 1) {
        if (weather == 3 && rand.nextInt(3) == 1) {
            weather++;
        } else if (weather == 2 && rand.nextInt(3) == 1) {
            weather--;
        } else {
            weather++;
        }
    }

    if (weather < 1) weather = 1;
    if (weather > 8) weather = 8;

    if (weather == 8 && weatherEffectMap.get(weather) != "Thunderstorm" && rand.nextInt(4) == 1) {
        weather = 3;
    }
    if (weather == 1 && weatherEffectMap.get(weather) != "Blizzard" && rand.nextInt(4) == 1) {
        weather = 2;
    }

    if (weather < 1) weather = 1;
    if (weather > 8) weather = 8;
}

    public void checkSupplies() {
        TextIO.getln();
        player.inventoryToString();
        TextIO.getln();
        Terminal.clean();
    }

    public void lookAtMap() {
        TextIO.getln();
        System.out.println("Map of the Oregon Trail:");
        System.out.println("----------------------------------------");

        StringBuilder pathLine = new StringBuilder();
        StringBuilder markerLine = new StringBuilder();

        int currentIndex = milesTraveled;

        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);

            pathLine.append("-");

            char marker = ' ';

            if (location instanceof Fort) {
                marker = 'F';
            } else if (location instanceof River) {
                marker = 'R';
            } else if (location instanceof Landmark) {
                marker = 'L';
            }

            if (i == currentIndex) {
                marker = '*';
            }

            markerLine.append(marker);
        }

        System.out.println(pathLine);
        System.out.println(markerLine);

        System.out.println("\nLegend:");
        System.out.println("* - Current Location");
        System.out.println("F - Fort");
        System.out.println("L - Landmark");
        System.out.println("R - River");
        System.out.println("----------------------------------------");
        TextIO.getln();
    }

    public void stopToRest() {
        Terminal.println("How many days would you like to rest?");
        this.daysTraveled += TextIO.getlnInt();
    }

    public void attemptToTrade() {
    }

    public void talkToLocals() {
        Terminal.print("A local tells you: " + getLastVisitedLandmark().getDescription());
        TextIO.getln();
    }

    public void hunt(){
        
    }

    public void atFort(){
    }

    private void chooseDepartureMonth() {
        Terminal.print("When would you like to leave? (1) March (2) April (3) May (4) June (5) July: ");
        this.month = TextIO.getInt() + 1;
    }

    private void buySupplies() {
        TextIO.getln();

        Terminal.println("You have $" + String.valueOf(player.getMoney()) + "0 to spend. You can get what you need at Matt's general store.");

        TextIO.getln();
        Terminal.clean();

        Terminal.println("Hello, I'm Matt! So you're going to Oregon! I can fix you up with what you need:");
        Terminal.println("- a team of oxen to pull your wagon");
        Terminal.println("- clothing for both summer and winter");
        Terminal.println("- plenty of food for the trip");
        Terminal.println("- ammunition for your rifles");
        Terminal.println("- spare parts for your wagon");

        TextIO.getln();
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
    }

    private void setupPaceEffects() {
        paceEffectMap = new HashMap<>();
        paceEffectMap.put(1, "Steady");
        paceEffectMap.put(2, "Strenuous");
        paceEffectMap.put(3, "Grueling");
    }

    private void setupHealthEffects() {
        healthEffectMap = new HashMap<>();
        healthEffectMap.put(3, "Good");
        healthEffectMap.put(2, "Fair");
        healthEffectMap.put(1, "Poor");
    }

    private void setupRationEffects() {
        rationEffectMap = new HashMap<>();
        rationEffectMap.put(1, "Filling");
        rationEffectMap.put(2, "Meager");
        rationEffectMap.put(3, "Bare-bones");
    }

    private void setupWeatherEffects() {
        weatherEffectMap = new HashMap<>();
        weatherEffectMap.put(1, "Blizzard");
        weatherEffectMap.put(2, "Snowing");
        weatherEffectMap.put(3, "Raining");
        weatherEffectMap.put(4, "Cold");
        weatherEffectMap.put(5, "Moderate");
        weatherEffectMap.put(6, "Warm");
        weatherEffectMap.put(7, "Hot");
        weatherEffectMap.put(8, "Thunderstorm");
    }

    private void createPlayer() {
        Terminal.print("Choose your profession: (1) Banker from Boston (2) Carpenter from Ohio (3) Farmer from Illinois: ");
        int professionChoice = TextIO.getlnInt();
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

    private void initLocations() {
        locations.add(new Fort("Independence", "A trading post and starting point.", 0));  
        locations.add(new Fort("Fort Kearny", "A fort on the plains, providing supplies and protection.", 200));
        locations.add(new Landmark("Chimney Rock", "A distinctive rock formation in Nebraska, often used by travelers to navigate the plains.", 250));
        locations.add(new Fort("Fort Laramie", "A military post that offers resupply and repairs.", 350));
        locations.add(new Landmark("Independence Rock", "A massive rock formation that served as a major landmark and guide for westward travelers.", 400));
        locations.add(new Landmark("South Pass", "A high pass through the Rocky Mountains that served as a major route for pioneers.", 450));
        locations.add(new Fort("Fort Bridger", "A trading post and the final fort on the trail.", 550));
        locations.add(new Landmark("Soda Springs", "A popular resting spot, famous for its naturally carbonated spring.", 700));
        locations.add(new Fort("Fort Hall", "A fur trading post and rest stop in the Snake River region.", 800));
        locations.add(new Fort("Fort Boise", "A fort established to protect travelers from Indian attacks and offer supplies.", 950));
        locations.add(new Landmark("Blue Mountains", "A mountain range in Oregon, known for its treacherous terrain and challenging climbs.", 1100));
        locations.add(new Landmark("The Dalles", "A series of rapids in the Columbia River, requiring travelers to portage their wagons.", 1200));
        locations.add(new Fort("Fort Walla Walla", "A fort on the Columbia River, providing a break before reaching the Cascades.", 1250));
        locations.add(new Landmark("Oregon City", "The final destination for travelers on the Oregon Trail, where many pioneers hoped to settle in the fertile Willamette Valley.", 1350));

        locations.add(new River("Kansas River", "A dangerous river crossing in the Kansas Territory.", 100));
        locations.add(new River("Platte River", "A major river that many pioneers use to guide their way west.", 150));
        locations.add(new River("North Platte River", "A branch of the Platte River that travelers followed.", 250));
        locations.add(new River("Sweetwater River", "A river that flows through the Rocky Mountains and was often crossed by wagon trains.", 350));
        locations.add(new River("Green River", "A large river, sometimes difficult to ford, especially during high water.", 500));
        locations.add(new River("Snake River", "A challenging river with rocky terrain and swift currents.", 750));
        locations.add(new River("Columbia River", "A dangerous river at the end of the trail, with rapids that test even the most skilled navigators.", 1300));

        Collections.sort(locations, new Comparator<Landmark>() {
            public int compare(Landmark l1, Landmark l2) {
                return Integer.compare(l1.getDistanceFromPrevious(), l2.getDistanceFromPrevious());
            }
        });
    }

    public void init() {
        Terminal.clean();
        Terminal.print("***THE OREGON TRAIL***\n");
        Terminal.print("***starting game***");
        Terminal.sleep(1000);
    }

    public int calculateHealth(Player p, ArrayList<Companion> c){
        return (int)(p.getHealth() + c.get(0).getHealth() + c.get(1).getHealth() + c.get(2).getHealth() + c.get(3).getHealth())/5;
    }

    public void start() {
        int choice;
        while (gameRunning) {
            Terminal.clean();
            if (isCurrentLocation(locations, milesTraveled)) {
                Terminal.println(getLastVisitedLandmark().getName());
            }
            Terminal.println("Today's Date: " + Date.calculate(daysTraveled, month, "date"));
            Terminal.println("Weather: " + weatherEffectMap.get(this.weather));
            Terminal.println("Health: " + healthEffectMap.get(calculateHealth(player, companions)));
            Terminal.println("Pace: " + paceEffectMap.get(this.pace));
            Terminal.println("Rations: " + rationEffectMap.get(this.rations));

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
                    if (isCurrentLocation(locations, milesTraveled)) {
                        talkToLocals();
                    } else {
                        hunt();
                    }
                    break;
                case 9:
                    if (isCurrentLocation(locations, milesTraveled)) {
                        atFort();
                        break;
                    }
                default:
                    Terminal.print("Invalid choice, please try again.");
                    break;
            }
        }
    }



    public void changePace() {
        Terminal.println("Change Pace");
        switch (pace) {
            case 1:
                Terminal.println("(Currently 'steady')");
                break;
            case 2:
                Terminal.println("(Currently 'strenuous')");
                break;
            case 3:
                Terminal.println("(Currently 'grueling')");
                break;
            default:
                Terminal.println("Uh oh! Error in the game.");
                break;
        }
        Terminal.println("The pace at which you travel can change. Your choices are:");
        Terminal.println("1. Steady - a steady pace with moderate resource usage.");
        Terminal.println("2. Strenuous - faster progress, but uses more resources.");
        Terminal.println("3. Grueling - very fast, but risks exhaustion and health.");
        Terminal.print("What is your choice? ");
        this.pace = TextIO.getlnInt();
        Terminal.println("New Pace Effect: " + paceEffectMap.get(this.pace)); // Show updated effect
    }

    public void changeFoodRations() {
        Terminal.println("Change food rations");
        switch (rations) {
            case 1:
                Terminal.println("(Currently 'filling')");
                break;
            case 2:
                Terminal.println("(Currently 'meager')");
                break;
            case 3:
                Terminal.println("(Currently 'bare bones')");
                break;
            default:
                Terminal.println("Uh oh! Error in the game.");
                break;
        }
        Terminal.println("The amount of food you distribute can change. Your choices are:");
        Terminal.println("1. Filling - everyone eats generously.");
        Terminal.println("2. Meager - sufficient, but small.");
        Terminal.println("3. Bare bones - minimal, everyone stays hungry.");
        Terminal.print("What is your choice? ");
        this.rations = TextIO.getlnInt();
    }

    private void showMainMenu() {
        if (isCurrentLocation(locations, milesTraveled)) {
            Terminal.println("1. Continue on Trail");
            Terminal.println("2. Check supplies");
            Terminal.println("3. Look at Map");
            Terminal.println("4. Change Pace");
            Terminal.println("5. Change food rations");
            Terminal.println("6. Stop to Rest");
            Terminal.println("7. Attempt to Trade");
            Terminal.println("8. Talk to Locals");
            Terminal.println("9. Buy supplies");
        } else {
            Terminal.println("1. Continue on Trail");
            Terminal.println("2. Check supplies");
            Terminal.println("3. Look at Map");
            Terminal.println("4. Change Pace");
            Terminal.println("5. Change food rations");
            Terminal.println("6. Stop to Rest");
            Terminal.println("7. Attempt to Trade");
            Terminal.println("8. Hunt for food");
        }
    }

    public boolean isCurrentLocation(ArrayList<Landmark> locations, int currentLocation) {
        for (Landmark landmark : locations) {
            if (landmark.getDistanceFromPrevious() == currentLocation) {
                return true;
            }
        }
        return false;
    }

    public Landmark getLastVisitedLandmark() {
        Landmark lastVisited = null;
        for (Landmark location : locations) {
            if (location instanceof Landmark) {
                if (location.getDistanceFromPrevious() <= milesTraveled) {
                    lastVisited = location;
                }
            }
        }

    return lastVisited;
}

}
