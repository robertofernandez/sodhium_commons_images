package ar.com.sodhium.commons.img.areas;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ar.com.sodhium.commons.img.operations.GeometryUtils;

public class ColoredAreasSet {

    @SerializedName("areas")
    @Expose
    HashMap<String, ColoredArea> areas;

    public ColoredAreasSet() {
        areas = new HashMap<>();
    }

    public void addColoredArea(ColoredArea area) {
        areas.put(area.toString(), area);
    }

    public HashMap<String, ColoredArea> getAreas() {
        return areas;
    }

    public void mergeOverlapped(double overlapPercentageLimit) {
        HashMap<String, ColoredArea> finalAreas = new HashMap<>();
        HashMap<String, ColoredArea> unProcessedAreas = new HashMap<>();
        unProcessedAreas.putAll(areas);
        ArrayList<String> allKeys = new ArrayList<>();
        allKeys.addAll(areas.keySet());

        for (String key : allKeys) {
            boolean merges = true;
            if (!unProcessedAreas.containsKey(key)) {
                continue;
            }
            ColoredArea area = unProcessedAreas.get(key);
            unProcessedAreas.remove(key);
            while (merges) {
                merges = false;
                ArrayList<String> unprocessedKeys = new ArrayList<>();
                unprocessedKeys.addAll(unProcessedAreas.keySet());
                for (String unprocessedKey : unprocessedKeys) {
                    if (!unProcessedAreas.containsKey(unprocessedKey)) {
                        continue;
                    }
                    ColoredArea unprocessedArea = unProcessedAreas.get(unprocessedKey);
                    if (GeometryUtils.getRelativeOverlappingAreaPercentage(area,
                            unprocessedArea) > overlapPercentageLimit) {
                        merges = true;
                        area = GeometryUtils.mergeColoredAreaWithMaxBoundaries(area, unprocessedArea);
                        unProcessedAreas.remove(unprocessedKey);
                    }
                }
            }
            finalAreas.put(area.toString(), area);
        }
        areas = finalAreas;
    }

    @Override
    public String toString() {
        return areas.toString();
    }

    public void moveTo(int x, int y) {
        Integer minX = null;
        Integer minY = null;
        for (ColoredArea area : areas.values()) {
            if (minX == null || minX.intValue() > area.getX()) {
                minX = area.getX();
            }
            if (minY == null || minY.intValue() > area.getY()) {
                minY = area.getY();
            }
        }
        if (minX != null) {
            int offsetX = x - minX;
            int offsetY = y - minY;
            for (ColoredArea area : areas.values()) {
                area.move(offsetX, offsetY);
            }
        }
        remapAreas();
    }

    private void remapAreas() {
        HashMap<String, ColoredArea> finalAreas = new HashMap<>();
        for (ColoredArea area : areas.values()) {
            finalAreas.put(area.toString(), area);
        }
        areas = finalAreas;
    }

    // FUTURE apply for non colored sets
    public void scaleTo(double width, double heigt) {
        Integer minX = null;
        Integer minY = null;
        Integer maxX = null;
        Integer maxY = null;

        for (ColoredArea area : areas.values()) {
            if (minX == null || minX.intValue() > area.getX()) {
                minX = area.getX();
            }
            if (minY == null || minY.intValue() > area.getY()) {
                minY = area.getY();
            }
            if (maxX == null || maxX.intValue() < area.getMaxX()) {
                maxX = area.getMaxX();
            }
            if (maxY == null || maxY.intValue() < area.getMaxY()) {
                maxY = area.getMaxY();
            }
        }

        if (minX != null) {
            Double currentWidth = (double) (maxX - minX);
            Double currentHeight = (double) (maxY - minY);
            if (currentHeight == 0 || currentWidth == 0) {
                return;
            }
            Double multiplierX = width / currentWidth;
            Double multiplierY = heigt / currentHeight;
            moveTo(0, 0);
            HashMap<String, ColoredArea> finalAreas = new HashMap<>();
            for (ColoredArea area : areas.values()) {
                ColoredArea newArea = new ColoredArea((int) (area.getX() * multiplierX),
                        (int) (area.getY() * multiplierY), (int) (area.getWidth() * multiplierX),
                        (int) (area.getHeight() * multiplierY), area.getColor());
                finalAreas.put(newArea.toString(), newArea);
            }
            areas = finalAreas;
            moveTo(minX, minY);
        }
    }

    public boolean matchesColors(ColoredAreasSet anotherSet, double colorTolerance, double overlappingTolerance) {
        for (ColoredArea area : areas.values()) {
            for (ColoredArea anotherArea : anotherSet.getAreas().values()) {
                if (GeometryUtils.getOverlappingAreaPercentage(area, anotherArea) > overlappingTolerance) {
                    if (area.getColor().getAbsoluteDistance(anotherArea.getColor()) > colorTolerance) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
