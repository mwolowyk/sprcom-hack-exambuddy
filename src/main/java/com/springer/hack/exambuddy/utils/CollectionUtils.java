package com.springer.hack.exambuddy.utils;

import java.util.Collection;
import java.util.HashSet;

public class CollectionUtils {

    public static Double calculateSimilarity(Collection<String> list1, Collection<String> list2,
                                             SimilarityCalculationType calculationType) {
        Double similarity = 0.0;
        if (list1.isEmpty() || list2.isEmpty()) {
            return 0.0;
        }
        switch (calculationType) {
            case dice:
                similarity = CollectionUtils.calculateDice(list1, list2);
                break;
            case jaccard:
                similarity = CollectionUtils.calculateJaccard(list1, list2);
                break;
            case overlapp:
                similarity = CollectionUtils.calculateOverlap(list1, list2);
                break;
        }
        return similarity;
    }

    private static Double calculateJaccard(Collection<String> list1, Collection<String> list2) {
        Collection<String> intersection = CollectionUtils.getIntersection(list1, list2);
        int divident = list1.size() + list2.size() - intersection.size();
        return ((double) intersection.size() / divident);
    }

    /**
     * Dice coefficient
     *
     * @param list1
     * @param list2
     * @return d(list1, list2) = 2 * (double)(intersection.size() /
     *         (list1.size() + list2.size()));
     */
    public static Double calculateDice(Collection<String> list1, Collection<String> list2) {
        Collection<String> intersection = CollectionUtils.getIntersection(list1, list2);
        int divident = list1.size() + list2.size();
        return (2 * ((double) intersection.size() / divident));
    }

    public static Collection<String> getIntersection(Collection<String> list1,
                                                     Collection<String> list2) {
        HashSet<String> hashSet = new HashSet<>(list1);
        hashSet.retainAll(list2);
        return hashSet;
    }

    /**
     * Overlap coefficient
     *
     * @param linksOut
     * @param linksOut2
     * @return o(list1, list2) = (double)(intersection.size() /
     *         Math.min(list1.size(),list2.size()));
     */
    private static Double calculateOverlap(Collection<String> linksOut, Collection<String> linksOut2) {
        Collection<String> intersection = CollectionUtils.getIntersection(linksOut, linksOut2);
        int divident = Math.min(linksOut.size(), linksOut2.size());
        double coefficient = 0.0;
        if (divident != 0) {
            coefficient = (double) intersection.size() / divident;
        }
        return coefficient;
    }

    public enum SimilarityCalculationType {
        overlapp, dice, jaccard;
    }
}
