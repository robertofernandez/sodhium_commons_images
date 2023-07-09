package ar.com.sodhium.commons.img.areas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import ar.com.sodhium.commons.img.statistics.ImageStatistics;

//TODO re-use from TextButtonAreasCluster
public class RectangularImageZonesCluster implements IntegerRectangularZone {
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int width;
    private int height;
    private int maxHeight;
    private double averageHeight;
    private HashMap<String, RectangularImageZone> zones;
    private HashMap<String, ImageStatistics<?>> statistics;

    public RectangularImageZonesCluster() {
        maxHeight = 0;
        averageHeight = 0;
        zones = new HashMap<>();
        statistics = new HashMap<>();
    }

    public boolean contains(RectangularImageZonesCluster anotherRectangle) {
        return MathUtils.contains(this, anotherRectangle);
    }

    public boolean containsInclusive(RectangularImageZonesCluster anotherRectangle) {
        return MathUtils.containsInclusive(this, anotherRectangle);
    }

    public RectangularImageZonesCluster(RectangularImageZone zone) {
        initialize(zone);
    }

    private void initialize(RectangularImageZone zone) {
        statistics = new HashMap<>();
        maxHeight = 0;
        averageHeight = 0;
        zones = new HashMap<>();
        zones.put(zone.getId(), zone);
        setMeasures(zone);
    }

    public void addZone(RectangularImageZone zone) {
        if (zones.size() == 0) {
            initialize(zone);
        } else {
            zones.put(zone.getId(), zone);
            updateMeasures(zone);
        }
    }

    public Collection<RectangularImageZone> getZones() {
        return zones.values();
    }

    public RectangularImageZonesCluster merge(RectangularImageZonesCluster targetCluster) {
        for (RectangularImageZone targetZone : targetCluster.getZones()) {
            addZone(targetZone);
        }
        return this;
    }

    public int distance(RectangularImageZonesCluster anotherCluster) {
        Integer distance = null;
        for (RectangularImageZone currentZone : anotherCluster.zones.values()) {
            Integer currentDistance = distance(currentZone);
            if (distance == null || currentDistance < distance) {
                distance = currentDistance;
            }
        }
        return distance;
    }

    public int distance(RectangularImageZone zone) {
        Integer distance = null;
        for (RectangularImageZone currentZone : zones.values()) {
            Integer currentDistance = currentZone.distance(zone);
            if (distance == null || currentDistance < distance) {
                distance = currentDistance;
            }
        }
        return distance;
    }

    public int verticalDistance(RectangularImageZone zone) {
        Integer distance = null;
        for (RectangularImageZone currentZone : zones.values()) {
            Integer currentDistance = currentZone.verticalDistance(zone);
            if (distance == null || currentDistance < distance) {
                distance = currentDistance;
            }
        }
        return distance;
    }

    public int maxVerticalDistance(RectangularImageZone zone) {
        Integer distance = null;
        for (RectangularImageZone currentZone : zones.values()) {
            Integer currentDistance = currentZone.verticalDistance(zone);
            if (distance == null || currentDistance > distance) {
                distance = currentDistance;
            }
        }
        return distance;
    }

    public int verticalDistance(RectangularImageZonesCluster anotherCluster) {
        Integer distance = null;
        for (RectangularImageZone currentZone : anotherCluster.zones.values()) {
            Integer currentDistance = verticalDistance(currentZone);
            if (distance == null || currentDistance < distance) {
                distance = currentDistance;
            }
        }
        return distance;
    }

    public int maxVerticalDistance(RectangularImageZonesCluster anotherCluster) {
        Integer distance = null;
        for (RectangularImageZone currentZone : anotherCluster.zones.values()) {
            Integer currentDistance = verticalDistance(currentZone);
            if (distance == null || currentDistance > distance) {
                distance = currentDistance;
            }
        }
        return distance;
    }

