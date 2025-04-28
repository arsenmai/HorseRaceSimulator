import java.io.*;
import java.nio.file.*;

/**
 * Analytics class for recording race results to a file.
 */
public class Analytics {
    private static final String FILE = "race_results.csv";

    /**
     * Appends the winner's name to the race results file.
     * @param winnerName the name of the horse that won the race
     */
    public void recordRace(String winnerName) {
        try {
            Files.write(
                Paths.get(FILE),
                (winnerName + "\n").getBytes(),
                StandardOpenOption.CREATE, 
                StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
