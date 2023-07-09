package ar.com.sodhium.commons.img.colors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import ar.com.sodhium.commons.img.areas.IntegerFigureArea;
import ar.com.sodhium.commons.img.areas.IntegerRectangularZone;
import ar.com.sodhium.commons.img.colors.map.ColorMap;
import ar.com.sodhium.commons.indexes.Indexer;

public class ColorsUtils {

    /**
     * Convert HSL values to a RGB Color.
     *
     * @param h
     *            Hue is specified as degrees in the range 0 - 360.
     * @param s
     *            Saturation is specified as a percentage in the range 1 - 100.
     * @param l
     *            Luminance is specified as a percentage in the range 1 - 100.
     * @paran alpha the alpha value between 0 - 1 adapted from
     *        https://svn.codehaus.org/griffon/builders/gfxbuilder/tags/GFXBUILDER_0.2/
     *        gfxbuilder-core/src/main/com/camick/awt/HSLColor.java
     */
    public static int[] hslToRgb(double h, double s, double l, double alpha) {
        if (s < 0.0f || s > 100.0f) {
            String message = "Color parameter outside of expected range - Saturation";
            throw new IllegalArgumentException(message);
        }
        if (l < 0.0f || l > 100.0f) {
            String message = "Color parameter outside of expected range - Luminance";
            throw new IllegalArgumentException(message);
        }
        if (alpha < 0.0f || alpha > 1.0f) {
            String message = "Color parameter outside of expected range - Alpha";
            throw new IllegalArgumentException(message);
        }
        // Formula needs all values between 0 - 1.
        h = h % 360.0f;
        h /= 360f;
        s /= 100f;
        l /= 100f;
        double q = 0;
        if (l < 0.5)
            q = l * (1 + s);
        else
            q = (l + s) - (s * l);
        double p = 2 * l - q;
        int r = (int) (Math.round(Math.max(0, hueToRgb(p, q, h + (1.0d / 3.0d)) * 256)));
        int g = (int) (Math.round(Math.max(0, hueToRgb(p, q, h) * 256)));
        int b = (int) (Math.round(Math.max(0, hueToRgb(p, q, h - (1.0d / 3.0d)) * 256)));
        int[] array = { r, g, b };
        return array;
    }

    private static double hueToRgb(double p, double q, double h) {
        if (h < 0)
            h += 1;
        if (h > 1)
            h -= 1;
        if (6 * h < 1) {
            return p + ((q - p) * 6 * h);
        }
        if (2 * h < 1) {
            return q;
        }
        if (3 * h < 2) {
            return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
        }
        return p;
    }

    public static RgbColor getPredominantColor(ColorMap map) {
        Iterator<RgbColor> iterator = map.getColorsIterator();
        HashMap<String, Integer> appearences = new HashMap<>();
        HashMap<String, RgbColor> colors = new HashMap<>();
        while (iterator.hasNext()) {
            RgbColor color = iterator.next();
            if (!appearences.containsKey(color.toString())) {
                appearences.put(color.toString(), 1);
                colors.put(color.toString(), color);
            } else {
                appearences.put(color.toString(), appearences.get(color.toString()) + 1);
            }
        }
        int maxAppearences = 0;
        String maxColorName = null;
        for (Entry<String, Integer> entry : appearences.entrySet()) {
            if (entry.getValue().intValue() > maxAppearences) {
                maxColorName = entry.getKey();
                maxAppearences = entry.getValue().intValue();
            }
        }
        if (maxColorName != null) {
            return colors.get(maxColorName);
        } else {
            return null;
        }
    }

    public static RgbColor getMeanColorInZone(Indexer red, Indexer green, Indexer blue, IntegerRectangularZone zone) {
        Double totalRed = 0D;
        Double totalGreen = 0D;
        Double totalBlue = 0D;
        Double elements = (double) (zone.getWidth() * zone.getHeight());
        for (int x = zone.getX(); x < zone.getMaxX(); x++) {
            for (int y = zone.getY(); y < zone.getMaxY(); y++) {
                RgbColor color = getColor(red, green, blue, x, y);
                totalRed += color.getRed();
                totalGreen += color.getGreen();
                totalBlue += color.getBlue();
            }
        }
        totalRed = totalRed / elements;
        totalGreen = totalGreen / elements;
        totalBlue = totalBlue / elements;

        return new RgbColor(totalRed.intValue(), totalGreen.intValue(), totalBlue.intValue());
    }

