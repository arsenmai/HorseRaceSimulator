/**
 * The Simulator class contains the main method to run the Horse Race Simulator.
 */
public class Simulator {
    public static void main(String[] args) {
        // Define the race length (for example, 30 units)
        Race race = new Race(30);
        
        // Create three horses with their respective symbols, names, and confidence ratings
        Horse horse1 = new Horse('♘', "PIPPI LONGSTOCKING", 0.6);
        Horse horse2 = new Horse('♞', "KOKOMO", 0.6);
        Horse horse3 = new Horse('❌', "EL JEFE", 0.4);
        
        // Add the horses to the race lanes
        race.addHorse(horse1, 1);
        race.addHorse(horse2, 2);
        race.addHorse(horse3, 3);
        
        // Start the race simulation
        race.startRace();
    }
}
