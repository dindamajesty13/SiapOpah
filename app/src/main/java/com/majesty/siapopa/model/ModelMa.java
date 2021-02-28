package com.majesty.siapopa.model;

public class ModelMa {
    String id_jenisMa, ma, opt, jumlah, id_detail, id_hasil;

    public ModelMa(String id_jenisMa, String ma, String opt, String jumlah, String id_detail, String id_hasil) {
        this.id_jenisMa = id_jenisMa;
        this.ma = ma;
        this.opt = opt;
        this.jumlah = jumlah;
        this.id_detail = id_detail;
        this.id_hasil = id_hasil;
    }

    public ModelMa() {
    }

    public String getId_jenisMa() {
        return id_jenisMa;
    }

    public void setId_jenisMa(String id_jenisMa) {
        this.id_jenisMa = id_jenisMa;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getId_detail() {
        return id_detail;
    }

    public void setId_detail(String id_detail) {
        this.id_detail = id_detail;
    }

    public String getId_hasil() {
        return id_hasil;
    }

    public void setId_hasil(String id_hasil) {
        this.id_hasil = id_hasil;
    }
}
