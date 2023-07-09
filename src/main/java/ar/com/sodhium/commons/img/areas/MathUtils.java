package ar.com.sodhium.commons.img.areas;

import java.awt.Point;

public class MathUtils {
    public static boolean overlaps(IntegerRectangularZone zone1, IntegerRectangularZone zone2) {
        if (zone1.getX() > zone2.getMaxX() || zone2.getX() > zone1.getMaxX()) {
            return false;
        }
        if (zone1.getY() > zone2.getMaxY() || zone2.getY() > zone1.getMaxY()) {
            return false;
        }
        return true;
    }

    public static boolean contains(IntegerRectangularZone zone1, IntegerRectangularZone zone2) {
        if (zone1.getX() < zone2.getX()) {
            if (zone1.getX() + zone1.getWidth() > zone2.getX() + zone2.getWidth()) {
                if (zone1.getY() < zone2.getY()) {
                    if (zone1.getY() + zone1.getHeight() > zone2.getY() + zone2.getHeight()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean contains(IntegerRectangularZone zone, int x, int y) {
        return (zone.getX() <= x && zone.getMaxX() >= x && zone.getY() <= y && zone.getMaxY() >= y);
    }

    public static boolean containsInclusive(IntegerRectangularZone zone1, IntegerRectangularZone zone2) {
        if (zone1.getX() <= zone2.getX()) {
            if (zone1.getX() + zone1.getWidth() >= zone2.getX() + zone2.getWidth()) {
                if (zone1.getY() <= zone2.getY()) {
                    if (zone1.getY() + zone1.getHeight() >= zone2.getY() + zone2.getHeight()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isAround(double number, double otherNumber, double tolerance) {
        return Math.abs(number - otherNumber) < tolerance;
    }

    public static Double getCuadraticDistance(double x1, double y1, double x2, double y2) {
        double difX = x1 - x2;
        double difY = y1 - y2;
        return difX * difX + difY * difY;
    }

    public static Point getCenter(IntegerRectangularZone zone) {
        int newTargetX = zone.getX() + (int) (zone.getWidth() / 2);
        int newTargetY = zone.getY() + (int) (zone.getHeight() / 2);
        return new Point(newTargetX, newTargetY);
    }
}
