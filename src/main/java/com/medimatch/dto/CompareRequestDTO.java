package com.medimatch.dto;

import java.util.Map;

public class CompareRequestDTO {

    private Object medicineA;
    private Object medicineB;
    private Object medA;
    private Object medB;

    public CompareRequestDTO() {
    }

    public CompareRequestDTO(Object medicineA, Object medicineB) {
        this.medicineA = medicineA;
        this.medicineB = medicineB;
    }

    public Object getMedicineA() {
        return medicineA != null ? medicineA : medA;
    }

    public void setMedicineA(Object medicineA) {
        this.medicineA = medicineA;
    }

    public Object getMedicineB() {
        return medicineB != null ? medicineB : medB;
    }

    public void setMedicineB(Object medicineB) {
        this.medicineB = medicineB;
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
}
