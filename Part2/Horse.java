public class Horse {
    String name;
    String breed;
    String color;
    String symbol;
    double confidence;
    int distanceTravelled;
    boolean fallen;

    public Horse(String name, String breed, String color, String symbol, double confidence) {
        this.name = name;
        this.breed = breed;
        this.color = color;
        this.symbol = symbol;
        this.confidence = confidence;
        this.distanceTravelled = 0;
        this.fallen = false;
    }

    public void move() {
        if (!fallen) {
            distanceTravelled += (Math.random() < confidence) ? (int)(Math.random() * 10) : (int)(Math.random() * 5);
            if (Math.random() < 0.05) { // 5% chance to fall
                fallen = true;
                confidence = Math.max(0, confidence - 0.1);
            }
        }
    }
}
