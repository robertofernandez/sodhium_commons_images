package ar.com.sodhium.commons.img.pixels;

public class PixelsSegment {
    private int startX;
    private int endX;
    private int y;

    public PixelsSegment(int startX, int endX, int y) {
        super();
        this.startX = startX;
        this.endX = endX;
        this.y = y;
    }

    public int getStartX() {
        return startX;
    }

    public int getEndX() {
        return endX;
    }

    public int getY() {
        return y;
    }

    public boolean adjacent(Integer x) {
        return startX == x + 1 || endX == x - 1;
    }

    public boolean adjacent(PixelsSegment segment) {
        return endX == segment.startX - 1 || startX == segment.endX + 1;
    }

    public boolean contains(Integer x) {
        return startX <= x && endX >= x;
    }

    public void addPixel(Integer x) {
        if (startX == x + 1) {
            startX = x;
        } else if (endX == x - 1) {
            endX = x;
        } else {
            throw new IllegalArgumentException("Pixel to add should be adjacent");
        }
    }

    public boolean isConnectedWith(PixelsSegment segment) {
        return (Math.abs(segment.y - y) == 1) && (contains(segment.startX) || contains(segment.endX));
    }

    public void addSegment(PixelsSegment segment) {
        startX = Math.min(startX, segment.startX);
        endX = Math.max(endX, segment.endX);
    }

}
