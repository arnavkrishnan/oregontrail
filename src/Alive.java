public abstract class Alive {
    private String name;
    private double health;
    private int morale;
    private int hygiene;

    public Alive(String name) {
        this.name = name;
        this.health = 3;
        this.morale = 100;
        this.hygiene = 100;
    }

    public abstract void act();
    public abstract void rest();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(int health) {
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
}
