package ar.com.sodhium.commons.img.colors.map;

import ar.com.sodhium.commons.indexes.Indexer;

public class GrayscaleMap {
    private int[] values;
    private int width;
    private int height;
    private Indexer index;

    public GrayscaleMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int[] getValues() {
        return values;
    }

    public void setValues(int[] values) throws Exception {
        this.values = values;
        index = new Indexer(values, width, height);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Indexer getIndex() {
        return index;
    }

    public void initializeEmpty() throws Exception {
        setValues(new int[width * height]);
    }
}