public abstract class Alive {
    private String name;
    private double health;
    private int morale;
    private int hygiene;
    private int stamina;

    public Alive(String name) {
        this.name = name;
        this.health = 3;
        this.morale = 100;
        this.hygiene = 100;
        this.stamina = 100;
    }

    public void rest(){
        this.health += 0.2;
        this.morale += 5;
        this.hygiene += 1;
        this.stamina += 8;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public int getMorale() {
        return morale;
    }

    public void setMorale(int morale) {
        this.morale = morale;
    }

    public int getHygiene() {
        return hygiene;
    }

    public void setHygiene(int hygiene) {
        this.hygiene = hygiene;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }
}
