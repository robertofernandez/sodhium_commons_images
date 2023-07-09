package ar.com.sodhium.commons.img.ml;

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
import ar.com.sodhium.commons.img.areas.IntegerRectangularZone;
import ar.com.sodhium.commons.img.areas.RectangularImageArea;
import ar.com.sodhium.commons.img.colors.map.ColorMap;
import ar.com.sodhium.commons.ml.BasicNeuralNetwork;

@Ignore
public class BasicImageNeuralNetworkUtilsTest {

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
    public void testClasification() throws Exception {
        int sideSize = 3;
        int trainingInputSize = sideSize * sideSize;

        int hiddenLayerElements = 9;

        // binary?
        int outputSize = 1;

        BasicNeuralNetwork network = new BasicNeuralNetwork(trainingInputSize, hiddenLayerElements, outputSize);

        File negativeResultsTrainingFolder = new File(
                "src/test/resources/ml/3x3/training/negative/");
        File negativeResultsTestingFolder = new File(
                "src/test/resources/ml/3x3/testing/negative/");

        File positiveResultsTrainingFolder = new File("src/test/resources/ml/3x3/training/positive/");
        File positiveResultsTestingFolder = new File("src/test/resources/ml/3x3/testing/positive/");

        ArrayList<String> trainingFileNames = new ArrayList<>();
        ArrayList<double[]> positiveTensors = new ArrayList<>();
        ArrayList<double[]> negativeTensors = new ArrayList<>();

        getTensors(sideSize, positiveResultsTrainingFolder, positiveTensors, trainingFileNames);
        getTensors(sideSize, negativeResultsTrainingFolder, negativeTensors, trainingFileNames);

        ArrayList<String> testingFileNames = new ArrayList<>();
        ArrayList<double[]> testingPositiveTensors = new ArrayList<>();
        ArrayList<double[]> testingNegativeTensors = new ArrayList<>();

        getTensors(sideSize, positiveResultsTestingFolder, testingPositiveTensors, testingFileNames);
        getTensors(sideSize, negativeResultsTestingFolder, testingNegativeTensors, testingFileNames);

        double[][] trainingData = new double[positiveTensors.size() + negativeTensors.size()][];
        double[][] resultsData = new double[positiveTensors.size() + negativeTensors.size()][];

        int i = 0;
        for (double[] tensor : positiveTensors) {
            trainingData[i] = tensor;
            resultsData[i] = getPositiveResult();
            i++;
        }
        for (double[] tensor : negativeTensors) {
            trainingData[i] = tensor;
            resultsData[i] = getNegativeResult();
            i++;
        }

        System.out.println("Training...");
        network.train(trainingData, resultsData, 100000);
        System.out.println("Trained!");

        i = 0;
        for (double[] tensor : testingPositiveTensors) {
            network.forwardPropagation(tensor);
            System.out.println(testingFileNames.get(i++) + ": " + network.getOutput() + " (should be near to 1)");
        }

        for (double[] tensor : testingNegativeTensors) {
            network.forwardPropagation(tensor);
            System.out.println(testingFileNames.get(i++) + ": " + network.getOutput() + " (should be near to 0)");
        }
    }

    @Test
    public void testClasification10x10() throws Exception {
        testClasification(10, 6, "logos_10x10");
    }

    @Test
    public void testClasification20x20() throws Exception {
        testClasification(20, 9, "logos_20x20");
    }
    
    public void testClasification(int sideSize, int hiddenLayerElements, String folderName) throws Exception {
        int trainingInputSize = sideSize * sideSize;

        // binary?
        int outputSize = 1;

        BasicNeuralNetwork network = new BasicNeuralNetwork(trainingInputSize, hiddenLayerElements, outputSize);

        File negativeResultsTrainingFolder = new File(
                "src/test/resources/ml/" +folderName + "/training/negative/");
        File negativeResultsTestingFolder = new File(
                "src/test/resources/ml/"  +folderName + "/testing/negative/");

        File positiveResultsTrainingFolder = new File("src/test/resources/ml/" +folderName + "/training/positive/");
        File positiveResultsTestingFolder = new File("src/test/resources/ml/"+  folderName + "/testing/positive/");

        ArrayList<String> trainingFileNames = new ArrayList<>();
        ArrayList<double[]> positiveTensors = new ArrayList<>();
        ArrayList<double[]> negativeTensors = new ArrayList<>();

        getTensors(sideSize, positiveResultsTrainingFolder, positiveTensors, trainingFileNames);
        getTensors(sideSize, negativeResultsTrainingFolder, negativeTensors, trainingFileNames);

        ArrayList<String> testingFileNames = new ArrayList<>();
        ArrayList<double[]> testingPositiveTensors = new ArrayList<>();
        ArrayList<double[]> testingNegativeTensors = new ArrayList<>();

        getTensors(sideSize, positiveResultsTestingFolder, testingPositiveTensors, testingFileNames);
        getTensors(sideSize, negativeResultsTestingFolder, testingNegativeTensors, testingFileNames);

        double[][] trainingData = new double[positiveTensors.size() + negativeTensors.size()][];
        double[][] resultsData = new double[positiveTensors.size() + negativeTensors.size()][];

        int i = 0;
        for (double[] tensor : positiveTensors) {
            trainingData[i] = tensor;
            resultsData[i] = getPositiveResult();
            i++;
        }
        for (double[] tensor : negativeTensors) {
            trainingData[i] = tensor;
            resultsData[i] = getNegativeResult();
            i++;
        }

        System.out.println("Training...");
        network.train(trainingData, resultsData, 100000);
        System.out.println("Trained!");

        i = 0;
        for (double[] tensor : testingPositiveTensors) {
            network.forwardPropagation(tensor);
            System.out.println(testingFileNames.get(i++) + ": " + network.getOutput() + " (should be near to 1)");
        }

        for (double[] tensor : testingNegativeTensors) {
            network.forwardPropagation(tensor);
            System.out.println(testingFileNames.get(i++) + ": " + network.getOutput() + " (should be near to 0)");
        }
    }

    private void getTensors(int sideSize, File folder, ArrayList<double[]> tensors, ArrayList<String> fileNames)
            throws IOException, Exception {
        for (File pictureFile : folder.listFiles()) {
            BufferedImage pictureImage = ImageIO.read(pictureFile);
            if (pictureImage == null) {
                System.out.println("image " + pictureFile.getName() + " discarded (null).");
                continue;
            }
            ImageRepresentation representation = new ImageRepresentation(pictureImage);
            ColorMap map = new ColorMap(representation.getImageWidth(), representation.getImageHeight());
            map.setRed(representation.getRed());
            map.setGreen(representation.getGreen());
            map.setBlue(representation.getBlue());
            if (map.getWidth() < sideSize || map.getHeight() < sideSize) {
                System.out.println("image " + pictureFile.getName() + " discarded.");
                continue;
            }
            fileNames.add(pictureFile.getName());
            int zoneStartX = Math.round((map.getWidth() - sideSize) / 2);
            int zoneStartY = Math.round((map.getHeight() - sideSize) / 2);

            IntegerRectangularZone zone = new RectangularImageArea(zoneStartX, zoneStartY, sideSize, sideSize);
            double[][] tensor = BasicImageNeuralNetworkUtils.getTensor(map, zone);
            double[] flatTensor = BasicImageNeuralNetworkUtils.flattenTensor(tensor);
            tensors.add(flatTensor);
        }
    }

    private double[] getPositiveResult() {
        double[] output = { 1d };
        return output;
    }

    private double[] getNegativeResult() {
        double[] output = { 0d };
        return output;
    }

}
