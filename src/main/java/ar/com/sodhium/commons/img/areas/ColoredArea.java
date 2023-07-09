package ar.com.sodhium.commons.img.areas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ar.com.sodhium.commons.img.colors.RgbColor;

public class ColoredArea extends RectangularImageArea {
    @SerializedName("color")
    @Expose
    private RgbColor color;

    public ColoredArea(int x, int y, int width, int height, RgbColor color) {
        super(x, y, width, height);
        this.color = color;
    }

    public static ColoredArea createWithLimits(int x, int y, int maxX, int maxY, RgbColor color) {
        int theWidth = maxX - x;
        int theWeight = maxY - y;
        return new ColoredArea(x, y, theWidth, theWeight, color);
    }

    public RgbColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "][" + getMaxX() + ", " + getMaxY() + "][color " + color + "]";
    }

    public ColoredArea clone(int offsetX, int offsetY) {
        return new ColoredArea(x + offsetX, y + offsetY, width, height, color);
    }
}
