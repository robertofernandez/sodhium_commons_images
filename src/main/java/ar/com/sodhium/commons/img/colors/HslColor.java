package ar.com.sodhium.commons.img.colors;

import java.awt.Color;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HslColor {
    @SerializedName("hue")
    @Expose
    private double hue;
    @SerializedName("saturation")
    @Expose
    private double saturation;
    @SerializedName("lightness")
    @Expose
    private double lightness;

    public HslColor(double hue, double saturation, double lightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.lightness = lightness;
    }

    public double getSaturation() {
        return saturation;
    }

    public double getHue() {
        return hue;
    }

    public double getLightness() {
        return lightness;
    }

    public double getDistance(HslColor anotherColor) {
        if (anotherColor == null) {
            return calculateDistance(hue, 255, 255, 255);
        }
        return calculateDistance(hue, hue - anotherColor.getHue(), saturation - anotherColor.getSaturation(),
                lightness - anotherColor.getLightness());
    }

    public double getAbsoluteDistance(HslColor anotherColor) {
        if (anotherColor == null) {
            return getAbsoluteDistance(new HslColor(255, 255, 255));
        }
        double hueDiff = hue - anotherColor.getHue();
        double saturationDiff = saturation - anotherColor.getSaturation();
        double lightnessDiff = lightness - anotherColor.getLightness();
        return Math.sqrt(hueDiff * hueDiff + saturationDiff * saturationDiff + lightnessDiff * lightnessDiff);
    }

    public boolean equalsMagnitude(HslColor anotherColor, double tolerance) {
        return (Math.abs(getMagnitude() - anotherColor.getMagnitude()) <= tolerance);
    }

    public boolean equalsMagnitudeThreshold(HslColor anotherColor, double threshold) {
        boolean m1 = getMagnitude() < threshold;
        boolean m2 = anotherColor.getMagnitude() < threshold;
        return m1 == m2;
    }

    public double getMagnitude() {
        return toRgbColor().getMagnitude();
    }

    private double calculateDistance(double hue, double hueDifference, double saturationDifference,
            double lightnessDifference) {
        if (hue < 128) {
            return Math.sqrt(2 * hueDifference * hueDifference + 4 * saturationDifference * saturationDifference
                    + 3 * lightnessDifference * lightnessDifference);
        } else {
            return Math.sqrt(3 * hueDifference * hueDifference + 4 * saturationDifference * saturationDifference
                    + 2 * lightnessDifference * lightnessDifference);
        }
    }

    public static double getDistance(int hue, int saturation, int lightness, int anotherhue, int anothersaturation,
            int anotherlightness) {
        HslColor color = new HslColor(hue, saturation, lightness);
        HslColor anotherColor = new HslColor(anotherhue, anothersaturation, anotherlightness);
        return color.getDistance(anotherColor);
    }

    public Color toAwtColor() {
        return toRgbColor().toAwtColor();
    }

    @Override
    public String toString() {
        return "<color h:'" + hue + "' s:'" + saturation + "' l:'" + lightness + "' />";
    }

    public HslColor cloneColor() {
        return new HslColor(hue, saturation, lightness);
    }

    public HslColor mergePondehue(HslColor anotherColor, long weight, long weight2) {
        int newhue = (int) Math
                .round((double) (hue * weight + anotherColor.hue * weight2) / (double) (weight + weight2));
        int newsaturation = (int) Math.round(
                (double) (saturation * weight + anotherColor.saturation * weight2) / (double) (weight + weight2));
        int newlightness = (int) Math
                .round((double) (lightness * weight + anotherColor.lightness * weight2) / (double) (weight + weight2));
        return new HslColor(newhue, newsaturation, newlightness);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof HslColor) {
            HslColor anotherColor = (HslColor) o;
            return hue == anotherColor.hue && saturation == anotherColor.saturation
                    && lightness == anotherColor.lightness;
        } else {
            return false;
        }
    }

    public RgbColor toRgbColor() {
        int[] r = ColorsUtils.hslToRgb(hue, saturation, lightness, 1);
        return new RgbColor(r[0], r[1], r[2]);
    }

    public boolean equalsColor(Color color) {
        return toRgbColor().equalsColor(color);
    }

    public HslColor getDifference(HslColor anotherColor) {
        if (anotherColor == null) {
            return this;
        }
        double hueDiff = hue - anotherColor.getHue();
        double saturationDiff = saturation - anotherColor.getSaturation();
        double lightnessDiff = lightness - anotherColor.getLightness();
        return new HslColor(hueDiff, saturationDiff, lightnessDiff);
    }
}
