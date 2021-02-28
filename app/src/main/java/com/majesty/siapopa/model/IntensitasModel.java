package com.majesty.siapopa.model;

public class IntensitasModel {
    String id_intensitas, wilayah_pengamatan, kabupaten, kecamatan, desa, periode_pengamatan,
            luas_tanaman, umur_tanaman, jenis_opt, luas_sembuh, luas_sisa_serangan, intensitas_serangan,
            puso_sisa_serang, jumlah_sisa_serang, luas_tambah_serang, intensitas_tambah, puso_tambah_serang,
            jumlah_tambah_serang, luas_pengendalian, luas_keadaan_serang, intensitas_keadaan, puso_keadaan_serang,
            jumlah_keadaan_serang, luas_waspada, tanggal_intensitas, id_hasil_pengamatan, frek_kimia, frek_nabati,
            batas_waktu, petugas_pengamatan, alamat, kodeapproval, blok;

    public IntensitasModel() {
    }

    public IntensitasModel(String id_intensitas, String wilayah_pengamatan, String kabupaten, String kecamatan, String desa,
                           String periode_pengamatan, String luas_tanaman, String umur_tanaman, String jenis_opt,
                           String luas_sembuh, String luas_sisa_serangan, String intensitas_serangan, String puso_sisa_serang,
                           String jumlah_sisa_serang, String luas_tambah_serang, String intensitas_tambah, String puso_tambah_serang,
                           String jumlah_tambah_serang, String luas_pengendalian, String luas_keadaan_serang, String intensitas_keadaan,
                           String puso_keadaan_serang, String jumlah_keadaan_serang, String luas_waspada, String tanggal_intensitas,
                           String id_hasil_pengamatan, String frek_nabati, String frek_kimia, String batas_waktu, String petugas_pengamatan,
                           String alamat, String kodeapproval, String blok) {
        this.id_intensitas = id_intensitas;
        this.wilayah_pengamatan = wilayah_pengamatan;
        this.kabupaten = kabupaten;
        this.kecamatan = kecamatan;
        this.desa = desa;
        this.periode_pengamatan = periode_pengamatan;
        this.luas_tanaman = luas_tanaman;
        this.umur_tanaman = umur_tanaman;
        this.jenis_opt = jenis_opt;
        this.luas_sembuh = luas_sembuh;
        this.luas_sisa_serangan = luas_sisa_serangan;
        this.intensitas_serangan = intensitas_serangan;
        this.puso_sisa_serang = puso_sisa_serang;
        this.jumlah_sisa_serang = jumlah_sisa_serang;
        this.luas_tambah_serang = luas_tambah_serang;
        this.intensitas_tambah = intensitas_tambah;
        this.puso_tambah_serang = puso_tambah_serang;
        this.jumlah_tambah_serang = jumlah_tambah_serang;
        this.luas_pengendalian = luas_pengendalian;
        this.luas_keadaan_serang = luas_keadaan_serang;
        this.intensitas_keadaan = intensitas_keadaan;
        this.puso_keadaan_serang = puso_keadaan_serang;
        this.jumlah_keadaan_serang = jumlah_keadaan_serang;
        this.luas_waspada = luas_waspada;
        this.tanggal_intensitas = tanggal_intensitas;
        this.id_hasil_pengamatan = id_hasil_pengamatan;
        this.frek_nabati = frek_nabati;
        this.frek_kimia = frek_kimia;
        this.batas_waktu = batas_waktu;
        this.petugas_pengamatan = petugas_pengamatan;
        this.alamat = alamat;
        this.blok = blok;
        this.kodeapproval = kodeapproval;
    }

    public String getBlok() {
        return blok;
    }

    public void setBlok(String blok) {
        this.blok = blok;
    }

    public String getKodeapproval() {
        return kodeapproval;
    }

    public void setKodeapproval(String kodeapproval) {
        this.kodeapproval = kodeapproval;
    }

    public String getBatas_waktu() {
        return batas_waktu;
    }

    public void setBatas_waktu(String batas_waktu) {
        this.batas_waktu = batas_waktu;
    }

    public String getPetugas_pengamatan() {
        return petugas_pengamatan;
    }

