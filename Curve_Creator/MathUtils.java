package Curve_Creator;

import java.awt.*;

public class MathUtils {
    public static double getDist(CurveCreator.point point, Point mouse) {
        return Math.hypot(point.getX()-mouse.x, point.getY()-mouse.y);
    }

    public static int clamp(int val, int min, int max) {
        val = Math.min(val, max);
        val = Math.max(val, min);
        return val;
    }

    public static double roundToPlace(double value, int decimalPlaces) {
        double factor = Math.pow(10, decimalPlaces);
        return Math.round(value*factor)/factor;
    }
}
