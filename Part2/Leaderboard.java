import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Leaderboard {
    public List<String> topHorses() {
        try {
            return Files.lines(Paths.get("race_results.csv"))
                    .collect(Collectors.groupingBy(l -> l, Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(5)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
