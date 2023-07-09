package ar.com.sodhium.commons.img.ml;

import ar.com.sodhium.commons.img.areas.IntegerRectangularZone;
import ar.com.sodhium.commons.img.colors.MagnitudeUtils;
import ar.com.sodhium.commons.img.colors.RgbColor;
import ar.com.sodhium.commons.img.colors.map.ColorMap;

public class BasicImageNeuralNetworkUtils {

    public static double[][] getTensor(ColorMap map) {
        double[][] tensor = new double[map.getHeight()][map.getWidth()];
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                RgbColor color = map.getRgbColor(j, i);
                tensor[i][j] = MagnitudeUtils.getNormalizedMagnitude(color.getRed(), color.getGreen(), color.getBlue());
            }
        }
        return tensor;
    }

    public static double[][] getTensor(ColorMap map, IntegerRectangularZone zone) {
        double[][] tensor = new double[zone.getHeight()][zone.getWidth()];
        for (int i = zone.getY(); i < zone.getHeight(); i++) {
            for (int j = zone.getX(); j < zone.getWidth(); j++) {
                RgbColor color = map.getRgbColor(j, i);
                tensor[i][j] = MagnitudeUtils.getNormalizedMagnitude(color.getRed(), color.getGreen(), color.getBlue());
            }
        }
        return tensor;
    }

    public static double[] flattenTensor(double[][] tensor) {
        int height = tensor.length;
        int width = tensor[0].length;

        double[] flattenedTensor = new double[height * width];
        int index = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                flattenedTensor[index++] = tensor[i][j];
            }
        }

        return flattenedTensor;
    }

}
