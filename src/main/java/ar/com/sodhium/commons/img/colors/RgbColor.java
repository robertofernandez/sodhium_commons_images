package ar.com.sodhium.commons.img.colors;

import java.awt.Color;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RgbColor {
    @SerializedName("red")
    @Expose
    private int red;
    @SerializedName("green")
    @Expose
    private int green;
    @SerializedName("blue")
    @Expose
    private int blue;

    public RgbColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getGreen() {
        return green;
    }

    public int getRed() {
        return red;
    }

    public int getBlue() {
        return blue;
    }

    public double getDistance(RgbColor anotherColor) {
        if (anotherColor == null) {
            return calculateDistance(red, 255, 255, 255);
        }
        return calculateDistance(red, red - anotherColor.getRed(), green - anotherColor.getGreen(),
                blue - anotherColor.getBlue());
    }

    public double getAbsoluteDistance(RgbColor anotherColor) {
        if (anotherColor == null) {
            return getAbsoluteDistance(new RgbColor(255, 255, 255));
        }
        int redDiff = red - anotherColor.getRed();
        int greenDiff = green - anotherColor.getGreen();
        int blueDiff = blue - anotherColor.getBlue();
        return Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
    }

    public boolean equalsMagnitude(RgbColor anotherColor, double tolerance) {
        return (Math.abs(getMagnitude() - anotherColor.getMagnitude()) <= tolerance);
    }

    public boolean equalsMagnitudeThreshold(RgbColor anotherColor, double threshold) {
        boolean m1 = getMagnitude() < threshold;
        boolean m2 = anotherColor.getMagnitude() < threshold;
        return m1 == m2;
    }

    public double getMagnitude() {
        return MagnitudeUtils.getAbsouluteMagnitude(red, green, blue);
    }

    private double calculateDistance(int red, int redDifference, int greenDifference, int blueDifference) {
        if (red < 128) {
            return Math.sqrt(2 * redDifference * redDifference + 4 * greenDifference * greenDifference
                    + 3 * blueDifference * blueDifference);
        } else {
            return Math.sqrt(3 * redDifference * redDifference + 4 * greenDifference * greenDifference
                    + 2 * blueDifference * blueDifference);
        }
    }

    public static double getDistance(int red, int green, int blue, int anotherRed, int anotherGreen, int anotherBlue) {
        RgbColor color = new RgbColor(red, green, blue);
        RgbColor anotherColor = new RgbColor(anotherRed, anotherGreen, anotherBlue);
        return color.getDistance(anotherColor);
    }

    public Color toAwtColor() {
        return new Color(red, green, blue);
    }

    @Override
    public String toString() {
        return "[r:" + red + ", g:" + green + ", b:" + blue + "]";
    }

    public RgbColor cloneColor() {
        return new RgbColor(red, green, blue);
    }

    public RgbColor mergePondered(RgbColor anotherColor, long weight, long weight2) {
        int newRed = (int) Math
                .round((double) (red * weight + anotherColor.red * weight2) / (double) (weight + weight2));
        int newGreen = (int) Math
                .round((double) (green * weight + anotherColor.green * weight2) / (double) (weight + weight2));
        int newBlue = (int) Math
                .round((double) (blue * weight + anotherColor.blue * weight2) / (double) (weight + weight2));
        return new RgbColor(newRed, newGreen, newBlue);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RgbColor) {
            RgbColor anotherColor = (RgbColor) o;
            return red == anotherColor.red && green == anotherColor.green && blue == anotherColor.blue;
        } else {
            return false;
        }
    }

    public boolean equalsColor(Color color) {
        return red == color.getRed() && green == color.getGreen() && blue == color.getBlue();
    }

    public HslColor toHslColor() {
        float[] hsb = Color.RGBtoHSB(red, green, blue, null);
        return new HslColor((hsb[0] * 360) % 360, hsb[1] * 100, hsb[2] * 100);
    }

    public RgbColor getDifference(RgbColor anotherColor) {
        if (anotherColor == null) {
            return this;
        }
        int redDiff = red - anotherColor.getRed();
        int greenDiff = green - anotherColor.getGreen();
        int blueDiff = blue - anotherColor.getBlue();
        return new RgbColor(redDiff, greenDiff, blueDiff);
    }
}
