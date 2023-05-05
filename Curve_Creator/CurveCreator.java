package Curve_Creator;
import java.awt.*;
import javax.swing.*;

public class CurveCreator {

    static final DrawingManager panel = new DrawingManager();
    static final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    static final int size = gd.getDisplayMode().getHeight()-100;

    public static void main(String[] args) {
        //Frame Setup------------------------------------------------------------------------------------------------------------------------------------
            JFrame frame = new JFrame("PID Simulator");
            panel.setLayout(null);
            panel.setPreferredSize(new Dimension(size+200, size));
    
            frame.add(panel);

            frame.setIconImage(CurveMethods.imageURL("https://github.com/CrazyMeowCows/Images/blob/main/AdambotsLogoBlack.png?raw=true"));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);  
        }

        static class DrawingManager extends JPanel {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
    
                setBackground(new Color(230, 230, 230));

                for (int x = 0; x <= size; x += size/10) {
                    g2d.drawLine(x, 0, x, size);
                }
                for (int y = 0; y <= size; y += size/10) {
                    g2d.drawLine(0, y, size, y);
                }
            }
        }
}
