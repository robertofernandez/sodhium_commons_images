package ar.com.sodhium.commons.img.areas;

import java.util.ArrayList;

public class RectangularImageZonesClustersSet {

    private ArrayList<RectangularImageZonesCluster> clusters;

    public RectangularImageZonesClustersSet(ArrayList<RectangularImageZonesCluster> clusters) {
        this.clusters = clusters;
    }

    public RectangularImageZonesClustersSet() {
        this.clusters = new ArrayList<>();
    }

    public ArrayList<RectangularImageZonesCluster> getClusters() {
        return clusters;
    }

    public void setClusters(ArrayList<RectangularImageZonesCluster> clusters) {
        this.clusters = clusters;
    }
}
