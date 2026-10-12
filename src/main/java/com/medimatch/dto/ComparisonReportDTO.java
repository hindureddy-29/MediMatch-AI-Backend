package com.medimatch.dto;

import java.util.ArrayList;
import java.util.List;

public class ComparisonReportDTO {

    private Object medA;
    private Object medB;
    private StandardizedMedicineDTO stdA;
    private StandardizedMedicineDTO stdB;
    private ScoresDTO scores;
    private IngredientAnalysisDTO ingredientAnalysis;
    private String classification;
    private String riskLevel;
    private List<AlertDTO> alerts = new ArrayList<>();

    public ComparisonReportDTO() {
    }

    public Object getMedA() {
        return medA;
    }

    public void setMedA(Object medA) {
        this.medA = medA;
    }

    public Object getMedB() {
        return medB;
    }

    public void setMedB(Object medB) {
        this.medB = medB;
    }

    public StandardizedMedicineDTO getStdA() {
        return stdA;
    }

    public void setStdA(StandardizedMedicineDTO stdA) {
        this.stdA = stdA;
    }

    public StandardizedMedicineDTO getStdB() {
        return stdB;
    }

    public void setStdB(StandardizedMedicineDTO stdB) {
        this.stdB = stdB;
    }

    public ScoresDTO getScores() {
        return scores;
    }

    public void setScores(ScoresDTO scores) {
        this.scores = scores;
    }

    public IngredientAnalysisDTO getIngredientAnalysis() {
        return ingredientAnalysis;
    }

    public void setIngredientAnalysis(IngredientAnalysisDTO ingredientAnalysis) {
        this.ingredientAnalysis = ingredientAnalysis;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public List<AlertDTO> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<AlertDTO> alerts) {
        this.alerts = alerts;
    }
}
