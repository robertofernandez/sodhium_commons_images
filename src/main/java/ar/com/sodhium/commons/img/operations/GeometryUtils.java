package ar.com.sodhium.commons.img.operations;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;

import ar.com.sodhium.commons.img.areas.ColoredArea;
import ar.com.sodhium.commons.img.areas.RectangularImageArea;
import ar.com.sodhium.commons.img.areas.RectangularImageZone;
import ar.com.sodhium.commons.img.colors.ColorsUtils;
import ar.com.sodhium.commons.img.colors.RgbColor;
import ar.com.sodhium.commons.img.colors.ThresholdUtils;
import ar.com.sodhium.commons.img.colors.map.ColorMap;
import ar.com.sodhium.commons.img.colors.map.GrayscaleMap;
import ar.com.sodhium.commons.img.operations.areas.AreasAmountLimitReached;
import ar.com.sodhium.commons.img.pixels.PixelsSegmentsCluster;
import ar.com.sodhium.commons.indexes.Indexer;
import ar.com.sodhium.commons.math.IntegerCoordinates;

public class GeometryUtils {
    public static final int BLACK_COLOR = 0;
    public static final int WHITE_COLOR = 255;
    private static final int DEFAULT_MAX_AREAS_LIMIT = 1000;

    public static void floodFillFromCenter(Indexer red, Indexer green, Indexer blue, int fillColorRed,
            int fillColorGreen, int fillColorBlue, int maxRatio, int baseColorRed, int baseColorGreen,
            int baseColorBlue) {
        Point initialPoint = findFirstColored(red, green, blue, baseColorRed, baseColorGreen, baseColorBlue, maxRatio);
        if (initialPoint != null) {
            floodFill(red, green, blue, (int) initialPoint.getX(), (int) initialPoint.getY(), fillColorRed,
                    fillColorGreen, fillColorBlue);
        }
    }

    public static void floodFillFromCenter(Indexer indexer, int fillColor, int maxRatio, int baseColor) {
        Point initialPoint = findFirstColored(indexer, baseColor, maxRatio);
        if (initialPoint != null) {
            floodFill(indexer, (int) initialPoint.getX(), (int) initialPoint.getY(), fillColor);
        }
    }

    private static Point findFirstColored(Indexer red, Indexer green, Indexer blue, int baseColorRed,
            int baseColorGreen, int baseColorBlue, int maxRatio) {
        int initialX = (int) (red.getWidth() / 2);
        int initialY = (int) (red.getHeight() / 2);
        for (int ratio = 0; ratio < maxRatio; ratio++) {
            for (int x = initialX - ratio; x <= initialX + ratio; x++) {
                for (int y = initialY - ratio; y <= initialY + ratio; y++) {
                    if (equalsColors(red, green, blue, x, y, baseColorRed, baseColorGreen, baseColorBlue)) {
                        return new Point(x, y);
                    }
                }
            }
        }
        return null;
    }

    private static Point findFirstColored(Indexer indexer, int baseColor, int maxRatio) {
        int initialX = (int) (indexer.getWidth() / 2);
        int initialY = (int) (indexer.getHeight() / 2);
        for (int ratio = 0; ratio < maxRatio; ratio++) {
            for (int x = initialX - ratio; x <= initialX + ratio; x++) {
                for (int y = initialY - ratio; y <= initialY + ratio; y++) {
                    if (indexer.get(x, y) == baseColor) {
                        return new Point(x, y);
                    }
                }
            }
        }
        return null;
    }

    public static void floodFill(Indexer red, Indexer green, Indexer blue, int initialX, int initialY, int fillColorRed,
            int fillColorGreen, int fillColorBlue) {
        Stack<Point> callStack = new Stack<>();
        callStack.add(new Point(initialX, initialY));
        int baseColorRed = red.get(initialX, initialY);
        int baseColorGreen = green.get(initialX, initialY);
        int baseColorBlue = blue.get(initialX, initialY);
        while (!callStack.isEmpty()) {
            Point point = callStack.pop();
            int x = (int) point.getX();
            int y = (int) point.getY();
            if (equalsColors(red, green, blue, x, y, baseColorRed, baseColorGreen, baseColorBlue)) {
                red.set(fillColorRed, x, y);
                green.set(fillColorGreen, x, y);
                blue.set(fillColorBlue, x, y);
                if (point.getX() + 1 < red.getWidth()) {
                    callStack.push(new Point(x + 1, y));
                }
                if (point.getX() - 1 >= 0) {
                    callStack.push(new Point(x - 1, y));
                }
                if (point.getY() - 1 >= 0) {
                    callStack.push(new Point(x, y - 1));
                }
                if (point.getY() + 1 < red.getHeight()) {
                    callStack.push(new Point(x, y + 1));
                }
            }
        }
    }

