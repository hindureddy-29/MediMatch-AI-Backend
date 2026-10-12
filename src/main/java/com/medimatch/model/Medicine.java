package com.medimatch.model;

import java.util.Objects;

public class Medicine {

    private Long id;
    private String name;
    private String brandName;
    private String genericName;
    private String composition;
    private String dosageForm;
    private String strength;
    private String manufacturer;
    private String category;
    private String atcCode;
    private Double price;

    public Medicine() {
    }

    public Medicine(Long id, String name, String brandName, String genericName,
                    String composition, String dosageForm, String strength,
                    String manufacturer, String category, String atcCode, Double price) {
        this.id = id;
        this.name = name;
        this.brandName = brandName;
        this.genericName = genericName;
        this.composition = composition;
        this.dosageForm = dosageForm;
        this.strength = strength;
        this.manufacturer = manufacturer;
        this.category = category;
        this.atcCode = atcCode;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAtcCode() {
        return atcCode;
    }

    public void setAtcCode(String atcCode) {
        this.atcCode = atcCode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicine medicine = (Medicine) o;
        return Objects.equals(id, medicine.id) && Objects.equals(name, medicine.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", composition='" + composition + '\'' +
                ", strength='" + strength + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                '}';
    }
}
