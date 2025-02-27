public class Landmark 
                        implements Location { //  wow funny formatting 
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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
