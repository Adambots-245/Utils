package Curve_Creator;
import java.awt.*;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JButton;

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
        // button.setFont(PIDSimulator.font);
        button.setBounds(x-w/2, y-h/2, w, h);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);

        // PIDSimulator.panel.add(button);
        return button;
    } 
}
