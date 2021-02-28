package com.majesty.siapopa.model;

public class WilayahModel {
    String id_lokasi, provinsi, kabupaten, kecamatan, desa, latitude, longitude, alamat, luas_tanaman;

    public WilayahModel() {
    }

    public WilayahModel(String id_lokasi, String provinsi, String kabupaten, String kecamatan, String desa, String latitude, String longitude, String alamat, String luas_tanaman) {
        this.id_lokasi = id_lokasi;
        this.provinsi = provinsi;
        this.kabupaten = kabupaten;
        this.kecamatan = kecamatan;
        this.desa = desa;
        this.latitude = latitude;
        this.longitude = longitude;
        this.alamat = alamat;
        this.luas_tanaman = luas_tanaman;
    }

    public String getId_lokasi() {
        return id_lokasi;
    }

    public void setId_lokasi(String id_lokasi) {
        this.id_lokasi = id_lokasi;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getKabupaten() {
        return kabupaten;
    }

    public void setKabupaten(String kabupaten) {
        this.kabupaten = kabupaten;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getDesa() {
        return desa;
    }

    public void setDesa(String desa) {
        this.desa = desa;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getLuas_tanaman() {
        return luas_tanaman;
    }

    public void setLuas_tanaman(String luas_tanaman) {
        this.luas_tanaman = luas_tanaman;
    }
}
