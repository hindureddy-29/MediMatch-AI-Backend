package com.medimatch.model;

import java.util.Objects;

public class ActiveIngredient {

    private String name;
    private String strength;
    private String unit;

    public ActiveIngredient() {
    }

    public ActiveIngredient(String name, String strength) {
        this.name = name;
        this.strength = strength;
    }

    public ActiveIngredient(String name, String strength, String unit) {
        this.name = name;
        this.strength = strength;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActiveIngredient that = (ActiveIngredient) o;
        return Objects.equals(name, that.name) && Objects.equals(strength, that.strength);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, strength);
    }

    @Override
    public String toString() {
        return "ActiveIngredient{" +
                "name='" + name + '\'' +
                ", strength='" + strength + '\'' +
                '}';
    }
}
