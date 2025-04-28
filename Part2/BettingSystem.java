/*
 * BettingSystem.java - Handles virtual betting
 */
import java.util.*;
import java.time.LocalDateTime;

public class BettingSystem {
    public static class BetRecord {
        public String horseName;
        public int amount;
        public LocalDateTime date;
        public boolean won;
        public double payout;

        public BetRecord(String horseName, int amount) {
            this.horseName = horseName;
            this.amount = amount;
            this.date = LocalDateTime.now();
            this.won = false;
            this.payout = 0;
        }
    }

    private Map<String, Integer> currentBets = new HashMap<>();
    private List<BetRecord> history = new ArrayList<>();

    public void placeBet(String horseName, int amount) {
        currentBets.put(horseName, currentBets.getOrDefault(horseName, 0) + amount);
        history.add(new BetRecord(horseName, amount));
    }

    public Map<String, Double> getCurrentOdds() {
        Map<String, Double> odds = new HashMap<>();
        int total = currentBets.values().stream().mapToInt(Integer::intValue).sum();
        for (Map.Entry<String, Integer> e : currentBets.entrySet()) {
            odds.put(e.getKey(), total > 0 ? (double) total / e.getValue() : 2.0);
        }
        return odds;
    }

    public double payout(String winnerName) {
        double winnings = 0;
        double odd = getCurrentOdds().getOrDefault(winnerName, 2.0);
        for (BetRecord r : history) {
            if (r.horseName.equals(winnerName)) {
                r.won = true;
                r.payout = r.amount * odd;
                winnings += r.payout;
            }
        }
        clearBets();
        return winnings;
    }

    public void clearBets() {
        currentBets.clear();
    }

    public List<BetRecord> getBetHistory() {
        return history;
    }
}
