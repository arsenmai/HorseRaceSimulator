/*
 * Track.java - Encapsulates track configuration and condition effects
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

    public int getLaneCount() { return laneCount; }
    public void setLaneCount(int c) { laneCount = c; }

    public int getTrackLength() { return trackLength; }
    public void setTrackLength(int l) { trackLength = l; }

    public String getTrackShape() { return shape; }
    public void setTrackShape(String s) { shape = s; }

    public String getWeatherCondition() { return weather; }
    public void setWeatherCondition(String w) { weather = w; }

    // Speed factor based on weather
    public double getConditionSpeedFactor() {
        switch (weather) {
            case "Muddy": return 0.7;
            case "Icy":   return 0.9;
            default:       return 1.0;
        }
    }

    // Fall risk multiplier based on weather
    public double getFallRiskMultiplier() {
        switch (weather) {
            case "Icy":   return 1.5;
            case "Muddy": return 1.2;
            default:       return 1.0;
        }
    }
}
