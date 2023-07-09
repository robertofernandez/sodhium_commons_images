package ar.com.sodhium.commons.img.statistics;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GraysDecilsHistogram extends ImageStatistics<GraysDecilsHistogram> {
    @SerializedName("values")
    @Expose
    private ArrayList<Double> values;

    public GraysDecilsHistogram(ArrayList<Double> values) {
        this.values = values;
    }

    public GraysDecilsHistogram() {
        values = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            values.add(0D);
        }
    }

    public void addValue(int value) {
        int i = Math.round(value / 10);
        values.set(i, values.get(i) + 1);
    }

    public ArrayList<Double> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return values.toString();
    }

    @Override
    public GraysDecilsHistogram average(Collection<GraysDecilsHistogram> inputs) {
        ArrayList<Double> newValues = new ArrayList<>();
        if (inputs.size() > 0) {
            for (int i = 0; i < 26; i++) {
                newValues.add(0D);
            }
            for (GraysDecilsHistogram histogram : inputs) {
                int index = 0;
                for (Double value : histogram.values) {
                    newValues.set(index, newValues.get(index) + value);
                    index++;
                }
            }
            for (int i = 0; i < 26; i++) {
                newValues.set(i, newValues.get(i) / inputs.size());
            }
        }
        return new GraysDecilsHistogram(newValues);
    }
    
    @Override
    public ImageStatistics<?> genericAverage(Collection<?> inputs) {
        ArrayList<GraysDecilsHistogram> inputs2 = new ArrayList<>();
        for (Object input : inputs) {
            if (input instanceof GraysDecilsHistogram) {
                inputs2.add((GraysDecilsHistogram) input);
            }
        }
        return average(inputs2);
    }

    @Override
    public boolean compatible(GraysDecilsHistogram anotherStatistic, double percentage) {
        int totals = 0;
        for (int i = 0; i < values.size(); i++) {
            totals += values.get(i);
            totals += anotherStatistic.values.get(i);
        }
        totals /= 2;
        int compatibles = 0;
        for (int i = 0; i < values.size(); i++) {
            if (StatisticsUtils.isCompatible(totals, values.get(i), anotherStatistic.values.get(i), percentage)) {
                compatibles++;
            }
        }
        return compatibles / values.size() > percentage / 100;
    }

    @Override
    public boolean absolutCompatible(GraysDecilsHistogram anotherStatistic, double percentage) {
        return absolutCompatible(anotherStatistic, percentage * 100);
    }
}
