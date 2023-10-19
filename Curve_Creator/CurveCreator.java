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
    static int statusCooldown = 0;

    static ArrayList<curvePoint> points = new ArrayList<curvePoint>(Arrays.asList(new curvePoint(0, 0, true), new curvePoint(1, 1, true)));
    static curvePoint selected;

    static Boolean movePoint = false;
    static Boolean mouseDown = false;

    static JTextField xField;
    static JTextField yField;
    static JTextField importField;
    static JLabel statusField;

    public static class curvePoint{
        double x;
        double y;
        Boolean fixed;
        public curvePoint(double x, double y, Boolean fixed) {
            this.x = x;
            this.y = y;
            this.fixed = fixed;
        }
        public curvePoint(Point pos) {
            this.x = pos.x/sizeD;
            this.y = pos.y/sizeD;
            this.fixed = false;
        }

        public void setPos(double x, double y) {
            this.x = x;
            this.y = y;
        }
        public void setPos(Point point) {
            this.x = point.x/sizeD;
            this.y = point.y/sizeD;
        }

        public double getX() {
            return this.x;
        }
        public double getY() {
            return this.y;
        }

        public int getScaledX() {
            return (int)(this.x*size);
        }
        public int getScaledY() {
            return (int)(this.y*size);
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

        FrameUtils.label("X:", size + 10, 85, 190, 25);
        FrameUtils.label("Y:", size + 10, 110, 190, 25);
        statusField = FrameUtils.label("", size + 10, size-60, 190, 50);

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
                if (statusCooldown < 130) {
                    statusCooldown++;
                } else {
                    statusField.setText("");
                }
            }
        });
        timer.start(); 

        //Add mouse listener for clicks -----------------------------------------------------------
        frame.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent me) {
                if (me.getX() < size+20) {
                    mouseDown = true;
                    for (curvePoint point : points){
                        if (MathUtils.getDist(point, getMousePos()) < moveThreshold) {
                            if (!point.fixed) {
                                selected = point;
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
                double prevX = 0;
                for (int i = 0; i < points.size(); i++) {
                    if (points.get(i).getX() < prevX) {
                        System.out.println("Invalid Array - Ensure points are sequential and constitute a valid function");
                        updateStatus("<html>Error - Check<br>Terminal</html>");
                        return;
                    }
                    prevX = points.get(i).getX();
                }
                String str = "";
                double inc = 0.01;
                for (double x = 0; x <= 1+inc; x += inc) {
                    for (int i = 0; i < points.size(); i++) {
                        if (points.get(i).getX() > x) {
                            str += MathUtils.roundToPlace(MathUtils.lerp(points.get(i-1).getY(), points.get(i).getY(), 
                                (x-points.get(i-1).getX())/(points.get(i).getX()-points.get(i-1).getX())), 4);
                            str += ",";
                            break;
                        }
                    }   
                }
                str += "1.0!-!";
                for (curvePoint point : points) {
                    str += MathUtils.roundToPlace(point.x, 5) + "," + MathUtils.roundToPlace(point.y, 5) + ",";
                }
                System.out.println("\n" + str.substring(0, str.length()-1));
                updateStatus("<html>Array Printed to<br>Terminal</html>");
            }
        });
        importArray.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<Double> vals = new ArrayList<Double>();
                    String[] input = importField.getText().trim().split("!-!")[1].split(",");
                    for (String string : input) {
                        string = string.replaceAll("\"", "");
                        string = string.trim();
                        vals.add(Double.valueOf(string));
                    }
                    points.clear();
                    for (int i = 0; i < vals.size()-1; i += 2) {
                        points.add(new curvePoint(vals.get(i), vals.get(i+1), i == 0 || i == vals.size()-2));
                    }
                    clearSelected();
                    importField.setText("");
                    updateStatus("<html>Array Imported<br>Successfully</html>");
                } catch (NumberFormatException|ArrayIndexOutOfBoundsException a) {
                    System.out.println("Invalid Entry - Ensure only the data string is inputted: \"0.0,0.01...1.0\"");
                    updateStatus("<html>Error - Check<br>Terminal</html>");
                }
            }
        });

        Action xyInputs = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (selected != null) {
                    try {
                        double x = Double.parseDouble(xField.getText());
                        double y = Double.parseDouble(yField.getText());
                        selected.setPos(MathUtils.clamp(x, 0, 1), MathUtils.clamp(y, 0, 1));
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

    public static void updateStatus (String status) {
        statusField.setText(status);
        statusCooldown = 0;
    }

    public static void addPoint(Point pos) {
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getScaledX() > pos.x) {
                points.add(i, new curvePoint(pos));
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
        xField.setText("" + MathUtils.roundToPlace(selected.getX(), 3));
        yField.setText("" + MathUtils.roundToPlace(selected.getY(), 3));
    }
    public static Point getMousePos() {
        int xPos = MouseInfo.getPointerInfo().getLocation().x-frame.getLocation().x+xPointerOffest;
        int yPos = MouseInfo.getPointerInfo().getLocation().y-frame.getLocation().y+yPointerOffest;
        xPos = (int)MathUtils.clamp(xPos, 0, size);
        yPos = (int)MathUtils.clamp(yPos, 0, size);
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
                g2d.drawArc(points.get(i).getScaledX()-5, size-points.get(i).getScaledY()-5, 10, 10, 0, 360);

                g2d.setColor(Color.red);
                g2d.setStroke(new BasicStroke(1));
                g2d.fillArc(points.get(i).getScaledX()-5, size-points.get(i).getScaledY()-5, 10, 10, 0, 360);

                if (i < points.size()-1) {
                    g2d.drawLine(points.get(i).getScaledX(), size-points.get(i).getScaledY(), points.get(i+1).getScaledX(), size-points.get(i+1).getScaledY());
                }
            }
        }
    }
}
