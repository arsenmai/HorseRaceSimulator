import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RaceGUI extends JFrame {
    private Track track = new Track(3, 100, "Straight", "Dry");
    private List<Horse> horses = new ArrayList<>();
    private Race currentRace;
    private BettingPanel bettingPanel;
    private RaceStatistics raceStats = new RaceStatistics();
    private Leaderboard leaderboard = new Leaderboard();
    private BettingSystem bettingSystem = new BettingSystem(horses, track);

    private JComboBox<Horse> horseSelect = new JComboBox<>();
    private JTextArea statsArea = new JTextArea();
    private JPanel raceVisualPanel;
    private LeaderboardPanel leaderboardPanel;
    private Timer raceTimer;
    private JScrollPane raceScrollPane;

    public RaceGUI() {
        super("Horse Racing Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLayout(new BorderLayout());

        // Setup UI panels
        JPanel customizationPanel = new JPanel(new GridLayout(1, 2));
        customizationPanel.add(new TrackCustomizationPanel());
        customizationPanel.add(new HorseCustomizationPanel());

        raceVisualPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawRace(g);
            }
        };
        raceVisualPanel.setPreferredSize(new Dimension(1000, 400));

        raceScrollPane = new JScrollPane(raceVisualPanel);
        raceScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        raceScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        rightSplit.setTopComponent(new StatisticsPanel());
        leaderboardPanel = new LeaderboardPanel();
        rightSplit.setBottomComponent(leaderboardPanel);
        rightSplit.setResizeWeight(0.5);

        bettingPanel = new BettingPanel();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(customizationPanel, BorderLayout.NORTH);
        mainPanel.add(raceScrollPane, BorderLayout.CENTER);
        mainPanel.add(rightSplit, BorderLayout.EAST);
        mainPanel.add(bettingPanel, BorderLayout.WEST);
        mainPanel.add(new RaceControlPanel(), BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        raceTimer = new Timer(16, e -> updateRace());
        setLocationRelativeTo(null);
        setVisible(true);

        raceStats.loadFromFile("race_statistics.csv");
    }

    private class RaceControlPanel extends JPanel {
        public RaceControlPanel() {
            JButton start = new JButton("Start Race");
            start.addActionListener(e -> {
                if (horses.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Add horses first!");
                    return;
                }
                if (raceTimer.isRunning()) {
                    raceTimer.stop();
                }
                if (bettingSystem.getCurrentBets().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You must place a bet before starting the race!");
                    return;
                }

                for (Horse horse : horses) {
                    horse.goBackToStart();
                }

                currentRace = new Race(horses, track);
                bettingSystem.calculateOdds();
                raceTimer.start();
            });

            JButton reset = new JButton("Reset");
            reset.addActionListener(e -> {
                raceTimer.stop();
                horses.clear();
                horseSelect.removeAllItems();
                statsArea.setText("");
                leaderboard = new Leaderboard();
                leaderboardPanel.update();
                currentRace = null;
                raceVisualPanel.repaint();
            });

            add(start);
            add(reset);
        }
    }

    private class TrackCustomizationPanel extends JPanel {
        public TrackCustomizationPanel() {
            setBorder(BorderFactory.createTitledBorder("Track Settings"));
            setLayout(new GridLayout(2, 2));

            add(new JLabel("Lanes:"));
            JSpinner lanes = new JSpinner(new SpinnerNumberModel(track.getLaneCount(), 1, 10, 1));
            lanes.addChangeListener(e -> track.setLaneCount((Integer) lanes.getValue()));
            add(lanes);

            add(new JLabel("Length:"));
            JSpinner length = new JSpinner(new SpinnerNumberModel(track.getTrackLength(), 10, 500, 10));
            length.addChangeListener(e -> track.setTrackLength((Integer) length.getValue()));
            add(length);

            add(new JLabel("Shape:"));
            JComboBox<String> shapes = new JComboBox<>(new String[]{"Straight", "Oval", "Figure-eight", "Zigzag"});
            shapes.setSelectedItem(track.getTrackShape());
            shapes.addActionListener(e -> track.setTrackShape((String) shapes.getSelectedItem()));
            add(shapes);

            add(new JLabel("Weather:"));
            JComboBox<String> weather = new JComboBox<>(new String[]{"Dry", "Muddy", "Icy"});
            weather.setSelectedItem(track.getWeatherCondition());
            weather.addActionListener(e -> track.setWeatherCondition((String) weather.getSelectedItem()));
            add(weather);
        }
    }

    private class HorseCustomizationPanel extends JPanel {
        public HorseCustomizationPanel() {
            setBorder(BorderFactory.createTitledBorder("Horse Settings"));
            setLayout(new GridLayout(7, 2));

            JTextField nameField = new JTextField();
            add(new JLabel("Name:")); add(nameField);

            JComboBox<String> breedBox = new JComboBox<>(new String[]{"Thoroughbred", "Arabian", "Quarter Horse"});
            add(new JLabel("Breed:")); add(breedBox);

            JComboBox<String> colorBox = new JComboBox<>(new String[]{"Brown", "Black", "Grey", "White"});
            add(new JLabel("Coat Color:")); add(colorBox);

            JComboBox<String> accessoryBox = new JComboBox<>(new String[]{"Regular", "Lightweight"});
            add(new JLabel("Accessory:")); add(accessoryBox);

            JComboBox<String> symbolBox = new JComboBox<>(new String[]{"🐎", "🦄", "🏇", "🎠"});
            add(new JLabel("Symbol:")); add(symbolBox);

            JSpinner confSpinner = new JSpinner(new SpinnerNumberModel(0.5, 0.0, 1.0, 0.1));
            add(new JLabel("Confidence:")); add(confSpinner);

            JButton addBtn = new JButton("Add Horse");
            addBtn.addActionListener(e -> {
                Horse h = new Horse(
                    symbolBox.getSelectedItem().toString(),
                    nameField.getText(),
                    breedBox.getSelectedItem().toString(),
                    colorBox.getSelectedItem().toString(),
                    accessoryBox.getSelectedItem().toString(),
                    (Double) confSpinner.getValue()
                );
                horses.add(h);
                horseSelect.addItem(h);
                bettingSystem = new BettingSystem(horses, track);
                bettingPanel.updateBalance();
                bettingPanel.updateLiveBettingTable();

                statsArea.append("Added: " + h.getName() + "\n");
                nameField.setText("");
            });
            add(addBtn);
        }
    }
    private class BettingPanel extends JPanel {
        private JLabel balanceLabel;
        private JTextArea liveBettingArea;
        private JScrollPane scrollPane;
    
        public BettingPanel() {
            setBorder(BorderFactory.createTitledBorder("Virtual Betting"));
            setLayout(new BorderLayout());
    
            JPanel topPanel = new JPanel(new GridLayout(7, 1));
            JTextField betField = new JTextField();
            JLabel oddsLabel = new JLabel("Odds: --");
            balanceLabel = new JLabel("Balance: $" + String.format("%.2f", bettingSystem.getBalance()));
            JButton place = new JButton("Place Bet");
    
            horseSelect.addActionListener(e -> {
                Horse selected = (Horse) horseSelect.getSelectedItem();
                if (selected != null) {
                    Map<String, Double> odds = bettingSystem.getCurrentOdds();
                    oddsLabel.setText("Odds: " + String.format("%.2f", odds.getOrDefault(selected.getName(), 2.0)));
                }
            });
    
            place.addActionListener(e -> {
                try {
                    Horse selected = (Horse) horseSelect.getSelectedItem();
                    if (selected == null) return;
    
                    int amount = Integer.parseInt(betField.getText());
                    bettingSystem.placeBet(selected.getName(), amount);
                    updateBalance();
                    updateLiveBettingTable();
                    JOptionPane.showMessageDialog(null, "Bet placed successfully!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid bet amount!");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            });
    
            topPanel.add(new JLabel("Select Horse:"));
            topPanel.add(horseSelect);
            topPanel.add(new JLabel("Bet Amount:"));
            topPanel.add(betField);
            topPanel.add(oddsLabel);
            topPanel.add(balanceLabel);
            topPanel.add(place);
    
            add(topPanel, BorderLayout.NORTH);
    
            // Live Betting Table
            JPanel bettingTablePanel = new JPanel(new BorderLayout());
            bettingTablePanel.add(new JLabel("Live Betting Table:"), BorderLayout.NORTH);
    
            liveBettingArea = new JTextArea();
            liveBettingArea.setEditable(false);
            liveBettingArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
    
            scrollPane = new JScrollPane(liveBettingArea);
            scrollPane.setPreferredSize(new Dimension(250, 150));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    
            bettingTablePanel.add(scrollPane, BorderLayout.CENTER);
            add(bettingTablePanel, BorderLayout.CENTER);
        }
    
        public void updateBalance() {
            balanceLabel.setText("Balance: $" + String.format("%.2f", bettingSystem.getBalance()));
        }
    
        public void updateLiveBettingTable() {
            if (horses.isEmpty()) return;
    
            StringBuilder sb = new StringBuilder();
            Map<String, Double> odds = bettingSystem.getCurrentOdds();
            Map<String, Integer> bets = bettingSystem.getCurrentBets();
    
            sb.append(String.format("%-14s | %-6s | %s\n", "Horse", "Odds", "Bet"));
            sb.append("---------------------------------\n");
    
            for (Horse horse : horses) {
                String name = horse.getName();
                double odd = odds.getOrDefault(name, 2.0);
                int amount = bets.getOrDefault(name, 0);
    
                sb.append(String.format("%-14s | %-6.2f | $%d\n", name, odd, amount));
            }
            liveBettingArea.setFont(new Font("Courier New", Font.PLAIN, 11));
            liveBettingArea.setText(sb.toString());
        }
    }
    
    private class StatisticsPanel extends JPanel {
        public StatisticsPanel() {
            setBorder(BorderFactory.createTitledBorder("Statistics & Analytics"));
            setLayout(new BorderLayout());
    
            statsArea.setEditable(false);
            statsArea.setLineWrap(true);
            statsArea.setWrapStyleWord(true);
    
            JScrollPane scrollPane = new JScrollPane(statsArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setPreferredSize(new Dimension(300, 300));
    
            add(scrollPane, BorderLayout.CENTER);
        }
    }
    
    private class LeaderboardPanel extends JPanel {
        private JTextArea lbArea;
    
        public LeaderboardPanel() {
            setBorder(BorderFactory.createTitledBorder("Leaderboard"));
            setLayout(new BorderLayout());
            lbArea = new JTextArea();
            lbArea.setEditable(false);
            add(new JScrollPane(lbArea), BorderLayout.CENTER);
            update();
        }
    
        public void update() {
            lbArea.setText("");
            for (String s : leaderboard.getTopHorses(5)) {
                lbArea.append(s + "\n");
            }
        }
    }
    
    // Race update logic
    private void updateRace() {
        if (currentRace == null) return;
    
        currentRace.update(0.016);
        bettingSystem.calculateOdds();
        bettingPanel.updateLiveBettingTable();
        raceVisualPanel.repaint();
    
        if (currentRace.isFinished()) {
            raceTimer.stop();
    
            Horse win = currentRace.getWinner();
            double t = (win != null) ? win.getFinishTime() : 0.0;
    
            if (win != null) {
                statsArea.append("Winner: " + win.getName() + " in " + String.format("%.2f", t) + "s\n");
            } else {
                statsArea.append("No Winner (all horses fell!)\n");
            }
    
            currentRace.recordResults(raceStats, leaderboard);
            raceStats.saveToFile("race_statistics.csv");
            leaderboardPanel.update();
    
            double winnings = bettingSystem.payout(win != null ? win.getName() : "");
            if (winnings > 0) {
                statsArea.append("You won $" + String.format("%.2f", winnings) + "!\n");
            } else {
                statsArea.append("You lost the bet!\n");
            }
    
            bettingPanel.updateBalance();
    
            List<BettingSystem.BetRecord> bets = bettingSystem.getBetHistory();
            int wins = 0, losses = 0;
    
            if (bets.isEmpty()) {
                statsArea.append("You didn't place any bets!\n");
            } else {
                for (BettingSystem.BetRecord bet : bets) {
                    if (bet.resolved) {
                        statsArea.append(bet.horseName + " - Bet: $" + bet.amount + ", " + (bet.won ? "WON $" + String.format("%.2f", bet.payout) : "LOST") + "\n");
                        if (bet.won) wins++;
                        else losses++;
                    }
                }
                if (wins + losses > 0) {
                    double winRate = (double) wins / (wins + losses);
                    if (winRate >= 0.7) {
                        statsArea.append("Great job! 🏆 Your betting strategy is very effective!\n");
                    } else if (winRate >= 0.4) {
                        statsArea.append("Not bad! 📈 Try to pick horses with higher odds next time.\n");
                    } else {
                        statsArea.append("Consider betting on safer options! 📉\n");
                    }
                }
            }
    
            for (Horse horse : horses) {
                if (horse.hasFallen()) {
                    statsArea.append(horse.getName() + " - Fell.\n");
                } else {
                    statsArea.append(horse.getName() + " - Finished, Time: " + String.format("%.2f", horse.getFinishTime()) + "s\n");
                }
            }
        }
    }
    
    // Drawing horses on the race track
    private void drawRace(Graphics g) {
        int w = 1000;
        int lanes = track.getLaneCount();
        int topMargin = 100;
        int laneHeight = 100;
        int panelHeight = topMargin * 2 + laneHeight * lanes;
        raceVisualPanel.setPreferredSize(new Dimension(w, panelHeight));
        raceVisualPanel.revalidate();
    
        g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    
        g.setColor(Color.GREEN);
        g.fillRect(45, 0, 5, panelHeight);
        g.setColor(Color.RED);
        g.fillRect(945, 0, 5, panelHeight);
        g.setColor(Color.BLACK);
    
        for (Horse horse : horses) {
            Point pos = getHorsePosition(horse, track.getTrackShape(), w, panelHeight);
            g.drawString(horse.hasFallen() ? "💥" : horse.getSymbol(), pos.x, pos.y);
        }
    }
    
    // Calculate horse position based on track shape
    private Point getHorsePosition(Horse horse, String shape, int width, int height) {
        double distance = horse.getDistanceTravelled();
        double x = 0, y = 0;
        double startX = 50;
        double finishX = 945;
        double scale = (finishX - startX) / track.getTrackLength();
        int topMargin = 100;
        int laneHeight = 100;
        int laneIndex = horses.indexOf(horse);
    
        switch (shape) {
            case "Oval":
                double centerX = width / 2.0;
                double centerY = topMargin + laneIndex * laneHeight + laneHeight / 2.0;
                double a = 300;
                double b = laneHeight / 2.0;
                double theta = distance * 0.05;
                x = centerX + a * Math.cos(theta);
                y = centerY + b * Math.sin(theta);
                break;
            case "Figure-eight":
                double midX = width / 2.0;
                double midY = topMargin + laneIndex * laneHeight + laneHeight / 2.0;
                if (distance % 200 < 100) {
                    double angle1 = (distance % 100) * 0.05;
                    x = midX - 100 + 50 * Math.cos(angle1);
                    y = midY + 50 * Math.sin(angle1);
                } else {
                    double angle2 = ((distance - 100) % 100) * 0.05;
                    x = midX + 100 + 50 * Math.cos(angle2);
                    y = midY + 50 * Math.sin(angle2);
                }
                break;
            case "Zigzag":
                double segmentLength = 50;
                double offsetInSegment = distance % segmentLength;
                int segment = (int) (distance / segmentLength);
                x = 50 + distance * 2;
                y = topMargin + laneIndex * laneHeight +
                    (segment % 2 == 0 ? offsetInSegment * 2 : laneHeight - offsetInSegment * 2);
                break;
            default:
                x = startX + distance * scale;
                y = topMargin + laneIndex * laneHeight + laneHeight / 2;
        }
        return new Point((int) x, (int) y);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RaceGUI::new);
    }
}    