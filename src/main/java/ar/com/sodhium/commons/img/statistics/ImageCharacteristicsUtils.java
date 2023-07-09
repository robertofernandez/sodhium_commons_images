package ar.com.sodhium.commons.img.statistics;

import ar.com.sodhium.commons.img.areas.IntegerRectangularZone;
import ar.com.sodhium.commons.indexes.Indexer;

public class ImageCharacteristicsUtils {

    /**
     * FUTURE improve using average
     * TODO use distance
     * 
     * @param indexer
     * @param rectangle
     * @return Color if uniform, -1 if not. Only for rectangles of size > 5.
     */
    public static int getHtmlBorderColor(Indexer indexer, IntegerRectangularZone rectangle, int tolerance) {
        if(rectangle.getWidth() < 5 || rectangle .getHeight() < 5) {
            return -1;
        }
        int firstValue = indexer.get(rectangle.getX() + 1, rectangle.getY() + 1);
        int currentValue = -1;
        for (int dx = 1; dx < rectangle.getWidth() - 2; dx++) {
            currentValue = indexer.get(rectangle.getX() + dx, rectangle.getY() + 1);
            if (Math.abs(currentValue - firstValue) > tolerance * 2) {
                return -1;
            }
            currentValue = indexer.get(rectangle.getX() + dx, rectangle.getY() + rectangle.getHeight() - 1);
            if (Math.abs(currentValue - firstValue) > tolerance * 2) {
                return -1;
            }
        }
        for (int dy = 1; dy < rectangle.getHeight() - 2; dy++) {
            currentValue = indexer.get(rectangle.getX() + 1, rectangle.getY() + dy);
            if (Math.abs(currentValue - firstValue) > tolerance * 2) {
                return -1;
            }
            currentValue = indexer.get(rectangle.getX() + rectangle.getWidth() - 2, rectangle.getY() + dy);
            if (Math.abs(currentValue - firstValue) > tolerance * 2) {
                return -1;
            }
        }
        return firstValue;
    }

}
