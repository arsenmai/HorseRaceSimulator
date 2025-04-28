import java.util.*;
import java.time.LocalDateTime;

public class BettingSystem {
    private double balance = 1000.0; // Starting virtual balance for the user
    private Map<String, Integer> currentBets = new HashMap<>();
    private Map<String, Double> currentOdds = new HashMap<>();
    private List<BetRecord> history = new ArrayList<>();
    private List<Horse> horses;
    private Track track;

    public BettingSystem(List<Horse> horses, Track track) {
        this.horses = horses;
        this.track = track;
        calculateOdds(); // Calculate odds at initialization
    }

    public void calculateOdds() {
        currentOdds.clear();
        double totalScore = 0;

        for (Horse h : horses) {
            double score = (h.getWinRatio() * 2.0 + h.getConfidence() + h.getBaseSpeed() / 10.0);

            // Adjust score based on weather conditions
            switch (track.getWeatherCondition()) {
                case "Muddy":
                    score *= 0.9;
                    break;
                case "Icy":
                    score *= 0.8;
                    break;
                default:
                    break;
            }

            totalScore += score;
            currentOdds.put(h.getName(), score); // Temporary raw score
        }

        // Convert raw scores to final odds
        for (Map.Entry<String, Double> entry : currentOdds.entrySet()) {
            double prob = entry.getValue() / totalScore;
            double odds = Math.max(1.5, 1 / prob); // Minimum odds 1.5x
            currentOdds.put(entry.getKey(), odds);
        }
    }

    public Map<String, Double> getCurrentOdds() {
        return currentOdds;
    }

    public Map<String, Integer> getCurrentBets() {
        return currentBets;
    }

    public double getBalance() {
        return balance;
    }

    public List<BetRecord> getBetHistory() {
        return history;
    }

    public void clearHistory() {
        history.clear();
    }

    public void clearBets() {
        currentBets.clear();
    }

    public void placeBet(String horseName, int amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("Not enough virtual currency!");
        }
        balance -= amount;
        currentBets.put(horseName, currentBets.getOrDefault(horseName, 0) + amount);
        history.add(new BetRecord(horseName, amount, currentOdds.getOrDefault(horseName, 2.0)));
    }

    public double payout(String winnerName) {
        double winnings = 0;
        double odd = currentOdds.getOrDefault(winnerName, 2.0);

        for (BetRecord record : history) {
            if (!record.resolved) {
                if (record.horseName.equals(winnerName)) {
                    record.won = true;
                    record.payout = record.amount * odd;
                    winnings += record.payout;
                    balance += record.payout;
                } else {
                    record.won = false;
                    record.payout = 0;
                }
                record.resolved = true;
            }
        }

        clearBets(); // Clear active bets after the race
        return winnings;
    }

    // Inner class to store information about individual bets
    public static class BetRecord {
        public String horseName;
        public int amount;
        public double odds;
        public boolean won;
        public double payout;
        public boolean resolved;
        public LocalDateTime placedAt;

        public BetRecord(String horseName, int amount, double odds) {
            this.horseName = horseName;
            this.amount = amount;
            this.odds = odds;
            this.won = false;
            this.payout = 0;
            this.resolved = false;
            this.placedAt = LocalDateTime.now();
        }
    }
}
