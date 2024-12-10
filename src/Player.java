public class Player extends Alive {
    private String profession;
    private double money;
    private Inventory inventory;

    public Player(String name, int money, String profession) {
        super(name);
        this.profession = profession;
        this.money = money;
        this.inventory = new Inventory();
    }

    @Override
    public void act() {
    }

    @Override
    public void rest() {
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void addItem(Item item){
        this.inventory.addItem(item);
    }

    public void makeCharge(double x) {
        this.money -= x;
    }

    public void inventoryToString(){
        inventory.printInventory();
    }
}
