public class Item {
    private ItemType type;
    private int quantity;

    public Item(ItemType type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }

    public ItemType getType() {
        return type;
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
        // bool to check if it went thru
        if (quantityToSubtract > 0 && this.quantity >= quantityToSubtract) {
            this.quantity -= quantityToSubtract;
            return true;
        }
        return false;
    }
}
