package ar.com.sodhium.commons.img;

import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;

import ar.com.sodhium.commons.img.colors.values.ColorValuesCollection;

/**
 * 
 * @author Roberto G. Fernandez
 * 
 */
public class ImageRepresentation {
    private BufferedImage image;
    private int imageHeight;
    private int imageWidth;
    private int[] map;
    private int[] red;
    private int[] green;
    private int[] blue;
    private int[] alpha;

    public ImageRepresentation(BufferedImage image) {
        this.image = image;
        init(image);
        decompose();
    }

    public void setComponents(int[] red, int[] green, int[] blue, int[] alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    private void init(BufferedImage inputImage) {
        image = inputImage;
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        map = new int[imageWidth * imageHeight];
        // The source image is transformed into a numerical representation
        // corresponding to its pixels, so they can be manipulated.
        // This must be placed in a try-catch block, because we have to be
        // prepared to pick up the exceptions of type "InterrruptedException"
        // that the grabPixels () method can throw
        try {
            // A PixelGrabber object is instantiated, passing as a parameter the
            // array of pixels where we want to save the numerical
            // representation of the image that we are going to manipulate
            PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, imageWidth, imageHeight, map, 0, imageWidth);
            if (!(pixelGrabber.grabPixels() && ((pixelGrabber.getStatus() & ImageObserver.ALLBITS) != 0))) {
                throw new Exception("Not all bits loaded");
            }
        } catch (Exception e) {
            // TODO check why this exception is thrown
            // System.out.println(e);
        }
    }


    public int[] getAlpha() {
        if (alpha == null)
            decompose();
        return alpha;
    }

    public void setAlpha(int[] alpha) {
        this.alpha = alpha;
    }

    public int[] getBlue() {
        if (blue == null)
            decompose();
        return blue;
    }

    public void setBlue(int[] blue) {
        this.blue = blue;
    }

    public int[] getGreen() {
        if (green == null)
            decompose();
        return green;
    }

    public void setGreen(int[] green) {
        this.green = green;
    }

    public int[] getRed() {
        if (red == null)
            decompose();
        return red;
    }

    public void setRed(int[] red) {
        this.red = red;
    }

    /**
     * Decomposes the image into RGB + alpha components.
     * 
     */
    public void decompose() {
        if (this.map == null)
            return;
        alpha = new int[imageWidth * imageHeight];
        red = new int[imageWidth * imageHeight];
        green = new int[imageWidth * imageHeight];
        blue = new int[imageWidth * imageHeight];

        for (int i = 0; i < imageWidth * imageHeight; i++) {
            blue[i] = map[i] & 0xFF;
            green[i] = (map[i] >> 8) & 0xFF;
            red[i] = (map[i] >> 16) & 0xFF;
            alpha[i] = (map[i] >> 24) & 0xFF;
        }
    }

    public void recompose() {
        if (alpha == null)
            return;
        for (int i = 0; i < imageWidth * imageHeight; i++) {
            map[i] = blue[i] | (green[i] << 8) | (red[i] << 16) | (alpha[i] << 24);
        }
    }

    public ColorValuesCollection getRedCollection() {
        return new ColorValuesCollection(getRed());
    }

    public ColorValuesCollection getGreenCollection() {
        return new ColorValuesCollection(getGreen());
    }

    public ColorValuesCollection getBlueCollection() {
        return new ColorValuesCollection(getBlue());
    }

    public BufferedImage getImage() {
        return image;
    }

//    public OppScoutBotTestIde getParentIde() {
//        return parentIde;
//    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public double differenceAmount(ImageRepresentation otherRepresentation) {
        Double totalDifference = 0D;
        for (int i = 0; i < red.length; i++) {
            totalDifference += (red[i] - otherRepresentation.red[i]) * (red[i] - otherRepresentation.red[i]);
            totalDifference += (green[i] - otherRepresentation.green[i]) * (green[i] - otherRepresentation.green[i]);
            totalDifference += (blue[i] - otherRepresentation.blue[i]) * (blue[i] - otherRepresentation.blue[i]);
        }
        return totalDifference / ((double) red.length * 3);
    }

}
