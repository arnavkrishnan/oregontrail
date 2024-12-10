public class River extends Landmark implements Location {

    public River(String name, String description, int distanceFromPrevious) {
        super(name, description, distanceFromPrevious);
    }

    @Override
    public void interact() {
        System.out.println("You must cross the " + getName() + " river.");
    }

    @Override
    public void showDetails() {
    }
}
