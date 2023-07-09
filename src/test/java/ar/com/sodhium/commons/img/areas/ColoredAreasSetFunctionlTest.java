package ar.com.sodhium.commons.img.areas;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ar.com.sodhium.commons.img.ImageRepresentation;
import ar.com.sodhium.commons.img.colors.map.ColorMap;
import ar.com.sodhium.commons.img.operations.GeometryUtils;
import ar.com.sodhium.commons.img.operations.areas.AreasAmountLimitReached;

public class ColoredAreasSetFunctionlTest {

    private ArrayList<ColoredArea> emptyCompanyAreas;
    private ArrayList<ColoredArea> emptyPersonAreas;
    private Double percentageimportance;
    private int companyLogoWidth;
    private int companyLogoHeight;
    private int personPictureWidth;
    private int personPictureHeight;
    private ColoredAreasSet emptyCompanySet;
    private ColoredAreasSet emptyPersonSet;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        percentageimportance = 1D;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Ignore
    @Test
    public void test() throws Exception {
        ArrayList<String> emptyCompanies = new ArrayList<>();
        ArrayList<String> emptyPersons = new ArrayList<>();
        ArrayList<String> notEmpty = new ArrayList<String>();

        File trainingFolder = new File("src/test/resources/profile_pics/training/");
        File testingFolder = new File("src/test/resources/profile_pics/testing/");

        for (File file : trainingFolder.listFiles()) {
            if (file.getName().contains("empty_company")) {
                ColorMap emptyCompanyMap = getMap(file);
                companyLogoWidth = emptyCompanyMap.getWidth();
                companyLogoHeight = emptyCompanyMap.getHeight();
                emptyCompanyAreas = GeometryUtils.getMaximumRectangularAreas(emptyCompanyMap, 0, 0, 50D, 200);
                emptyCompanySet = buildSet(emptyCompanyAreas, companyLogoWidth, companyLogoHeight);
            } else if (file.getName().contains("empty_person")) {
                ColorMap emptyPersonMap = getMap(file);
                emptyPersonAreas = GeometryUtils.getMaximumRectangularAreas(emptyPersonMap, 0, 0, 50D, 200);
                personPictureWidth = emptyPersonMap.getWidth();
                personPictureHeight = emptyPersonMap.getHeight();
                emptyPersonSet = buildSet(emptyPersonAreas, personPictureWidth, personPictureHeight);
            }
        }

        for (File file : testingFolder.listFiles()) {
            try {
                ColorMap fileMap = getMap(file);
                ArrayList<ColoredArea> areas = GeometryUtils.getMaximumRectangularAreas(fileMap, 0, 0, 50D, 200);
                ColoredAreasSet set = buildSet(areas, fileMap.getWidth(), fileMap.getHeight());
                set.scaleTo(companyLogoWidth, companyLogoHeight);
                boolean matchesEmptyCompany = emptyCompanySet.matchesColors(set, 20, 3);
                set.scaleTo(personPictureWidth, personPictureHeight);
                boolean matchesEmptyPerson = emptyPersonSet.matchesColors(set, 20, 3);
                if (matchesEmptyCompany) {
                    emptyCompanies.add(file.getName());
                }
                if (matchesEmptyPerson) {
                    emptyPersons.add(file.getName());
                }
                if (!matchesEmptyCompany && !matchesEmptyPerson) {
                    notEmpty.add(file.getName());
                }
            } catch (AreasAmountLimitReached e1) {
                notEmpty.add(file.getName());
            } catch (Exception e) {
                System.out.println("Could not analyse " + file.getName());
            }
        }

        String pageCode = getPageForResults(emptyCompanies, emptyPersons, notEmpty);
        System.out.println(pageCode);

        System.out.println("---------------------");
        System.out.println("Empty persons pictures");
        System.out.println("---------------------");
        for (String personFileName : emptyPersons) {
            String[] splitedName = personFileName.split("_");
            String numberAndExtension = splitedName[splitedName.length-1];
            String number = numberAndExtension.substring(0, numberAndExtension.length() - 4);
            System.out.println(number);
        }

        System.out.println("---------------------");
        System.out.println("Empty companies pictures");
        System.out.println("---------------------");
        for (String personFileName : emptyCompanies) {
            String[] splitedName = personFileName.split("_");
            String numberAndExtension = splitedName[splitedName.length-1];
            String number = numberAndExtension.substring(0, numberAndExtension.length() - 4);
            System.out.println(number);
        }


    }

