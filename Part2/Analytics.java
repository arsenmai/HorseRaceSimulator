import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Analytics {
    private static final String FILE = "race_results.csv";

    // Just record the winner's name on each line
    public void recordRace(String winnerName) {
        try {
            Files.write(
                Paths.get(FILE),
                (winnerName + "\n").getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
