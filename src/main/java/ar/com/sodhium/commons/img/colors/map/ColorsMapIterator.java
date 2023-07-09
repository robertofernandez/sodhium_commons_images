package ar.com.sodhium.commons.img.colors.map;

import java.util.Iterator;

import ar.com.sodhium.commons.img.colors.RgbColor;

public class ColorsMapIterator implements Iterator<RgbColor> {
    private ColorMap map;
    private int index;

    public ColorsMapIterator(ColorMap map) {
        this.map = map;
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < map.getGreen().length;
    }

    @Override
    public RgbColor next() {
        RgbColor output = new RgbColor(map.getRed()[index], map.getGreen()[index], map.getBlue()[index]);
        index++;
        return output;
    }

}
