package ar.com.sodhium.commons.img.statistics;

import java.util.Collection;

public abstract class ImageStatistics<T extends ImageStatistics<?>> {
    public abstract T average(Collection<T> inputs);
    public abstract ImageStatistics<?> genericAverage(Collection<?> inputs);
    public abstract boolean compatible(T anotherStatistic, double percentage);
    public abstract boolean absolutCompatible(T anotherStatistic, double tolerance);
}
