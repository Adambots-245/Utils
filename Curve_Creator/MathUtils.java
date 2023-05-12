package Curve_Creator;
import java.awt.*;

public class MathUtils {
    public static double getDist(Point point1, Point point2) {
        return Math.hypot(point1.x-point2.x, point1.y-point2.y);
    }

    public static int clamp(int val, int min, int max) {
        val = Math.min(val, max);
        val = Math.max(val, min);
        return val;
    }
}
