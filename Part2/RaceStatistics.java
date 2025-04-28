import java.util.Map;
import java.util.HashMap;
import java.io.*;

/**
 * Manages race statistics and track records, including loading and saving data from/to a file.
 */
public class RaceStatistics {
    private Map<String, Double> trackRecords = new HashMap<>();

    /**
     * Loads track records from a specified file.
     * @param filename the name of the file to load records from
     */
    public void loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    trackRecords.put(parts[0], Double.parseDouble(parts[1]));
                }
            }
        } catch (IOException e) {
            // File might not exist yet; ignore if not found
        }
    }

    /**
     * Saves the current track records to a specified file.
     * @param filename the name of the file to save records to
     */
    public void saveToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Map.Entry<String, Double> entry : trackRecords.entrySet()) {
                pw.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the track record with a new time if it is faster than the existing one.
     * @param trackKey the unique identifier for the track (e.g., "Oval-Dry")
     * @param time the new race completion time
     */
    public void updateRecord(String trackKey, double time) {
        trackRecords.put(trackKey, Math.min(trackRecords.getOrDefault(trackKey, Double.MAX_VALUE), time));
    }

    /**
     * Returns all recorded track records.
     * @return a map of track keys to best times
     */
    public Map<String, Double> getTrackRecords() {
        return trackRecords;
    }
}
