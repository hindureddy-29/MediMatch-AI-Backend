package com.medimatch;

import com.medimatch.dto.ComparisonReportDTO;
import com.medimatch.model.Medicine;
import com.medimatch.service.ActiveIngredientService;
import com.medimatch.service.DuplicateDetectionService;
import com.medimatch.service.FuzzyMatchingService;
import com.medimatch.service.MedicineStandardizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DuplicateDetectionServiceTest {

    private DuplicateDetectionService duplicateDetectionService;

    @BeforeEach
    void setUp() {
        MedicineStandardizationService standardizationService = new MedicineStandardizationService();
        FuzzyMatchingService fuzzyMatchingService = new FuzzyMatchingService();
        ActiveIngredientService activeIngredientService = new ActiveIngredientService(fuzzyMatchingService);

        duplicateDetectionService = new DuplicateDetectionService(
                standardizationService, fuzzyMatchingService, activeIngredientService);
    }

    @Test
    void testGenericEquivalent_DoloVsCalpol() {
        Medicine medA = new Medicine();
        medA.setName("Dolo 650 Tablet");
        medA.setComposition("Paracetamol 650mg");
        medA.setDosageForm("tablet");
        medA.setStrength("650mg");

        Medicine medB = new Medicine();
        medB.setName("Calpol 650mg Tablet");
        medB.setComposition("Paracetamol 650mg");
        medB.setDosageForm("tablet");
        medB.setStrength("650mg");

        ComparisonReportDTO report = duplicateDetectionService.evaluatePair(medA, medB);

        assertEquals("GENERIC_EQUIVALENT", report.getClassification());
        assertEquals("IDENTICAL_COMPOSITION", report.getIngredientAnalysis().getStatus());
        assertEquals(100, report.getScores().getIngredientScore());
        assertTrue(report.getScores().getOverallScore() >= 70);
    }

    @Test
    void testLasaHazard_CelebrexVsCelexa() {
        Medicine medA = new Medicine();
        medA.setName("Celebrex 200mg Capsule");
        medA.setComposition("Celecoxib 200mg");

        Medicine medB = new Medicine();
        medB.setName("Celexa 20mg Tablet");
        medB.setComposition("Citalopram 20mg");

        ComparisonReportDTO report = duplicateDetectionService.evaluatePair(medA, medB);

        assertEquals("LASA_ALERT", report.getClassification());
        assertEquals("CRITICAL", report.getRiskLevel());
        assertTrue(report.getAlerts().stream().anyMatch(a -> "LASA_HAZARD".equals(a.getCode())));
    }

    @Test
    void testStrengthMismatch_PacimolVsDolo() {
        Medicine medA = new Medicine();
        medA.setName("Pacimol 500 Tablet");
        medA.setComposition("Paracetamol 500mg");
        medA.setStrength("500mg");

        Medicine medB = new Medicine();
        medB.setName("Dolo 650 Tablet");
        medB.setComposition("Paracetamol 650mg");
        medB.setStrength("650mg");

        ComparisonReportDTO report = duplicateDetectionService.evaluatePair(medA, medB);

        assertTrue(report.getAlerts().stream().anyMatch(a -> "DOSAGE_MISMATCH".equals(a.getCode())));
    }

    @Test
    void testUncertaintyFlag_MissingIngredients() {
        Medicine medA = new Medicine();
        medA.setName("Unknown Drug 500mg");
        medA.setComposition("");

        Medicine medB = new Medicine();
        medB.setName("Known Drug 500mg");
        medB.setComposition("");

        ComparisonReportDTO report = duplicateDetectionService.evaluatePair(medA, medB);

        assertTrue(report.getAlerts().stream().anyMatch(a -> "UNCERTAINTY_FLAG".equals(a.getCode())));
    }
}
