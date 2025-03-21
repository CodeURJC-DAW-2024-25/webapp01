package es.daw01.savex.utils;

public class LevenshteinUtils {

    private LevenshteinUtils() {
        /* Prevent instantiation */
    }

    /**
     * Calculate the Levenshtein distance between two strings
     * @param a First string
     * @param b Second string
     * @return Levenshtein distance between the two strings
     */
    public static int calculateLevenshteinDistance(String a, String b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        int[][] distance = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            distance[i][0] = i;
        }

        for (int j = 0; j <= b.length(); j++) {
            distance[0][j] = j;
        }

        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;

                distance[i][j] = Math.min(
                    Math.min(distance[i - 1][j] + 1, distance[i][j - 1] + 1),
                    distance[i - 1][j - 1] + cost
                );
            }
        }

        return distance[a.length()][b.length()];
    }
}