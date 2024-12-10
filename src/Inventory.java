import java.util.*;

public class Inventory {
    private Map<String, Item> items;

    public Inventory() {
        this.items = new HashMap<>();
    }

    public void addItem(Item item) {
        if (items.containsKey(item.getName())) {
            Item existingItem = items.get(item.getName());
            existingItem.addQuantity(item.getQuantity());
        } else {
            items.put(item.getName(), item);
        }
    }

    public void removeItem(String itemName) {
        items.remove(itemName);
    }

    public boolean subtractItemQuantity(String itemName, int quantity) {
        Item item = items.get(itemName);
        if (item != null) {
            return item.subtractQuantity(quantity);
        }
        return false;
    } // same as Item class  I made thiss return bool so that I can see if it actually went thru for error checking

    public int getItemQuantity(String itemName) {
        Item item = items.get(itemName);
        return (item != null) ? item.getQuantity() : 0;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items.values());
    }

    public boolean containsItem(String itemName) {
        return items.containsKey(itemName);
    }

    public void printInventory() {
        if (items.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("Inventory:");
            for (Item item : items.values()) {
                System.out.println("Item: " + item.getName() + ", Quantity: " + item.getQuantity());
            }
        }
    }
}
