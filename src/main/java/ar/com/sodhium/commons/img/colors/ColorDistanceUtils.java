package ar.com.sodhium.commons.img.colors;

import ar.com.sodhium.commons.img.statistics.MeanColorsSet;

public class ColorDistanceUtils {
    public static boolean anyGray(MeanColorsSet meanColorsSet) {
        return anyNear(meanColorsSet, new RgbColor(160, 160, 160), 50)
                || anyNear(meanColorsSet, new RgbColor(190, 170, 170), 50)
                || anyNear(meanColorsSet, new RgbColor(170, 190, 170), 50);
    }

    public static boolean anyOrange(MeanColorsSet meanColorsSet) {
        return anyNear(meanColorsSet, new RgbColor(240, 90, 147), 50)
                || anyNear(meanColorsSet, new RgbColor(250, 180, 206), 50);
    }

    public static boolean anyBlue(MeanColorsSet meanColorsSet) {
        return anyNear(meanColorsSet, new RgbColor(63, 231, 173), 50);
    }

    public static boolean anyNear(MeanColorsSet meanColorsSet, RgbColor rgbColor, int distance) {
        if (meanColorsSet == null) {
            return false;
        }
        if (meanColorsSet.getMainColor().getDistance(rgbColor) < distance) {
            return true;
        } else {
            if (meanColorsSet.getSecondaryColor() != null) {
                if (meanColorsSet.getSecondaryColor().getDistance(rgbColor) < distance) {
                    return true;
                }
            }
        }
        return false;
    }

}
