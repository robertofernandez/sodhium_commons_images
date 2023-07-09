package ar.com.sodhium.commons.img.areas;

import java.util.ArrayList;
import java.util.HashMap;

import ar.com.sodhium.commons.img.statistics.ImageStatistics;

public class RectangularZonesBlock implements IntegerRectangularZone {
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int width;
    private int height;
    private ArrayList<RectangularImageZone> components;
    private HashMap<String, ImageStatistics<?>> statistics;
    private RectangularImageZone parent;

    public RectangularZonesBlock() {
        components = new ArrayList<>();
        statistics = new HashMap<>();
    }

    public RectangularZonesBlock(RectangularImageZone rectangle) {
        x = rectangle.getX();
        y = rectangle.getY();
        width = rectangle.getWidth();
        height = rectangle.getHeight();
        dx = x + width;
        dy = y + height;
        components = new ArrayList<>();
        components.add(rectangle);
        statistics = new HashMap<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean overlapsY(RectangularImageZone rectangle) {
        for (RectangularImageZone component : components) {
            if (component.overlapsY(rectangle)) {
                return true;
            }
        }
        return false;
    }

    public boolean overlaps(IntegerRectangularZone rectangle) {
        return MathUtils.overlaps(this, rectangle);
    }

    public boolean componentsOverlaps(IntegerRectangularZone rectangle) {
        for (RectangularImageZone component : components) {
            if (MathUtils.overlaps(component, rectangle)) {
                return true;
            }
        }
        return false;
    }

    public void addComponent(RectangularImageZone rectangle) {
        components.add(rectangle);
        updateMeasures(rectangle);
    }

    private void updateMeasures(RectangularImageZone rectangle) {
        x = Math.min(x, rectangle.getX());
        y = Math.min(y, rectangle.getY());
        dx = Math.max(dx, rectangle.getX() + rectangle.getWidth());
        dy = Math.max(dy, rectangle.getY() + rectangle.getHeight());
        width = dx - x;
        height = dy - y;
    }

    public RectangularZonesBlock proposeAdd(RectangularImageZone rectangle) {
        RectangularZonesBlock output = new RectangularZonesBlock();
        output.setMeasures(this);
        output.updateMeasures(rectangle);
        return output;
    }

    private void setMeasures(IntegerRectangularZone rectangle) {
        x = rectangle.getX();
        y = rectangle.getY();
        dx = rectangle.getX() + rectangle.getWidth();
        dy = rectangle.getY() + rectangle.getHeight();
        width = dx - x;
        height = dy - y;
    }

    public ArrayList<RectangularImageZone> getComponents() {
        return components;
    }

    public int getMaxX() {
        return x + width;
    }

    public int getMaxY() {
        return y + height;
    }

    public RectangularImageZone getParent() {
        return parent;
    }

    public void setParent(RectangularImageZone parent) {
        this.parent = parent;
    }

    public HashMap<String, ImageStatistics<?>> getStatistics() {
        return statistics;
    }

    @Override
    public String toString() {
        return "<" + x + ", " + y + "><" + getMaxX() + ", " + getMaxY() + ">";
    }

    public String getId() {
        return "RZB:" + x + "_" + y + width + "_" + height;
    }

    public void merge(RectangularZonesBlock anotherCluster) {
        for (RectangularImageZone zone : anotherCluster.getComponents()) {
            addComponent(zone);
        }
    }

}
