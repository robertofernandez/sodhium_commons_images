package ar.com.sodhium.commons.img.operations;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ar.com.sodhium.commons.img.areas.RectangularImageArea;

public class GeometryUtilsTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testOverlappingArea() {
        RectangularImageArea area1 = new RectangularImageArea(20, 30, 40, 50);
        RectangularImageArea area2 = new RectangularImageArea(25, 25, 80, 20);
        RectangularImageArea overlappingArea = GeometryUtils.overlappingArea(area1, area2);
        double percentage = GeometryUtils.getOverlappingAreaPercentage(area1, area2);
        System.out.println("AO:" + overlappingArea + " -> " + percentage + "%");
        percentage = GeometryUtils.getRelativeOverlappingAreaPercentage(area1, area2);
        System.out.println("RO: " + overlappingArea + " -> " + percentage + "%");

        overlappingArea = GeometryUtils.overlappingArea(area2, area1);
        percentage = GeometryUtils.getOverlappingAreaPercentage(area2, area1);
        System.out.println("AO:" + overlappingArea + " -> " + percentage + "%");
        percentage = GeometryUtils.getRelativeOverlappingAreaPercentage(area1, area2);
        System.out.println("RO: " + overlappingArea + " -> " + percentage + "%");
    }


}
