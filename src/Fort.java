public class Fort implements Location {
    private String name;
    private String description;
    private int distanceFromPrevious;

    public Fort(String name, String description, int distanceFromPrevious) {
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

    public void buySupplies() {
    }

    public int getDistanceFromPrevious() {
        return distanceFromPrevious;
    }
}
