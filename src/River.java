public class River implements Location {
    private String name;
    private int distance;

    public River(String name, int distance) {
        this.name = name;
        this.distance = distance;
    }

    @Override
    public void interact() {
        System.out.println("You must cross the " + name + " river.");
    }

    @Override
    public void showDetails() {
    }

    public int getDistance() {
        return distance;
    }
}
