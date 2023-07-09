package ar.com.sodhium.commons.img.colors;

import static junit.framework.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

public class ColorsUtilsTest {

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
    public void testGetHex() {
        System.out.println(ColorsUtils.getHex(new RgbColor(0, 0, 0)));
        System.out.println(ColorsUtils.getHex(new RgbColor(255, 255, 255)));
        System.out.println(ColorsUtils.getHex(new RgbColor(0, 230, 51)));
    }

    @Test
    public void shouldTrueToSimilarGreens() {
        double tolerance = 20;
        assertTrue(seemsColors(0, 242, 56, 0, 242, 58, tolerance));
        assertTrue(seemsColors(0, 241, 65, 0, 242, 58, tolerance));
        assertTrue(seemsColors(0, 241, 65, 0, 242, 56, tolerance));
    }

    @Test
    public void shouldFalseToGreensAndGray() {
        double tolerance = 20;
        Assert.assertFalse(seemsColors(0, 238, 0, 240, 240, 240, tolerance));
        Assert.assertFalse(seemsColors(240, 240, 240, 0, 242, 58, tolerance));
    }

    @Test
    public void shouldAproximate() {
        RgbColor color = new RgbColor(100, 120, 125);
        RgbColor color2 = new RgbColor(99, 121, 135);
        RgbColor meanColor = ColorsUtils.getMeanColor(color, color2);
        Assert.assertEquals(99, meanColor.getRed());
        Assert.assertEquals(120, meanColor.getGreen());
        Assert.assertEquals(130, meanColor.getBlue());
    }

    private boolean seemsColors(int red1, int green1, int blue1, int red2, int green2, int blue2, double tolerance) {
        RgbColor oneColor = new RgbColor(red1, green1, blue1);
        RgbColor anotherColor = new RgbColor(red2, green2, blue2);
        return ColorsUtils.seemsColor(oneColor, anotherColor, tolerance);
    }

}
