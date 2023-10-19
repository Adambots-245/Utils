package Curve_Creator;

import java.awt.*;

public class MathUtils {
    public static double getDist(CurveCreator.curvePoint point, Point mouse) {
        return Math.hypot(point.getScaledX()-mouse.x, point.getScaledY()-mouse.y);
    }
    public static double lerp(double min, double max, double f) {
        return min+f*(max-min);
    }

    public static double clamp(double val, double min, double max) {
        val = Math.min(val, max);
        val = Math.max(val, min);
        return val;
    }

    public static double roundToPlace(double value, int decimalPlaces) {
        double factor = Math.pow(10, decimalPlaces);
        return Math.round(value*factor)/factor;
    }
}
