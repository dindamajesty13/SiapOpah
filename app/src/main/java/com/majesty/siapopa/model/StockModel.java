package com.majesty.siapopa.model;

public class StockModel {
    String id_stock, username, pestisida, provinsi, kabupaten, stock_akhir, sasaran, tanggal_stock;

    public StockModel() {
    }

    public StockModel(String id_stock, String username, String pestisida, String provinsi, String kabupaten, String stock_akhir, String sasaran, String tanggal_stock) {
        this.id_stock = id_stock;
        this.username = username;
        this.pestisida = pestisida;
        this.provinsi = provinsi;
        this.kabupaten = kabupaten;
        this.stock_akhir = stock_akhir;
        this.sasaran = sasaran;
        this.tanggal_stock = tanggal_stock;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getId_stock() {
        return id_stock;
    }

    public void setId_stock(String id_stock) {
        this.id_stock = id_stock;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPestisida() {
        return pestisida;
    }

    public void setPestisida(String pestisida) {
        this.pestisida = pestisida;
    }

    public String getKabupaten() {
        return kabupaten;
    }

    public void setKabupaten(String kabupaten) {
        this.kabupaten = kabupaten;
    }

    public String getStock_akhir() {
        return stock_akhir;
    }

    public void setStock_akhir(String stock_akhir) {
        this.stock_akhir = stock_akhir;
    }

    public String getSasaran() {
        return sasaran;
    }

    public void setSasaran(String sasaran) {
        this.sasaran = sasaran;
    }

    public String getTanggal_stock() {
        return tanggal_stock;
    }

    public void setTanggal_stock(String tanggal_stock) {
        this.tanggal_stock = tanggal_stock;
    }
}
