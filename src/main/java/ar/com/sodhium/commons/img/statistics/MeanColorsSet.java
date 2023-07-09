package ar.com.sodhium.commons.img.statistics;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ar.com.sodhium.commons.img.colors.RgbColor;
import ar.com.sodhium.commons.img.pixels.ColorsCluster;

public class MeanColorsSet extends ImageStatistics<MeanColorsSet> {
    @SerializedName("main-color")
    @Expose
    private RgbColor mainColor;
    @SerializedName("secondary-color")
    @Expose
    private RgbColor secondaryColor;
    @SerializedName("weight")
    @Expose
    private long weight = 0;

    public MeanColorsSet(ArrayList<ColorsCluster> clusters) {
        mainColor = clusters.get(0).getMeanColor();
        weight = clusters.get(0).getPixels().size();
        if (clusters.size() > 1) {
            secondaryColor = clusters.get(1).getMeanColor();
            weight += clusters.get(1).getPixels().size();
        } else {
            secondaryColor = mainColor;
        }
    }

    public MeanColorsSet(RgbColor mainColor, RgbColor secondaryColor, long weight) {
        super();
        this.mainColor = mainColor;
        this.secondaryColor = secondaryColor;
        this.weight = weight;
    }

    public MeanColorsSet merge(MeanColorsSet anotherSet) {
        if (secondaryColor == null) {
            return anotherSet;
        } else if (anotherSet.secondaryColor == null) {
            return this;
        }
        boolean reversed = false;
        if (mainColor.getDistance(anotherSet.getMainColor()) > mainColor.getDistance(anotherSet.getSecondaryColor())) {
            reversed = true;
        }
        if (!reversed) {
            RgbColor newMainColor = mainColor.cloneColor().mergePondered(anotherSet.getMainColor(), weight,
                    anotherSet.weight);
            RgbColor newSecondaryColor = secondaryColor.cloneColor().mergePondered(anotherSet.getSecondaryColor(),
                    weight, anotherSet.weight);
            return new MeanColorsSet(newMainColor, newSecondaryColor, weight + anotherSet.weight);
        } else {
            RgbColor newMainColor = mainColor.cloneColor().mergePondered(anotherSet.getSecondaryColor(), weight,
                    anotherSet.weight);
            RgbColor newSecondaryColor = secondaryColor.cloneColor().mergePondered(anotherSet.getMainColor(), weight,
                    anotherSet.weight);
            return new MeanColorsSet(newMainColor, newSecondaryColor, weight + anotherSet.weight);
        }
    }

    @Override
    public MeanColorsSet average(Collection<MeanColorsSet> inputs) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ImageStatistics<?> genericAverage(Collection<?> inputs) {
        ArrayList<MeanColorsSet> inputs2 = new ArrayList<>();
        for (Object input : inputs) {
            if (input instanceof MeanColorsSet) {
                inputs2.add((MeanColorsSet) input);
            }
        }
        return average(inputs2);
    }

    @Override
    public boolean compatible(MeanColorsSet anotherStatistic, double percentage) {
        return absolutCompatible(anotherStatistic, percentage * 7);
    }

    @Override
    public boolean absolutCompatible(MeanColorsSet anotherStatistic, double tolerance) {
        if (secondaryColor != null && anotherStatistic.secondaryColor != null) {
            return (anotherStatistic.mainColor.getDistance(mainColor) < tolerance
                    && anotherStatistic.secondaryColor.getDistance(secondaryColor) < tolerance * 1.5)
                    || (anotherStatistic.mainColor.getDistance(secondaryColor) < tolerance
                            && anotherStatistic.secondaryColor.getDistance(mainColor) < tolerance * 1.5);
        } else if (secondaryColor == null && anotherStatistic.secondaryColor == null) {
            return anotherStatistic.mainColor.getDistance(mainColor) < tolerance;
        } else if (secondaryColor == null) {
            return anotherStatistic.mainColor.getDistance(mainColor) < tolerance
                    || anotherStatistic.secondaryColor.getDistance(mainColor) < tolerance;
        } else {
            return anotherStatistic.mainColor.getDistance(mainColor) < tolerance
                    || anotherStatistic.mainColor.getDistance(secondaryColor) < tolerance;
        }
    }

    @Override
    public String toString() {
        return "<mean-colors>\\n" + mainColor.toString() + "\n" + secondaryColor.toString() + "\n</mean-colors>";
    }

    public RgbColor getMainColor() {
        return mainColor;
    }

    public RgbColor getSecondaryColor() {
        return secondaryColor;
    }
}
