import java.util.*;
import java.util.stream.Collectors;

public class Leaderboard {
    private Map<String, Integer> winCounts = new HashMap<>();

    public void updateLeaderboard(String horseName, int wins) {
        winCounts.put(horseName, wins);
    }

    public List<String> getTopHorses(int topN) {
        return winCounts.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
            .limit(topN)
            .map(e -> e.getKey() + " (" + e.getValue() + " wins)")
            .collect(Collectors.toList());
    }
}
