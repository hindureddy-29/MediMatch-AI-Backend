package com.medimatch.dto;

import com.medimatch.model.Medicine;

public class MatchResultDTO {

    private Medicine medicine;
    private ComparisonReportDTO evaluation;
    private int overallScore;

    public MatchResultDTO() {
    }

    public MatchResultDTO(Medicine medicine, ComparisonReportDTO evaluation, int overallScore) {
        this.medicine = medicine;
        this.evaluation = evaluation;
        this.overallScore = overallScore;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public ComparisonReportDTO getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(ComparisonReportDTO evaluation) {
        this.evaluation = evaluation;
    }

    public int getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(int overallScore) {
        this.overallScore = overallScore;
    }
}
