import java.util.*;
import java.lang.*;
import java.io.*;
import java.text.*;

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
    private HashMap<Integer, PaceType> paceEffectMap;
    private HashMap<Integer, HealthType> healthEffectMap;
    private HashMap<Integer, RationType> rationEffectMap;
    private HashMap<Integer, WeatherType> weatherEffectMap;

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

        endGame();
    }

    public void continueOnTrail() {
    // very advanced calculation
    int dailyMiles = calculateDailyTravel();
        milesTraveled += dailyMiles;
        daysTraveled++;

        // must stop at landmark. look around maybe. idk
        Landmark nextLandmark = getNextLandmark();
        if (!isCurrentLocation(locations, milesTraveled)){
            milesTraveled=Math.min(milesTraveled, getNextLandmark().getDistanceFromPrevious()-getLastVisitedLandmark().getDistanceFromPrevious());
        }

        // river?
        if (isCurrentLocation(locations, milesTraveled)) {
            if (getLastVisitedLandmark() instanceof River){
                River river = (River) nextLandmark;
                handleRiverCrossing(river);
            }
        } else {
            // normal
            doChanges();
            calculateWeather();
            getEvent();
            Terminal.getln();
        }
    }

    private int calculateDailyTravel() {
        // - stamina and morale improve performance
        // - no. oxen can affect the speed (more oxen = faster)
        // - pace determines how quickly the group moves (duh)
        double staminaFactor = (double) player.getStamina() / 100;
        double moraleFactor = (double) player.getMorale() / 100;
        int oxenFactor = oxen.size(); // Number of oxen (cuz array list)

        staminaFactor++;
        moraleFactor++;

        // Calculate daily miles based on factors
        int dailyMiles = (int) (staminaFactor * moraleFactor * oxenFactor * pace)*5;
        return Math.max(1, dailyMiles);  // gotta at least move
    
    }




    public void getEvent(){
        EventType event = Event.getRandomEvent(player, companions, oxen, weatherEffectMap.get(weather), Date.calculate(daysTraveled, month, "date"), rations, pace, 100, 100, 100);
        Random rand = new Random();
        switch (event) {
            case WILD_FRUIT:
                Terminal.println("You find some wild fruit.");
                player.addItem(new Item(ItemType.FOOD, player.getProfession().equals(Profession.FARMER) ? 40 : 20));
                Terminal.println(player.getProfession().equals(Profession.FARMER) ? "+40 food" : "+20 food");
                break;
    
            case WAGON_BROKEN:
                int part = rand.nextInt(3);
                int chance = player.getProfession().equals(Profession.CARPENTER) ? rand.nextInt(5) : rand.nextInt(7);
                if (part == 0){
                    Terminal.println("The wagon wheel is broken.");
                    if (chance < 3){
                        Terminal.println("You were able to repair the wheel.");
                    } else {
                        Terminal.println("You were unable to repair the wheel. You must replace the broken wheel.");
                        if (player.subtractItem(new Item(ItemType.WHEELS, 1))){
                            Terminal.println("You successfully replaced the wheel.");
                        } else {
                            Terminal.println("You do not have any wheels to replace the part.");
                            Terminal.println("Unable to move, you are stranded in " + getLastVisitedLandmark().getName());
                            endGame();
                        }
                    }
                } else if (part == 1){
                    Terminal.println("The wagon axle is broken.");
                    if (chance < 3){
                        Terminal.println("You were able to repair the axle.");
                    } else {
                        Terminal.println("You were unable to repair the axle. You must replace it.");
                        if (player.subtractItem(new Item(ItemType.AXELS, 1))){
                            Terminal.println("You successfully replaced the axle.");
                        } else {
                            Terminal.println("You do not have any axles to replace the part.");
                            Terminal.println("Unable to move, you are stranded in " + getLastVisitedLandmark().getName());
                            endGame();
                        }
                    }
                } else {
                    Terminal.println("The wagon tongue is broken.");
                    if (chance < 3){
                        Terminal.println("You were able to repair the tongue.");
                    } else {
                        Terminal.println("You were unable to repair the tongue. You must replace it.");
                        if (player.subtractItem(new Item(ItemType.TONGUES, 1))){
                            Terminal.println("You successfully replaced the tongue.");
                        } else {
                            Terminal.println("You do not have any tongues to replace the part.");
                            Terminal.println("Unable to move, you are stranded in " + getLastVisitedLandmark().getName());
                            endGame();
                        }
                    }
                }
                break;
    
            case WAGON_FIRE:
                Terminal.println("The wagon lights on fire!");
                player.subtractItem(new Item(ItemType.FOOD, (int)0.6*player.getInventory().getItemQuantity(ItemType.FOOD)));
                Terminal.print("You lost 60% of your food.");
                player.subtractItem(new Item(ItemType.CLOTHES, (int)0.5*player.getInventory().getItemQuantity(ItemType.CLOTHES)));
                Terminal.println("You lost 50% of your clothes.");
                player.subtractItem(new Item(ItemType.BULLETS, (int)0.9*player.getInventory().getItemQuantity(ItemType.BULLETS)));
                Terminal.print("You lost 90% of your food.");
                player.subtractItem(new Item(ItemType.WHEELS, player.getInventory().getItemQuantity(ItemType.WHEELS)));
                player.subtractItem(new Item(ItemType.AXELS, player.getInventory().getItemQuantity(ItemType.AXELS)));
                player.subtractItem(new Item(ItemType.TONGUES, player.getInventory().getItemQuantity(ItemType.TONGUES)));
                Terminal.println("You lost any spare parts.");
                break;
    
            case WAGON_ROBBED:
                Terminal.println("The wagon gets robbed!");
                int lootChance = rand.nextInt(100);
                if (lootChance < 30){
                    Terminal.println("The robbers steal some of your food and supplies.");
                    if (player.subtractItem(new Item(ItemType.FOOD, 20))){
                        Terminal.println("They took 20 food.");
                    }
                    if (player.subtractItem(new Item(ItemType.CLOTHES, 5))){
                        Terminal.println("They took some of your clothing.");
                    }
                } else {
                    Terminal.println("Luckily, the robbers don't manage to steal anything valuable.");
                }
                break;
    
            case OXEN_INJURED_OR_DEAD:
                int oxenChance = rand.nextInt(3);
                if (oxenChance == 0){
                    Terminal.println("One of your oxen has broken its leg. You must rest to heal it.");
                    stopToRest();
                } else if (oxenChance == 1){
                    Terminal.println("You lost an ox! You must continue with fewer oxen, which will slow your travel.");
                    player.subtractItem(new Item(ItemType.OXEN, 1));
                } else {
                    Terminal.println("You have lost all your oxen. Without oxen, you cannot move.");
                    Terminal.println("You are stranded and unable to continue your journey.");
                    endGame();
                }
                break;
    
            case PERSON_HAS_DISEASE:
                Terminal.println("A companion has fallen ill.");
                //disease event
                // classic part of the game. it's not the oregon trial unless everyone dies of dysentery
                Companion illCompanion = companions.get(rand.nextInt(companions.size()));
                Terminal.println(illCompanion.getName() + " has contracted a disease.");
                int cureChance = rand.nextInt(100);
                if (cureChance < 50){
                    Terminal.println(illCompanion.getName() + " recovers after rest and medicine.");
                } else {
                    Terminal.println(illCompanion.getName() + " is too weak to recover. You must try again or risk their death.");
                }
                break;
    
            case PERSON_HAS_BROKEN:
                Terminal.println("A companion has broken a limb.");
                Companion injuredCompanion = companions.get(rand.nextInt(companions.size()));
                Terminal.println(injuredCompanion.getName() + " has broken a limb and will need to rest.");
                injuredCompanion.setHealth(injuredCompanion.getHealth()-0.5);
                stopToRest();
                break;
    
            case PERSON_HAS_DIED:
                Terminal.println("A companion has died.");
                Companion deceasedCompanion = companions.remove(rand.nextInt(companions.size()));
                Terminal.println(deceasedCompanion.getName() + " has died due to illness or injury.");
                break;
    
            case FIND_ABANDONED_WAGON:
                Terminal.println("You find an abandoned wagon.");
                int findChance = rand.nextInt(100);
                if (findChance < 50){
                    Terminal.println("You find some useful supplies, including food and spare parts.");
                    player.addItem(new Item(ItemType.FOOD, 30));
                    player.addItem(new Item(ItemType.WHEELS, 1));
                    player.addItem(new Item(ItemType.AXELS, 1));
                    player.addItem(new Item(ItemType.TONGUES, 1));
                } else {
                    Terminal.println("Unfortunately, the wagon has been looted, and there is nothing useful.");
                }
                break;
    
            case NOTHING_HAPPENS:
                Terminal.println("Nothing significant happens.");
                break;
    
            default:
                Terminal.println("Congrats - you found an error. Go buy a lotto ticket, or something.");
        }
    }
    
    public void doChanges(){
        player.subtractItem(new Item(ItemType.FOOD, rations*companions.size()*3));
        if (rations==1){
            ;
        } else if (rations == 2){
            player.setMorale(player.getMorale()-2);
            player.setHealth(player.getHealth()-0.052);
            player.setHygiene(player.getHygiene()-1);
            player.setStamina(player.getStamina()-3);

            for (Companion c : companions){
                c.setMorale(c.getMorale()-2);
                c.setHealth(c.getHealth()-0.052);
                c.setHygiene(c.getHygiene()-1);
                c.setStamina(c.getStamina()-3);
            }
        } else {
            player.setMorale(player.getMorale()-5);
            player.setHealth(player.getHealth()-0.13);
            player.setHygiene(player.getHygiene()-3);
            player.setStamina(player.getStamina()-7);

            for (Companion c : companions){
                c.setMorale(c.getMorale()-5);
                c.setHealth(c.getHealth()-0.11);
                c.setHygiene(c.getHygiene()-3);
                c.setStamina(c.getStamina()-7);
            }
        }
        
        if(player.getInventory().getItemQuantity(ItemType.FOOD)<1){
            System.out.println("LOW FOOD?");
            System.out.println(player.getInventory().getItemQuantity(ItemType.FOOD));
            System.exit(0);
            player.setMorale(player.getMorale()-12);
            player.setHealth(player.getHealth()-0.27);
            player.setHygiene(player.getHygiene()-7);
            player.setStamina(player.getStamina()-18);

            for (Companion c : companions){
                c.setMorale(c.getMorale()-12);
                c.setHealth(c.getHealth()-0.31);
                c.setHygiene(c.getHygiene()-7);
                c.setStamina(c.getStamina()-18);
            }
        }
    }

    public void calculateWeather() {
        Random rand = new Random();
        int currentMonth = Integer.parseInt(Date.calculate(daysTraveled, month, "month"));
        int currentDay = Integer.parseInt(Date.calculate(daysTraveled, month, "day"));
    
        boolean isColdMonth = (currentMonth == 12 || currentMonth == 1 || currentMonth == 2);
        boolean isHotMonth = (currentMonth == 6 || currentMonth == 7 || currentMonth == 8);
    
        if (isColdMonth) {
            if (this.weather == 4 || this.weather == 5) {
                if (rand.nextInt(10) < 2) {
                    this.weather = (this.weather == 5) ? 2 : 1;
                } else if (rand.nextInt(10) < 6) {
                    this.weather = 2;
                }
            }
        } else if (currentMonth >= 3 && currentMonth <= 5) {
            if (this.weather == 3) {
                if (rand.nextInt(10) < 2) {
                    this.weather = 8;
                }
            } else if (this.weather == 4 || this.weather == 5) {
                if (rand.nextInt(10) < 4) {
                    this.weather = 3;
                }
            }
        } else if (isHotMonth) {
            if (this.weather == 7 || this.weather == 6) {
                if (rand.nextInt(10) < 4) {
                    this.weather = 8;
                } else if (rand.nextInt(10) < 3) {
                    this.weather = 3;
                }
            }
        } else if (currentMonth >= 9 && currentMonth <= 11) {
            if (this.weather == 4 || this.weather == 5) {
                if (rand.nextInt(10) < 3) {
                    this.weather = 3;
                } else if (rand.nextInt(10) < 2) {
                    this.weather = 8;
                }
            }
        }
    
        if (this.weather == 2 && this.weather != 3) {
            if (rand.nextInt(4) == 1) {
                this.weather = 3;
            }
        }
    
        if (this.weather == 1 && this.weather != 2) {
            if (rand.nextInt(4) == 1) {
                this.weather = 2;
            }
        }
    
        if (this.weather == 8 && this.weather != 3) {
            if (rand.nextInt(4) == 1) {
                this.weather = 3;
            }
        }
    
        if (this.weather < 1) this.weather = 1;
        if (this.weather > 8) this.weather = 8;
    }

    public void checkSupplies() {
        player.inventoryToString();
        Terminal.getln();
        Terminal.clean();
    }

    public void lookAtMap() {
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
        Terminal.getln();
    }

    public void stopToRest() {
        Terminal.println("How many days would you like to rest?");
        this.daysTraveled += TextIO.getlnInt();
    }

    public void attemptToTrade() {
    }

    public void talkToLocals() {
        Terminal.clean();
        Terminal.print("A local tells you: " + getLastVisitedLandmark().getDescription());
        Terminal.getln();
    }

    public void hunt(){
        
    }

    public void atFort(){
    }

    private void chooseDepartureMonth() {
    int month;
    do {
        Terminal.print("When would you like to leave? (1) March (2) April (3) May (4) June (5) July: ");
        month = TextIO.getInt();
        if (month < 1 || month > 5) {
            Terminal.print("Invalid choice. Please enter a number between 1 and 5.\n");
        }
    } while (month < 1 || month > 5);
    this.month = month;
}


    private void buySupplies() {
        Terminal.println("You have $" + String.valueOf(player.getMoney()) + "0 to spend. You can get what you need at Matt's general store.");

        Terminal.getln();
        Terminal.clean();

        Terminal.println("Hello, I'm Matt! So you're going to Oregon! I can fix you up with what you need:");
        Terminal.println("- a team of oxen to pull your wagon");
        Terminal.println("- clothing for both summer and winter");
        Terminal.println("- plenty of food for the trip");
        Terminal.println("- ammunition for your rifles");
        Terminal.println("- spare parts for your wagon");

        Terminal.getln();
        Terminal.clean();

        int yoke = MattsGeneralStore.buyOxen(player);
        player.makeCharge(40*yoke);
        player.addItem(new Item(ItemType.OXEN, yoke*2));
        oxen.add(new Oxen());

        Terminal.clean();

        int food = MattsGeneralStore.buyFood(player);
        player.makeCharge(0.20*food);
        player.addItem(new Item(ItemType.FOOD, food));

        Terminal.clean();

        int clothes = MattsGeneralStore.buyClothes(player);
        player.makeCharge(10*clothes);
        player.addItem(new Item(ItemType.CLOTHES, clothes));

        Terminal.clean();

        int ammo = MattsGeneralStore.buyAmmo(player);
        player.makeCharge(2*ammo);
        player.addItem(new Item(ItemType.BULLETS, 20*ammo));

        Terminal.clean();

        int[] parts = MattsGeneralStore.buyParts(player);
        player.makeCharge(10*(parts[0] + parts[1] + parts[2]));
        player.addItem(new Item(ItemType.WHEELS, parts[0]));
        player.addItem(new Item(ItemType.AXELS, parts[1]));
        player.addItem(new Item(ItemType.TONGUES, parts[2]));

        Terminal.clean();
    }

    private void setupPaceEffects() {
        paceEffectMap = new HashMap<>();
        paceEffectMap.put(1, PaceType.STEADY);
        paceEffectMap.put(2, PaceType.STRENUOUS);
        paceEffectMap.put(3, PaceType.GRUELING);
    }

    private void setupHealthEffects() {
        healthEffectMap = new HashMap<>();
        healthEffectMap.put(3, HealthType.GOOD);
        healthEffectMap.put(2, HealthType.FAIR);
        healthEffectMap.put(1, HealthType.POOR);
    }

    private void setupRationEffects() {
        rationEffectMap = new HashMap<>();
        rationEffectMap.put(1, RationType.FILLING);
        rationEffectMap.put(2, RationType.MEAGER);
        rationEffectMap.put(3, RationType.BARE_BONES);
    }

    private void setupWeatherEffects() {
        weatherEffectMap = new HashMap<>();
        weatherEffectMap.put(1, WeatherType.BLIZZARD);
        weatherEffectMap.put(2, WeatherType.SNOWING);
        weatherEffectMap.put(3, WeatherType.RAINING);
        weatherEffectMap.put(4, WeatherType.COLD);
        weatherEffectMap.put(5, WeatherType.MODERATE);
        weatherEffectMap.put(6, WeatherType.WARM);
        weatherEffectMap.put(7, WeatherType.HOT);
        weatherEffectMap.put(8, WeatherType.THUNDERSTORM);
    }

    private void createPlayer() {
        Terminal.print("Choose your profession: (1) Banker from Boston (2) Carpenter from Ohio (3) Farmer from Illinois: ");
        int professionChoice = TextIO.getlnInt();
        Profession profession;
        int startingMoney = 0;

        switch (professionChoice) {
            case 1: 
                profession = Profession.BANKER;
                startingMoney = 1600;
                break;
            case 2: 
                profession = Profession.CARPENTER;
                startingMoney = 800;
                break;
            case 3: 
                profession = Profession.FARMER;
                startingMoney = 400;
                break;
            default:
                Terminal.print("Invalid input. The developer has not yet enabled error-checking on this section of the code. You will be defaulted to a Carpenter.");
                profession = Profession.CARPENTER;
                startingMoney = 800;
                Terminal.getln();
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

        locations.add(new River("Kansas River", "A dangerous river crossing in the Kansas Territory.", 100, 30, 3));
        locations.add(new River("Platte River", "A major river that many pioneers use to guide their way west.", 150, 50, 4));
        locations.add(new River("North Platte River", "A branch of the Platte River that travelers followed.", 250, 60, 5));
        locations.add(new River("Sweetwater River", "A river that flows through the Rocky Mountains and was often crossed by wagon trains.", 350, 80, 6));
        locations.add(new River("Green River", "A large river, sometimes difficult to ford, especially during high water.", 500, 120, 8));
        locations.add(new River("Snake River", "A challenging river with rocky terrain and swift currents.", 750, 150, 10));
        locations.add(new River("Columbia River", "A dangerous river at the end of the trail, with rapids that test even the most skilled navigators.", 1300, 200, 12));


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
        Terminal.load(100,100);
    }

    public int calculateHealth(Player p, ArrayList<Companion> c){
        return (int)(p.getHealth() + c.get(0).getHealth() + c.get(1).getHealth() + c.get(2).getHealth() + c.get(3).getHealth())/5;
    }

    public void checkIfHealthy() {
        Alive[] livings = new Alive[companions.size() + 1 + oxen.size()];
        livings[0] = player;
        int index = 1;
        for (Companion companion : companions) {
            livings[index++] = companion;
        }
        for (Oxen ox : oxen) {
            livings[index++] = ox;
        }
    
        for (Alive entity : livings) {
            if (entity instanceof Player) {
                Player p = (Player) entity;
                if (p.getHealth() <= 0.0) {
                    Terminal.print("You have died due to critical health. Your body could no longer fight off the illness or injury.");
                } else if (p.getHygiene() <= 0) {
                    Terminal.print("You have contracted a fatal bacterial infection due to poor hygiene.");
                } else if (p.getStamina() <= 0) {
                    Terminal.print("You have died from exhaustion. Your body was too weak to continue.");
                } else if (p.getMorale() <= 0) {
                    Terminal.print("You have died from a complete mental breakdown. You lost the will to go on.");
                }
                endGame();
            } else if (entity instanceof Companion) {
                Companion companion = (Companion) entity;
                if (companion.getHealth() <= 0.0) {
                    Terminal.print(companion.getName() + " has died from poor health.");
                    companions.remove(companion);
                } else if (companion.getHygiene() <= 0) {
                    Terminal.print(companion.getName() + " has contracted a fatal bacterial infection due to poor hygiene.");
                    companions.remove(companion);
                } else if (companion.getStamina() <= 0) {
                    Terminal.print(companion.getName() + " has died from exhaustion.");
                    companions.remove(companion);
                } else if (companion.getMorale() <= 0) {
                    Terminal.print(companion.getName() + " has died due to a complete breakdown of morale.");
                    companions.remove(companion);
                }
            } else if (entity instanceof Oxen) {
                Oxen ox = (Oxen) entity;
                if (ox.getHealth() <= 0.0) {
                    Terminal.print(ox.getName() + " has died from poor health.");
                    oxen.remove(ox);
                } else if (ox.getHygiene() <= 0) {
                    Terminal.print(ox.getName() + " has contracted a fatal infection due to poor hygiene.");
                    oxen.remove(ox);
                } else if (ox.getStamina() <= 0) {
                    Terminal.print(ox.getName() + " has died from exhaustion.");
                    oxen.remove(ox);
                } else if (ox.getMorale() <= 0) {
                    Terminal.print(ox.getName() + " has died due to a complete breakdown of morale.");
                    oxen.remove(ox);
                }
            }
        }
    }
    
    public void start() {
        int choice;
        while (gameRunning) {
            Terminal.clean();
            if (oxen.size() == 0){
                Terminal.clean();
                Terminal.print("You cannot continue trail without oxen. Stranded on the Oregon Trail, you are left to die.");
                Terminal.getln();
                endGame();
            }

            checkIfHealthy();

            if (isCurrentLocation(locations, milesTraveled)) {
                Terminal.println(getLastVisitedLandmark().getName());
            }
            Terminal.println(String.format("%-15s %s", "Today's Date: ", Date.calculate(daysTraveled, month, "date")));
            Terminal.println(String.format("%-15s %s", "Weather: ", weatherEffectMap.get(this.weather)));
            Terminal.println(String.format("%-15s %s", "Health: ", healthEffectMap.get(calculateHealth(player, companions))));
            Terminal.println(String.format("%-15s %s", "Pace: ", paceEffectMap.get(this.pace)));
            Terminal.println(String.format("%-15s %s", "Rations: ", rationEffectMap.get(this.rations)));

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

    private Landmark getNextLandmark() {
        for (Landmark location : locations) {
            if (location.getDistanceFromPrevious() > milesTraveled) {
                return location;
            }
        }
        return null;
    }

    private void handleRiverCrossing(River river) {
        Terminal.println("You have reached the " + river.getName() + "!");
        Terminal.println("It is " + river.getDepth() + " feet deep and " + river.getWidth() + " feet wide.");
        
        Terminal.println("You have three options to cross the river:");
        Terminal.println("1. Ford the wagon");
        Terminal.println("2. Caulk the wagon");
        Terminal.println("3. Get a ferry");
    
        int choice = TextIO.getlnInt();
    
        // Handle the player's choice
        Random rand = new Random();
        switch (choice) {
            case 1: // Ford the wagon
                fordWagon(river, rand);
                break;
            case 2: // Caulk the wagon
                caulkWagon(river, rand);
                break;
            case 3: // Get a ferry
                getFerry(river);
                break;
            default:
                Terminal.println("Invalid choice. Please try again.");
                handleRiverCrossing(river); // invalid
        }
    }
    
    private void fordWagon(River river, Random rand) {
        Terminal.sleep(1000);
        if (river.getDepth() < 4 && river.getWidth() < 200) {
            Terminal.println("You successfully ford the wagon across the river!");
        } else {
            Terminal.println("The river is too deep and wide! There is a chance of damage.");
            if (rand.nextInt(100) < 50) {
                Terminal.println("Your wagon is damaged during the crossing!");
                handleRiverDamage();
            } else {
                Terminal.println("You manage to cross without damage!");
            }
        }
        Terminal.getln();
    }
    
    private void caulkWagon(River river, Random rand) {
        Terminal.sleep(1000);
        if (river.getDepth() < 6 && river.getWidth() < 300) {
            Terminal.println("You caulk the wagon and float it across the river.");
            if (rand.nextInt(100) < 30) {
                Terminal.println("Your wagon is damaged during the crossing!");
                handleRiverDamage();
            } else {
                Terminal.println("The crossing is successful, and the wagon survives!");
            }
        } else {
            Terminal.println("The river is too dangerous to caulk the wagon!");
            handleRiverDamage();
        }
        Terminal.getln();
    }
    
    private void getFerry(River river) {
        // Always successful, but costs money
        // Todo: maybe make different rivers cost more?
        int cost = 15; 
        if (player.getMoney() >= cost) {
            player.makeCharge(cost);
            Terminal.println("You take a ferry across the river for $" + cost + ".");
            Terminal.println("The crossing is safe and without damage!");
        } else {
            Terminal.println("You don't have enough money to afford the ferry.");
            Terminal.println("You must try another way to cross the river.");
            handleRiverCrossing(river);
        }
        Terminal.getln();
    }
    
    private void handleRiverDamage() {
        // randomly damage the player's supplies and oxen
        Random rand = new Random();
        int foodLoss = rand.nextInt(20) + 10;
        int clothingLoss = rand.nextInt(10) + 5;
        int sparePartsLoss = rand.nextInt(2) + 1;
    
        // subtract items from the player's inventory
        player.subtractItem(new Item(ItemType.FOOD, foodLoss));
        player.subtractItem(new Item(ItemType.CLOTHES, clothingLoss));
        player.subtractItem(new Item(ItemType.WHEELS, sparePartsLoss));
        player.subtractItem(new Item(ItemType.AXELS, sparePartsLoss));
        player.subtractItem(new Item(ItemType.TONGUES, sparePartsLoss));
    
        // randomly lose oxen and companions
        if (rand.nextInt(100) < 30) {
            loseOxen(rand);
        }
        if (rand.nextInt(100) < 20) {
            loseCompanion(rand);
        }
    }
    
    private void loseOxen(Random rand) {
        int oxenLost = rand.nextInt(2) + 1; // lose 1 - 2 oxen (remember that is maximum 1 yoke cuz u buy them in yoke)
        for (int i = 0; i < oxenLost && !oxen.isEmpty(); i++) {
            Oxen ox = oxen.remove(rand.nextInt(oxen.size()));
            Terminal.println("You lost an ox.");
        }
        Terminal.getln();
    }
    
    private void loseCompanion(Random rand) {
        if (!companions.isEmpty()) {
            Companion companion = companions.remove(rand.nextInt(companions.size()));
            Terminal.println(companion.getName() + " has died during the river crossing.");
        }
        Terminal.getln();
    }

    public void endGame() {
        Terminal.clean();
        System.out.println("***********************************************");
        System.out.println("*                 GAME OVER                 *");
        System.out.println("***********************************************");
        System.out.println("\n" + player.getName() + ", your journey has come to an end.");
        System.out.println("\nTotal Days Traveled: " + daysTraveled);
        System.out.println("Total Miles Traveled: " + milesTraveled + " miles");
        System.out.println("\nThank you for playing the Oregon Trail!");
        System.out.println("\nPress any key to see your gravestone...");
        Terminal.getln();
        displayGravestone();
    }

    public void displayGravestone() {
        Terminal.clean();
        System.out.println("        _.---,._,'");
        System.out.println("       /' _.--.<");
        System.out.println("         /'     `'");
        System.out.println("       /' _.---._____");
        System.out.println("       \\.'   ___, .-'`");
        System.out.println("           /'    \\\\             .");
        System.out.println("         /'       `-.          -|-");
        System.out.println("        |                       |");
        System.out.println("        |                   .-'~~~`-.");
        System.out.println("        |                 .'         `.");
        System.out.println("        |                 |  R  I  P  |");
        System.out.println("  jgs   |                 |           |");
        System.out.println("        |                 |           |");
        System.out.println("         \\              \\\\|           |//");
        System.out.println("   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        Terminal.println("Thank you for playing The Oregon Trail. While you didn't make it to Oregon, we can only hope that those who follow in your footsteps learn from your mistakes.");
        Terminal.println("When you died, you had " + NumberFormat.getCurrencyInstance().format(player.getMoney()) + " remaining.");

        String location;
        switch (player.getProfession()) {
            case Profession.BANKER:
                location = "Boston";
                break;
            case Profession.CARPENTER:
                location = "Ohio";
                break;
            case Profession.FARMER:
                location = "Illinois";
                break;
            default:
                location = "various islands in the Caribbean";
                break;
        }

        if (player.getMoney() < 1) {
            Terminal.println("Your story ends here. You had too little left to make it.");
            Terminal.println("The people in " + location + " won't hear of your journey.");
        } else if (player.getMoney() < 10) {
            Terminal.println("You have just enough to send a letter to your Aunt in " + location + ", though it may never reach her.");
            Terminal.println("Your name will fade into memory, but your efforts will be quietly remembered.");
        } else if (player.getMoney() < 25) {
            Terminal.println("You can send a letter and a postcard to your Aunt in " + location + ".");
            Terminal.println("You may not have made it to Oregon, but your story will be remembered in small circles.");
        } else if (player.getMoney() < 50) {
            Terminal.println("You have enough money to send a letter, postcard, and a little extra to your Aunt in " + location + ".");
            Terminal.println("A small group in " + location + " will remember your journey and what you tried to achieve.");
        } else if (player.getMoney() < 100) {
            Terminal.println("You have enough to send a letter, postcard, and a decent sum to your Aunt in " + location + ".");
            Terminal.println("You may not have succeeded, but your perseverance will be appreciated in your community.");
        } else {
            Terminal.println("With the money you have left, you’ll be able to send a letter and some funds to your Aunt in " + location + ".");
            Terminal.println("Your name will be quietly remembered, and your story will be shared by those who knew you.");
        }

        Terminal.println("Your journey may have ended, but your story is part of the trail. Press any key to exit.");
        Terminal.getln();
        System.exit(0);
    }
}
