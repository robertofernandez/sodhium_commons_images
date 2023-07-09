package ar.com.sodhium.commons.img.pixels;

import java.util.ArrayList;
import java.util.HashMap;

import ar.com.sodhium.commons.img.colors.RgbColor;

public class ColorsCluster implements Comparable<ColorsCluster> {
    private ArrayList<ColoredPixel> pixels;
    private long totalRed;
    private long totalGreen;
    private long totalBlue;

    public ColorsCluster() {
        pixels = new ArrayList<>();
    }

    public RgbColor getMeanColor() {
        if (pixels.size() == 0) {
            return new RgbColor(0, 0, 0);
        }
        int red = (int) Math.round((double) totalRed / (double) pixels.size());
        int green = (int) Math.round((double) totalBlue / (double) pixels.size());
        int blue = (int) Math.round((double) totalGreen / (double) pixels.size());
        return new RgbColor(red, green, blue);
    }

    public double getDistanceToMean(RgbColor color) {
        if (pixels.size() == 0) {
            return 0;
        }
        return getMeanColor().getDistance(color);
    }

    public boolean addIfNear(ColoredPixel pixel, double maxDistance) {
        double distanceToMean = getDistanceToMean(pixel.getColor());
        if (distanceToMean <= maxDistance) {
            addPixel(pixel);
            return true;
        } else {
            return false;
        }
    }

    public void addPixel(ColoredPixel pixel) {
        pixels.add(pixel);
        totalRed += pixel.getColor().getRed();
        totalGreen += pixel.getColor().getGreen();
        totalBlue += pixel.getColor().getBlue();
    }

    public ArrayList<ColoredPixel> getPixels() {
        return pixels;
    }

    public ArrayList<ColorsCluster> split() {
        ArrayList<ColorsCluster> extremePoints = breakDown();
        ArrayList<ColorsCluster> finalList = new ArrayList<>();
        ArrayList<ColorsCluster> remainingList = extremePoints;

        while (remainingList.size() > 1) {
            ColorsCluster initialCluster = remainingList.get(0);
            double minDistance = initialCluster.getDistanceToMean(remainingList.get(1).getMeanColor());
            int minIndex = 1;
            for (int i = 2; i < remainingList.size(); i++) {
                double distanceToMean = remainingList.get(i).getDistanceToMean(initialCluster.getMeanColor());
                if (distanceToMean < minDistance) {
                    minDistance = distanceToMean;
                    minIndex = i;
                }
            }
            initialCluster.merge(remainingList.get(minIndex));
            finalList.add(initialCluster);
            remainingList.remove(minIndex);
            remainingList.remove(0);
        }
        if (remainingList.size() > 0) {
            ColorsCluster remainingCluster = remainingList.get(0);
            if (finalList.size() > 1) {
                double minDistance = remainingCluster.getDistanceToMean(finalList.get(0).getMeanColor());
                int minIndex = 0;
                for (int i = 1; i < finalList.size(); i++) {
                    double distance = remainingCluster.getDistanceToMean(finalList.get(i).getMeanColor());
                    if (minDistance > distance) {
                        minIndex = i;
                        minDistance = distance;
                    }
                }
                finalList.get(minIndex).merge(remainingCluster);
            } else {
                finalList.add(remainingCluster);
            }
        }

        return finalList;
    }

    private void merge(ColorsCluster colorsCluster) {
        for (ColoredPixel pixel : colorsCluster.pixels) {
            addPixel(pixel);
        }
    }

    public ArrayList<ColorsCluster> breakDown() {
        ArrayList<RgbColor> extremes = getExtremePixelColors();
        ArrayList<ColorsCluster> output = new ArrayList<>();
        for (int i = 0; i < extremes.size(); i++) {
            output.add(new ColorsCluster());
        }
        for (ColoredPixel coloredPixel : pixels) {
            int nearestExtremeIndex = getNearestIndex(extremes, coloredPixel.getColor());
            output.get(nearestExtremeIndex).addPixel(coloredPixel);
        }
        return output;
    }

    private ArrayList<RgbColor> getExtremePixelColors() {
        RgbColor minRed = pixels.get(0).getColor();
        RgbColor maxRed = pixels.get(0).getColor();
        RgbColor minBlue = pixels.get(0).getColor();
        RgbColor maxBlue = pixels.get(0).getColor();
        RgbColor minGreen = pixels.get(0).getColor();
        RgbColor maxGreen = pixels.get(0).getColor();
        for (ColoredPixel pixel : pixels) {
            if (pixel.getColor().getRed() < minRed.getRed()) {
                minRed = pixel.getColor();
            }
            if (pixel.getColor().getRed() > maxRed.getRed()) {
                maxRed = pixel.getColor();
            }
            if (pixel.getColor().getGreen() < minGreen.getGreen()) {
                minGreen = pixel.getColor();
            }
            if (pixel.getColor().getGreen() > maxGreen.getGreen()) {
                maxGreen = pixel.getColor();
            }
            if (pixel.getColor().getBlue() < minBlue.getBlue()) {
                minBlue = pixel.getColor();
            }
            if (pixel.getColor().getBlue() > maxBlue.getBlue()) {
                maxBlue = pixel.getColor();
            }
        }
        HashMap<String, RgbColor> output = new HashMap<>();
        addColor(output, minRed);
        addColor(output, maxRed);
        addColor(output, minGreen);
        addColor(output, maxGreen);
        addColor(output, minBlue);
        addColor(output, maxBlue);
        return new ArrayList<>(output.values());
    }

    private static void addColor(HashMap<String, RgbColor> map, RgbColor color) {
        map.put("" + color.getRed() + ";" + color.getGreen() + ";" + color.getBlue(), color);
    }

    private static int getNearestIndex(ArrayList<RgbColor> colors, RgbColor color) {
        int minIndex = 0;
        double minDistance = colors.get(0).getDistance(color);
        for (int i = 1; i < colors.size(); i++) {
            double distance = colors.get(i).getDistance(color);
            if (minDistance > distance) {
                minDistance = distance;
                minIndex = i;
            }
        }
        return minIndex;
    }

    @Override
    public String toString() {
        return "<cluster size:'" + pixels.size() + "'>\n" + getMeanColor().toString() + "\n</cluster>";
    }

    @Override
    public int compareTo(ColorsCluster o) {
        return Integer.valueOf(o.getPixels().size()).compareTo(Integer.valueOf(pixels.size()));
    }
}
