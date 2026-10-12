package com.medimatch.dto;

import java.util.List;

public class StandardizedMedicineDTO {

    private String raw;
    private String cleanedName;
    private String strength;
    private String form;
    private List<String> tokens;

    public StandardizedMedicineDTO() {
    }

    public StandardizedMedicineDTO(String raw, String cleanedName, String strength, String form, List<String> tokens) {
        this.raw = raw;
        this.cleanedName = cleanedName;
        this.strength = strength;
        this.form = form;
        this.tokens = tokens;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getCleanedName() {
        return cleanedName;
    }

    public void setCleanedName(String cleanedName) {
        this.cleanedName = cleanedName;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }
}
