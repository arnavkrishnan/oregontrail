import java.util.*;

public class Inventory {
    private Map<ItemType, Item> items;

    public Inventory() {
        this.items = new HashMap<>();
    }

    public void addItem(Item item) {
        if (items.containsKey(item.getType())) {
            Item existingItem = items.get(item.getType());
            existingItem.addQuantity(item.getQuantity());
        } else {
            items.put(item.getType(), item);
        }
    }

    public void removeItem(ItemType itemType) {
        items.remove(itemType);
    }

    public boolean subtractItemQuantity(ItemType itemType, int quantity) {
        Item item = items.get(itemType);
        if (item != null) {
            return item.subtractQuantity(quantity);
        }
        return false;
    }

    public int getItemQuantity(ItemType itemType) {
        Item item = items.get(itemType);
        return (item != null) ? item.getQuantity() : 0;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items.values());
    }

    public boolean containsItem(ItemType itemType) {
        return items.containsKey(itemType);
    }

    public void printInventory() {
        if (items.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("Inventory:");
            for (Item item : items.values()) {
                System.out.println("Item: " + item.getType() + ", Quantity: " + item.getQuantity());
            }
        }
    }
}
