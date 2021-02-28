package com.majesty.siapopa.model;

public class BarModel {
    String tanggal, intensitas;

    public BarModel() {
    }

    public BarModel(String tanggal, String intensitas) {
        this.tanggal = tanggal;
        this.intensitas = intensitas;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getIntensitas() {
        return intensitas;
    }

    public void setIntensitas(String intensitas) {
        this.intensitas = intensitas;
    }
}
