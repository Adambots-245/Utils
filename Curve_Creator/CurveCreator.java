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
    static JTextField importField;

    public static class point{
        int x;
        int y;
        Boolean fixed;
        public point(int x, int y, Boolean fixed) {
            this.x = x;
            this.y = y;
            this.fixed = fixed;
        }

        public void setPos(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public void setPos(Point point) {
            this.x = point.x;
            this.y = point.y;
        }

        public int getX() {
            return this.x;
        }
        public int getY() {
            return this.y;
        }
    }

    public static void main(String[] args) {
        frame = new JFrame("Curve Creator");

        //Create panel to add to frame ------------------------------------------------------------
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(size+200, size));

        JButton deletePoint = FrameUtils.button("Delete Point", size + 10, 10, 180, 30);
        JButton generateArray = FrameUtils.button("Generate Array", size + 10, 45, 180, 30);
        JButton importArray = FrameUtils.button("Import Array", size + 10, 145, 180, 30);

        FrameUtils.label("X:", size + 10, 85);
        FrameUtils.label("Y:", size + 10, 110);

        xField = FrameUtils.field("0", size + 35, 85, 155, 25);
        yField = FrameUtils.field("0", size + 35, 110, 155, 25);
        importField = FrameUtils.field("", size + 10, 185, 180, 25);

        frame.add(panel);

        //Setup Frame -----------------------------------------------------------------------------
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("Curve_Creator/AdambotsLogoBlack.png"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
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
        generateArray.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int prevX = 0;
                Boolean badArray = false;
                String str = "{";
                for (point Point : points){
                    if (Point.getX() < prevX) {badArray = true;}
                    prevX = Point.getX();
                    str += "{";
                    str += MathUtils.roundToPlace((double)Point.getX()/sizeD, 4);
                    str += ", ";
                    str += MathUtils.roundToPlace((double)Point.getY()/sizeD, 4);
                    str += "},";
                }
                if (!badArray) {
                    System.out.println(str.substring(0, str.length()-1) + "}");
                } else {
                    System.out.println("Invalid Array - Ensure points are sequential and constitute a valid function");
                }
            }
        });
        importArray.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<Double> vals = new ArrayList<Double>();
                    String[] input = importField.getText().split(",");
                    for (String string : input) {
                        string = string.replace("{", "").replace("}", "");
                        string = string.trim();
                        vals.add(Double.valueOf(string));
                    }
                    points.clear();
                    for (int i = 0; i < vals.size()-1; i += 2) {
                        points.add(new point((int)(vals.get(i)*sizeD), (int)(vals.get(i+1)*sizeD), i == 0 || i == vals.size()-2));
                    }
                    clearSelected();
                    importField.setText("");
                    System.out.println("Points Imported Successfully");
                } catch (NumberFormatException value) {
                    System.out.println("Invalid Entry - Ensure only the 2D array is inputted: {{...}, {...}}");
                }
            }
        });

        Action xyInputs = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (selected != null) {
                    try {
                        double x = Double.parseDouble(xField.getText())*sizeD;
                        double y = Double.parseDouble(yField.getText())*sizeD;
                        selected.setPos(MathUtils.clamp((int)x, 0, size), MathUtils.clamp((int)y, 0, size));
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
            g2d.setColor(new Color(200, 200, 200));
            g2d.fillRect(0, 0, size, size);

            g2d.setColor(Color.BLACK);
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
