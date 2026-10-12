package com.medimatch;

import com.medimatch.dto.StandardizedMedicineDTO;
import com.medimatch.service.MedicineStandardizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MedicineStandardizationServiceTest {

    private MedicineStandardizationService standardizationService;

    @BeforeEach
    void setUp() {
        standardizationService = new MedicineStandardizationService();
    }

    @Test
    void testStandardize_ExtractsStrengthAndForm() {
        StandardizedMedicineDTO result = standardizationService.standardize("Dolo 650 Tablet");

        assertEquals("dolo", result.getCleanedName());
        assertEquals("650mg", result.getStrength());
        assertEquals("tablet", result.getForm());
        assertTrue(result.getTokens().contains("dolo"));
    }

    @Test
    void testStandardize_HandlesAbbreviationsAndPunctuation() {
        StandardizedMedicineDTO result = standardizationService.standardize("Augmentin-625 Duo (Tab)");

        assertEquals("625mg", result.getStrength());
        assertEquals("tablet", result.getForm());
        assertTrue(result.getTokens().contains("augmentin"));
        assertTrue(result.getTokens().contains("duo"));
    }

    @Test
    void testStandardize_EmptyInput() {
        StandardizedMedicineDTO result = standardizationService.standardize("");
        assertEquals("", result.getCleanedName());
        assertEquals("", result.getStrength());
    }
}
