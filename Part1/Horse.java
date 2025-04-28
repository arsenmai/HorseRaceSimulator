public class Horse {
    // Private fields for encapsulation
    private String name;
    private char symbol;
    private int distanceTravelled;
    private boolean fallen;
    private double confidence;

    // Constructor
    public Horse(char horseSymbol, String horseName, double horseConfidence) {
        this.symbol = horseSymbol;
        this.name = horseName;
        this.confidence = Math.max(0, Math.min(horseConfidence, 1));
        this.distanceTravelled = 0;
        this.fallen = false;
    }

    public void fall() {
        this.fallen = true;
    }

    public double getConfidence() {
        return this.confidence;
    }

    public int getDistanceTravelled() {
        return this.distanceTravelled;
    }

    public String getName() {
        return this.name;
    }

    public char getSymbol() {
        return this.symbol;
    }

    public void goBackToStart() {
        this.distanceTravelled = 0;
        this.fallen = false;
    }

    public boolean hasFallen() {
        return this.fallen;
    }

    public void moveForward() {
        if (!this.fallen) {
            this.distanceTravelled++;
        }
    }

    public void setConfidence(double newConfidence) {
        this.confidence = Math.max(0, Math.min(newConfidence, 1));
    }

    public void setSymbol(char newSymbol) {
        this.symbol = newSymbol;
    }
}
