package es.daw01.savex.utils;

public class DataUtils {
    /**
     * Transforms a number to keep it within the range [min, max]
     * 
     * @param value number to clamp
     * @param min  minimum value
     * @param max maximum value
     * @return the clamped value
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
