// RaceGUI.java – Finalized with refined track lanes and emoji symbols rendering

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class RaceGUI extends JFrame {
    private JPanel raceTrackPanel;
    private JPanel horsePanel;
    private JPanel trackPanel;
    private JPanel shapePanel;
    private JPanel controlPanel;
    private JComboBox<String> trackShapeCombo;
    private JSpinner laneSpinner, lengthSpinner;
    private List<JComboBox<String>> breedCombos, colorCombos, saddleCombos, symbolCombos;
    private List<Horse> horses;
    private Timer raceTimer;
    private int trackLength;
    private int pixelScale = 12;
    private int laneHeight = 60;
    private JButton startButton;

    public RaceGUI() {
        setTitle("Horse Race Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1150, 850);
        setLocationRelativeTo(null);

        horses = new ArrayList<>();

        JPanel configContainer = new JPanel(new GridLayout(1, 3));
        configContainer.add(createTrackPanel());
        configContainer.add(createShapePanel());
        configContainer.add(createHorsePanel());

        raceTrackPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTrack(g);
            }
        };
        raceTrackPanel.setPreferredSize(new Dimension(1000, 500));

        controlPanel = new JPanel();
        startButton = new JButton("Start Race");
        startButton.setEnabled(false);
        startButton.addActionListener(e -> startRace());
        controlPanel.add(startButton);

        add(configContainer, BorderLayout.NORTH);
        add(new JScrollPane(raceTrackPanel), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createTrackPanel() {
        trackPanel = new JPanel(new GridLayout(3, 1));
        trackPanel.setBorder(BorderFactory.createTitledBorder("Track Configuration"));

        laneSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        JPanel laneConfig = new JPanel();
        laneConfig.add(new JLabel("Lanes:"));
        laneConfig.add(laneSpinner);

        lengthSpinner = new JSpinner(new SpinnerNumberModel(50, 10, 200, 5));
        JPanel lengthConfig = new JPanel();
        lengthConfig.add(new JLabel("Length:"));
        lengthConfig.add(lengthSpinner);

        JButton confirmButton = new JButton("Confirm Settings");
        confirmButton.addActionListener(e -> prepareHorses());

        trackPanel.add(laneConfig);
        trackPanel.add(lengthConfig);
        trackPanel.add(confirmButton);
        return trackPanel;
    }

    private JPanel createShapePanel() {
        shapePanel = new JPanel();
        shapePanel.setBorder(BorderFactory.createTitledBorder("Track Shape"));
        trackShapeCombo = new JComboBox<>(new String[]{"Straight", "Oval", "Zigzag", "Figure-eight", "Custom"});
        shapePanel.add(new JLabel("Shape:"));
        shapePanel.add(trackShapeCombo);
        return shapePanel;
    }

    private JPanel createHorsePanel() {
        horsePanel = new JPanel();
        horsePanel.setBorder(BorderFactory.createTitledBorder("Horse Customization"));
        horsePanel.setLayout(new BoxLayout(horsePanel, BoxLayout.Y_AXIS));
        return horsePanel;
    }

    private void prepareHorses() {
        horses.clear();
        horsePanel.removeAll();

        int lanes = (Integer) laneSpinner.getValue();
        trackLength = (Integer) lengthSpinner.getValue();

        breedCombos = new ArrayList<>();
        colorCombos = new ArrayList<>();
        saddleCombos = new ArrayList<>();
        symbolCombos = new ArrayList<>();

        for (int i = 0; i < lanes; i++) {
            JPanel row = new JPanel(new GridLayout(1, 4));
            row.setBorder(BorderFactory.createTitledBorder("Horse " + (i + 1)));

            JComboBox<String> breed = new JComboBox<>(new String[]{"Thoroughbred", "Arabian", "Quarter Horse"});
            JComboBox<String> color = new JComboBox<>(new String[]{"Brown", "Black", "Grey", "White"});
            JComboBox<String> saddle = new JComboBox<>(new String[]{"Standard", "Lightweight", "Colorful"});
            JComboBox<String> symbol = new JComboBox<>(new String[]{"🐎", "🦄", "🏇", "A", "B", "C"});

            breedCombos.add(breed);
            colorCombos.add(color);
            saddleCombos.add(saddle);
            symbolCombos.add(symbol);

            row.add(breed);
            row.add(color);
            row.add(saddle);
            row.add(symbol);
            horsePanel.add(row);
        }

        JButton applyButton = new JButton("Apply Customization");
        applyButton.addActionListener(e -> applyHorseData());
        horsePanel.add(applyButton);
        horsePanel.revalidate();
        horsePanel.repaint();
    }

    private void applyHorseData() {
        horses.clear();
        for (int i = 0; i < breedCombos.size(); i++) {
            String breed = (String) breedCombos.get(i).getSelectedItem();
            String saddle = (String) saddleCombos.get(i).getSelectedItem();
            char symbol = ((String) symbolCombos.get(i).getSelectedItem()).charAt(0);
            double confidence = 0.6;
            if (breed.equals("Thoroughbred")) confidence += 0.1;
            if (saddle.equals("Lightweight")) confidence += 0.05;
            Horse h = new Horse(symbol, "Horse" + (i + 1), Math.min(confidence, 1.0));
            horses.add(h);
        }
        startButton.setEnabled(true);
        raceTrackPanel.repaint();
    }

    private void drawTrack(Graphics g) {
        int lanes = horses.size();
        int width = getWidth() - 250;
        int startX = 100;
        String shape = (String) trackShapeCombo.getSelectedItem();

        g.setColor(new Color(240, 255, 240));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.RED);
        g.fillRect(startX + width, 0, 5, getHeight());

        for (int i = 0; i < lanes; i++) {
            int y = (i + 1) * laneHeight;
            g.setColor(Color.LIGHT_GRAY);
            g.fillRoundRect(startX - 5, y - 30, width + 10, 45, 10, 10);
            g.setColor(Color.DARK_GRAY);
            g.drawRoundRect(startX - 5, y - 30, width + 10, 45, 10, 10);

            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            g.drawString("Lane " + (i + 1), 20, y - 5);

            if (i < horses.size()) {
                Horse h = horses.get(i);
                int dx = h.getDistanceTravelled();
                int x = switch (shape) {
                    case "Zigzag" -> startX + (int) (dx * pixelScale * Math.sin(dx % 2 == 0 ? 1 : -1));
                    case "Oval" -> startX + (int) (dx * pixelScale * 0.95);
                    case "Figure-eight" -> startX + (int) (dx * pixelScale * 0.9);
                    case "Custom" -> startX + (dx * pixelScale);
                    default -> startX + (dx * pixelScale);
                };
                g.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
                g.drawString(h.getSymbol() + " " + h.getName(), x, y);
                if (h.hasFallen()) {
                    g.setColor(Color.RED);
                    g.drawString("×", x, y - 15);
                }
            }
        }

        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g.drawString("Track Shape: " + shape, 20, 20);
    }

    private void startRace() {
        raceTimer = new Timer(150, null);
        raceTimer.addActionListener(e -> {
            boolean finished = false;
            for (Horse h : horses) {
                if (!h.hasFallen() && Math.random() < h.getConfidence()) h.moveForward();
                if (!h.hasFallen() && Math.random() < 0.1 * h.getConfidence() * h.getConfidence()) h.fall();
                if (h.getDistanceTravelled() >= trackLength) finished = true;
            }
            raceTrackPanel.repaint();
            if (finished) {
                raceTimer.stop();
                JOptionPane.showMessageDialog(this, "Race Finished!", "Result", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        for (Horse h : horses) h.goBackToStart();
        raceTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RaceGUI::new);
    }
}