    public void setPetugas_pengamatan(String petugas_pengamatan) {
        this.petugas_pengamatan = petugas_pengamatan;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getFrek_kimia() {
        return frek_kimia;
    }

    public void setFrek_kimia(String frek_kimia) {
        this.frek_kimia = frek_kimia;
    }

    public String getFrek_nabati() {
        return frek_nabati;
    }

    public void setFrek_nabati(String frek_nabati) {
        this.frek_nabati = frek_nabati;
    }

    public String getId_intensitas() {
        return id_intensitas;
    }

    public void setId_intensitas(String id_intensitas) {
        this.id_intensitas = id_intensitas;
    }

    public String getWilayah_pengamatan() {
        return wilayah_pengamatan;
    }

    public void setWilayah_pengamatan(String wilayah_pengamatan) {
        this.wilayah_pengamatan = wilayah_pengamatan;
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

    public String getPeriode_pengamatan() {
        return periode_pengamatan;
    }

    public void setPeriode_pengamatan(String periode_pengamatan) {
        this.periode_pengamatan = periode_pengamatan;
    }

    public String getLuas_tanaman() {
        return luas_tanaman;
    }

    public void setLuas_tanaman(String luas_tanaman) {
        this.luas_tanaman = luas_tanaman;
    }

    public String getUmur_tanaman() {
        return umur_tanaman;
    }

    public void setUmur_tanaman(String umur_tanaman) {
        this.umur_tanaman = umur_tanaman;
    }

    public String getJenis_opt() {
        return jenis_opt;
    }

    public void setJenis_opt(String jenis_opt) {
        this.jenis_opt = jenis_opt;
    }

    public String getLuas_sembuh() {
        return luas_sembuh;
    }

    public void setLuas_sembuh(String luas_sembuh) {
        this.luas_sembuh = luas_sembuh;
    }

    public String getLuas_sisa_serangan() {
        return luas_sisa_serangan;
    }

    public void setLuas_sisa_serangan(String luas_sisa_serangan) {
        this.luas_sisa_serangan = luas_sisa_serangan;
    }

    public String getIntensitas_serangan() {
        return intensitas_serangan;
    }

    public void setIntensitas_serangan(String intensitas_serangan) {
        this.intensitas_serangan = intensitas_serangan;
    }

    public String getPuso_sisa_serang() {
        return puso_sisa_serang;
    }

    public void setPuso_sisa_serang(String puso_sisa_serang) {
        this.puso_sisa_serang = puso_sisa_serang;
    }

    public String getJumlah_sisa_serang() {
        return jumlah_sisa_serang;
    }

    public void setJumlah_sisa_serang(String jumlah_sisa_serang) {
        this.jumlah_sisa_serang = jumlah_sisa_serang;
    }

    public String getLuas_tambah_serang() {
        return luas_tambah_serang;
    }

    public void setLuas_tambah_serang(String luas_tambah_serang) {
        this.luas_tambah_serang = luas_tambah_serang;
    }

    public String getIntensitas_tambah() {
        return intensitas_tambah;
    }

    public void setIntensitas_tambah(String intensitas_tambah) {
        this.intensitas_tambah = intensitas_tambah;
    }

    public String getPuso_tambah_serang() {
        return puso_tambah_serang;
    }

    public void setPuso_tambah_serang(String puso_tambah_serang) {
        this.puso_tambah_serang = puso_tambah_serang;
    }

    public String getJumlah_tambah_serang() {
        return jumlah_tambah_serang;
    }

    public void setJumlah_tambah_serang(String jumlah_tambah_serang) {
        this.jumlah_tambah_serang = jumlah_tambah_serang;
    }

    public String getLuas_pengendalian() {
        return luas_pengendalian;
    }

    public void setLuas_pengendalian(String luas_pengendalian) {
        this.luas_pengendalian = luas_pengendalian;
    }

    public String getLuas_keadaan_serang() {
        return luas_keadaan_serang;
    }

    public void setLuas_keadaan_serang(String luas_keadaan_serang) {
        this.luas_keadaan_serang = luas_keadaan_serang;
    }

    public String getIntensitas_keadaan() {
        return intensitas_keadaan;
    }

    public void setIntensitas_keadaan(String intensitas_keadaan) {
        this.intensitas_keadaan = intensitas_keadaan;
    }

    public String getPuso_keadaan_serang() {
        return puso_keadaan_serang;
    }

    public void setPuso_keadaan_serang(String puso_keadaan_serang) {
        this.puso_keadaan_serang = puso_keadaan_serang;
    }

    public String getJumlah_keadaan_serang() {
        return jumlah_keadaan_serang;
    }

    public void setJumlah_keadaan_serang(String jumlah_keadaan_serang) {
        this.jumlah_keadaan_serang = jumlah_keadaan_serang;
    }

    public String getLuas_waspada() {
        return luas_waspada;
    }

    public void setLuas_waspada(String luas_waspada) {
        this.luas_waspada = luas_waspada;
    }

    public String getTanggal_intensitas() {
        return tanggal_intensitas;
    }

    public void setTanggal_intensitas(String tanggal_intensitas) {
        this.tanggal_intensitas = tanggal_intensitas;
    }

    public String getId_hasil_pengamatan() {
        return id_hasil_pengamatan;
    }

    public void setId_hasil_pengamatan(String id_hasil_pengamatan) {
        this.id_hasil_pengamatan = id_hasil_pengamatan;
    }
}
