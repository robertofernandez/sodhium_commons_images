package ar.com.sodhium.commons.img.statistics;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GraysAverageLevel extends ImageStatistics<GraysAverageLevel> {

    @SerializedName("value")
    @Expose
    private Double value;

    public GraysAverageLevel(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "" + value.toString() + "%";
    }

    @Override
    public GraysAverageLevel average(Collection<GraysAverageLevel> inputs) {
        if (inputs.size() < 1) {
            return new GraysAverageLevel(0D);
        }
        double total = 0;
        for (GraysAverageLevel level : inputs) {
            total += level.getValue();
        }
        return new GraysAverageLevel(total / inputs.size());
    }

    @Override
    public ImageStatistics<?> genericAverage(Collection<?> inputs) {
        ArrayList<GraysAverageLevel> inputs2 = new ArrayList<>();
        for (Object input : inputs) {
            if (input instanceof GraysAverageLevel) {
                inputs2.add((GraysAverageLevel) input);
            }
        }
        return average(inputs2);
    }

    @Override
    public boolean compatible(GraysAverageLevel anotherStatistic, double percentage) {
        return StatisticsUtils.isAbsoluteCompatible(value, anotherStatistic.value, percentage);
    }

    @Override
    public boolean absolutCompatible(GraysAverageLevel anotherStatistic, double percentage) {
        return compatible(anotherStatistic, percentage);
    }

    public Double getDistance(GraysAverageLevel another) {
        return (value - another.value) * (value - another.value) * 3;
    }

}
