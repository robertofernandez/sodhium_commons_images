package ar.com.sodhium.commons.img.colors;

public class MagnitudeUtils {
    public static double getMagnitude(int red, int green, int blue) {
        return Math.sqrt(red * blue * blue + green * green * blue + red * red * green);
    }

    public static double getCubicMagnitude(int red, int green, int blue) {
        return red * blue * blue + green * green * blue + red * red * green;
    }

    public static int getAbsouluteMagnitude(double magnitude) {
        double output = magnitude * 256 / getMagnitude(256, 256, 256);
        return (int) (Math.round(output));
    }

    public static double getAbsouluteMagnitude(int red, int green, int blue) {
        double output = getMagnitude(red, green, blue) * 256 / getMagnitude(256, 256, 256);
        return (int) (Math.round(output));
    }

    public static double getNormalizedMagnitude(int red, int green, int blue) {
        return getAbsouluteMagnitude(red, green, blue) / 255D;
    }

    public static int[] getAbsoulteMagnitude(int[] red, int[] green, int[] blue) {
        int[] output = new int[red.length];
        for (int i = 0; i < red.length; i++) {
            output[i] = getAbsouluteMagnitude(getMagnitude(red[i], green[i], blue[i]));
        }
        return output;
    }

    public static double[] getMagnitude(int[] red, int[] green, int[] blue) {
        double[] output = new double[red.length];
        for (int i = 0; i < red.length; i++) {
            output[i] = getMagnitude(red[i], green[i], blue[i]);
        }
        return output;
    }

    public static int[] getMaxLevels(int[] red, int[] green, int[] blue) {
        int[] output = new int[red.length];
        for (int i = 0; i < red.length; i++) {
            output[i] = Math.max(Math.max(red[i], green[i]), blue[i]);
        }
        return output;
    }

    public static int[] negativeImage(int[] color) {
        int[] output = new int[color.length];
        for (int i = 0; i < color.length; i++) {
            output[i] = negativeColor(color[i]);
        }
        return output;
    }

    public static int negativeColor(int level) {
        return 255 - level;
    }

}
