import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Analytics {
    private static final String FILE_NAME = "race_results.csv";

    public void recordRace(String winnerName) throws IOException {
        Files.write(Paths.get(FILE_NAME), (winnerName + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
