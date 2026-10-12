package com.medimatch.service;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FuzzyMatchingService {

    private final LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();
    private final JaroWinklerSimilarity jaroWinklerSimilarity = new JaroWinklerSimilarity();

    /**
     * Calculates Levenshtein Distance
     */
    public int calculateLevenshteinDistance(String s1, String s2) {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null) return s2.length();
        if (s2 == null) return s1.length();
        return levenshteinDistance.apply(s1, s2);
    }

    /**
     * Calculates normalized Levenshtein Similarity (0.0 to 1.0)
     */
    public double calculateLevenshteinSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        if (s1.equals(s2)) return 1.0;
        if (s1.isEmpty() && s2.isEmpty()) return 1.0;
        if (s1.isEmpty() || s2.isEmpty()) return 0.0;

        int dist = calculateLevenshteinDistance(s1, s2);
        int maxLen = Math.max(s1.length(), s2.length());
        return Math.max(0.0, (double) (maxLen - dist) / maxLen);
    }

    /**
     * Calculates Jaro-Winkler Similarity (0.0 to 1.0)
     */
    public double calculateJaroWinklerSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        if (s1.equals(s2)) return 1.0;
        if (s1.isEmpty() && s2.isEmpty()) return 1.0;
        if (s1.isEmpty() || s2.isEmpty()) return 0.0;

        Double score = jaroWinklerSimilarity.apply(s1, s2);
        return score != null ? score : 0.0;
    }

    /**
     * Calculates Token Overlap / Jaccard similarity (0.0 to 1.0)
     */
    public double calculateTokenOverlap(List<String> tokensA, List<String> tokensB) {
        if ((tokensA == null || tokensA.isEmpty()) && (tokensB == null || tokensB.isEmpty())) {
            return 1.0;
        }
        if (tokensA == null || tokensB == null || tokensA.isEmpty() || tokensB.isEmpty()) {
            return 0.0;
        }

        Set<String> setA = new HashSet<>(tokensA);
        Set<String> setB = new HashSet<>(tokensB);

        Set<String> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        Set<String> union = new HashSet<>(setA);
        union.addAll(setB);

        if (union.isEmpty()) return 1.0;
        return (double) intersection.size() / union.size();
    }

    /**
     * Calculates composite blended name similarity (0 to 100)
     */
    public int calculateBlendedNameSimilarity(String nameA, String nameB, List<String> tokensA, List<String> tokensB) {
        double lev = calculateLevenshteinSimilarity(nameA, nameB);
        double jaro = calculateJaroWinklerSimilarity(nameA, nameB);
        double token = calculateTokenOverlap(tokensA, tokensB);

        // For single-word brand names, prioritize character and phonetic similarity
        if (tokensA != null && tokensB != null && tokensA.size() <= 1 && tokensB.size() <= 1) {
            return (int) Math.round((0.55 * jaro + 0.45 * lev) * 100.0);
        }

        return (int) Math.round((0.40 * jaro + 0.30 * lev + 0.30 * token) * 100.0);
    }
}
