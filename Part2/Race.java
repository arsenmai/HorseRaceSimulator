import java.util.List;

public class Race {
    private List<Horse> horses;
    private int trackLength;

    public Race(List<Horse> horses, int trackLength) {
        this.horses = horses;
        this.trackLength = trackLength;
    }

    public void start() {
        for (Horse horse : horses) {
            horse.move();
        }
    }

    public boolean isRaceFinished() {
        return horses.stream().anyMatch(h -> h.distanceTravelled >= trackLength);
    }
}
