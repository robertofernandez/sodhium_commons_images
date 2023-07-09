package ar.com.sodhium.commons.img.statistics;

public class StatisticsUtils {
    public static boolean isCompatible(double total, double value1, double value2, double percentage) {
        return (Math.abs(value1 - value2) / total) < percentage / 100;
    }

    public static boolean isCompatible255(double value1, double value2, double percentage) {
        return isCompatible(255, value1, value2, percentage);
    }

    public static boolean isAbsoluteCompatible(double value1, double value2, double tolerance) {
        return (Math.abs(value1 - value2)) < tolerance;
    }
}
