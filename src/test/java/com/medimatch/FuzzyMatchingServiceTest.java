package com.medimatch;

import com.medimatch.service.FuzzyMatchingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FuzzyMatchingServiceTest {

    private FuzzyMatchingService fuzzyMatchingService;

    @BeforeEach
    void setUp() {
        fuzzyMatchingService = new FuzzyMatchingService();
    }

    @Test
    void testLevenshteinSimilarity_IdenticalStrings() {
        double score = fuzzyMatchingService.calculateLevenshteinSimilarity("paracetamol", "paracetamol");
        assertEquals(1.0, score, 0.001);
    }

    @Test
    void testLevenshteinSimilarity_CloseStrings() {
        double score = fuzzyMatchingService.calculateLevenshteinSimilarity("augmentin", "augmenton");
        assertTrue(score > 0.8, "Expected high similarity for 1-character typo");
    }

    @Test
    void testJaroWinklerSimilarity_HighPrefixMatch() {
        double score = fuzzyMatchingService.calculateJaroWinklerSimilarity("telma", "telmikind");
        assertTrue(score > 0.7, "Expected high prefix similarity for Telma vs Telmikind");
    }

    @Test
    void testTokenOverlap_ExactAndPartial() {
        double fullOverlap = fuzzyMatchingService.calculateTokenOverlap(
                List.of("amoxicillin", "clavulanic"),
                List.of("amoxicillin", "clavulanic")
        );
        assertEquals(1.0, fullOverlap, 0.001);

        double partialOverlap = fuzzyMatchingService.calculateTokenOverlap(
                List.of("amoxicillin", "clavulanic"),
                List.of("amoxicillin")
        );
        assertEquals(0.5, partialOverlap, 0.001);
    }
}
