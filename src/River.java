public class River extends Landmark implements Location {

    private int depth;
    private int width;

    public River(String name, String description, int distanceFromPrevious, int depth, int width) {
        super(name, description, distanceFromPrevious);
        this.depth=depth;
        this.width=width;
    }

    @Override
    public void interact() {
        System.out.println("You must cross the " + this.getName() + " river.");
    }

    @Override
    public void showDetails() {
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    
}
