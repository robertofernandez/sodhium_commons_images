package ar.com.sodhium.commons.img.colors.values;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ar.com.sodhium.commons.img.colors.RgbColor;
import ar.com.sodhium.commons.img.colors.map.ColorMap;

public class ColorSignature {
    @SerializedName("width")
    @Expose
    private int width;
    @SerializedName("height")
    @Expose
    private int height;
    @SerializedName("pixels")
    @Expose
    private ArrayList<RgbColor> pixels;

    public ColorSignature(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new ArrayList<>();
    }

    public ColorSignature(ColorMap map) {
        this.width = map.getWidth();
        this.height = map.getHeight();
        pixels = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels.add(map.getRgbColor(x, y));
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public ArrayList<RgbColor> getPixels() {
        return pixels;
    }

    public ColorMap getAsMap() throws Exception {
        ColorMap map = new ColorMap(width, height);
        map.initializeEmpty();
        int i = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                RgbColor rgbColor = pixels.get(i++);
                map.setRgbColor(x, y, rgbColor);
            }
        }
        return map;
    }
}
