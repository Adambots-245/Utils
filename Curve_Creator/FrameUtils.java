package Curve_Creator;

import java.awt.*;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.io.IOException;

public class FrameUtils {
    public static Image imageURL(String link){  
        Image finalImage = null;
        try {
            URL logoURL = new URL(link);
            Image icon = ImageIO.read(logoURL);  
            finalImage = icon;
        } catch(IOException ie) {
            ie.printStackTrace();
        }
        return finalImage;
    }

    public static JButton button(String name, int x, int y, int w, int h){  
        JButton button = new JButton(name);
        button.setFont(CurveCreator.font);
        button.setBounds(x, y, w, h);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);

        CurveCreator.panel.add(button);
        return button;
    } 

    public static JLabel label(String name, int x, int y, int w, int h){  
        JLabel label = new JLabel(name);
        label.setFont(CurveCreator.font);
        label.setForeground(Color.BLACK);
        label.setBounds(x, y, w, h);

        CurveCreator.panel.add(label);
        return label;
    }

    public static JTextField field(String value, int x, int y, int w, int h){  
        JTextField field = new JTextField(value);
        field.setFont(CurveCreator.font);
        field.setBounds(x, y, w, h);

        CurveCreator.panel.add(field);
        return field;
    } 
}
