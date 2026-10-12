package com.medimatch.service;

import com.medimatch.dto.IngredientAnalysisDTO;
import com.medimatch.model.ActiveIngredient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ActiveIngredientService {

    private final FuzzyMatchingService fuzzyMatchingService;
    private static final Pattern INGREDIENT_STRENGTH_PATTERN = Pattern.compile("^(.*?)\\s*(\\d+(?:\\.\\d+)?\\s*(?:mg|mcg|g|ml|iu|%)?)$", Pattern.CASE_INSENSITIVE);

    public ActiveIngredientService(FuzzyMatchingService fuzzyMatchingService) {
        this.fuzzyMatchingService = fuzzyMatchingService;
    }

    /**
     * Parses an active composition string into structured ActiveIngredient objects
     * e.g., "Amoxicillin 500mg + Clavulanic Acid 125mg"
     */
    public List<ActiveIngredient> parseComposition(String compositionStr) {
        if (compositionStr == null || compositionStr.isBlank()) {
            return Collections.emptyList();
        }

        List<ActiveIngredient> ingredients = new ArrayList<>();
        // Split by +, /, ;, & or 'and'
        String[] parts = compositionStr.split("(?i)[+,/;]|\\band\\b");

        for (String part : parts) {
            String trimmed = part.trim().toLowerCase();
            if (trimmed.isEmpty()) continue;

            Matcher matcher = INGREDIENT_STRENGTH_PATTERN.matcher(trimmed);
            if (matcher.find()) {
                String name = matcher.group(1).trim();
                String strength = matcher.group(2).replaceAll("\\s+", "").trim();
                ingredients.add(new ActiveIngredient(name, strength));
            } else {
                ingredients.add(new ActiveIngredient(trimmed, ""));
            }
        }

        return ingredients;
    }

    /**
     * Compares two lists of active ingredients for chemical equivalence and strength parity
     */
    public IngredientAnalysisDTO compareIngredients(List<ActiveIngredient> listA, List<ActiveIngredient> listB) {
        if (listA == null || listB == null || listA.isEmpty() || listB.isEmpty()) {
            return new IngredientAnalysisDTO(
                    "UNKNOWN",
                    0.0,
                    "Active ingredients not available for full chemical verification",
                    false
            );
        }

        int matchedCount = 0;
        List<Map<String, String>> strengthMismatches = new ArrayList<>();

        for (ActiveIngredient itemA : listA) {
            ActiveIngredient matchedItemB = null;

            for (ActiveIngredient itemB : listB) {
                if (itemA.getName().equalsIgnoreCase(itemB.getName()) ||
                        fuzzyMatchingService.calculateLevenshteinSimilarity(itemA.getName(), itemB.getName()) >= 0.85) {
                    matchedItemB = itemB;
                    break;
                }
            }

            if (matchedItemB != null) {
                matchedCount++;
                if (!itemA.getStrength().isEmpty() && !matchedItemB.getStrength().isEmpty()
                        && !itemA.getStrength().equalsIgnoreCase(matchedItemB.getStrength())) {
                    Map<String, String> mismatch = new HashMap<>();
                    mismatch.put("ingredient", itemA.getName());
                    mismatch.put("strengthA", itemA.getStrength());
                    mismatch.put("strengthB", matchedItemB.getStrength());
                    strengthMismatches.add(mismatch);
                }
            }
        }

        int totalIngredients = Math.max(listA.size(), listB.size());
        double score = totalIngredients > 0 ? (double) matchedCount / totalIngredients : 0.0;

        String status;
        if (score >= 0.99) {
            status = "IDENTICAL_COMPOSITION";
        } else if (score >= 0.5) {
            status = "PARTIAL_OVERLAP";
        } else {
            status = "DISTINCT";
        }

        String details = matchedCount + "/" + totalIngredients + " active ingredients matched";
        boolean hasStrengthMismatch = !strengthMismatches.isEmpty();

        IngredientAnalysisDTO dto = new IngredientAnalysisDTO(status, score, details, hasStrengthMismatch);
        dto.setStrengthMismatches(strengthMismatches);
        return dto;
    }
}
