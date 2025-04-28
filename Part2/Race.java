import java.util.List;
import java.util.ArrayList;

public class Race {
    private List<Horse> horses;
    private Track track;
    private boolean finished;
    private Horse winner;
    private double winningTime;
    private double elapsedTime;

    public Race(List<Horse> horses, Track track) {
        this.horses = horses;
        this.track = track;
        this.finished = false;
        this.winner = null;
        this.winningTime = 0;
        this.elapsedTime = 0;
    }

    // Updates the race state each frame
    public void update(double deltaTime) {
        if (finished) return;

        elapsedTime += deltaTime;

        double conditionFactor = track.getConditionSpeedFactor();

        for (Horse horse : horses) {
            if (!horse.hasFallen() && horse.getDistanceTravelled() < track.getTrackLength()) {
                double random = Math.random();
                double fallChance = 0.001 * (1.0 - horse.getConfidence()) * track.getFallRiskMultiplier();

                if (random < fallChance) {
                    horse.fall();
                } else if (random < fallChance + 0.08) {
                    horse.moveForward(horse.getBaseSpeed() * deltaTime * 1.2 * conditionFactor);
                } else if (random < fallChance + 0.18) {
                    horse.moveForward(horse.getBaseSpeed() * deltaTime * 0.8 * conditionFactor);
                } else {
                    horse.moveForward(horse.getBaseSpeed() * deltaTime * conditionFactor);
                }

                // Check immediately if horse finishes after movement
                if (!horse.hasFallen() && horse.getDistanceTravelled() >= track.getTrackLength() && horse.getFinishTime() < 0) {
                    horse.setFinishTime(elapsedTime);
                }
            }
        }

        if (isFinished()) {
            finished = true;
            winner = getWinner();
            if (winner != null) {
                winningTime = winner.getFinishTime();
            }
        }
    }

    // Generates a summary of the race
    public List<String> getRaceSummary() {
        List<String> summary = new ArrayList<>();
        for (Horse horse : horses) {
            String status;
            if (horse.hasFallen()) {
                status = "Fell";
            } else if (horse.getFinishTime() >= 0) {
                status = "Finished, Time: " + String.format("%.2f", horse.getFinishTime()) + "s";
            } else {
                status = "Incomplete";
            }
            summary.add(horse.getName() + " - " + status);
        }
        return summary;
    }

    // Checks if the race is finished
    public boolean isFinished() {
        for (Horse horse : horses) {
            if (!horse.hasFallen() && horse.getDistanceTravelled() < track.getTrackLength()) {
                return false; // At least one horse is still running
            }
        }
        return true; // All horses either finished or fell
    }

    // Determines the winner (fastest horse that finished)
    public Horse getWinner() {
        Horse best = null;
        double bestTime = Double.MAX_VALUE;

        for (Horse horse : horses) {
            if (!horse.hasFallen() && horse.getFinishTime() >= 0) {
                if (horse.getFinishTime() < bestTime) {
                    best = horse;
                    bestTime = horse.getFinishTime();
                }
            }
        }
        return best;
    }

    // Returns winning time
    public double getWinningTime() {
        return winningTime;
    }

    // Returns winner horse
    public Horse getWinnerHorse() {
        return winner;
    }

    // Returns elapsed race time
    public double getElapsedTime() {
        return elapsedTime;
    }

    // Records the race results into statistics and leaderboard
    public void recordResults(RaceStatistics stats, Leaderboard lb) {
        if (winner == null) {
            System.out.println("No winner to record.");
            return;
        }

        String trackKey = track.getTrackShape() + "-" + track.getWeatherCondition();

        winner.recordRace(winningTime, 1, trackKey);
        lb.updateLeaderboard(winner.getName(), winner.getWins());
        stats.updateRecord(trackKey, winningTime);
    }
}