    public static PixelsSegmentsCluster floodFillCluster(ColorMap map, int initialX, int initialY,
            HashMap<String, String> usedPixels) {
        Color color = map.getColor(initialX, initialY);
        PixelsSegmentsCluster output = new PixelsSegmentsCluster(color);
        Stack<Point> callStack = new Stack<>();
        callStack.add(new Point(initialX, initialY));

        while (!callStack.isEmpty()) {
            Point point = callStack.pop();
            int x = (int) point.getX();
            int y = (int) point.getY();
            String key = "" + x + ";" + y;
            if (usedPixels.containsKey(key)) {
                continue;
            }
            Color newColor = map.getColor(x, y);
            if (equalsColors(newColor, color)) {
                output.addPixel(x, y);
                usedPixels.put(key, key);
                if (point.getX() + 1 < map.getWidth()) {
                    callStack.push(new Point(x + 1, y));
                }
                if (point.getX() - 1 >= 0) {
                    callStack.push(new Point(x - 1, y));
                }
                if (point.getY() - 1 >= 0) {
                    callStack.push(new Point(x, y - 1));
                }
                if (point.getY() + 1 < map.getHeight()) {
                    callStack.push(new Point(x, y + 1));
                }
            }
        }
        return output;
    }

    public static PixelsSegmentsCluster diagonalFloodFillCluster(ColorMap map, int initialX, int initialY,
            HashMap<String, String> usedPixels) {
        Color color = map.getColor(initialX, initialY);
        PixelsSegmentsCluster output = new PixelsSegmentsCluster(color);
        Stack<Point> callStack = new Stack<>();
        callStack.add(new Point(initialX, initialY));

        while (!callStack.isEmpty()) {
            Point point = callStack.pop();
            int x = (int) point.getX();
            int y = (int) point.getY();
            String key = "" + x + ";" + y;
            if (usedPixels.containsKey(key)) {
                continue;
            }
            Color newColor = map.getColor(x, y);
            if (equalsColors(newColor, color)) {
                output.addPixel(x, y);
                usedPixels.put(key, key);
                if (point.getX() + 1 < map.getWidth()) {
                    callStack.push(new Point(x + 1, y));
                    if (point.getY() - 1 >= 0) {
                        callStack.push(new Point(x + 1, y - 1));
                    }
                    if (point.getY() + 1 < map.getHeight()) {
                        callStack.push(new Point(x + 1, y + 1));
                    }
                }
                if (point.getX() - 1 >= 0) {
                    callStack.push(new Point(x - 1, y));
                    if (point.getY() - 1 >= 0) {
                        callStack.push(new Point(x - 1, y - 1));
                    }
                    if (point.getY() + 1 < map.getHeight()) {
                        callStack.push(new Point(x - 1, y + 1));
                    }
                }
                if (point.getY() - 1 >= 0) {
                    callStack.push(new Point(x, y - 1));
                }
                if (point.getY() + 1 < map.getHeight()) {
                    callStack.push(new Point(x, y + 1));
                }
            }
        }
        return output;
    }

    public static boolean equalsColors(Color newColor, Color color) {
        return newColor.getRed() == color.getRed() && newColor.getGreen() == color.getGreen()
                && newColor.getBlue() == color.getBlue();
    }

    public static void floodFill(Indexer indexer, int initialX, int initialY, int fillColor) {
        Stack<Point> callStack = new Stack<>();
        callStack.add(new Point(initialX, initialY));
        int baseColor = indexer.get(initialX, initialY);
        while (!callStack.isEmpty()) {
            Point point = callStack.pop();
            int x = (int) point.getX();
            int y = (int) point.getY();
            if (indexer.get(x, y) == baseColor) {
                indexer.set(fillColor, x, y);
                if (point.getX() + 1 < indexer.getWidth()) {
                    callStack.push(new Point(x + 1, y));
                }
                if (point.getX() - 1 >= 0) {
                    callStack.push(new Point(x - 1, y));
                }
                if (point.getY() - 1 >= 0) {
                    callStack.push(new Point(x, y - 1));
                }
                if (point.getY() + 1 < indexer.getHeight()) {
                    callStack.push(new Point(x, y + 1));
                }
            }
        }
    }

    public static void blackDotsConnection(Indexer red, Indexer green, Indexer blue) {
        HashMap<String, Point> dotsMap = new HashMap<>();
        for (int x = 0; x < red.getWidth(); x++) {
            for (int y = 0; y < red.getHeight(); y++) {
                if (equalsColors(red, green, blue, x, y, BLACK_COLOR, BLACK_COLOR, BLACK_COLOR)) {
                    dotsMap.put(createHashStringIndex(x, y), new Point(x, y));
                }
            }
        }
        System.out.println("dots found: " + dotsMap.values().size());
        if (dotsMap.values().size() == 0) {
            return;
        }
        Entry<String, Point> entry = dotsMap.entrySet().iterator().next();
        dotsMap.remove(entry.getKey());
        Point point = entry.getValue();
        Point firstPoint = point;
        while (!dotsMap.isEmpty()) {
            Double minDistance = null;
            Point nextPoint = null;
            for (Point otherPoint : dotsMap.values()) {
                double distance = otherPoint.distance(point);
                if (minDistance == null || minDistance > distance) {
                    minDistance = distance;
                    nextPoint = otherPoint;
                }
            }
            fillGap(point, nextPoint, red, green, blue);
            point = nextPoint;
            dotsMap.remove(createHashStringIndex(point));
        }
        fillGap(point, firstPoint, red, green, blue);
    }

