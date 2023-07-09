package ar.com.sodhium.commons.img.areas;

import java.util.Collection;

public class IntegerRectanglesSet {
    private Collection<RectangularImageZone> rectangles;
    private RectangularImageZone globalParent;

    public IntegerRectanglesSet(Collection<RectangularImageZone> rectangles, RectangularImageZone globalParent) {
        super();
        this.rectangles = rectangles;
        this.globalParent = globalParent;
    }

    public boolean isEmpty() {
        return rectangles == null || rectangles.isEmpty();
    }

    public Collection<RectangularImageZone> getRectangles() {
        return rectangles;
    }

    public void setRectangles(Collection<RectangularImageZone> rectangles) {
        this.rectangles = rectangles;
    }

    public RectangularImageZone getGlobalParent() {
        return globalParent;
    }

    public void setGlobalParent(RectangularImageZone globalParent) {
        this.globalParent = globalParent;
    }

}
