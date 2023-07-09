package ar.com.sodhium.commons.img.pixels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import ar.com.sodhium.commons.img.colors.RgbColor;
import ar.com.sodhium.commons.img.colors.map.ColorMap;
import ar.com.sodhium.commons.img.operations.GeometryUtils;

public class ColorsClusteringUtils {
    public static Integer MAX_COLORS = 17;

    public static ArrayList<PixelsSegmentsCluster> getClusters(ColorMap map) {
        ArrayList<PixelsSegmentsCluster> output = new ArrayList<>();
        HashMap<String, String> usedPixels = new HashMap<>();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                String key = "" + x + ";" + y;
                if (!usedPixels.containsKey(key)) {
                    PixelsSegmentsCluster cluster = GeometryUtils.diagonalFloodFillCluster(map, x, y, usedPixels);
                    cluster.internalMerge();
                    output.add(cluster);
                }
            }
        }
        return output;
    }

    public static ArrayList<ColorCount> countColors(ColorMap map) {
        ArrayList<ColorCount> output = new ArrayList<>();
        HashMap<Long, ColorCount> tempColors = new HashMap<>();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                RgbColor rgbColor = map.getRgbColor(x, y);
                Long key = ColorCount.getLongValue(rgbColor);
                if (!tempColors.containsKey(key)) {
                    tempColors.put(key, new ColorCount(rgbColor));
                } else {
                    tempColors.get(key).incrementAmount();
                }
            }
        }
        output.addAll(tempColors.values());
        Collections.sort(output);
        return output;
    }

    public static ArrayList<ColorMap> replaceColors(ColorMap map) throws Exception {
        ArrayList<ColorCount> allColors = countColors(map);
        ArrayList<ColorMap> output = new ArrayList<>();
        int amount = Math.min(allColors.size(), MAX_COLORS);
        for (int i = 0; i < amount; i++) {
            output.add(replaceAllColors(map, i, allColors, amount));
        }
        return output;
    }

    private static ColorMap replaceAllColors(ColorMap map, int startIndex, ArrayList<ColorCount> allColors, int amount)
            throws Exception {
        ColorMap output = map.cloneMap();
        for (int i = startIndex + 1; i < amount; i++) {
            output.replaceColor(allColors.get(i).getColor(), allColors.get(startIndex).getColor());
        }
        return output;
    }
}
