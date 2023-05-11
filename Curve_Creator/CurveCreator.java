package Curve_Creator;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import java.awt.event.*;

public class CurveCreator {

    static final DrawingManager panel = new DrawingManager();
    static final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    static final int size = gd.getDisplayMode().getHeight()-100;
    static JFrame frame;

    static final int xPointerOffest = -7;
    static final int yPointerOffest = 30;

    static ArrayList<Point> points = new ArrayList<Point>(Arrays.asList(new Point(0, 0), new Point(size, size)));

    public static void main(String[] args) {
        frame = new JFrame("PID Simulator");
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(size+200, size));

        frame.add(panel);

        frame.setIconImage(CurveMethods.imageURL("https://github.com/CrazyMeowCows/Images/blob/main/AdambotsLogoBlack.png?raw=true"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); 

        frame.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent me) { }
            public void mouseReleased(MouseEvent me) { }
            public void mouseEntered(MouseEvent me) { }
            public void mouseExited(MouseEvent me) { }

            public void mouseClicked(MouseEvent me) { 
                if (me.getX() < size) {
                    addPoint(getMousePos());
                    frame.repaint();
                }

                System.out.println(MouseInfo.getPointerInfo().getLocation().x - frame.getLocation().x);
                System.out.println(MouseInfo.getPointerInfo().getLocation().y - frame.getLocation().y); //invert y
            }
        });
    }

    public static void addPoint(Point point) {
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).x > point.x) {
                points.add(i, point);
                return;
            }
        }
    }

    public static Point getMousePos() {
        return new Point(MouseInfo.getPointerInfo().getLocation().x-frame.getLocation().x+xPointerOffest, MouseInfo.getPointerInfo().getLocation().y-frame.getLocation().y+yPointerOffest);
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

            for (int i = 0; i < points.size(); i++) {
                g2d.setColor(Color.black);
                g2d.drawArc(points.get(i).x-5, size-points.get(i).y-5, 10, 10, 0, 360);
                g2d.setColor(Color.red);
                g2d.fillArc(points.get(i).x-5, size-points.get(i).y-5, 10, 10, 0, 360);

                if (i < points.size()-1) {
                    g2d.drawLine(points.get(i).x, size-points.get(i).y, points.get(i+1).x, size-points.get(i+1).y);
                }
            }
        }
    }
}
