package com.majesty.siapopa.model;

public class ModelOpt {
    String id_jenisOPT, opt, jumlah, username, id_detail, id_hasil, tanggal_insert, mutlak, id_populasi;

    public ModelOpt() {
    }

    public ModelOpt(String id_jenisOPT, String opt, String jumlah, String username, String id_detail, String id_hasil, String tanggal_insert, String mutlak, String id_populasi) {
        this.id_jenisOPT = id_jenisOPT;
        this.opt = opt;
        this.jumlah = jumlah;
        this.username = username;
        this.id_detail = id_detail;
        this.id_hasil = id_hasil;
        this.tanggal_insert = tanggal_insert;
        this.mutlak = mutlak;
        this.id_populasi = id_populasi;
    }

    public String getId_populasi() {
        return id_populasi;
    }

    public void setId_populasi(String id_populasi) {
        this.id_populasi = id_populasi;
    }

    public String getId_jenisOPT() {
        return id_jenisOPT;
    }

    public void setId_jenisOPT(String id_jenisOPT) {
        this.id_jenisOPT = id_jenisOPT;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getTanggal_insert() {
        return tanggal_insert;
    }

    public void setTanggal_insert(String tanggal_insert) {
        this.tanggal_insert = tanggal_insert;
    }

    public String getMutlak() {
        return mutlak;
    }

    public void setMutlak(String mutlak) {
        this.mutlak = mutlak;
    }
}
