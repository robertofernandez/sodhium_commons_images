package ar.com.sodhium.commons.img.statistics;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ColorsAverageLevel extends ImageStatistics<ColorsAverageLevel> {
    @SerializedName("red")
    @Expose
    private Double red;
    @SerializedName("green")
    @Expose
    private Double green;
    @SerializedName("blue")
    @Expose
    private Double blue;

    public ColorsAverageLevel(Double red, Double green, Double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public String toString() {
        return "" + red.toString() + "%; " + green.toString() + "%; " + blue.toString() + "%;";
    }

    public Double getRed() {
        return red;
    }

    public Double getGreen() {
        return green;
    }

    public Double getBlue() {
        return blue;
    }

    @Override
    public ColorsAverageLevel average(Collection<ColorsAverageLevel> inputs) {
        if (inputs.size() < 1) {
            return new ColorsAverageLevel(0D, 0D, 0D);
        }
        double totalRed = 0;
        double totalGreen = 0;
        double totalBlue = 0;
        for (ColorsAverageLevel level : inputs) {
            totalRed += level.getRed();
            totalGreen += level.getGreen();
            totalBlue += level.getBlue();
        }
        return new ColorsAverageLevel(totalRed / inputs.size(), totalGreen / inputs.size(), totalBlue / inputs.size());
    }
    
    @Override
    public ImageStatistics<?> genericAverage(Collection<?> inputs) {
        ArrayList<ColorsAverageLevel> inputs2 = new ArrayList<>();
        for (Object input : inputs) {
            if (input instanceof ColorsAverageLevel) {
                inputs2.add((ColorsAverageLevel) input);
            }
        }
        return average(inputs2);
    }

    @Override
    public boolean compatible(ColorsAverageLevel anotherStatistic, double percentage) {
        return StatisticsUtils.isCompatible255(red, anotherStatistic.red, percentage)
                && StatisticsUtils.isCompatible255(green, anotherStatistic.green, percentage)
                && StatisticsUtils.isCompatible255(blue, anotherStatistic.blue, percentage);
    }

    @Override
    public boolean absolutCompatible(ColorsAverageLevel anotherStatistic, double tolerance) {
        return StatisticsUtils.isAbsoluteCompatible(red, anotherStatistic.red, tolerance)
                && StatisticsUtils.isAbsoluteCompatible(green, anotherStatistic.green, tolerance)
                && StatisticsUtils.isAbsoluteCompatible(blue, anotherStatistic.blue, tolerance);
    }

    public Double getDistance(ColorsAverageLevel another) {
        return (red - another.red) * (red - another.red) + (green - another.green) * (green - another.green)
                + (blue - another.blue) * (blue - another.blue);
    }
}
