package ar.com.sodhium.commons.img.pixels;

import java.awt.Color;

import ar.com.sodhium.commons.img.colors.RgbColor;

public class ColorCount implements Comparable<ColorCount> {
    private RgbColor color;
    private Long amount;

    public ColorCount(RgbColor color) {
        this.color = color;
        amount = 1L;
    }

    public static Long getLongValue(Color color) {
        return getLongValue(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static Long getLongValue(RgbColor color) {
        return getLongValue(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static Long getLongValue(int red, int green, int blue) {
        return red * 1000000L + green * 1000L + blue;
    }

    public RgbColor getColor() {
        return color;
    }

    public Long getAmount() {
        return amount;
    }

    public void incrementAmount() {
        amount = amount + 1;
    }

    @Override
    public int compareTo(ColorCount o) {
        return o.amount.compareTo(amount);
    }

    @Override
    public String toString() {
        return "ColorCount [color=" + color + ", amount=" + amount + "]";
    }
    
    
}