    public static void blackDotsConnection(Indexer map) {
        HashMap<String, Point> dotsMap = new HashMap<>();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (map.get(x, y) == BLACK_COLOR) {
                    dotsMap.put(createHashStringIndex(x, y), new Point(x, y));
                }
            }
        }
        System.out.println("dots found: " + dotsMap.values().size());
        if (dotsMap.values().size() == 0) {
            return;
        }
        Entry<String, Point> entry = dotsMap.entrySet().iterator().next();
        dotsMap.remove(entry.getKey());
        Point point = entry.getValue();
        Point firstPoint = point;
        while (!dotsMap.isEmpty()) {
            Double minDistance = null;
            Point nextPoint = null;
            for (Point otherPoint : dotsMap.values()) {
                double distance = otherPoint.distance(point);
                if (minDistance == null || minDistance > distance) {
                    minDistance = distance;
                    nextPoint = otherPoint;
                }
            }
            fillGap(point, nextPoint, map);
            point = nextPoint;
            dotsMap.remove(createHashStringIndex(point));
        }
        fillGap(point, firstPoint, map);
    }

    private static void fillGap(Point initialPoint, Point finalPoint, Indexer red, Indexer green, Indexer blue) {
        List<Point> line = robertsDrawLine(initialPoint, finalPoint);
        for (Point point : line) {
            red.set(BLACK_COLOR, (int) point.getX(), (int) point.getY());
            green.set(BLACK_COLOR, (int) point.getX(), (int) point.getY());
            blue.set(BLACK_COLOR, (int) point.getX(), (int) point.getY());
        }
    }

    private static void fillGap(Point initialPoint, Point finalPoint, Indexer map) {
        List<Point> line = robertsDrawLine(initialPoint, finalPoint);
        for (Point point : line) {
            map.set(BLACK_COLOR, (int) point.getX(), (int) point.getY());
        }
    }

    public static List<Point> bresenhamDrawLine(Point initialPoint, Point finalPoint) {
        ArrayList<Point> output = new ArrayList<>();
        int x1 = ((int) initialPoint.getX());
        int y1 = ((int) initialPoint.getY());
        int x2 = ((int) finalPoint.getX());
        int y2 = ((int) finalPoint.getY());
        int m_new = 2 * (y2 - y1);
        int slope_error_new = m_new - (x2 - x1);
        for (int x = x1, y = y1; x <= x2; x++) {
            output.add(new Point(x, y));
            // Add slope to increment angle formed
            slope_error_new += m_new;
            // Slope error reached limit, time to
            // increment y and update slope error.
            if (slope_error_new >= 0) {
                y++;
                slope_error_new -= 2 * (x2 - x1);
            }
        }
        return output;
    }

    public static List<Point> robertsDrawLine(Point initialPoint, Point finalPoint) {
        List<Point> output = new ArrayList<>();
        int x1 = ((int) initialPoint.getX());
        int y1 = ((int) initialPoint.getY());
        int x2 = ((int) finalPoint.getX());
        int y2 = ((int) finalPoint.getY());
        if (Math.abs(x1 - x2) <= 1) {
            if (initialPoint.getY() < finalPoint.getY()) {
                return robertsDrawVerticalLine(initialPoint, finalPoint);
            } else {
                return robertsDrawVerticalLine(finalPoint, initialPoint);
            }
        } else if (Math.abs(y1 - y2) <= 1) {
            if (initialPoint.getX() < finalPoint.getX()) {
                return robertsDrawHorizontalLine(initialPoint, finalPoint);
            } else {
                return robertsDrawHorizontalLine(finalPoint, initialPoint);
            }
        } else {
            int newX = x2 + (x1 - x2) / 2;
            int newY = y2 + (y1 - y2) / 2;
            Point newPoint = new Point(newX, newY);
            output = robertsDrawLine(initialPoint, newPoint);
            output.addAll(robertsDrawLine(newPoint, finalPoint));
            return output;
        }
    }

    private static List<Point> robertsDrawHorizontalLine(Point initialPoint, Point finalPoint) {
        List<Point> output = new ArrayList<>();
        int y = (int) initialPoint.getY();
        double middle = finalPoint.getX() - initialPoint.getX() / 2;
        int count = 0;
        for (int x = (int) initialPoint.getX(); x <= finalPoint.getX(); x++, count++) {
            if (count > middle) {
                y = (int) finalPoint.getY();
            }
            output.add(new Point(x, y));
        }
        return output;
    }

    private static List<Point> robertsDrawVerticalLine(Point initialPoint, Point finalPoint) {
        List<Point> output = new ArrayList<>();
        int x = (int) initialPoint.getX();
        double middle = Math.abs(finalPoint.getY() - initialPoint.getY()) / 2;
        int count = 0;
        for (int y = (int) initialPoint.getY(); y <= finalPoint.getY(); y++, count++) {
            if (count > middle) {
                x = (int) finalPoint.getX();
            }
            output.add(new Point(x, y));
        }
        return output;
    }

    private static String createHashStringIndex(int x, int y) {
        return "x" + x + ";y" + y;
    }

    private static String createHashStringIndex(Point point) {
        return "x" + ((int) point.getX()) + ";y" + ((int) point.getY());
    }

    public static RectangularImageArea overlappingArea(RectangularImageArea area1, RectangularImageArea area2) {
        int x = Math.max(area1.getX(), area2.getX());
        int y = Math.max(area1.getY(), area2.getY());
        int maxX = Math.min(area1.getMaxX(), area2.getMaxX());
        int maxY = Math.min(area1.getMaxY(), area2.getMaxY());
        if (maxX <= x || maxY <= y) {
            return new RectangularImageArea(0, 0, 0, 0);
        }
        return RectangularImageArea.createWithLimits(x, y, maxX, maxY);
    }

    public static double getOverlappingAreaPercentage(RectangularImageArea area1, RectangularImageArea area2) {
        RectangularImageArea overlappingArea = overlappingArea(area1, area2);
        double areaA1 = area1.getArea();
        double areaA2 = area2.getArea();
        if (areaA1 == 0 || areaA2 == 0) {
            return 0;
        }
        return 100 * ((2 * overlappingArea.getArea()) / (areaA1 + areaA2));
    }

    public static double getRelativeOverlappingAreaPercentage(RectangularImageArea area1, RectangularImageArea area2) {
        RectangularImageArea overlappingArea = overlappingArea(area1, area2);
        double areaA1 = area1.getArea();
        double areaA2 = area2.getArea();
        if (areaA1 == 0 || areaA2 == 0) {
            return 0;
        }
        return 100 * (overlappingArea.getArea() / Math.min(areaA1, areaA2));
    }

    public static ColoredArea getMaximumRectangularArea(Indexer red, Indexer green, Indexer blue, int initialX,
            int initialY, double tolerance) {
        int width = red.getWidth();
        RgbColor initialColor = ColorsUtils.getColor(red, green, blue, initialX, initialY);
        boolean maxSquareFound = false;
        boolean maxRectangleFound = false;
        int currentX = initialX;
        int currentY = initialY;
        boolean limitedVertical = false;
        boolean limitedHorizontal = false;
        while (!maxSquareFound) {
            currentX++;
            currentY++;
            if (currentX >= width) {
                currentX--;
                currentY--;
                maxSquareFound = true;
                limitedVertical = true;
                break;
            }
            if (currentY >= width) {
                currentX--;
                currentY--;
                maxSquareFound = true;
                limitedHorizontal = true;
                break;
            }
            // lines analysis
            limitedVertical = !isLineColored(red, green, blue, currentX, initialY, currentX, currentY - 1, initialColor,
                    tolerance);
            limitedHorizontal = !isLineColored(red, green, blue, initialX, currentY, currentX - 1, currentY,
                    initialColor, tolerance);
            // point analysis
            RgbColor currentColor = ColorsUtils.getColor(red, green, blue, currentX, currentY);
            if (initialColor.getDistance(currentColor) > tolerance) {
                maxSquareFound = true;
                currentX--;
                currentY--;
                break;
            }
            if (limitedVertical || limitedHorizontal) {
                currentX--;
                currentY--;
                maxSquareFound = true;
                limitedVertical = true;
                break;
            }
        }
        // FUTURE check biggest rectangle in case none of the coordinates are
        // limited
        if (limitedHorizontal && limitedVertical) {
            maxRectangleFound = true;
        } else if (limitedHorizontal) {
            while (!maxRectangleFound) {
                currentX++;
                if (currentX >= width) {
                    maxRectangleFound = true;
                    currentX--;
                    break;
                }
                limitedVertical = !isLineColored(red, green, blue, currentX, initialY, currentX, currentY, initialColor,
                        tolerance);
                if (limitedVertical) {
                    maxRectangleFound = true;
                    currentX--;
                    break;
                }
            }
        } else {
            while (!maxRectangleFound) {
                currentY++;
                if (currentY >= red.getHeight()) {
                    maxRectangleFound = true;
                    currentY--;
                    break;
                }
                limitedHorizontal = !isLineColored(red, green, blue, initialX, currentY, currentX, currentY,
                        initialColor, tolerance);
                if (limitedHorizontal) {
                    currentY--;
                    maxRectangleFound = true;
                    break;
                }
            }
        }
        int outputWidth = currentX - initialX + 1;
        int outputHeight = currentY - initialY + 1;

        RectangularImageArea zone = new RectangularImageArea(initialX, initialY, outputWidth, outputHeight);
        RgbColor meanColorInZone = ColorsUtils.getMeanColorInZone(red, green, blue, zone);
        // RgbColor meanColorInZone = ColorsUtils.getPredominantColorInZone(red,
        // green, blue, zone);

        return new ColoredArea(initialX, initialY, outputWidth, outputHeight, meanColorInZone);
    }

    public static boolean isLineColored(Indexer red, Indexer green, Indexer blue, int initialX, int initialY,
            int finalX, int finalY, RgbColor color, double tolerance) {
        int currentX = initialX;
        int currentY = initialY;
        for (; currentX <= finalX && currentY <= finalY;) {
            if (initialX < finalX) {
                currentX++;
            }
            if (currentY < finalY) {
                currentY++;
            }
            RgbColor currentColor = ColorsUtils.getColor(red, green, blue, currentX, currentY);
            if (color.getDistance(currentColor) > tolerance) {
                return false;
            }
            if (currentX == finalX && currentY == finalY) {
                break;
            }
        }
        return true;
    }

    public static RectangularImageZone paintBlockAndGetBoundaries(Indexer red, Indexer green, Indexer blue,
            int initialX, int initialY, int fillColorRed, int fillColorGreen, int fillColorBlue) {
        int minX = initialX;
        int minY = initialY;
        int maxX = initialX;
        int maxY = initialY;
        Stack<Point> callStack = new Stack<>();
        callStack.add(new Point(initialX, initialY));
        int baseColorRed = red.get(initialX, initialY);
        int baseColorGreen = green.get(initialX, initialY);
        int baseColorBlue = blue.get(initialX, initialY);
        while (!callStack.isEmpty()) {
            Point point = callStack.pop();
            int x = (int) point.getX();
            int y = (int) point.getY();
            if (equalsColors(red, green, blue, x, y, baseColorRed, baseColorGreen, baseColorBlue)) {
                if (x < minX) {
                    minX = x;
                }
                if (y < minY) {
                    minY = y;
                }
                if (x > maxX) {
                    maxX = x;
                }
                if (y > maxY) {
                    maxY = y;
                }
                red.set(fillColorRed, x, y);
                green.set(fillColorGreen, x, y);
                blue.set(fillColorBlue, x, y);
                if (point.getX() + 1 < red.getWidth()) {
                    callStack.push(new Point(x + 1, y));
                }
                if (point.getX() - 1 >= 0) {
                    callStack.push(new Point(x - 1, y));
                }
                if (point.getY() - 1 >= 0) {
                    callStack.push(new Point(x, y - 1));
                }
                if (point.getY() + 1 < red.getHeight()) {
                    callStack.push(new Point(x, y + 1));
                }
            }
        }
        return new RectangularImageZone(minX, minY, maxX - minX, maxY - minY);
    }

    public static RectangularImageZone paintSourroundingBlockAndGetBoundaries(Indexer red, Indexer green, Indexer blue,
            int initialX, int initialY, int fillColorRed, int fillColorGreen, int fillColorBlue) {
        int minX = initialX;
        int minY = initialY;
        int maxX = initialX;
        int maxY = initialY;
        Stack<Point> callStack = new Stack<>();
        callStack.add(new Point(initialX, initialY));
        int baseColorRed = red.get(initialX, initialY);
        int baseColorGreen = green.get(initialX, initialY);
        int baseColorBlue = blue.get(initialX, initialY);
        while (!callStack.isEmpty()) {
            Point point = callStack.pop();
            int x = (int) point.getX();
            int y = (int) point.getY();
            if (equalsColors(red, green, blue, x, y, baseColorRed, baseColorGreen, baseColorBlue)) {
                if (x < minX) {
                    minX = x;
                }
                if (y < minY) {
                    minY = y;
                }
                if (x > maxX) {
                    maxX = x;
                }
                if (y > maxY) {
                    maxY = y;
                }
                red.set(fillColorRed, x, y);
                green.set(fillColorGreen, x, y);
                blue.set(fillColorBlue, x, y);
                boolean rightAvailable = point.getX() + 1 < red.getWidth();
                boolean leftAvailable = point.getX() - 1 >= 0;
                boolean downAvailable = point.getY() - 1 >= 0;
                boolean upAvailable = point.getY() + 1 < red.getHeight();
                if (rightAvailable) {
                    callStack.push(new Point(x + 1, y));
                    if (downAvailable) {
                        callStack.push(new Point(x + 1, y - 1));
                    }
                    if (upAvailable) {
                        callStack.push(new Point(x + 1, y + 1));
                    }
                }
                if (leftAvailable) {
                    callStack.push(new Point(x - 1, y));
                    if (downAvailable) {
                        callStack.push(new Point(x - 1, y - 1));
                    }
                    if (upAvailable) {
                        callStack.push(new Point(x - 1, y + 1));
                    }
                }
                if (downAvailable) {
                    callStack.push(new Point(x, y - 1));
                }
                if (upAvailable) {
                    callStack.push(new Point(x, y + 1));
                }
            }
        }
        return new RectangularImageZone(minX, minY, maxX - minX, maxY - minY);
    }

    public static RectangularImageZone paintSourroundingBlockAndGetBoundaries(RgbColor initialColor, Indexer red,
            Indexer green, Indexer blue, int initialX, int initialY, int fillColorRed, int fillColorGreen,
            int fillColorBlue, double tolerance) {
        int minX = initialX;
        int minY = initialY;
        int maxX = initialX;
        int maxY = initialY;
        Stack<Point> callStack = new Stack<>();
        callStack.add(new Point(initialX, initialY));
        while (!callStack.isEmpty()) {
            Point point = callStack.pop();
            int x = (int) point.getX();
            int y = (int) point.getY();
            if (ColorsUtils.seemsColor(initialColor, new RgbColor(red.get(x, y), green.get(x, y), blue.get(x, y)),
                    tolerance)) {
                if (x < minX) {
                    minX = x;
                }
                if (y < minY) {
                    minY = y;
                }
                if (x > maxX) {
                    maxX = x;
                }
                if (y > maxY) {
                    maxY = y;
                }
                red.set(fillColorRed, x, y);
                green.set(fillColorGreen, x, y);
                blue.set(fillColorBlue, x, y);
                boolean rightAvailable = point.getX() + 1 < red.getWidth();
                boolean leftAvailable = point.getX() - 1 >= 0;
                boolean downAvailable = point.getY() - 1 >= 0;
                boolean upAvailable = point.getY() + 1 < red.getHeight();
                if (rightAvailable) {
                    callStack.push(new Point(x + 1, y));
                    if (downAvailable) {
                        callStack.push(new Point(x + 1, y - 1));
                    }
                    if (upAvailable) {
                        callStack.push(new Point(x + 1, y + 1));
                    }
                }
                if (leftAvailable) {
                    callStack.push(new Point(x - 1, y));
                    if (downAvailable) {
                        callStack.push(new Point(x - 1, y - 1));
                    }
                    if (upAvailable) {
                        callStack.push(new Point(x - 1, y + 1));
                    }
                }
                if (downAvailable) {
                    callStack.push(new Point(x, y - 1));
                }
                if (upAvailable) {
                    callStack.push(new Point(x, y + 1));
                }
            }
        }
        return new RectangularImageZone(minX, minY, maxX - minX, maxY - minY);
    }

    public static RectangularImageZone paintSourroundingBlockAndGetBoundaries(Indexer values, int initialX,
            int initialY, int fillColor) {
        int minX = initialX;
        int minY = initialY;
        int maxX = initialX;
        int maxY = initialY;
        Stack<Point> callStack = new Stack<>();
        callStack.add(new Point(initialX, initialY));
        int baseColor = values.get(initialX, initialY);
        while (!callStack.isEmpty()) {
            Point point = callStack.pop();
            int x = (int) point.getX();
            int y = (int) point.getY();
            if (values.get(x, y) == baseColor) {
                if (x < minX) {
                    minX = x;
                }
                if (y < minY) {
                    minY = y;
                }
                if (x > maxX) {
                    maxX = x;
                }
                if (y > maxY) {
                    maxY = y;
                }
                values.set(fillColor, x, y);
                boolean rightAvailable = point.getX() + 1 < values.getWidth();
                boolean leftAvailable = point.getX() - 1 >= 0;
                boolean downAvailable = point.getY() - 1 >= 0;
                boolean upAvailable = point.getY() + 1 < values.getHeight();
                if (rightAvailable) {
                    callStack.push(new Point(x + 1, y));
                    if (downAvailable) {
                        callStack.push(new Point(x + 1, y - 1));
                    }
                    if (upAvailable) {
                        callStack.push(new Point(x + 1, y + 1));
                    }
                }
                if (leftAvailable) {
                    callStack.push(new Point(x - 1, y));
                    if (downAvailable) {
                        callStack.push(new Point(x - 1, y - 1));
                    }
                    if (upAvailable) {
                        callStack.push(new Point(x - 1, y + 1));
                    }
                }
                if (downAvailable) {
                    callStack.push(new Point(x, y - 1));
                }
                if (upAvailable) {
                    callStack.push(new Point(x, y + 1));
                }
            }
        }
        return new RectangularImageZone(minX, minY, maxX - minX, maxY - minY);
    }

    private static boolean equalsColors(Indexer red, Indexer green, Indexer blue, int x, int y, int fillColorRed,
            int fillColorGreen, int fillColorBlue) {
        return red.get(x, y) == fillColorRed && blue.get(x, y) == fillColorBlue && green.get(x, y) == fillColorGreen;
    }

    public static List<Boolean> hasColor(GrayscaleMap map) {
        ArrayList<Boolean> output = new ArrayList<>();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (map.getIndex().get(x, y) == WHITE_COLOR) {
                    output.add(Boolean.FALSE);
                } else {
                    output.add(Boolean.TRUE);
                }
            }
        }
        return output;
    }

    public static boolean isCircle15x15(List<Boolean> input) {
        Boolean[] reference = { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE,
                Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE,
                Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE,
                Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE,
                Boolean.FALSE, Boolean.FALSE };
        int i = 0;
        int differences = 0;
        for (Boolean inputItem : input) {
            if (!inputItem.equals(reference[i++])) {
                differences++;
            }
        }
        System.out.println("Differences = " + differences);
        return differences < 15;
    }

    public static ColorMap downscaleRegion(ColorMap input, int targetSizeX, int targetSizeY, int initialX, int initialY,
            int regionSizeX, int regionSizeY) throws Exception {
        ColorMap output = new ColorMap(targetSizeX, targetSizeY);
        output.initializeEmpty();
        int sampleSizeX = (int) Math.ceil((double) (regionSizeX / targetSizeX));
        int sampleSizeY = (int) Math.ceil((double) (regionSizeY / targetSizeY));

        for (int x = 0; x < targetSizeX; ++x) {
            for (int y = 0; y < targetSizeY; ++y) {
                int indexX = Math.round(x * regionSizeX / targetSizeX);
                int indexY = Math.round(y * regionSizeY / targetSizeY);
                int averageRed = getAverageColor(input.getRedIndex(), indexX + initialX, indexY + initialY, sampleSizeX,
                        sampleSizeY);
                int averageBlue = getAverageColor(input.getBlueIndex(), indexX + initialX, indexY + initialY,
                        sampleSizeX, sampleSizeY);
                int averageGreen = getAverageColor(input.getGreenIndex(), indexX + initialX, indexY + initialY,
                        sampleSizeX, sampleSizeY);
                output.getRedIndex().set(averageRed, x, y);
                output.getGreenIndex().set(averageGreen, x, y);
                output.getBlueIndex().set(averageBlue, x, y);
            }
        }

        return output;
    }

    public static GrayscaleMap downscaleRegion(GrayscaleMap input, int targetSizeX, int targetSizeY, int initialX,
            int initialY, int regionSizeX, int regionSizeY) throws Exception {
        GrayscaleMap output = new GrayscaleMap(targetSizeX, targetSizeY);
        output.initializeEmpty();
        int sampleSizeX = (int) Math.ceil((double) (regionSizeX / targetSizeX));
        int sampleSizeY = (int) Math.ceil((double) (regionSizeY / targetSizeY));

        for (int x = 0; x < targetSizeX; ++x) {
            for (int y = 0; y < targetSizeY; ++y) {
                int indexX = Math.round(x * regionSizeX / targetSizeX);
                int indexY = Math.round(y * regionSizeY / targetSizeY);
                int averageColor = getAverageColor(input.getIndex(), indexX + initialX, indexY + initialY, sampleSizeX,
                        sampleSizeY);
                output.getIndex().set(averageColor, x, y);
            }
        }

        return output;
    }

    private static int getAverageColor(Indexer indexer, int initialX, int initialY, int sampleSizeX, int sampleSizeY) {
        int finalX = Math.min(initialX + sampleSizeX, indexer.getWidth());
        int finalY = Math.min(initialY + sampleSizeY, indexer.getHeight());
        int sampleWidth = finalX - initialX;
        int sampleHeight = finalY - initialY;
        if (sampleWidth == 0 || sampleHeight == 0) {
            return 0;
        }
        int sum = 0;
        for (int x = initialX; x < finalX; x++) {
            for (int y = initialY; y < finalY; y++) {
                sum += indexer.get(x, y);
            }
        }
        return Math.round(sum / (sampleWidth * sampleHeight));
    }

    public static boolean isCircle(RectangularImageZone rectangle, GrayscaleMap magnitudeMap) throws Exception {
        if (rectangle.getWidth() < 15 || rectangle.getHeight() < 15) {
            return false;
        }
        GrayscaleMap downscaledRegion = GeometryUtils.downscaleRegion(magnitudeMap, 15, 15, rectangle.getX(),
                rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        GrayscaleMap thresholdMap = ThresholdUtils.intGrayscaleThreshold(downscaledRegion.getValues(),
                downscaledRegion.getValues(), downscaledRegion.getValues(), downscaledRegion.getWidth(),
                downscaledRegion.getHeight(), 225 * 225 * 225);
        GeometryUtils.blackDotsConnection(thresholdMap.getIndex());
        GeometryUtils.floodFillFromCenter(thresholdMap.getIndex(), 0, 3, 255);
        List<Boolean> hasColor = GeometryUtils.hasColor(thresholdMap);
        return GeometryUtils.isCircle15x15(hasColor);
    }

    /**
     * 
     * @param rectangle
     * @param magnitudeMap
     * @param tolerancePercentage Tolerance for the elements away from the average.
     * @param tolerance           Tolerance amount to decide if an element is far
     *                            away from the average.
     * @return Color of the square, if it is a square, null if not.
     * @throws Exception
     */
    public static RgbColor coloredSquare(RectangularImageZone rectangle, ColorMap colorMap, Double tolerance,
            Double tolerancePercentage) throws Exception {
        Double totalRed = 0D;
        Double totalGreen = 0D;
        Double totalBlue = 0D;
        Double totalElements = 0D;
        for (int x = rectangle.getX(); x < rectangle.getMaxX(); x++) {
            for (int y = rectangle.getY(); y < rectangle.getMaxY(); y++) {
                RgbColor currentColor = colorMap.getRgbColor(x, y);
                totalRed += currentColor.getRed();
                totalGreen += currentColor.getGreen();
                totalBlue += currentColor.getBlue();
                totalElements++;
            }
        }
        Double averageRed = totalRed / totalElements;
        Double averageGreen = totalGreen / totalElements;
        Double averageBlue = totalBlue / totalElements;

        RgbColor averageColor = new RgbColor(averageRed.intValue(), averageGreen.intValue(), averageBlue.intValue());
        Double elementsInRange = 0D;
        for (int x = rectangle.getX(); x < rectangle.getMaxX(); x++) {
            for (int y = rectangle.getY(); y < rectangle.getMaxY(); y++) {
                RgbColor currentColor = colorMap.getRgbColor(x, y);
                double absoluteDistance = currentColor.getAbsoluteDistance(averageColor);
                if (absoluteDistance <= tolerance) {
                    elementsInRange++;
                }
            }
        }
        Double proportion = (elementsInRange / totalElements) * 100;
        if (proportion >= tolerancePercentage) {
            return averageColor;
        } else {
            return null;
        }
    }

    public static ColorMap enlargeRegion(ColorMap input, int targetSizeX, int targetSizeY, int i, int j,
            int regionSizeX, int regionSizeY) throws Exception {
        ColorMap output = new ColorMap(targetSizeX, targetSizeY);
        output.initializeEmpty();
        for (int x = 0; x < targetSizeX; ++x) {
            for (int y = 0; y < targetSizeY; ++y) {
                int indexX = Math.round(i + x * regionSizeX / targetSizeX);
                int indexY = Math.round(j + y * regionSizeY / targetSizeY);
                int red = input.getRedIndex().get(indexX, indexY);
                int green = input.getGreenIndex().get(indexX, indexY);
                int blue = input.getBlueIndex().get(indexX, indexY);
                output.getRedIndex().set(red, x, y);
                output.getGreenIndex().set(green, x, y);
                output.getBlueIndex().set(blue, x, y);
            }
        }

        return output;
    }

    public static ColorMap copyRegion(ColorMap input, int initialX, int initialY, int width, int height)
            throws Exception {
        ColorMap output = new ColorMap(width, height);
        output.initializeEmpty();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int indexX = Math.round(initialX + x);
                int indexY = Math.round(initialY + y);
                int red = input.getRedIndex().get(indexX, indexY);
                int green = input.getGreenIndex().get(indexX, indexY);
                int blue = input.getBlueIndex().get(indexX, indexY);
                output.getRedIndex().set(red, x, y);
                output.getGreenIndex().set(green, x, y);
                output.getBlueIndex().set(blue, x, y);
            }
        }

        return output;
    }

    public static ArrayList<ColoredArea> getMaximumRectangularAreas(ColorMap map, int initialX, int initialY,
            double tolerance) throws AreasAmountLimitReached {
        return getMaximumRectangularAreas(map, initialX, initialY, tolerance, DEFAULT_MAX_AREAS_LIMIT);
    }

    public static ArrayList<ColoredArea> getMaximumRectangularAreas(ColorMap map, int initialX, int initialY,
            double tolerance, int limit) throws AreasAmountLimitReached {
        return getMaximumRectangularAreas(map.getRedIndex(), map.getGreenIndex(), map.getBlueIndex(), initialX,
                initialY, tolerance, limit);
    }

    public static ArrayList<ColoredArea> getMaximumRectangularAreas(Indexer red, Indexer green, Indexer blue,
            int initialX, int initialY, double tolerance) throws AreasAmountLimitReached {
        return getMaximumRectangularAreas(red, green, blue, initialX, initialY, tolerance, DEFAULT_MAX_AREAS_LIMIT);
    }

    public static ArrayList<ColoredArea> getMaximumRectangularAreas(Indexer red, Indexer green, Indexer blue,
            int initialX, int initialY, double tolerance, int limit) throws AreasAmountLimitReached {
        HashMap<IntegerCoordinates, IntegerCoordinates> processedCoordinates = new HashMap<>();
        ArrayList<ColoredArea> output = new ArrayList<>();
        Stack<IntegerCoordinates> coordinatesToProcess = new Stack<>();
        coordinatesToProcess.push(new IntegerCoordinates(0, 0));
        while (coordinatesToProcess.size() > 0) {
            IntegerCoordinates coordinates = coordinatesToProcess.pop();
            if (processedCoordinates.containsKey(coordinates)) {
                continue;
            } else {
                processedCoordinates.put(coordinates, coordinates);
            }
            ColoredArea area = getMaximumRectangularArea(red, green, blue, coordinates.getX(), coordinates.getY(),
                    tolerance);
            if (area != null && area.getWidth() > 0 && area.getHeight() > 0) {
                output.add(area);
                if (output.size() > limit) {
                    throw new AreasAmountLimitReached(limit);
                }
                if (area.getMaxX() < red.getWidth()) {
                    coordinatesToProcess.push(new IntegerCoordinates(area.getMaxX(), area.getY()));
                }
                if (area.getMaxY() < red.getHeight()) {
                    coordinatesToProcess.push(new IntegerCoordinates(area.getX(), area.getMaxY()));
                }
            }
        }
        return output;
    }

    public static RectangularImageArea mergeWithMaxBoundaries(RectangularImageArea area1, RectangularImageArea area2) {
        return RectangularImageArea.createWithLimits(Math.min(area1.getX(), area2.getX()),
                Math.min(area1.getY(), area2.getY()), Math.max(area1.getMaxX(), area2.getMaxX()),
                Math.max(area1.getMaxY(), area2.getMaxY()));
    }

    public static ColoredArea mergeColoredAreaWithMaxBoundaries(ColoredArea area1, ColoredArea area2) {
        RgbColor meanColor = ColorsUtils.getMeanColor(area1.getColor(), area2.getColor());
        RectangularImageArea area = mergeWithMaxBoundaries(area1, area2);
        return new ColoredArea(area.getX(), area.getY(), area.getWidth(), area.getHeight(), meanColor);
    }
}
