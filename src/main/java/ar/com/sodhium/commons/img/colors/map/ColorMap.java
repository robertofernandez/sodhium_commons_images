package ar.com.sodhium.commons.img.colors.map;

import java.awt.Color;
import java.util.Iterator;
import java.util.Vector;

import ar.com.sodhium.commons.img.colors.RgbColor;
import ar.com.sodhium.commons.img.operations.DrawUtils;
import ar.com.sodhium.commons.indexes.Indexer;

public class ColorMap {
    private int[] red;
    private int[] green;
    private int[] blue;
    private int width;
    private int height;
    private Indexer redIndex;
    private Indexer blueIndex;
    private Indexer greenIndex;

    public static ColorMap cloneColors(int[] red, int[] green, int[] blue, int width, int height) throws Exception {
        ColorMap output = new ColorMap(width, height);
        int[] newRed = DrawUtils.copyArray(red);
        int[] newGreen = DrawUtils.copyArray(green);
        int[] newBlue = DrawUtils.copyArray(blue);
        output.setRed(newRed);
        output.setBlue(newBlue);
        output.setGreen(newGreen);
        return output;
    }

    public ColorMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public ColorMap(int width, int height, Vector<int[]> components) throws Exception {
        this.width = width;
        this.height = height;
        setRed(components.get(0));
        setGreen(components.get(1));
        setBlue(components.get(2));
    }

    public ColorMap cloneMap() throws Exception {
        return cloneColors(red, green, blue, width, height);
    }

    public void initializeEmpty() throws Exception {
        setRed(new int[width * height]);
        setGreen(new int[width * height]);
        setBlue(new int[width * height]);
    }

    public int[] getRed() {
        return red;
    }

    public void setRed(int[] red) throws Exception {
        this.red = red;
        redIndex = new Indexer(red, width, height);
    }

    public int[] getGreen() {
        return green;
    }

    public void setGreen(int[] green) throws Exception {
        this.green = green;
        greenIndex = new Indexer(green, width, height);
    }

    public int[] getBlue() {
        return blue;
    }

    public void setBlue(int[] blue) throws Exception {
        this.blue = blue;
        blueIndex = new Indexer(blue, width, height);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Indexer getRedIndex() {
        return redIndex;
    }

    public Indexer getGreenIndex() {
        return greenIndex;
    }

    public Indexer getBlueIndex() {
        return blueIndex;
    }

    public Color getColor(int x, int y) {
        int red = getRedIndex().get(x, y);
        int green = getGreenIndex().get(x, y);
        int blue = getBlueIndex().get(x, y);
        return new Color(red, green, blue);
    }

    public RgbColor getRgbColor(int x, int y) {
        int red = getRedIndex().get(x, y);
        int green = getGreenIndex().get(x, y);
        int blue = getBlueIndex().get(x, y);
        return new RgbColor(red, green, blue);
    }

    public void setColor(int x, int y, Color color) {
        getRedIndex().set(color.getRed(), x, y);
        getGreenIndex().set(color.getGreen(), x, y);
        getBlueIndex().set(color.getBlue(), x, y);
    }

    public void setRgbColor(int x, int y, RgbColor color) {
        getRedIndex().set(color.getRed(), x, y);
        getGreenIndex().set(color.getGreen(), x, y);
        getBlueIndex().set(color.getBlue(), x, y);
    }

    public void replaceColor(RgbColor color, RgbColor color2) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (getRgbColor(x, y).equals(color)) {
                    setRgbColor(x, y, color2);
                }
            }
        }
    }

    public Iterator<RgbColor> getColorsIterator() {
        return new ColorsMapIterator(this);
    }
}
