package ar.com.sodhium.commons.img.operations;

import ar.com.sodhium.commons.img.colors.RgbColor;
import ar.com.sodhium.commons.img.colors.map.ColorMap;
import ar.com.sodhium.commons.indexes.Indexer;

public class DrawUtils {
    public static void drawRect(Indexer redIndex, Indexer greenIndex, Indexer blueIndex, int red, int green, int blue,
            int x, int y, int rectWidth, int rectHeight) {
        drawRectInColor(redIndex, red, x, y, rectWidth, rectHeight);
        drawRectInColor(greenIndex, green, x, y, rectWidth, rectHeight);
        drawRectInColor(blueIndex, blue, x, y, rectWidth, rectHeight);
    }

    public static void drawHorizontalLine(Indexer redIndex, Indexer greenIndex, Indexer blueIndex, int red, int green,
            int blue, int x, int y, int width) {
        drawHorizontalLineInColor(redIndex, red, x, y, width);
        drawHorizontalLineInColor(greenIndex, green, x, y, width);
        drawHorizontalLineInColor(blueIndex, blue, x, y, width);
    }

    public static void drawRectInColor(Indexer colorIndex, int colorValue, int x, int y, int rectWidth,
            int rectHeight) {
        for (int i = 0; i < Math.min(rectWidth, colorIndex.getWidth()); i++) {
            safeSet(colorIndex, colorValue, x + i, y);
            safeSet(colorIndex, colorValue, x + i, y + rectHeight);
        }
        for (int j = 0; j < Math.min(rectHeight, colorIndex.getHeight()); j++) {
            safeSet(colorIndex, colorValue, x, y + j);
            safeSet(colorIndex, colorValue, x + rectWidth, y + j);
        }
    }

    private static void safeSet(Indexer colorIndex, int colorValue, int x, int y) {
        if (x < colorIndex.getWidth() && y < colorIndex.getHeight()) {
            colorIndex.set(colorValue, x, y);
        }
    }

    public static void drawHorizontalLineInColor(Indexer colorIndex, int colorValue, int x, int y, int width) {
        for (int i = 0; i < width; i++) {
            colorIndex.set(colorValue, x + i, y);
        }
    }

    public static int[] copyArray(int[] array) {
        int[] newArray = new int[array.length];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }

    public static void drawRect(ColorMap colorOutput, int red, int green, int blue, int x, int y, int rectWidth,
            int rectHeight) {
        drawRect(colorOutput.getRedIndex(), colorOutput.getGreenIndex(), colorOutput.getBlueIndex(), red, green, blue,
                x, y, rectWidth, rectHeight);

    }

    public static void drawRect(ColorMap colorOutput, RgbColor mainColor, int x, int y, int width, int height) {
        drawRect(colorOutput, mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), x, y, width, height);
    }
}
