import java.awt.*;
import javax.swing.JPanel;

public class TrackShape {
    public static void drawShape(Graphics g, String shape, JPanel panel) {
        int w = panel.getWidth(), h = panel.getHeight();
        g.setColor(Color.LIGHT_GRAY);
        switch (shape) {
            case "Oval":
                g.drawOval(50, 50, w - 100, h - 100);
                break;
            case "Figure-eight":
                g.drawOval(50, 50, (w / 2) - 60, h - 100);
                g.drawOval((w / 2) + 10, 50, (w / 2) - 60, h - 100);
                break;
            case "Zigzag":
                for (int x = 50; x < w - 50; x += 40)
                    g.drawLine(x, 50, x + 20, h - 50);
                break;
            default: // Straight
                g.drawLine(50, h / 2, w - 50, h / 2);
        }
    }
}
