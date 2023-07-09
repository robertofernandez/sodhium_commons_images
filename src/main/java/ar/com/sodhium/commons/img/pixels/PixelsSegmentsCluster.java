package ar.com.sodhium.commons.img.pixels;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

public class PixelsSegmentsCluster {
    private Color color;
    private HashMap<Integer, ArrayList<PixelsSegment>> segmentLists;

    public PixelsSegmentsCluster(Color color) {
        this.color = color;
        segmentLists = new HashMap<>();
    }

    public void addPixel(Integer x, Integer y) {
        if (!segmentLists.containsKey(y)) {
            segmentLists.put(y, new ArrayList<>());
        }
        ArrayList<PixelsSegment> segments = segmentLists.get(y);
        boolean found = false;
        for (PixelsSegment segment : segments) {
            if (segment.adjacent(x)) {
                found = true;
                segment.addPixel(x);
                break;
            } else if (segment.contains(x)) {
                found = true;
                break;
            }
        }
        if (!found) {
            segments.add(new PixelsSegment(x, x, y));
        }
    }

    public Color getColor() {
        return color;
    }

    public ArrayList<PixelsSegment> getAllSegments() {
        ArrayList<PixelsSegment> output = new ArrayList<>();
        for (ArrayList<PixelsSegment> list : segmentLists.values()) {
            output.addAll(list);
        }
        return output;
    }

    /**
     * 
     * @param segmentsOutput target where connected segments are added.
     * @return The remaining segments disconnected with the bunch added to passed segmentsOutput parameter.
     */
    public PixelsSegmentsCluster getConnectedSegments(ArrayList<PixelsSegment> segmentsOutput) {
        PixelsSegmentsCluster output = new PixelsSegmentsCluster(color);
        ArrayList<Integer> keys = new ArrayList<>();
        keys.addAll(segmentLists.keySet());
        Collections.sort(keys);
        PixelsSegment lastSegment = null;
        boolean connectionBroken = false;
        for (Integer key : keys) {
            PixelsSegment segment = null;
            for (PixelsSegment currentSegment : segmentLists.get(key)) {
                if (segment == null && !connectionBroken) {
                    segment = currentSegment;
                } else {
                    output.addSegment(currentSegment);
                }
            }
            if (!connectionBroken) {
                if (segment != null && (lastSegment == null || lastSegment.isConnectedWith(segment))) {
                    lastSegment = segment;
                    segmentsOutput.add(segment);
                } else {
                    connectionBroken = true;
                    output.addSegment(segment);
                }
            }
        }
        return output;
    }

    public void addSegment(PixelsSegment segment) {
        if (!segmentLists.containsKey(segment.getY())) {
            segmentLists.put(segment.getY(), new ArrayList<>());
        }
        segmentLists.get(segment.getY()).add(segment);
    }

    public HashMap<Integer, ArrayList<PixelsSegment>> getSegmentLists() {
        return segmentLists;
    }
    
    public void internalMerge() {
        for (Entry<Integer, ArrayList<PixelsSegment>> entry : segmentLists.entrySet()) {
            segmentLists.put(entry.getKey(), mergeSegments(entry.getValue()));
        }
    }

    private ArrayList<PixelsSegment> mergeSegments(ArrayList<PixelsSegment> input) {
        ArrayList<PixelsSegment> output = new ArrayList<>();
        Collections.sort(input, new Comparator<PixelsSegment>() {
            @Override
            public int compare(PixelsSegment o1, PixelsSegment o2) {
                return Integer.valueOf(o1.getStartX()).compareTo(Integer.valueOf(o2.getStartX()));
            }
        });
        PixelsSegment lastSegment = null;
        for (PixelsSegment segment : input) {
            if(lastSegment == null) {
                lastSegment = segment;
                output.add(segment);
            } else {
                if(lastSegment.adjacent(segment)) {
                    lastSegment.addSegment(segment);
                } else {
                    lastSegment = segment;
                    output.add(segment);
                }
            }
        }
        return output;
    }
}
