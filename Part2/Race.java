// Race.java
// Manages the race by controlling the horses, race length, and progression logic

import java.util.concurrent.TimeUnit;

public class Race {
    private int raceLength;             // Total distance horses must travel to win
    private Horse lane1Horse;           // Horse in lane 1
    private Horse lane2Horse;           // Horse in lane 2
    private Horse lane3Horse;           // Horse in lane 3

    // Constructor to create a race of a specified length
    public Race(int distance) {
        raceLength = distance;
        lane1Horse = null;
        lane2Horse = null;
        lane3Horse = null;
    }

    // Adds a horse to a specific lane
    public void addHorse(Horse theHorse, int laneNumber) {
        if (laneNumber == 1) {
            lane1Horse = theHorse;
        } else if (laneNumber == 2) {
            lane2Horse = theHorse;
        } else if (laneNumber == 3) {
            lane3Horse = theHorse;
        } else {
            System.out.println("Cannot add horse to lane " + laneNumber);
        }
    }

    // Starts the race and manages the entire racing loop
    public void startRace() {
        boolean finished = false;

        // Reset all horses to start positions
        if (lane1Horse != null) lane1Horse.goBackToStart();
        if (lane2Horse != null) lane2Horse.goBackToStart();
        if (lane3Horse != null) lane3Horse.goBackToStart();

        // Continue race until a horse wins
        while (!finished) {
            if (lane1Horse != null) moveHorse(lane1Horse);
            if (lane2Horse != null) moveHorse(lane2Horse);
            if (lane3Horse != null) moveHorse(lane3Horse);

            printRace(); // Display current race state

            // Check if any horse has crossed the finish line
            if ((lane1Horse != null && raceWonBy(lane1Horse)) ||
                (lane2Horse != null && raceWonBy(lane2Horse)) ||
                (lane3Horse != null && raceWonBy(lane3Horse))) {
                finished = true;
            }

            try {
                TimeUnit.MILLISECONDS.sleep(150); // Add slight delay for a better animation effect
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Announce the winning horse and boost their confidence
        if (lane1Horse != null && raceWonBy(lane1Horse)) {
            lane1Horse.increaseConfidence();
            System.out.println("And the winner is... " + lane1Horse.getName() + "!");
        } else if (lane2Horse != null && raceWonBy(lane2Horse)) {
            lane2Horse.increaseConfidence();
            System.out.println("And the winner is... " + lane2Horse.getName() + "!");
        } else if (lane3Horse != null && raceWonBy(lane3Horse)) {
            lane3Horse.increaseConfidence();
            System.out.println("And the winner is... " + lane3Horse.getName() + "!");
        }
    }

    // Logic to move a horse forward or cause it to fall
    private void moveHorse(Horse theHorse) {
        if (!theHorse.hasFallen()) {
            // Move forward based on confidence
            if (Math.random() < theHorse.getConfidence()) {
                theHorse.moveForward();
            }
            // Chance to fall (depends on confidence squared)
            if (Math.random() < 0.1 * theHorse.getConfidence() * theHorse.getConfidence()) {
                theHorse.fall();
            }
        }
    }

    // Check if a horse has reached or passed the finish line
    private boolean raceWonBy(Horse theHorse) {
        return theHorse.getDistanceTravelled() >= raceLength;
    }

    // Display the full race track in the console
    private void printRace() {
        System.out.print("\u000C"); // Clear console (works in BlueJ/command line)

        multiplePrint('=', raceLength + 3);
        System.out.println();

        if (lane1Horse != null) {
            printLane(lane1Horse);
            System.out.println();
        }
        if (lane2Horse != null) {
            printLane(lane2Horse);
            System.out.println();
        }
        if (lane3Horse != null) {
            printLane(lane3Horse);
            System.out.println();
        }

        multiplePrint('=', raceLength + 3);
        System.out.println();
    }

    // Display a single horse's lane progress
    private void printLane(Horse horse) {
        int before = horse.getDistanceTravelled();
        int after = raceLength - before;

        System.out.print('|');
        multiplePrint(' ', before);
        System.out.print(horse.hasFallen() ? '\u2322' : horse.getSymbol()); // If fallen, show special symbol
        multiplePrint(' ', after);
        System.out.print('|');
    }

    // Print a character multiple times
    private void multiplePrint(char c, int count) {
        for (int i = 0; i < count; i++) {
            System.out.print(c);
        }
    }
}
