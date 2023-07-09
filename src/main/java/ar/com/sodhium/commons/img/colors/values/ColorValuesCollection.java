package ar.com.sodhium.commons.img.colors.values;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Roberto G. Fernandez
 * 
 */
public class ColorValuesCollection implements Collection<Integer> {
    private int[] color;

    public ColorValuesCollection(int[] color) {
        super();
        this.color = color;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#size()
     */
    public int size() {
        if (color != null)
            return color.length;
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#iterator()
     */
    public Iterator<Integer> iterator() {
        return new ColorValuesIterator(color);
    }

    @Override
    public boolean isEmpty() {
        return color == null || color.length == 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Object[] toArray() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(Integer e) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
    }

}
