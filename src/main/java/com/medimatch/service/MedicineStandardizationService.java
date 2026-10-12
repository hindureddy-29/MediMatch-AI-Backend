package com.medimatch.service;

import com.medimatch.dto.StandardizedMedicineDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MedicineStandardizationService {

    private static final Map<String, String> FORM_MAPPINGS = new HashMap<>();
    private static final Map<String, String> UNIT_MAPPINGS = new HashMap<>();

    // Matches explicit units like 650mg, 500 mcg, 10 ml
    private static final Pattern EXPLICIT_STRENGTH_PATTERN = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*(mg|mcg|g|ml|iu|%)", Pattern.CASE_INSENSITIVE);
    // Matches standalone numbers that represent common dosages (e.g. Dolo 650, Augmentin 625)
    private static final Pattern STANDALONE_NUMBER_PATTERN = Pattern.compile("\\b(\\d+(?:\\.\\d+)?)\\b");
    private static final Pattern CLEAN_PUNCTUATION_PATTERN = Pattern.compile("[-/\\\\()\\[\\],+]");

    static {
        FORM_MAPPINGS.put("tab", "tablet");
        FORM_MAPPINGS.put("tabs", "tablet");
        FORM_MAPPINGS.put("tablet", "tablet");
        FORM_MAPPINGS.put("tablets", "tablet");
        FORM_MAPPINGS.put("cap", "capsule");
        FORM_MAPPINGS.put("caps", "capsule");
        FORM_MAPPINGS.put("capsule", "capsule");
        FORM_MAPPINGS.put("capsules", "capsule");
        FORM_MAPPINGS.put("syr", "syrup");
        FORM_MAPPINGS.put("syrup", "syrup");
        FORM_MAPPINGS.put("susp", "suspension");
        FORM_MAPPINGS.put("inj", "injection");
        FORM_MAPPINGS.put("injection", "injection");
        FORM_MAPPINGS.put("oint", "ointment");
        FORM_MAPPINGS.put("crm", "cream");
        FORM_MAPPINGS.put("sol", "solution");
        FORM_MAPPINGS.put("drops", "drops");
        FORM_MAPPINGS.put("gel", "gel");
        FORM_MAPPINGS.put("inf", "infusion");

        UNIT_MAPPINGS.put("milligram", "mg");
        UNIT_MAPPINGS.put("milligrams", "mg");
        UNIT_MAPPINGS.put("gram", "g");
        UNIT_MAPPINGS.put("grams", "g");
        UNIT_MAPPINGS.put("microgram", "mcg");
        UNIT_MAPPINGS.put("micrograms", "mcg");
        UNIT_MAPPINGS.put("milliliter", "ml");
        UNIT_MAPPINGS.put("milliliters", "ml");
    }

    /**
     * Standardizes a raw medicine name:
     * 1. Extracts strength (with unit or standalone dosage number defaulting to mg)
     * 2. Cleans punctuation and whitespace
     * 3. Normalizes dosage form (e.g. tab -> tablet)
     * 4. Tokenizes and reconstructs cleaned core brand name
     */
    public StandardizedMedicineDTO standardize(String rawName) {
        if (rawName == null || rawName.isBlank()) {
            return new StandardizedMedicineDTO(rawName, "", "", "", Collections.emptyList());
        }

        String text = rawName.toLowerCase().trim();

        // 1. Try matching explicit unit (650mg, 10ml, etc.)
        String strength = "";
        String matchedNumber = "";
        Matcher explicitMatcher = EXPLICIT_STRENGTH_PATTERN.matcher(text);

        if (explicitMatcher.find()) {
            matchedNumber = explicitMatcher.group(1);
            strength = matchedNumber + explicitMatcher.group(2).toLowerCase();
        } else {
            // Check for standalone numeric token (e.g. 650, 500, 625)
            Matcher numberMatcher = STANDALONE_NUMBER_PATTERN.matcher(text);
            if (numberMatcher.find()) {
                matchedNumber = numberMatcher.group(1);
                strength = matchedNumber + "mg"; // clinical convention: numeric suffix in pharmaceuticals represents mg
            }
        }

        // 2. Clean punctuation
        text = CLEAN_PUNCTUATION_PATTERN.matcher(text).replaceAll(" ");

        // 3. Tokenize words
        String[] words = text.split("\\s+");
        String form = "";
        List<String> filteredTokens = new ArrayList<>();

        for (String word : words) {
            if (word.isBlank()) continue;

            if (FORM_MAPPINGS.containsKey(word)) {
                form = FORM_MAPPINGS.get(word);
            } else if (!matchedNumber.isEmpty() && word.equals(matchedNumber)) {
                // skip standalone number from brand tokens
                continue;
            } else if (!strength.isEmpty() && word.equalsIgnoreCase(strength)) {
                // skip concatenated strength like "650mg"
                continue;
            } else if (UNIT_MAPPINGS.containsKey(word) || word.equals("mg") || word.equals("ml") || word.equals("mcg")) {
                // skip isolated unit
                continue;
            } else {
                filteredTokens.add(word);
            }
        }

        String cleanedName = String.join(" ", filteredTokens).trim();

        return new StandardizedMedicineDTO(rawName, cleanedName, strength, form, filteredTokens);
    }
}
