public class Item {
    private String name;
    private int quantity;

    public Item(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(int quantityToAdd) {
        if (quantityToAdd > 0) {
            this.quantity += quantityToAdd;
        }
    }

    public boolean subtractQuantity(int quantityToSubtract) {
        if (quantityToSubtract > 0 && this.quantity >= quantityToSubtract) {
            this.quantity -= quantityToSubtract;
            return true;
        }
        return false;
    }
}//i made this one return /true/false for error checking to se if it went thru