package ar.com.sodhium.commons.img.areas;

public class FrameArea implements IntegerFigureArea {
    private IntegerRectangularZone innerZone;
    private IntegerRectangularZone outerZone;

    public FrameArea(IntegerRectangularZone innerZone, IntegerRectangularZone outerZone) {
        this.innerZone = innerZone;
        this.outerZone = outerZone;
    }
    @Override
    public boolean contains(int x, int y) {
        return MathUtils.contains(outerZone, x, y) && ! MathUtils.contains(innerZone, x, y);
    }

}
