# Horse Race Simulator

Welcome to the **Horse Race Simulator**! This project contains both a **text-based** and a **graphical (GUI)** simulation of horse racing, including horse customization, track settings, betting systems, race analytics, and leaderboards.

---

##  Repository Structure

```bash
HorseRaceSimulator/
|
|├── Part1/        # Text-based horse race simulator
|   ├── Horse.java
|   ├── Race.java
|   └── Main.java
|
|├── Part2/        # GUI-based horse race simulator
|   ├── Horse.java
|   ├── Race.java
|   ├── RaceGUI.java
|   ├── Analytics.java
|   ├── BettingSystem.java
|   ├── Leaderboard.java
|   ├── RaceStatistics.java
|   ├── Track.java
|   ├── TrackShape.java
|   └── Main.java
|
|├── race_results.csv   # Stores race winners for analytics
|├── betting_history.csv # Stores betting outcomes
|└── README.md
```

---

##  Setup Instructions

### Requirements
- Java Development Kit (**JDK 8+**)
- (Optional) IDE like **VS Code**, **IntelliJ IDEA**, or **Eclipse**
- Git (only if you clone the repository)

---

##  How to Run Locally

### 1. Clone the Repository

```bash
git clone https://github.com/arsenmai/Horse-Race-Simulator.git
cd HorseRaceSimulator
```

### 2. Compile the Code

#### For Part 1 (Text-based):

```bash
cd Part1
javac *.java
```

#### For Part 2 (GUI-based):

```bash
cd Part2
javac *.java
```

### 3. Run the Simulator

#### To run **text-based simulation** (Part 1):

```bash
cd Part1
java Main
```

#### To run **graphical (GUI) simulation** (Part 2):

```bash
cd Part2
java RaceGUI
```

---

## How to Use

- **Start Race (Text Mode)**: Runs simulation from Part1 Main.java
- **Start Race (GUI Mode)**: Opens graphical race simulator from RaceGUI.java
- **Track Customization**: Adjust lanes, length, shape, and weather.
- **Horse Customization**: Customize name, breed, color, symbol, and confidence.
- **Betting**: Place virtual bets on horses.
- **Leaderboard/Analytics**: Track race results and top winners.

---

## Project Dependencies

- Java JDK 8+
- Java Swing (built-in)

No additional libraries or frameworks needed!

---

## Notes

- No Maven or Gradle required.
- CSV files will be auto-generated.
- Supports both **text-based** and **GUI-based** simulations separately.

---

# Summary Commands

```bash
# Compiling
javac *.java

# Running text-based
java Main

# Running GUI-based
java RaceGUI
```

---

