package Curve_Creator;

import java.awt.*;

public class MathUtils {
    public static double getDist(CurveCreator.curvePoint point, Point mouse) {
        return Math.hypot(point.getScaledX()-mouse.x, point.getScaledY()-mouse.y);
    }

    /** 
    Linearly interpolates between min and max value by a value between [0,1]
    */
    public static double lerp(double min, double max, double f) {
        return min+f*(max-min);
    }

    /** 
    Clamps a value between a minimum and a maximum
    */
    public static double clamp(double val, double min, double max) {
        val = Math.min(val, max);
        val = Math.max(val, min);
        return val;
    }

    /** 
    Rounds a value to a specified amount of decimal places
    */
    public static double roundToPlace(double value, int decimalPlaces) {
        double factor = Math.pow(10, decimalPlaces);
        return Math.round(value*factor)/factor;
    }
}
