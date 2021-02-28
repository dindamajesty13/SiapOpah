package com.majesty.siapopa.model;

public class ModelStockKab {
    String id_wilayah, kabupaten, jumlah_pestisida, id_stock, username;

    public ModelStockKab() {
    }

    public ModelStockKab(String id_wilayah, String kabupaten, String jumlah_pestisida, String id_stock, String username) {
        this.id_wilayah = id_wilayah;
        this.kabupaten = kabupaten;
        this.jumlah_pestisida = jumlah_pestisida;
        this.id_stock = id_stock;
        this.username = username;
    }

    public String getId_wilayah() {
        return id_wilayah;
    }

    public void setId_wilayah(String id_wilayah) {
        this.id_wilayah = id_wilayah;
    }

    public String getKabupaten() {
        return kabupaten;
    }

    public void setKabupaten(String kabupaten) {
        this.kabupaten = kabupaten;
    }

    public String getJumlah_pestisida() {
        return jumlah_pestisida;
    }

    public void setJumlah_pestisida(String jumlah_pestisida) {
        this.jumlah_pestisida = jumlah_pestisida;
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
}
