import java.util.HashMap;
import java.util.Map;

public class Horse {
    private String name;
    private String symbol;
    private String breed;
    private String coatColor;
    private String accessory;

    private double distanceTravelled;
    private boolean fallen;
    private double confidence;
    private double baseSpeed;
    private double stamina;
    private double finishTime = -1; // -1 means not finished yet
    private int racesParticipated;
    private int wins;
    private Map<String, Double> trackTimes;

    public Horse(String symbol, String name, String breed, String coatColor, String accessory, double confidence) {
        this.symbol = symbol;
        this.name = name;
        this.breed = breed;
        this.coatColor = coatColor;
        this.accessory = accessory;
        this.confidence = clamp(confidence, 0, 1);
        this.distanceTravelled = 0;
        this.fallen = false;
        this.baseSpeed = calculateBaseSpeed();
        this.stamina = calculateStamina();
        this.racesParticipated = 0;
        this.wins = 0;
        this.trackTimes = new HashMap<>();
    }

    // Clamp a value between min and max
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    // Calculates the base speed based on breed and confidence
    private double calculateBaseSpeed() {
        switch (breed) {
            case "Thoroughbred": return 6 + confidence;
            case "Arabian":      return 5.5 + confidence * 1.1;
            case "Quarter Horse":return 6.5 + confidence * 0.9;
            default:              return 5 + confidence;
        }
    }

    // Calculates stamina bonus based on accessory
    private double calculateStamina() {
        return "Lightweight".equals(accessory) ? 1.2 : 1.0;
    }

    // Sets finish time if not already set
    public void setFinishTime(double time) {
        if (finishTime == -1) {
            finishTime = time;
        }
    }

    // Returns the time when horse finished
    public double getFinishTime() {
        return finishTime;
    }

    // Override toString to display horse name when used in JComboBox
    @Override
    public String toString() {
        return name;
    }

    // Movement and race behavior
    public void moveForward(double delta) {
        if (!fallen) {
            distanceTravelled += delta;
        }
    }

    public void fall() {
        fallen = true;
        confidence = clamp(confidence - 0.1, 0, 1);
    }

    public void goBackToStart() {
        distanceTravelled = 0;
        fallen = false;
        finishTime = -1;
    }

    // Record race results and update performance
    public void recordRace(double time, int position, String trackKey) {
        racesParticipated++;
        if (position == 1) {
            wins++;
            confidence = clamp(confidence + 0.05, 0, 1);
        }
        trackTimes.put(trackKey, Math.min(trackTimes.getOrDefault(trackKey, Double.MAX_VALUE), time));
    }

    // Getters and Setters
    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public String getBreed() { return breed; }
    public String getCoatColor() { return coatColor; }
    public String getAccessory() { return accessory; }
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = clamp(confidence, 0, 1); }
    public double getDistanceTravelled() { return distanceTravelled; }
    public double getBaseSpeed() { return baseSpeed; }
    public double getStamina() { return stamina; }
    public boolean hasFallen() { return fallen; }
    public int getRacesParticipated() { return racesParticipated; }
    public int getWins() { return wins; }
    public double getWinRatio() { return racesParticipated == 0 ? 0 : (double) wins / racesParticipated; }
    public Map<String, Double> getTrackTimes() { return trackTimes; }
}
