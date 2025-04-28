/**
 * Represents the race track configuration including lanes, length, shape, and weather effects.
 */
public class Track {
    private int laneCount;
    private int trackLength;
    private String shape;
    private String weather;

    public Track(int lanes, int length, String shape, String weather) {
        this.laneCount = lanes;
        this.trackLength = length;
        this.shape = shape;
        this.weather = weather;
    }

    public int getLaneCount() {
        return laneCount;
    }

    public void setLaneCount(int laneCount) {
        this.laneCount = laneCount;
    }

    public int getTrackLength() {
        return trackLength;
    }

    public void setTrackLength(int trackLength) {
        this.trackLength = trackLength;
    }

    public String getTrackShape() {
        return shape;
    }

    public void setTrackShape(String shape) {
        this.shape = shape;
    }

    public String getWeatherCondition() {
        return weather;
    }

    public void setWeatherCondition(String weather) {
        this.weather = weather;
    }

    /**
     * Returns the speed factor based on the current weather condition.
     * @return speed adjustment multiplier
     */
    public double getConditionSpeedFactor() {
        switch (weather) {
            case "Muddy": return 0.7;
            case "Icy": return 0.9;
            default: return 1.0;
        }
    }

    /**
     * Returns the fall risk multiplier based on the current weather condition.
     * @return fall risk adjustment multiplier
     */
    public double getFallRiskMultiplier() {
        switch (weather) {
            case "Icy": return 1.5;
            case "Muddy": return 1.2;
            default: return 1.0;
        }
    }
}
