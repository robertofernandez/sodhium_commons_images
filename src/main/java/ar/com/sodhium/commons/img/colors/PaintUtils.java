package ar.com.sodhium.commons.img.colors;

import java.awt.Color;

import ar.com.sodhium.commons.img.colors.map.ColorMap;

public class PaintUtils {

    public static ColorMap paintUsingMagnitudeAndColors(ColorMap colorsMap, ColorMap magnitudeMap) throws Exception {
        ColorMap outputMap = magnitudeMap.cloneMap();
        for (int x = 0; x < outputMap.getWidth(); x++) {
            for (int y = 0; y < outputMap.getHeight(); y++) {
                Color magnitudeColor = magnitudeMap.getColor(x, y);
                if (x < colorsMap.getWidth() && y < colorsMap.getHeight()) {
                    double cubicMagnitude = MagnitudeUtils.getCubicMagnitude(magnitudeColor.getRed() + 1,
                            magnitudeColor.getGreen() + 1, magnitudeColor.getBlue() + 1);
                    RgbColor colorFromPalette = colorsMap.getRgbColor(x, y);
                    Double red0 = (double) (colorFromPalette.getRed() + 1);
                    Double green0 = (double) (colorFromPalette.getGreen() + 1);
                    Double blue0 = (double) (colorFromPalette.getBlue() + 1);

                    Double d = green0 / red0;
                    Double e = blue0 / red0;

                    Double finalRed = Math.cbrt(cubicMagnitude / (e * e + d * d + d));
                    Double finalGreen = d * finalRed;
                    Double finalBlue = e * finalRed;

                    int red = (int) Math.round(Math.max(0, Math.min(255, finalRed - 1)));
                    int green = (int) Math.round(Math.max(0, Math.min(255, finalGreen - 1)));
                    int blue = (int) Math.round(Math.max(0, Math.min(255, finalBlue - 1)));

                    outputMap.setColor(x, y, new Color(red, green, blue));
                } else {
                    outputMap.setColor(x, y, magnitudeColor);
                }
            }
        }
        return outputMap;
    }

    public static ColorMap paintUsingMagnitudeDifferencesAndColors(ColorMap colorsMap, ColorMap finalMap,
            ColorMap baseMap) throws Exception {
        ColorMap outputMap = baseMap.cloneMap();
        for (int x = 0; x < baseMap.getWidth(); x++) {
            for (int y = 0; y < baseMap.getHeight(); y++) {
                if (x < finalMap.getWidth() && y < finalMap.getHeight()) {
                    RgbColor color = finalMap.getRgbColor(x, y);
                    RgbColor baseColor = baseMap.getRgbColor(x, y);
                    double magnitude = MagnitudeUtils.getMagnitude(color.getRed(), color.getGreen(), color.getBlue());
                    double baseMagnitude = MagnitudeUtils.getMagnitude(baseColor.getRed(), baseColor.getGreen(),
                            baseColor.getBlue());
                    double difference = baseMagnitude - magnitude;
                    double normalizedDifference = difference / MagnitudeUtils.getMagnitude(256, 256, 256);

                    // FIXME configurable tolerance
                    if (normalizedDifference > 0.1) {
                        double differenceReference = 255;
                        if (normalizedDifference > 0) {
                            differenceReference = 255 - (normalizedDifference * 255);
                        }
                        int finalDif = (int) differenceReference;
                        double cubicMagnitude = MagnitudeUtils.getCubicMagnitude(finalDif, finalDif, finalDif);
                        RgbColor colorFromPalette = colorsMap.getRgbColor(x, y);
                        Double red0 = (double) (colorFromPalette.getRed() + 1);
                        Double green0 = (double) (colorFromPalette.getGreen() + 1);
                        Double blue0 = (double) (colorFromPalette.getBlue() + 1);

                        Double d = green0 / red0;
                        Double e = blue0 / red0;

                        Double finalRed = Math.cbrt(cubicMagnitude / (e * e + d * d + d));
                        Double finalGreen = d * finalRed;
                        Double finalBlue = e * finalRed;

                        int red = (int) Math.round(Math.max(0, Math.min(255, finalRed - 1)));
                        int green = (int) Math.round(Math.max(0, Math.min(255, finalGreen - 1)));
                        int blue = (int) Math.round(Math.max(0, Math.min(255, finalBlue - 1)));
                        outputMap.setColor(x, y, new Color(red, green, blue));
                    } else {
                        outputMap.setRgbColor(x, y, new RgbColor(255, 255, 255));
                    }
                } else {
                    outputMap.setRgbColor(x, y, new RgbColor(255, 255, 255));
                }
            }
        }

        return outputMap;
    }
}
