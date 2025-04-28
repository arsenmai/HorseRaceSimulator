/*
 * RaceStatistics.java - Manages track records
 */
import java.util.Map;
import java.util.HashMap;
import java.io.*;

public class RaceStatistics {
    private Map<String, Double> trackRecords = new HashMap<>();

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
            // File might not exist yet
        }
    }

    public void saveToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Map.Entry<String, Double> e : trackRecords.entrySet()) {
                pw.println(e.getKey() + "," + e.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateRecord(String trackKey, double time) {
        trackRecords.put(trackKey, Math.min(trackRecords.getOrDefault(trackKey, Double.MAX_VALUE), time));
    }

    public Map<String, Double> getTrackRecords() {
        return trackRecords;
    }
}
