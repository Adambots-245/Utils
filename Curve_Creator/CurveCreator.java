package Curve_Creator;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

import java.awt.event.*;

public class CurveCreator {
    static JFrame frame;

    static final Font font = new Font("Serif", Font.PLAIN, 20);

    static final DrawingManager panel = new DrawingManager();
    static final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    static final int size = gd.getDisplayMode().getHeight()-100;

    static final int xPointerOffest = -7;
    static final int yPointerOffest = -30;

    static ArrayList<Point> points = new ArrayList<Point>(Arrays.asList(new Point(0, 0), new Point(size, size)));
    static Point selected;

    static Boolean movePoint = false;
    static Boolean mouseDown = false;

    static JTextField xField;
    static JTextField yField;

    public static void main(String[] args) {
        frame = new JFrame("Curve Creator");

        //Create panel to add to frame ------------------------------------------------------------
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(size+200, size));

        JButton deletePoint = FrameUtils.button("Delete Point", size + 10, 10, 180, 30);
        JButton deselectPoint = FrameUtils.button("Deselect Point", size + 10, 45, 180, 30);

        FrameUtils.label("X:", size + 10, 85);
        FrameUtils.label("Y:", size + 10, 110);

        xField = FrameUtils.field("0", size + 35, 85);
        yField = FrameUtils.field("0", size + 35, 110);

        frame.add(panel);

        //Setup Frame -----------------------------------------------------------------------------
        frame.setIconImage(FrameUtils.imageURL("https://github.com/CrazyMeowCows/Images/blob/main/AdambotsLogoBlack.png?raw=true"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); 

        //Create Timer ----------------------------------------------------------------------------
        Timer timer = new Timer(15, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (selected != null) {
                    if (MathUtils.getDist(selected, getMousePos()) > 30 && mouseDown) {
                        movePoint = true;
                    }
                    if (movePoint) {
                        selected.setLocation(getMousePos());
                        frame.repaint();

                        updatePosText();
                    }
                } else {
                    xField.setText("0");
                    yField.setText("0");
                }
            }
        });
        timer.start(); 

        //Add mouse listener for clicks -----------------------------------------------------------
        frame.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent me) {
                if (me.getX() < size) {
                    mouseDown = true;
                    for (Point point : points) {
                        if (MathUtils.getDist(point, getMousePos()) < 30) {
                            selected = point;
                            updatePosText();
                            frame.repaint();
                            return;
                        }
                    }
                    addPoint(getMousePos());
                    selected = null;
                    frame.repaint();
                }
            }
            public void mouseReleased(MouseEvent me) {
                mouseDown = false;
                movePoint = false;
                frame.repaint();
            }
            public void mouseEntered(MouseEvent me) { }
            public void mouseExited(MouseEvent me) { }
            public void mouseClicked(MouseEvent me) { }
        });

        //Add action listeners for buttons --------------------------------------------------------
        deletePoint.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                points.remove(selected);
                frame.repaint();
            }
        });
        deselectPoint.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                selected = null;
                frame.repaint();
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

    public static void updatePosText() {
        if (xField.getText() != Integer.toString(selected.x)) {
            xField.setText("" + selected.x);
        }
        if (yField.getText() != Integer.toString(selected.y)) {
            yField.setText("" + selected.y);
        }
    }

    public static Point getMousePos() {
        int xPos = MouseInfo.getPointerInfo().getLocation().x-frame.getLocation().x+xPointerOffest;
        int yPos = MouseInfo.getPointerInfo().getLocation().y-frame.getLocation().y+yPointerOffest;
        xPos = MathUtils.clamp(xPos, 0, size);
        yPos = MathUtils.clamp(yPos, 0, size);
        return new Point(xPos, size - yPos);
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
                if (points.get(i) == selected) {
                    g2d.setColor(Color.white);
                    g2d.setStroke(new BasicStroke(5));
                } else {
                    g2d.setColor(Color.black);
                }
                g2d.drawArc(points.get(i).x-5, size-points.get(i).y-5, 10, 10, 0, 360);

                g2d.setColor(Color.red);
                g2d.setStroke(new BasicStroke(1));
                g2d.fillArc(points.get(i).x-5, size-points.get(i).y-5, 10, 10, 0, 360);

                if (i < points.size()-1) {
                    g2d.drawLine(points.get(i).x, size-points.get(i).y, points.get(i+1).x, size-points.get(i+1).y);
                }
            }
        }
    }
}
