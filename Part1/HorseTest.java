/**
 * The Simulator class contains the main method to run the Horse Race Simulator.
 */
public class HorseTest {
    public static void main(String[] args) {
        Horse horse = new Horse('♘', "TEST HORSE", 0.6);

        // Test: Initial values
        System.out.println("Initial Name: " + horse.getName());                 // Should print "TEST HORSE"
        System.out.println("Initial Symbol: " + horse.getSymbol());            // Should print ♘
        System.out.println("Initial Confidence: " + horse.getConfidence());    // Should print 0.6
        System.out.println("Initial Distance: " + horse.getDistanceTravelled());// Should print 0
        System.out.println("Has Fallen? " + horse.hasFallen());                // Should print false

        // Test: moveForward()
        horse.moveForward();
        System.out.println("Distance after moveForward(): " + horse.getDistanceTravelled()); // Should be 1

        // Test: fall()
        horse.fall();
        System.out.println("Has Fallen? " + horse.hasFallen());                // Should be true

        // Test: goBackToStart()
        horse.goBackToStart();
        System.out.println("After goBackToStart(), Distance: " + horse.getDistanceTravelled()); // 0
        System.out.println("After goBackToStart(), Fallen? " + horse.hasFallen());              // false

        // Test: setConfidence() beyond bounds
        horse.setConfidence(1.5);
        System.out.println("Confidence after setConfidence(1.5): " + horse.getConfidence()); // Should be 1.0

        horse.setConfidence(-0.5);
        System.out.println("Confidence after setConfidence(-0.5): " + horse.getConfidence()); // Should be 0.0
    }
}
