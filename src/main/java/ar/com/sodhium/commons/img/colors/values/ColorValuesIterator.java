package ar.com.sodhium.commons.img.colors.values;

import java.util.Iterator;

/**
 * @author Roberto G. Fernandez
 *
 */
public class ColorValuesIterator implements Iterator<Integer> {

    private int[] colorValues;
    int index;

    public ColorValuesIterator(int[] colorValues) {
        super();
        index = 0;
        this.colorValues = colorValues;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return index < colorValues.length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#next()
     */
    public Integer next() {
        return Integer.valueOf(colorValues[index++]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove() {

    }

    /**
     * @param args
     */
    public static void main(String[] args) {

    }

}
