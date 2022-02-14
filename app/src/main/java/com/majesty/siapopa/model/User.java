package com.majesty.siapopa.model;

public class User {
    private String username, password, alamat, id_usergroup, provinsi, kabupaten, nama, id_lokasi;

    public User() {
    }

    public User(String username, String id_lokasi, String password, String alamat, String id_usergroup, String provinsi, String kabupaten, String nama) {
        this.username = username;
        this.id_lokasi = id_lokasi;
        this.password = password;
        this.alamat = alamat;
        this.id_usergroup = id_usergroup;
        this.provinsi = provinsi;
        this.kabupaten = kabupaten;
        this.nama = nama;
    }

    public String getId_lokasi() {
        return id_lokasi;
    }

    public void setId_lokasi(String id_lokasi) {
        this.id_lokasi = id_lokasi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getId_usergroup() {
        return id_usergroup;
    }

    public void setId_usergroup(String id_usergroup) {
        this.id_usergroup = id_usergroup;
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
}


