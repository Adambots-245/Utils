package Curve_Creator;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

import java.awt.event.*;

public class CurveCreator {
    static JFrame frame;

    public static class point{
        int x;
        int y;
        Boolean fixed;
        public point(int x, int y, Boolean fixed){
            this.x = x;
            this.y = y;
            this.fixed = fixed;
        }

        public void setPos(int x, int y){
            this.x = x;
            this.y = y;
        }
        public void setPos(Point point){
            this.x = point.x;
            this.y = point.y;
        }

        public int getX() {return this.x;}
        public int getY() {return this.y;}
    }

    static final Font font = new Font("Serif", Font.PLAIN, 20);

    static final DrawingManager panel = new DrawingManager();
    static final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    static final int size = gd.getDisplayMode().getHeight()-100;
    static final double sizeD = (double)size;

    static final int xPointerOffest = -7;
    static final int yPointerOffest = -30;
    static final double moveThreshold = 15;

    static ArrayList<point> points = new ArrayList<point>(Arrays.asList(new point(0, 0, true), new point(size, size, true)));
    static point selected;

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
        JButton generateString = FrameUtils.button("Generate String", size + 10, 45, 180, 30);

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
                    if (MathUtils.getDist(selected, getMousePos()) > moveThreshold && mouseDown) {
                        movePoint = true;
                    }
                    if (movePoint) {
                        selected.setPos(getMousePos());
                        frame.repaint();
                        updatePosText();
                    }
                }
            }
        });
        timer.start(); 

        //Add mouse listener for clicks -----------------------------------------------------------
        frame.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent me) {
                if (me.getX() < size+20) {
                    mouseDown = true;
                    for (point Point : points){
                        if (MathUtils.getDist(Point, getMousePos()) < moveThreshold) {
                            if (!Point.fixed) {
                                selected = Point;
                                updatePosText();
                                frame.repaint();
                            } else {
                                clearSelected();
                            }
                            return;
                        }
                    }
                    if (selected == null) {
                        addPoint(getMousePos());
                    } else {
                        clearSelected();
                    }
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
                clearSelected();
            }
        });
        generateString.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                String str = "{";
                for (point Point : points){
                    str += "{";
                    str += MathUtils.roundToPlace((double)Point.getX()/sizeD, 4);
                    str += ", ";
                    str += MathUtils.roundToPlace((double)Point.getY()/sizeD, 4);
                    str += "},";
                }
                System.out.println(str.substring(0, str.length()-1) + "}");
            }
        });

        Action xyInputs = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (selected != null) {
                    try {
                        double x = Double.parseDouble(xField.getText())*sizeD;
                        double y = Double.parseDouble(yField.getText())*sizeD;
                        selected.setPos((int)x, (int)y);
                        frame.repaint();
                    } catch (NumberFormatException value) {
                        System.out.println("Invalid Entry");
                    }
                }
            }
        };
        xField.addActionListener(xyInputs);
        yField.addActionListener(xyInputs);
    }

    public static void addPoint(Point pos) {
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).x > pos.x) {
                points.add(i, new point(pos.x, pos.y, false));
                return;
            }
        }
    }

    public static void clearSelected() {
        selected = null;
        frame.repaint();
        xField.setText("0");
        yField.setText("0");
    }

    public static void updatePosText() {
        xField.setText("" + MathUtils.roundToPlace((double)selected.getX()/sizeD, 3));
        yField.setText("" + MathUtils.roundToPlace((double)selected.getY()/sizeD, 3));
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
                g2d.drawArc(points.get(i).getX()-5, size-points.get(i).getY()-5, 10, 10, 0, 360);

                g2d.setColor(Color.red);
                g2d.setStroke(new BasicStroke(1));
                g2d.fillArc(points.get(i).getX()-5, size-points.get(i).getY()-5, 10, 10, 0, 360);

                if (i < points.size()-1) {
                    g2d.drawLine(points.get(i).getX(), size-points.get(i).getY(), points.get(i+1).getX(), size-points.get(i+1).getY());
                }
            }
        }
    }
}
