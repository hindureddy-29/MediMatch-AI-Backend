package com.medimatch.service;

import com.medimatch.dto.*;
import com.medimatch.model.ActiveIngredient;
import com.medimatch.model.Medicine;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DuplicateDetectionService {

    private final MedicineStandardizationService standardizationService;
    private final FuzzyMatchingService fuzzyMatchingService;
    private final ActiveIngredientService activeIngredientService;

    public DuplicateDetectionService(MedicineStandardizationService standardizationService,
                                     FuzzyMatchingService fuzzyMatchingService,
                                     ActiveIngredientService activeIngredientService) {
        this.standardizationService = standardizationService;
        this.fuzzyMatchingService = fuzzyMatchingService;
        this.activeIngredientService = activeIngredientService;
    }

    /**
     * Evaluates a pair of medicines and produces a comprehensive comparison report
     */
    public ComparisonReportDTO evaluatePair(Object medA, Object medB) {
        String nameA = extractString(medA, "name");
        String compA = extractString(medA, "composition");
        String formA = extractString(medA, "dosageForm");
        String strengthA = extractString(medA, "strength");

        String nameB = extractString(medB, "name");
        String compB = extractString(medB, "composition");
        String formB = extractString(medB, "dosageForm");
        String strengthB = extractString(medB, "strength");

        // Step 1: Standardize both medicine names
        StandardizedMedicineDTO stdA = standardizationService.standardize(nameA);
        StandardizedMedicineDTO stdB = standardizationService.standardize(nameB);

        // Step 2: Compute string metrics on standardized names
        double levSimilarity = fuzzyMatchingService.calculateLevenshteinSimilarity(stdA.getCleanedName(), stdB.getCleanedName());
        double jaroSimilarity = fuzzyMatchingService.calculateJaroWinklerSimilarity(stdA.getCleanedName(), stdB.getCleanedName());
        double tokenOverlap = fuzzyMatchingService.calculateTokenOverlap(stdA.getTokens(), stdB.getTokens());

        int nameSimilarity = fuzzyMatchingService.calculateBlendedNameSimilarity(
                stdA.getCleanedName(), stdB.getCleanedName(), stdA.getTokens(), stdB.getTokens());

        // Step 3: Active ingredient comparison
        List<ActiveIngredient> ingListA = activeIngredientService.parseComposition(compA);
        List<ActiveIngredient> ingListB = activeIngredientService.parseComposition(compB);

        IngredientAnalysisDTO ingredientAnalysis = activeIngredientService.compareIngredients(ingListA, ingListB);
        int ingredientScore = (int) Math.round(ingredientAnalysis.getMatchScore() * 100.0);

        // Form and Strength checks
        String resolvedFormA = !stdA.getForm().isEmpty() ? stdA.getForm() : formA.toLowerCase().trim();
        String resolvedFormB = !stdB.getForm().isEmpty() ? stdB.getForm() : formB.toLowerCase().trim();
        boolean formsMatch = resolvedFormA.isEmpty() || resolvedFormB.isEmpty() || resolvedFormA.equals(resolvedFormB);

        String resolvedStrengthA = !stdA.getStrength().isEmpty() ? stdA.getStrength() : strengthA.toLowerCase().trim();
        String resolvedStrengthB = !stdB.getStrength().isEmpty() ? stdB.getStrength() : strengthB.toLowerCase().trim();
        boolean strengthMismatch = !resolvedStrengthA.isEmpty() && !resolvedStrengthB.isEmpty()
                && !resolvedStrengthA.equals(resolvedStrengthB);

        // Step 4: Overall score and classification
        int overallScore;
        String classification;
        String riskLevel;
        List<AlertDTO> alerts = new ArrayList<>();

        boolean hasIngredients = !ingListA.isEmpty() && !ingListB.isEmpty();

        if (hasIngredients) {
            // Chemical ingredients are authoritative
            overallScore = (int) Math.round(0.65 * ingredientScore + 0.35 * nameSimilarity);

            if ("IDENTICAL_COMPOSITION".equals(ingredientAnalysis.getStatus())) {
                if (nameSimilarity >= 75) {
                    classification = "EXACT_DUPLICATE";
                    riskLevel = "CRITICAL";
                    alerts.add(new AlertDTO("danger", "DUPLICATE_ENTRY",
                            "Both brand name and active composition indicate duplicate inventory entries."));
                } else {
                    classification = "GENERIC_EQUIVALENT";
                    riskLevel = "INFO";
                    alerts.add(new AlertDTO("info", "THERAPEUTIC_SUBSTITUTE",
                            "Different brand names but identical active pharmaceutical composition (Generic alternative)."));
                }

                if (ingredientAnalysis.isHasStrengthMismatch() || strengthMismatch) {
                    alerts.add(new AlertDTO("warning", "DOSAGE_MISMATCH",
                            "Strength variation detected: \"" + resolvedStrengthA + "\" vs \"" + resolvedStrengthB + "\". Risk of underdose or overdose."));
                }
            } else if ("PARTIAL_OVERLAP".equals(ingredientAnalysis.getStatus())) {
                classification = "PARTIAL_MATCH";
                riskLevel = "WARNING";
                alerts.add(new AlertDTO("warning", "PARTIAL_COMPOSITION",
                        "Partial overlap in active ingredients. Check combination ratios before substitution."));
            } else {
                // Chemical composition differs! Screen for LASA (Look-Alike Sound-Alike)
                if (nameSimilarity >= 65 || jaroSimilarity >= 0.75) {
                    classification = "LASA_ALERT";
                    riskLevel = "CRITICAL";
                    alerts.add(new AlertDTO("danger", "LASA_HAZARD",
                            "CRITICAL ALERT: Look-Alike / Sound-Alike (LASA) medicine! Names are " + nameSimilarity +
                                    "% similar, but active ingredients are completely different!"));
                } else {
                    classification = "DISTINCT_MEDICINE";
                    riskLevel = "LOW";
                }
            }
        } else {
            // Active ingredients missing -> Uncertainty alert
            overallScore = nameSimilarity;
            classification = nameSimilarity >= 75 ? "UNVERIFIED_SUSPECTED_DUPLICATE" : "DISTINCT_MEDICINE";
            riskLevel = nameSimilarity >= 75 ? "WARNING" : "LOW";

            alerts.add(new AlertDTO("warning", "UNCERTAINTY_FLAG",
                    "Active ingredient verification is incomplete or unavailable. Matching is based on standardized name tokens only."));
        }

        if (!formsMatch) {
            alerts.add(new AlertDTO("info", "DOSAGE_FORM_DIFFERENCE",
                    "Dosage form difference: " + resolvedFormA + " vs " + resolvedFormB + "."));
        }

        ScoresDTO scoresDTO = new ScoresDTO(
                overallScore,
                nameSimilarity,
                ingredientScore,
                (int) Math.round(levSimilarity * 100.0),
                (int) Math.round(jaroSimilarity * 100.0),
                (int) Math.round(tokenOverlap * 100.0)
        );

        ComparisonReportDTO report = new ComparisonReportDTO();
        report.setMedA(medA);
        report.setMedB(medB);
        report.setStdA(stdA);
        report.setStdB(stdB);
        report.setScores(scoresDTO);
        report.setIngredientAnalysis(ingredientAnalysis);
        report.setClassification(classification);
        report.setRiskLevel(riskLevel);
        report.setAlerts(alerts);

        return report;
    }

    private String extractString(Object obj, String field) {
        if (obj == null) return "";
        if (obj instanceof Medicine m) {
            return switch (field) {
                case "name" -> m.getName() != null ? m.getName() : "";
                case "composition" -> m.getComposition() != null ? m.getComposition() : "";
                case "dosageForm" -> m.getDosageForm() != null ? m.getDosageForm() : "";
                case "strength" -> m.getStrength() != null ? m.getStrength() : "";
                default -> "";
            };
        }
        if (obj instanceof Map<?, ?> map) {
            Object val = map.get(field);
            return val != null ? val.toString() : "";
        }
        if ("name".equals(field)) {
            return obj.toString();
        }
        return "";
    }
}
