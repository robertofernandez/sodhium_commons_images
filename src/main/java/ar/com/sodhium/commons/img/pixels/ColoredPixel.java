package ar.com.sodhium.commons.img.pixels;

import ar.com.sodhium.commons.img.colors.RgbColor;

public class ColoredPixel {
    private RgbColor color;
    private int x;
    private int y;

    public ColoredPixel(RgbColor color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public RgbColor getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "<pixel x:'" + x + "' y:'" + y + "' r:'" + color.getRed() + "' g:'" + color.getGreen() + "' b:'"
                + color.getBlue() + "' />";
    }
}
