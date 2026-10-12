package com.medimatch.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IngredientAnalysisDTO {

    private String status; // IDENTICAL_COMPOSITION, PARTIAL_OVERLAP, DISTINCT, UNKNOWN
    private double matchScore;
    private String details;
    private boolean hasStrengthMismatch;
    private List<Map<String, String>> strengthMismatches = new ArrayList<>();

    public IngredientAnalysisDTO() {
    }

    public IngredientAnalysisDTO(String status, double matchScore, String details, boolean hasStrengthMismatch) {
        this.status = status;
        this.matchScore = matchScore;
        this.details = details;
        this.hasStrengthMismatch = hasStrengthMismatch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isHasStrengthMismatch() {
        return hasStrengthMismatch;
    }

    public void setHasStrengthMismatch(boolean hasStrengthMismatch) {
        this.hasStrengthMismatch = hasStrengthMismatch;
    }

    public List<Map<String, String>> getStrengthMismatches() {
        return strengthMismatches;
    }

    public void setStrengthMismatches(List<Map<String, String>> strengthMismatches) {
        this.strengthMismatches = strengthMismatches;
    }
}
