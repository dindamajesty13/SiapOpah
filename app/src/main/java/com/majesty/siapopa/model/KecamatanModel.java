package com.majesty.siapopa.model;

public class KecamatanModel {
    String kecamatan, id, kabupaten;

    public KecamatanModel() {
    }

    public KecamatanModel(String kecamatan, String id, String kabupaten) {
        this.kecamatan = kecamatan;
        this.id = id;
        this.kabupaten = kabupaten;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
