// Horse.java
// Represents a single horse in the race, with movement, falling behavior, and confidence management

public class Horse {
    private String name;                // The name of the horse
    private char symbol;                // A character representing the horse on the track
    private int distanceTravelled;       // Total distance the horse has moved
    private boolean fallen;             // Indicates whether the horse has fallen
    private double confidence;          // Value (0-1) that affects how often the horse moves

    // Constructor to create a new horse with specified symbol, name, and initial confidence
    public Horse(char horseSymbol, String horseName, double horseConfidence) {
        this.symbol = horseSymbol;
        this.name = horseName;
        this.confidence = Math.max(0, Math.min(horseConfidence, 1)); // Ensure confidence stays within [0,1]
        this.distanceTravelled = 0;
        this.fallen = false;
    }

    // Set the horse to fallen state and reduce confidence slightly
    public void fall() {
        this.fallen = true;
        this.confidence = Math.max(0, this.confidence - 0.1); // Decrease confidence when horse falls
    }

    // Get the horse's confidence
    public double getConfidence() {
        return this.confidence;
    }

    // Get the total distance travelled
    public int getDistanceTravelled() {
        return this.distanceTravelled;
    }

    // Get the horse's name
    public String getName() {
        return this.name;
    }

    // Get the symbol used to represent this horse
    public char getSymbol() {
        return this.symbol;
    }

    // Reset the horse back to starting position and state
    public void goBackToStart() {
        this.distanceTravelled = 0;
        this.fallen = false;
    }

    // Check if the horse has fallen
    public boolean hasFallen() {
        return this.fallen;
    }

    // Move the horse one step forward if not fallen
    public void moveForward() {
        if (!this.fallen) {
            this.distanceTravelled++;
        }
    }

    // Update the horse's confidence
    public void setConfidence(double newConfidence) {
        this.confidence = Math.max(0, Math.min(newConfidence, 1));
    }

    // Update the horse's representing symbol
    public void setSymbol(char newSymbol) {
        this.symbol = newSymbol;
    }

    // Slightly boost confidence (after winning, for example)
    public void increaseConfidence() {
        this.confidence = Math.min(1, this.confidence + 0.05);
    }
}
