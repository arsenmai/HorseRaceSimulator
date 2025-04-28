// RaceGUI.java - Fully Refactored and Upgraded Version

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class RaceGUI extends JFrame {

    private JPanel raceVisualPanel;
    private javax.swing.Timer raceTimer;
    private int[] horsePositions;
    private boolean raceRunning = false;

    private int laneCount = 3;
    private int trackLength = 50;
    private String trackShape = "Straight";
    private String weatherCondition = "Dry";

    private List<String> horses = new ArrayList<>();
    private JComboBox<String> horseSelect;
    private JTextArea statsArea;

    private BettingSystem bettingSystem = new BettingSystem();

    public RaceGUI() {
        setTitle("Horse Racing Simulator");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setupUI();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

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
        raceVisualPanel.setPreferredSize(new Dimension(1200, 300));

        mainPanel.add(customizationPanel, BorderLayout.NORTH);
        mainPanel.add(raceVisualPanel, BorderLayout.CENTER);
        mainPanel.add(new StatisticsPanel(), BorderLayout.EAST);
        mainPanel.add(new BettingPanel(), BorderLayout.WEST);
        mainPanel.add(new RaceControlPanel(), BorderLayout.SOUTH);

        add(mainPanel);

        raceTimer = new javax.swing.Timer(100, e -> updateRace());
    }

    class TrackCustomizationPanel extends JPanel {
        public TrackCustomizationPanel() {
            setBorder(BorderFactory.createTitledBorder("Track Customization"));
            setLayout(new GridLayout(2, 4));

            add(new JLabel("Lane Count:"));
            JSpinner laneSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
            laneSpinner.addChangeListener(e -> laneCount = (Integer) laneSpinner.getValue());
            add(laneSpinner);

            add(new JLabel("Track Length:"));
            JSpinner lengthSpinner = new JSpinner(new SpinnerNumberModel(50, 10, 500, 10));
            lengthSpinner.addChangeListener(e -> trackLength = (Integer) lengthSpinner.getValue());
            add(lengthSpinner);

            add(new JLabel("Track Shape:"));
            JComboBox<String> shapes = new JComboBox<>(new String[]{"Straight", "Oval", "Figure-eight", "Zigzag"});
            shapes.addActionListener(e -> trackShape = (String) shapes.getSelectedItem());
            add(shapes);

            add(new JLabel("Weather Condition:"));
            JComboBox<String> conditions = new JComboBox<>(new String[]{"Dry", "Muddy", "Icy"});
            conditions.addActionListener(e -> weatherCondition = (String) conditions.getSelectedItem());
            add(conditions);
        }
    }

    class HorseCustomizationPanel extends JPanel {
        public HorseCustomizationPanel() {
            setBorder(BorderFactory.createTitledBorder("Horse Customization"));
            setLayout(new GridLayout(6, 2));

            JTextField horseName = new JTextField();
            add(new JLabel("Horse Name:"));
            add(horseName);

            JComboBox<String> breed = new JComboBox<>(new String[]{"Thoroughbred", "Arabian", "Quarter Horse"});
            add(new JLabel("Breed:"));
            add(breed);

            JComboBox<String> color = new JComboBox<>(new String[]{"Brown", "Black", "Grey", "White"});
            add(new JLabel("Coat Color:"));
            add(color);

            JComboBox<String> symbol = new JComboBox<>(new String[]{"🐎", "🦄", "🏇"});
            add(new JLabel("Symbol:"));
            add(symbol);

            JButton addHorseButton = new JButton("Add Horse");
            addHorseButton.addActionListener(e -> {
                String horseInfo = horseName.getText() + " (" + breed.getSelectedItem() + ") " + symbol.getSelectedItem();
                horses.add(horseInfo);
                horseSelect.addItem(horseInfo);
                statsArea.append(horseInfo + " added.\n");
                horseName.setText("");
            });
            add(addHorseButton);
        }
    }

    class StatisticsPanel extends JPanel {
        public StatisticsPanel() {
            setBorder(BorderFactory.createTitledBorder("Statistics & Analytics"));
            setLayout(new BorderLayout());
            statsArea = new JTextArea();
            statsArea.setEditable(false);
            add(new JScrollPane(statsArea), BorderLayout.CENTER);
        }
    }

    class BettingPanel extends JPanel {
        public BettingPanel() {
            setBorder(BorderFactory.createTitledBorder("Virtual Betting"));
            setLayout(new GridLayout(6, 1));

            horseSelect = new JComboBox<>();
            JTextField betAmount = new JTextField();

            JButton placeBetButton = new JButton("Place Bet");
            placeBetButton.addActionListener(e -> {
                String selectedHorse = (String) horseSelect.getSelectedItem();
                int amount = Integer.parseInt(betAmount.getText());
                bettingSystem.placeBet(selectedHorse, amount);
                JOptionPane.showMessageDialog(null, "Bet placed on " + selectedHorse + " for $" + amount);
            });

            add(new JLabel("Select Horse:"));
            add(horseSelect);
            add(new JLabel("Bet Amount:"));
            add(betAmount);
            add(placeBetButton);
        }
    }

    class RaceControlPanel extends JPanel {
        public RaceControlPanel() {
            JButton startRaceButton = new JButton("Start Race");
            startRaceButton.addActionListener(e -> startRace());
            add(startRaceButton);

            JButton resetRaceButton = new JButton("Reset Race");
            resetRaceButton.addActionListener(e -> resetRace());
            add(resetRaceButton);
        }
    }

    private void startRace() {
        if (horses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add horses before starting the race.");
            return;
        }
        horsePositions = new int[horses.size()];
        raceRunning = true;
        raceTimer.start();
    }

    private void resetRace() {
        horses.clear();
        horseSelect.removeAllItems();
        statsArea.setText("");
        horsePositions = null;
        raceRunning = false;
        raceVisualPanel.repaint();
    }

    private void drawRace(Graphics g) {
        if (horsePositions == null || horses.isEmpty()) return;

        int laneHeight = raceVisualPanel.getHeight() / horses.size();

        for (int i = 0; i < horsePositions.length; i++) {
            g.drawLine(0, laneHeight * i, raceVisualPanel.getWidth(), laneHeight * i);
            int yOffset = 0;

            switch (trackShape) {
                case "Zigzag" -> yOffset = (horsePositions[i] / 50) % 2 == 0 ? -10 : 10;
                case "Oval" -> yOffset = (int) (10 * Math.sin(Math.toRadians(horsePositions[i])));
                case "Figure-eight" -> yOffset = (int) (15 * Math.sin(Math.toRadians(2 * horsePositions[i])));
            }

            g.drawString(horses.get(i), horsePositions[i], (laneHeight * i) + (laneHeight / 2) + yOffset);
        }
    }

    private void updateRace() {
        if (!raceRunning) return;

        boolean raceFinished = false;

        for (int i = 0; i < horsePositions.length; i++) {
            horsePositions[i] += (int) (Math.random() * 10);
            if (horsePositions[i] >= raceVisualPanel.getWidth() - 100) {
                raceFinished = true;
                raceTimer.stop();
                raceRunning = false;
                JOptionPane.showMessageDialog(this, horses.get(i) + " wins the race!\nWinnings: $" + bettingSystem.payout(horses.get(i)));
                break;
            }
        }

        raceVisualPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RaceGUI::new);
    }
}
