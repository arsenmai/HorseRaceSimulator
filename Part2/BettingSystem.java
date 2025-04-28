import java.util.*;

public class BettingSystem {
    private Map<String, Integer> bets = new HashMap<>();
    private Map<String, Double> odds = new HashMap<>();

    public void placeBet(String horseName, int amount) {
        bets.put(horseName, bets.getOrDefault(horseName, 0) + amount);
        odds.putIfAbsent(horseName, 2.0); // Default odds for simplicity
    }

    public double payout(String winner) {
        int bet = bets.getOrDefault(winner, 0);
        double odd = odds.getOrDefault(winner, 2.0);
        return bet * odd;
    }
}
