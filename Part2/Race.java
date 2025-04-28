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

    public void update(double deltaTime) {
        if (finished) return;

        elapsedTime += deltaTime;

        double conditionFactor = track.getConditionSpeedFactor(); // Учитываем погодный коэффициент!

        for (Horse horse : horses) {
            if (!horse.hasFallen() && horse.getDistanceTravelled() < track.getTrackLength()) {
                double random = Math.random();
                double fallChance = 0.001 * (1.0 - horse.getConfidence()) * track.getFallRiskMultiplier(); // Погода также влияет на шанс упасть

                if (random < fallChance) {
                    horse.fall();
                } else if (random < fallChance + 0.08) {
                    horse.moveForward(horse.getBaseSpeed() * deltaTime * 1.2 * conditionFactor);
                } else if (random < fallChance + 0.18) {
                    horse.moveForward(horse.getBaseSpeed() * deltaTime * 0.8 * conditionFactor);
                } else {
                    horse.moveForward(horse.getBaseSpeed() * deltaTime * conditionFactor);
                }
            }
            if (!horse.hasFallen() && horse.getDistanceTravelled() >= track.getTrackLength() && horse.getFinishTime() < 0) {
                horse.setFinishTime(elapsedTime); // Устанавливаем её финишное время
            }
        }

        if (isFinished()) {
            finished = true;
            winner = getWinner();
            if (winner != null) {
                winningTime = elapsedTime;
            }
        }
    }

    public List<String> getRaceSummary() {
        List<String> summary = new ArrayList<>();
        for (Horse horse : horses) {
            String status;
            if (horse.hasFallen()) {
                status = "Fell";
                summary.add(horse.getName() + " - " + status);
            } else if (horse.getFinishTime() >= 0) {
                status = "Finished";
                summary.add(horse.getName() + " - " + status + ", Time: " + String.format("%.2f", horse.getFinishTime()) + "s");
            } else {
                status = "Incomplete";
                summary.add(horse.getName() + " - " + status);
            }
        }
        return summary;
    }


    public boolean isFinished() {
        for (Horse horse : horses) {
            if (!horse.hasFallen() && horse.getDistanceTravelled() < track.getTrackLength()) {
                return false; // есть хотя бы одна лошадь, которая ещё бежит
            }
        }
        return true; // все лошади либо упали, либо дошли до финиша
    }

    public Horse getWinner() {
        Horse best = null;
        double maxDistance = -1;

        for (Horse horse : horses) {
            if (!horse.hasFallen() && horse.getDistanceTravelled() > maxDistance) {
                best = horse;
                maxDistance = horse.getDistanceTravelled();
            }
        }
        return best; // возвращаем лошадь, прошедшую дальше всех
    }

    public double getWinningTime() {
        return winningTime;
    }

    public Horse getWinnerHorse() {
        return winner;
    }

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
