package ar.com.sodhium.commons.img.colors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MagnitudeUtilsTest {

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
    public void testGetAbsouluteMagnitude() {
        double absouluteMagnitude0 = MagnitudeUtils.getAbsouluteMagnitude(0, 0, 0);
        double absouluteMagnitude255 = MagnitudeUtils.getAbsouluteMagnitude(255, 255, 255);
        double absouluteMagnitudeRed1 = MagnitudeUtils.getAbsouluteMagnitude(190, 55, 55);

        System.out.println("black: " + absouluteMagnitude0 + "; white: " + absouluteMagnitude255 + "; red: "
                + absouluteMagnitudeRed1);
    }

    @Test
    public void testGetNormalizedMagnitude() {
        double absouluteMagnitude0 = MagnitudeUtils.getNormalizedMagnitude(0, 0, 0);
        double absouluteMagnitude255 = MagnitudeUtils.getNormalizedMagnitude(255, 255, 255);
        double absouluteMagnitudeRed1 = MagnitudeUtils.getNormalizedMagnitude(190, 55, 55);

        System.out.println("black: " + absouluteMagnitude0 + "; white: " + absouluteMagnitude255 + "; red: "
                + absouluteMagnitudeRed1);
    }

}
