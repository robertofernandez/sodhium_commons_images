/*
 * Copyright 2019 WillDom S.A. All Rights Reserved.
 * Proprietary and Confidential information of WillDom S.A.
 * Disclosure, use or reproduction without the written authorization of WillDom S.A. is prohibited.
 */
package ar.com.sodhium.commons.img.areas;

import java.awt.Rectangle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//TODO clean up

/**
 * It consists of a descriptor of an area inside an external image. The image is
 * not referenced from the class, and any operation related to it should be
 * performed by external classes.
 * 
 * @author Roberto G. Fernandez
 *
 */
public class RectangularImageArea implements IntegerRectangularZone {
    @SerializedName("x")
    @Expose
    protected int x;
    @SerializedName("y")
    @Expose
    protected int y;
    @SerializedName("width")
    @Expose
    protected int width;
    @SerializedName("height")
    @Expose
    protected int height;

    public RectangularImageArea(int x, int y, int width, int height) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public static RectangularImageArea createWithLimits(int x, int y, int maxX, int maxY) {
        int theWidth = maxX - x;
        int theWeight = maxY - y;
        if(theWidth <= 0 || theWeight <= 0) {
            return new RectangularImageArea(x, y, 0, 0);
        }
        return new RectangularImageArea(x, y, theWidth, theWeight);
    }

    public RectangularImageArea(RectangularImageZone zone) {
        this(zone.getX(), zone.getY(), zone.getWidth(), zone.getHeight());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getId() {
        return "RIA:" + x + "_" + y + width + "_" + height;
    }

    @Override
    public String toString() {
        return "<" + x + ", " + y + "><" + getMaxX() + ", " + getMaxY() + ">";
    }

    public int getMaxX() {
        return x + width;
    }

    public int getMaxY() {
        return y + height;
    }

    public int distance(RectangularImageArea area) {
        int horizontalDistance = 0;
        int verticalDistance = 0;
        if (x < area.x) {
            horizontalDistance = area.x - (x + width);
        } else {
            horizontalDistance = x - (area.x + area.width);
        }
        if (y < area.y) {
            verticalDistance = area.y - (y + height);
        } else {
            verticalDistance = y - (area.y + area.height);
        }
        return Math.max(horizontalDistance, verticalDistance);
    }

    public Rectangle asRectangle() {
        return new Rectangle(x, y, width, height);
    }
    
    public double getArea() {
        return height * width;
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int offsetX, int offsetY) {
        this.x = x + offsetX;
        this.y = y + offsetY;
    }
}
