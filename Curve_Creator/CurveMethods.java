package Curve_Creator;
import java.awt.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.io.IOException;

public class CurveMethods {
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

    static class Vector2 {              
        public int x;
        public int y;

        public Vector2(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
