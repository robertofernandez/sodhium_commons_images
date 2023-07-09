package ar.com.sodhium.commons.img.areas;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import ar.com.sodhium.commons.img.colors.RgbColor;

public class ColoredAreasSetTest {

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
    public void test() {
        ColoredAreasSet set = new ColoredAreasSet();
        RgbColor color = new RgbColor(10, 100, 200);
        ColoredArea area1 = new ColoredArea(0, 0, 480, 320, color);
        set.addColoredArea(area1);

        Gson gson = new Gson();
        String json = gson.toJson(set);

        System.out.println(json);

        ColoredAreasSet setFromJson = gson.fromJson(json, ColoredAreasSet.class);

        System.out.println(setFromJson);

    }

}
