package ar.com.sodhium.commons.img.statistics;

import ar.com.sodhium.commons.img.areas.IntegerRectangularZone;
import ar.com.sodhium.commons.indexes.Indexer;

public class StatisticsCalculations {
    public static double calculateGrayProportion255(int[] color, IntegerRectangularZone rectangle, int width,
            int height) throws Exception {
        if (rectangle.getWidth() == 0 || rectangle.getHeight() == 0) {
            return 0;
        }
        Indexer indexer = new Indexer(color, width, height);
        return calculateGrayProportion255(indexer, rectangle);
    }

    public static double calculateGrayProportion255(Indexer indexer, IntegerRectangularZone rectangle) {
        int total = 0;
        for (int dx = 0; dx < rectangle.getWidth(); dx++) {
            for (int dy = 0; dy < rectangle.getHeight(); dy++) {
                total += indexer.get(rectangle.getX() + dx, rectangle.getY() + dy);
            }
        }
        return total / (rectangle.getWidth() * rectangle.getHeight());
    }

    public static GraysDecilsHistogram calculateGrayDecils(int[] color, IntegerRectangularZone rectangle, int width,
            int height) throws Exception {
        GraysDecilsHistogram output = new GraysDecilsHistogram();
        if (rectangle.getWidth() == 0 || rectangle.getHeight() == 0) {
            return output;
        }
        Indexer indexer = new Indexer(color, width, height);
        for (int dx = 0; dx < rectangle.getWidth(); dx++) {
            for (int dy = 0; dy < rectangle.getHeight(); dy++) {
                int value = indexer.get(rectangle.getX() + dx, rectangle.getY() + dy);
                output.addValue(value);
            }
        }
        return output;
    }

    public static ColorsAverageLevel calculateColorsAverageLevel(int width, int height, int[] originalRed,
            int[] originalGreen, int[] originalBlue, IntegerRectangularZone rectangle) throws Exception {
        double redProportion = StatisticsCalculations.calculateGrayProportion255(originalRed, rectangle, width, height);
        double greenProportion = StatisticsCalculations.calculateGrayProportion255(originalGreen, rectangle, width,
                height);
        double blueProportion = StatisticsCalculations.calculateGrayProportion255(originalBlue, rectangle, width,
                height);
        ColorsAverageLevel colorsAverageLevel = new ColorsAverageLevel(redProportion, greenProportion, blueProportion);
        return colorsAverageLevel;
    }
}
