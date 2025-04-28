import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages the leaderboard for horses based on their win counts.
 */
public class Leaderboard {
    private Map<String, Integer> winCounts = new HashMap<>();

    /**
     * Updates the leaderboard with the current number of wins for a horse.
     * @param horseName the name of the horse
     * @param wins the total number of wins for the horse
     */
    public void updateLeaderboard(String horseName, int wins) {
        winCounts.put(horseName, wins);
    }

    /**
     * Returns a list of the top N horses sorted by their win counts in descending order.
     * @param topN the number of top horses to retrieve
     * @return a list of horse names and their win counts formatted as strings
     */
    public List<String> getTopHorses(int topN) {
        return winCounts.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
            .limit(topN)
            .map(e -> e.getKey() + " (" + e.getValue() + " wins)")
            .collect(Collectors.toList());
    }
}
