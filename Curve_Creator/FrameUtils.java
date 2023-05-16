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

    public static JLabel label(String name, int x, int y){  
        JLabel label = new JLabel(name);
        label.setFont(CurveCreator.font);
        label.setForeground(Color.BLACK);
        label.setBounds(x, y, 150, 25);

        CurveCreator.panel.add(label);
        return label;
    }

    public static JTextField text(String value, int x, int y){  
        JTextField text = new JTextField(value);
        text.setFont(CurveCreator.font);
        text.setBounds(x, y, 180, 25);

        CurveCreator.panel.add(text);
        return text;
    } 
}
