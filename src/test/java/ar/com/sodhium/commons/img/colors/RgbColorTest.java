package ar.com.sodhium.commons.img.colors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RgbColorTest {

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
    public void testGetDistance() {
        RgbColor color1 = new RgbColor(6, 238, 58);
        RgbColor color2 = new RgbColor(2, 241, 54);
        RgbColor color3 = new RgbColor(68, 228, 104);
        RgbColor color4 = new RgbColor(247, 233, 254);
        System.out.println(color1.getDistance(color2));
        System.out.println(color1.getDistance(color3));
        System.out.println(color1.getDistance(color4));
    }

    @Test
    public void testGetHslDistance() {
        RgbColor original = new RgbColor(51, 102, 0);
        RgbColor afterEffect = new RgbColor(22, 103, 16);
        RgbColor difference = original.getDifference(afterEffect);
        System.out.println("Difference: " + difference);
        HslColor hsl = difference.toHslColor();
        System.out.println("Difference hsl: " + hsl);
        HslColor originalHsl = original.toHslColor();
        System.out.println("original hsl: " + originalHsl);
        HslColor afterEffectHsl = afterEffect.toHslColor();
        System.out.println("after effect hsl: " + afterEffectHsl);
        System.out.println("Difference hsl: " + originalHsl.getDifference(afterEffectHsl));
    }

}
