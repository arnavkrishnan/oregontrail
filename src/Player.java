public class Player extends Alive {
    private Profession profession;
    private double money;
    private Inventory inventory;

    public Player(String name, int money, Profession profession) {
        super(name);
        this.profession = profession;
        this.money = money;
        this.inventory = new Inventory();
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
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

    public boolean subtractItem(Item item){
        if (this.inventory.subtractItemQuantity(item.getType(), item.getQuantity())){
            return true;
        } else {
            return false;
        }
    }

    public void makeCharge(double x) {
        this.money -= x;
    }

    public void inventoryToString(){
        inventory.printInventory();
    }
}