    private ColorMap getMap(File file) throws IOException, Exception {
        BufferedImage pictureImage = ImageIO.read(file);
        if (pictureImage == null) {
            System.out.println("image " + file.getName() + " discarded (null).");
            throw new Exception("can not read image");
        }
        ImageRepresentation representation = new ImageRepresentation(pictureImage);
        ColorMap map = new ColorMap(representation.getImageWidth(), representation.getImageHeight());
        map.setRed(representation.getRed());
        map.setGreen(representation.getGreen());
        map.setBlue(representation.getBlue());

        return map;
    }

    public String getPageForResults(ArrayList<String> emptyCompanies, ArrayList<String> emptyPersons,
            ArrayList<String> notEmpty) {
        String output = getPageIntro();
        output += getSectionIntro("Empty companies");
        output += getPhotosCode(emptyCompanies);
        output += getSectionEnding();

        output += getSectionIntro("Empty persons");
        output += getPhotosCode(emptyPersons);
        output += getSectionEnding();

        output += getSectionIntro("Not empty");
        output += getPhotosCode(notEmpty);
        output += getSectionEnding();

        output += getPageEnding();

        return output;
    }

    private String getPhotosCode(ArrayList<String> elements) {
        String output = "";
        for (String element : elements) {
            output += "  <div>\n";
            output += "    <img class='grid-item' src='testing/" + element + "' alt=''>\n";
            output += "    <figcaption>" + element + "</figcaption>\n";
            output += "  </div>\n";
        }
        return output;
    }

    public String getPageIntro() {
        String output = "";
        output += "<!DOCTYPE html>\n";
        output += "<html>\n";
        output += "<head>\n";
        output += "  <title>Pictures detection tests results</title>\n";
        output += "  <style>\n";
        output += "    .gallery {\n";
        output += "      display: flex;\n";
        output += "      flex-wrap: wrap;\n";
        output += "      justify-content: center;\n";
        output += "    }\n";
        output += "    \n";
        output += "    .gallery img {\n";
        output += "      max-width: 200px;\n";
        output += "      height: auto;\n";
        output += "      margin: 10px;\n";
        output += "    }\n";
        output += "  \n";
        output += "    figcaption {\n";
        output += "      font-family: 'Arial', sans-serif;\n";
        output += "      font-size: 12px;\n";
        output += "      color: #999999;\n";
        output += "      text-align: center;\n";
        output += "      margin-top: 10px;\n";
        output += "    }\n";
        output += "  \n";
        output += "    h1 {\n";
        output += "      font-family: 'Arial', sans-serif;\n";
        output += "      font-size: 28px;\n";
        output += "      font-weight: bold;\n";
        output += "      color: #333333;\n";
        output += "      text-align: center;\n";
        output += "      text-transform: uppercase;\n";
        output += "      letter-spacing: 2px;\n";
        output += "      margin-bottom: 20px;\n";
        output += "    }\n";
        output += "\n";
        output += "    h2 {\n";
        output += "      font-family: 'Arial', sans-serif;\n";
        output += "      font-size: 24px;\n";
        output += "      font-weight: bold;\n";
        output += "      color: #666666;\n";
        output += "      text-align: center;\n";
        output += "      margin-bottom: 10px;\n";
        output += "    }\n";
        output += "  </style>\n";
        output += "</head>\n";
        output += "<body>\n";
        output += "\n";
        output += "<h1>Pictures detection tests results</h1>\n";

        return output;
    }

    public String getSectionIntro(String title) {
        String output = "";
        output += "<h2>" + title + "</h2>\n";
        output += "\n";
        output += "<div class=\"gallery\">\n";
        return output;
    }

    public String getSectionEnding() {
        String output = "";
        output += "</div>\n";
        return output;
    }

    public String getPageEnding() {
        String output = "";
        output += "</body>\n";
        output += "</html>\n";
        return output;
    }

    public ColoredAreasSet buildSet(ArrayList<ColoredArea> rectangularAreas, int width, int height) {
        RectangularImageArea totalArea = new RectangularImageArea(0, 0, width, height);
        ColoredAreasSet set = new ColoredAreasSet();

        for (ColoredArea rectangle : rectangularAreas) {
            if (100 * (rectangle.getArea() / totalArea.getArea()) < percentageimportance) {
                continue;
            }
            set.addColoredArea(rectangle);
        }
        set.mergeOverlapped(95);

        return set;
    }

}