    public int horizontalDistance(RectangularImageZone zone) {
        Integer distance = null;
        for (RectangularImageZone currentZone : zones.values()) {
            Integer currentDistance = currentZone.horizontalDistance(zone);
            if (distance == null || currentDistance < distance) {
                distance = currentDistance;
            }
        }
        return distance;
    }

    public int horizontalDistance(RectangularImageZonesCluster anotherCluster) {
        Integer distance = null;
        for (RectangularImageZone currentZone : anotherCluster.zones.values()) {
            Integer currentDistance = horizontalDistance(currentZone);
            if (distance == null || currentDistance < distance) {
                distance = currentDistance;
            }
        }
        return distance;
    }

    private void updateMeasures(RectangularImageZone zone) {
        x = Math.min(x, zone.getX());
        y = Math.min(y, zone.getY());
        dx = Math.max(dx, zone.getX() + zone.getWidth());
        dy = Math.max(dy, zone.getY() + zone.getHeight());
        width = dx - x;
        height = dy - y;
        maxHeight = Math.max(maxHeight, zone.getHeight());
        if (zones.size() > 0) {
            int totalHeight = 0;
            for (RectangularImageZone zone2 : zones.values()) {
                totalHeight += zone2.getHeight();
            }
            averageHeight = totalHeight / zones.size();
        }
    }

    private void setMeasures(RectangularImageZone zone) {
        x = zone.getX();
        y = zone.getY();
        dx = zone.getX() + zone.getWidth();
        dy = zone.getY() + zone.getHeight();
        width = dx - x;
        height = dy - y;
        maxHeight = zone.getHeight();
        averageHeight = maxHeight;
    }

    @Override
    public String toString() {
        return "<" + x + ", " + y + ">" + zones.size() + "<" + dx + ", " + dy + ">";
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public double getAverageHeight() {
        return averageHeight;
    }

    public HashMap<String, ImageStatistics<?>> getAverageStatistics() {
        HashMap<String, ArrayList<ImageStatistics<?>>> allStatistics = new HashMap<>();
        if (zones.size() < 1) {
            return new HashMap<>();
        }
        RectangularImageZone referenceZone = zones.values().iterator().next();
        for (RectangularImageZone zone : zones.values()) {
            for (Entry<String, ImageStatistics<?>> entry : zone.getStatistics().entrySet()) {
                if (!allStatistics.containsKey(entry.getKey())) {
                    allStatistics.put(entry.getKey(), new ArrayList<>());
                }
                allStatistics.get(entry.getKey()).add(entry.getValue());
            }
        }
        HashMap<String, ImageStatistics<?>> averageStatistics = new HashMap<>();
        for (Entry<String, ImageStatistics<?>> entry : referenceZone.getStatistics().entrySet()) {
            Collection<ImageStatistics<?>> inputs = allStatistics.get(entry.getKey());
            ImageStatistics<?> value = entry.getValue();
            ImageStatistics<?> average = value.genericAverage(inputs);
            averageStatistics.put(entry.getKey(), average);
        }
        return averageStatistics;
    }

    @Override
    public int getMaxX() {
        return dx;
    }

    @Override
    public int getMaxY() {
        return dy;
    }

    public HashMap<String, ImageStatistics<?>> getStatistics() {
        return statistics;
    }
    
    public int globalHorizontalDistance(RectangularImageZonesCluster anotherCluster) {
        int horizontalDistance = 0;
        if (x < anotherCluster.x) {
            horizontalDistance = anotherCluster.x - (x + width);
        } else {
            horizontalDistance = x - (anotherCluster.x + anotherCluster.width);
        }
        return horizontalDistance;
    }

    public int globalVerticalDistance(RectangularImageZonesCluster anotherCluster) {
        int verticalDistance = 0;
        if (y < anotherCluster.y) {
            verticalDistance = anotherCluster.y - (y + height);
        } else {
            verticalDistance = y - (anotherCluster.y + anotherCluster.height);
        }
        return verticalDistance;
    }

}
