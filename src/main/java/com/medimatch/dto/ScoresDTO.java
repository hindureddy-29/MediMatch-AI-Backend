package com.medimatch.dto;

public class ScoresDTO {

    private int overallScore;
    private int nameSimilarity;
    private int ingredientScore;
    private int levenshtein;
    private int jaroWinkler;
    private int tokenOverlap;

    public ScoresDTO() {
    }

    public ScoresDTO(int overallScore, int nameSimilarity, int ingredientScore,
                     int levenshtein, int jaroWinkler, int tokenOverlap) {
        this.overallScore = overallScore;
        this.nameSimilarity = nameSimilarity;
        this.ingredientScore = ingredientScore;
        this.levenshtein = levenshtein;
        this.jaroWinkler = jaroWinkler;
        this.tokenOverlap = tokenOverlap;
    }

    public int getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(int overallScore) {
        this.overallScore = overallScore;
    }

    public int getNameSimilarity() {
        return nameSimilarity;
    }

    public void setNameSimilarity(int nameSimilarity) {
        this.nameSimilarity = nameSimilarity;
    }

    public int getIngredientScore() {
        return ingredientScore;
    }

    public void setIngredientScore(int ingredientScore) {
        this.ingredientScore = ingredientScore;
    }

    public int getLevenshtein() {
        return levenshtein;
    }

    public void setLevenshtein(int levenshtein) {
        this.levenshtein = levenshtein;
    }

    public int getJaroWinkler() {
        return jaroWinkler;
    }

    public void setJaroWinkler(int jaroWinkler) {
        this.jaroWinkler = jaroWinkler;
    }

    public int getTokenOverlap() {
        return tokenOverlap;
    }

    public void setTokenOverlap(int tokenOverlap) {
        this.tokenOverlap = tokenOverlap;
    }
}