    public static RgbColor getColor(Indexer red, Indexer green, Indexer blue, int x, int y) {
        int colorRed = red.get(x, y);
        int colorGreen = green.get(x, y);
        int colorBlue = blue.get(x, y);
        return new RgbColor(colorRed, colorGreen, colorBlue);
    }

    public static RgbColor getPredominantColorInZone(Indexer red, Indexer green, Indexer blue,
            IntegerRectangularZone limits) {
        HashMap<String, Integer> appearences = new HashMap<>();
        HashMap<String, RgbColor> colors = new HashMap<>();

        for (int x = limits.getX(); x < limits.getMaxX(); x++) {
            for (int y = limits.getY(); y < limits.getMaxY(); y++) {
                RgbColor color = getColor(red, green, blue, x, y);
                if (!appearences.containsKey(color.toString())) {
                    appearences.put(color.toString(), 1);
                    colors.put(color.toString(), color);
                } else {
                    appearences.put(color.toString(), appearences.get(color.toString()) + 1);
                }
            }
        }
        int maxAppearences = 0;
        String maxColorName = null;
        for (Entry<String, Integer> entry : appearences.entrySet()) {
            if (entry.getValue().intValue() > maxAppearences) {
                maxColorName = entry.getKey();
                maxAppearences = entry.getValue().intValue();
            }
        }
        if (maxColorName != null) {
            return colors.get(maxColorName);
        } else {
            return null;
        }
    }

    public static RgbColor getPredominantColorInZone(ColorMap map, IntegerFigureArea area,
            IntegerRectangularZone limits) {
        HashMap<String, Integer> appearences = new HashMap<>();
        HashMap<String, RgbColor> colors = new HashMap<>();

        for (int x = limits.getX(); x < limits.getMaxX(); x++) {
            for (int y = limits.getY(); y < limits.getMaxY(); y++) {
                if (area.contains(x, y)) {
                    RgbColor color = map.getRgbColor(x, y);
                    if (!appearences.containsKey(color.toString())) {
                        appearences.put(color.toString(), 1);
                        colors.put(color.toString(), color);
                    } else {
                        appearences.put(color.toString(), appearences.get(color.toString()) + 1);
                    }
                }
            }
        }
        int maxAppearences = 0;
        String maxColorName = null;
        for (Entry<String, Integer> entry : appearences.entrySet()) {
            if (entry.getValue().intValue() > maxAppearences) {
                maxColorName = entry.getKey();
                maxAppearences = entry.getValue().intValue();
            }
        }
        if (maxColorName != null) {
            return colors.get(maxColorName);
        } else {
            return null;
        }
    }

    public static String getHex(RgbColor color) {
        String red = getTwoDigitsHex(color.getRed());
        String green = getTwoDigitsHex(color.getGreen());
        String blue = getTwoDigitsHex(color.getBlue());
        return red + green + blue;
    }

    private static String getTwoDigitsHex(int value) {
        String hexString = Integer.toHexString(value);
        if (hexString.length() < 2) {
            hexString = "0" + hexString;
        }
        return hexString.toUpperCase();
    }

    public static boolean seemsColor(RgbColor oneColor, RgbColor anotherColor, double tolerance) {
        boolean distanceSimilitude = oneColor.getDistance(anotherColor) < tolerance;

        return distanceSimilitude;
    }

    public static RgbColor getMeanColor(RgbColor color, RgbColor color2) {
        int red = (int) ((color.getRed() + color2.getRed()) / 2);
        int green = (int) ((color.getGreen() + color2.getGreen()) / 2);
        int blue = (int) ((color.getBlue() + color2.getBlue()) / 2);
        return new RgbColor(red, green, blue);
    }
}
