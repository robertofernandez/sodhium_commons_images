package ar.com.sodhium.commons.img.statistics;

import ar.com.sodhium.commons.img.areas.RectangularImageZone;
import ar.com.sodhium.commons.img.colors.map.ColorMap;
import ar.com.sodhium.commons.img.colors.map.GrayscaleMap;

public class ImageStatisticsUtils {
    public static final String GRAY_PROPORTION255 = "GrayProportion255";
    public static final String GRAY_PROPORTION = "GrayProportion";
    public static final String COLORS_AVERAGE_LEVEL255 = "ColorsAverageLevel255";
    public static final String BORDER_COLOR = "Border color";

    public static void fillDefaultStatistics(ColorMap originalMap, GrayscaleMap intThresholdInColors,
            RectangularImageZone rectangle) throws Exception {
        double proportion = StatisticsCalculations.calculateGrayProportion255(intThresholdInColors.getValues(),
                rectangle, intThresholdInColors.getWidth(), intThresholdInColors.getHeight());
        ColorsAverageLevel colorsAverageLevel = StatisticsCalculations.calculateColorsAverageLevel(
                originalMap.getWidth(), originalMap.getHeight(), originalMap.getRed(), originalMap.getGreen(),
                originalMap.getBlue(), rectangle);
        rectangle.getStatistics().put(COLORS_AVERAGE_LEVEL255, colorsAverageLevel);
        rectangle.getStatistics().put(GRAY_PROPORTION, new GraysAverageLevel(proportion / 255));
        rectangle.getStatistics().put(GRAY_PROPORTION255, new GraysAverageLevel(proportion));

        // FUTURE use better tolerance adjustments
        int borderColorRed = ImageCharacteristicsUtils.getHtmlBorderColor(originalMap.getRedIndex(), rectangle, 6);
        int borderColorGreen = ImageCharacteristicsUtils.getHtmlBorderColor(originalMap.getGreenIndex(), rectangle, 6);
        int borderColorBlue = ImageCharacteristicsUtils.getHtmlBorderColor(originalMap.getBlueIndex(), rectangle, 6);

        if (borderColorRed != -1 && borderColorGreen != -1 && borderColorBlue != -1) {
            rectangle.getStatistics().put(BORDER_COLOR, new ColorsAverageLevel((double) borderColorRed,
                    (double) borderColorGreen, (double) borderColorBlue));
        }
    }
}
