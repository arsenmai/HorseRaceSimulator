import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RaceGUI extends JFrame {
    private Track track = new Track(3, 100, "Straight", "Dry");
    private List<Horse> horses = new ArrayList<>();
    private Race currentRace;
    private RaceStatistics raceStats = new RaceStatistics();
    private Leaderboard leaderboard = new Leaderboard();
    private BettingSystem bettingSystem = new BettingSystem();

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

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(customizationPanel, BorderLayout.NORTH);
        mainPanel.add(raceScrollPane, BorderLayout.CENTER);
        mainPanel.add(rightSplit, BorderLayout.EAST);
        mainPanel.add(new BettingPanel(), BorderLayout.WEST);
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
                raceTimer.stop(); // Остановить прошлую гонку
            }

            // Сброс всех лошадей
            for (Horse horse : horses) {
                horse.goBackToStart(); // вернули на старт
            }

            // Создать новую гонку
            currentRace = new Race(horses, track);

            raceTimer.start(); // запустить таймер снова
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

            JComboBox<String> symbolBox = new JComboBox<>(new String[]{"🐎", "🦄", "🏇"});
            add(new JLabel("Symbol:")); add(symbolBox);

            JSpinner confSpinner = new JSpinner(new SpinnerNumberModel(0.5, 0.0, 1.0, 0.1));
            add(new JLabel("Confidence:")); add(confSpinner);

            JButton addBtn = new JButton("Add Horse");
            addBtn.addActionListener(e -> {
                String sym = symbolBox.getSelectedItem().toString();
                Horse h = new Horse(sym,
                        nameField.getText(),
                        breedBox.getSelectedItem().toString(),
                        colorBox.getSelectedItem().toString(),
                        accessoryBox.getSelectedItem().toString(),
                        (Double) confSpinner.getValue());
                horses.add(h);
                horseSelect.addItem(h);
                statsArea.append("Added: " + h.getName() + "\n");
                nameField.setText("");
            });
            add(addBtn);
        }
    }

    private class BettingPanel extends JPanel {
        public BettingPanel() {
            setBorder(BorderFactory.createTitledBorder("Virtual Betting"));
            setLayout(new GridLayout(5, 1));

            JTextField betField = new JTextField();
            JLabel oddsLabel = new JLabel("Odds: --");
            JButton place = new JButton("Place Bet");
            place.addActionListener(e -> {
                Horse h = (Horse) horseSelect.getSelectedItem();
                int amt;
                try { amt = Integer.parseInt(betField.getText()); }
                catch (NumberFormatException ex) { return; }
                bettingSystem.placeBet(h.getName(), amt);
                Map<String, Double> odds = bettingSystem.getCurrentOdds();
                oddsLabel.setText("Odds: " + String.format("%.2f", odds.getOrDefault(h.getName(), 2.0)));
            });

            add(new JLabel("Select Horse:")); add(horseSelect);
            add(new JLabel("Bet Amount:")); add(betField);
            add(oddsLabel); add(place);
        }
    }
     private class StatisticsPanel extends JPanel {
         public StatisticsPanel() {
             setBorder(BorderFactory.createTitledBorder("Statistics & Analytics"));
             setLayout(new BorderLayout());

             statsArea.setEditable(false);
             statsArea.setLineWrap(true);         // Перенос строк
             statsArea.setWrapStyleWord(true);     // Перенос по словам (не обрезает слова)

             JScrollPane scrollPane = new JScrollPane(statsArea);
             scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
             scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

             scrollPane.setPreferredSize(new Dimension(300, 300)); // 300px ширина

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

    private void updateRace() {
        if (currentRace == null) return;

        currentRace.update(0.016); // только вызов гонки!

        raceVisualPanel.repaint();

        if (currentRace.isFinished()) {
            raceTimer.stop();
            Horse win = currentRace.getWinner();
            double t = currentRace.getWinningTime();

            if (win != null) {
                statsArea.append("Winner: " + win.getName() + " in " + String.format("%.2f", t) + "s\n");
            } else {
                statsArea.append("No Winner (all horses fell!)\n");
            }

            currentRace.recordResults(raceStats, leaderboard);
            raceStats.saveToFile("race_statistics.csv");
            leaderboardPanel.update();
            // ➡ Показать все данные по лошадям:
            for (String summary : currentRace.getRaceSummary()) {
                statsArea.append(summary + "\n");
            }
        }
    }



    private void drawRace(Graphics g) {
        int w = 1000;
        int lanes = track.getLaneCount();
        int topMargin = 100;
        int laneHeight = 100;
        int panelHeight = topMargin * 2 + laneHeight * lanes;
        raceVisualPanel.setPreferredSize(new Dimension(w, panelHeight));
        raceVisualPanel.revalidate();

        g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Стартовая и финишная линии
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
                int segment = (int)(distance / segmentLength);
                x = 50 + distance * 2;
                if (segment % 2 == 0) {
                    y = topMargin + laneIndex * laneHeight + offsetInSegment * 2;
                } else {
                    y = topMargin + laneIndex * laneHeight + laneHeight - offsetInSegment * 2;
                }
                break;
            default:
                x = startX + distance * scale;
                y = topMargin + laneIndex * laneHeight + laneHeight / 2;
        }
        return new Point((int)x, (int)y);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RaceGUI::new);
    }
}
