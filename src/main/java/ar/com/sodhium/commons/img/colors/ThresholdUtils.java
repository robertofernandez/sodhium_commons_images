package ar.com.sodhium.commons.img.colors;

import java.util.ArrayList;
import java.util.Vector;

import ar.com.sodhium.commons.img.colors.map.ColorMap;
import ar.com.sodhium.commons.img.colors.map.GrayscaleMap;
import ar.com.sodhium.commons.indexes.Indexer;

public class ThresholdUtils {
    public static ColorMap intThresholdInColors(int[] red, int[] green, int[] blue, int width, int height,
            int cubicAmount) throws Exception {
        Vector<int[]> intThreshold = intThreshold(red, green, blue, width, height, cubicAmount);
        ColorMap output = new ColorMap(width, height);
        output.setRed(intThreshold.get(0));
        output.setGreen(intThreshold.get(1));
        output.setBlue(intThreshold.get(2));
        return output;
    }

    public static GrayscaleMap intGrayscaleThreshold(int[] red, int[] green, int[] blue, int width, int height,
            int cubicAmount) throws Exception {
        Vector<int[]> intThreshold = intThreshold(red, green, blue, width, height, cubicAmount);
        GrayscaleMap output = new GrayscaleMap(width, height);
        output.setValues(intThreshold.get(0));
        return output;
    }

    /**
     * Preserves pixels only with colors near to black or white.
     * 
     * @param red
     * @param green
     * @param blue
     * @param width
     * @param height
     * @param amount
     * @param maxDifference
     * @return
     * @throws Exception
     */
    public static GrayscaleMap texterizeByThreshold(ColorMap input, int width, int height, int amount,
            int maxDifference) throws Exception {
        int reverseAmount = 255 - amount;
        GrayscaleMap output = new GrayscaleMap(width, height);
        output.initializeEmpty();
        for (int x = 0; x < input.getWidth(); x++) {
            for (int y = 0; y < input.getHeight(); y++) {
                int red = input.getRedIndex().get(x, y);
                int green = input.getGreenIndex().get(x, y);
                int blue = input.getBlueIndex().get(x, y);
                int difference = Math.max(Math.abs(red - green),
                        Math.max(Math.abs(red - blue), Math.abs(blue - green)));
                if (difference <= maxDifference && ((red >= amount && green >= amount && blue >= amount)
                        || (red <= reverseAmount && green <= reverseAmount && blue <= reverseAmount))) {
                    output.getIndex().set(0, x, y);
                } else {
                    output.getIndex().set(255, x, y);
                }
            }
        }

        return output;
    }

    /**
     * Preserves pixels only with colors near to black or white.
     * 
     * @param red
     * @param green
     * @param blue
     * @param width
     * @param height
     * @param amount
     * @param maxDifference
     * @return
     * @throws Exception
     */
    public static GrayscaleMap texterizeByThresholdNegativeImage(ColorMap input, int width, int height, int amount,
            int maxDifference) throws Exception {
        int reverseAmount = 255 - amount;
        GrayscaleMap output = new GrayscaleMap(width, height);
        output.initializeEmpty();
        for (int x = 0; x < input.getWidth(); x++) {
            for (int y = 0; y < input.getHeight(); y++) {
                int red = input.getRedIndex().get(x, y);
                int green = input.getGreenIndex().get(x, y);
                int blue = input.getBlueIndex().get(x, y);
                int difference = Math.max(Math.abs(red - green),
                        Math.max(Math.abs(red - blue), Math.abs(blue - green)));
                if (difference <= maxDifference && ((red >= amount && green >= amount && blue >= amount)
                        || (red <= reverseAmount && green <= reverseAmount && blue <= reverseAmount))) {
                    int absouluteMagnitude = (int) Math.round(MagnitudeUtils.getAbsouluteMagnitude(red, green, blue));
                    output.getIndex().set(MagnitudeUtils.negativeColor(absouluteMagnitude), x, y);
                } else {
                    output.getIndex().set(255, x, y);
                }
            }
        }

        return output;
    }

    public static Vector<int[]> intThreshold(int[] red, int[] green, int[] blue, int width, int height, int cubicAmount)
            throws Exception {
        Vector<int[]> output = new Vector<int[]>();
        int[] newRed = new int[height * width];
        int[] newGreen = new int[height * width];
        int[] newBlue = new int[height * width];
        output.add(newRed);
        output.add(newGreen);
        output.add(newBlue);
        Indexer redIndex = new Indexer(red, width, height);
        Indexer newRedIndex = new Indexer(newRed, width, height);
        Indexer greenIndex = new Indexer(green, width, height);
        Indexer newGreenIndex = new Indexer(newGreen, width, height);
        Indexer blueIndex = new Indexer(blue, width, height);
        Indexer newBlueIndex = new Indexer(newBlue, width, height);
        int i, j;
        for (i = 0; i < height; i++)
            for (j = 0; j < width; j++) {
                int cred = redIndex.get(j, i);
                int cgreen = greenIndex.get(j, i);
                int cblue = blueIndex.get(j, i);
                int value = 0;
                if (cred * cgreen * cblue >= cubicAmount) {
                    value = 255;
                }
                newRedIndex.set(value, j, i);
                newGreenIndex.set(value, j, i);
                newBlueIndex.set(value, j, i);
            }
        return output;
    }

    public static ArrayList<Boolean> thresholdAsBoolean(int[] red, int[] green, int[] blue, int width, int height,
            int cubicAmount) throws Exception {
        ArrayList<Boolean> output = new ArrayList<>();
        Indexer redIndex = new Indexer(red, width, height);
        Indexer greenIndex = new Indexer(green, width, height);
        Indexer blueIndex = new Indexer(blue, width, height);
        int i, j;
        for (i = 0; i < height; i++)
            for (j = 0; j < width; j++) {
                int cred = redIndex.get(j, i);
                int cgreen = greenIndex.get(j, i);
                int cblue = blueIndex.get(j, i);
                if (cred * cgreen * cblue >= cubicAmount) {
                    output.add(Boolean.FALSE);
                } else {
                    output.add(Boolean.TRUE);
                }
            }
        return output;
    }

    public static Vector<int[]> intThresholdPrinting(int[] red, int[] green, int[] blue, int width, int height,
            int cubicAmount) throws Exception {
        Vector<int[]> output = new Vector<int[]>();
        int[] newRed = new int[height * width];
        int[] newGreen = new int[height * width];
        int[] newBlue = new int[height * width];
        output.add(newRed);
        output.add(newGreen);
        output.add(newBlue);
        Indexer redIndex = new Indexer(red, width, height);
        Indexer newRedIndex = new Indexer(newRed, width, height);
        Indexer greenIndex = new Indexer(green, width, height);
        Indexer newGreenIndex = new Indexer(newGreen, width, height);
        Indexer blueIndex = new Indexer(blue, width, height);
        Indexer newBlueIndex = new Indexer(newBlue, width, height);
        int i, j;
        for (i = 0; i < height; i++)
            for (j = 0; j < width; j++) {
                int cred = redIndex.get(j, i);
                int cgreen = greenIndex.get(j, i);
                int cblue = blueIndex.get(j, i);
                int value = 0;
                if (cred * cgreen * cblue >= cubicAmount) {
                    value = 255;
                    System.out.print("Boolean.FALSE,");
                } else {
                    System.out.print("Boolean.TRUE,");
                }
                newRedIndex.set(value, j, i);
                newGreenIndex.set(value, j, i);
                newBlueIndex.set(value, j, i);
            }
        return output;
    }

}
