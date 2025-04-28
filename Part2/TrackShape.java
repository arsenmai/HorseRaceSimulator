import java.awt.*;
import javax.swing.JPanel;

public class TrackShape {
    public static void drawShape(Graphics g, String shape, JPanel panel) {
        int width = panel.getWidth();
        int height = panel.getHeight();

        g.setColor(Color.LIGHT_GRAY);
        switch (shape) {
            case "Oval":
                g.drawOval(50, 50, width - 100, height - 100);
                break;
            case "Figure-eight":
                g.drawOval(50, 50, (width / 2) - 60, height - 100);
                g.drawOval((width / 2) + 10, 50, (width / 2) - 60, height - 100);
                break;
            case "Zigzag":
                for (int i = 0; i < width; i += 40) {
                    g.drawLine(i, 50, i + 20, height - 50);
                }
                break;
            default: // Straight
                g.drawLine(50, height / 2, width - 50, height / 2);
        }
    }
}
