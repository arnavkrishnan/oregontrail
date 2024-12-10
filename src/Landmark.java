public class Landmark implements Location {
    private String name;
    private String description;
    private int distanceFromPrevious;

    public Landmark(String name, String description, int distanceFromPrevious) {
        this.name = name;
        this.description = description;
        this.distanceFromPrevious = distanceFromPrevious;
    }

    @Override
    public void interact() {
    }

    @Override
    public void showDetails() {
    }

    public int getDistanceFromPrevious() {
        return distanceFromPrevious;
    }
}
