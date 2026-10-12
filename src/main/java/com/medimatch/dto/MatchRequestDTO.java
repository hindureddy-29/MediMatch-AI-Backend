package com.medimatch.dto;

import java.util.Map;

public class MatchRequestDTO {

    private Object query;
    private Integer threshold = 40;

    public MatchRequestDTO() {
    }

    public MatchRequestDTO(Object query, Integer threshold) {
        this.query = query;
        this.threshold = threshold;
    }

    public Object getQuery() {
        return query;
    }

    public void setQuery(Object query) {
        this.query = query;
    }

    public Integer getThreshold() {
        return threshold != null ? threshold : 40;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public String extractQueryName() {
        if (query == null) return "";
        if (query instanceof String s) return s;
        if (query instanceof Map<?, ?> map) {
            Object name = map.get("name");
            return name != null ? name.toString() : "";
        }
        return query.toString();
    }

    public String extractQueryComposition() {
        if (query instanceof Map<?, ?> map) {
            Object comp = map.get("composition");
            return comp != null ? comp.toString() : "";
        }
        return "";
    }

    public String extractQueryDosageForm() {
        if (query instanceof Map<?, ?> map) {
            Object form = map.get("dosageForm");
            return form != null ? form.toString() : "";
        }
        return "";
    }

    public String extractQueryStrength() {
        if (query instanceof Map<?, ?> map) {
            Object str = map.get("strength");
            return str != null ? str.toString() : "";
        }
        return "";
    }
}
