package com.majesty.siapopa.model;

public class HasilModel {
    private String id_hasil, provinsi, kabupaten, kecamatan, desa, blok, luas_hamparan, luas_diamati,
    luas_persemaian, ph_tanah, komoditas, varietas, dari_umur, hingga_umur, pola_tanam, petugas_pengamatan, tanggal_pengamatan, total_intensitas, opt, kimia, nabati, cara_lain, eradikasi, frek_kimia, frek_nabati, jumlah_pengendalian;

    public HasilModel() {
    }

    public HasilModel(String id_hasil, String provinsi, String kabupaten, String kecamatan, String desa, String blok, String luas_hamparan, String total_intensitas, String opt,
                      String luas_diamati, String luas_persemaian, String ph_tanah, String komoditas, String varietas, String dari_umur, String hingga_umur, String pola_tanam, String petugas_pengamatan, String tanggal_pengamatan,
                      String kimia, String nabati, String cara_lain, String eradikasi,String frek_kimia,String frek_nabati,String jumlah_pengendalian) {
        this.id_hasil = id_hasil;
        this.provinsi = provinsi;
        this.kabupaten = kabupaten;
        this.kecamatan = kecamatan;
        this.desa = desa;
        this.blok = blok;
        this.luas_hamparan = luas_hamparan;
        this.luas_diamati = luas_diamati;
        this.luas_persemaian = luas_persemaian;
        this.ph_tanah = ph_tanah;
        this.komoditas = komoditas;
        this.varietas = varietas;
        this.dari_umur = dari_umur;
        this.hingga_umur = hingga_umur;
        this.pola_tanam = pola_tanam;
        this.petugas_pengamatan = petugas_pengamatan;
        this.tanggal_pengamatan = tanggal_pengamatan;
        this.total_intensitas = total_intensitas;
        this.opt = opt;
        this.kimia = kimia;
        this.nabati = nabati;
        this.cara_lain = cara_lain;
        this.eradikasi = eradikasi;
        this.frek_kimia = frek_kimia;
        this.frek_nabati = frek_nabati;
        this.jumlah_pengendalian = jumlah_pengendalian;
    }

    public String getKimia() {
        return kimia;
    }

    public void setKimia(String kimia) {
        this.kimia = kimia;
    }

    public String getNabati() {
        return nabati;
    }

    public void setNabati(String nabati) {
        this.nabati = nabati;
    }

    public String getCara_lain() {
        return cara_lain;
    }

    public void setCara_lain(String cara_lain) {
        this.cara_lain = cara_lain;
    }

    public String getEradikasi() {
        return eradikasi;
    }

    public void setEradikasi(String eradikasi) {
        this.eradikasi = eradikasi;
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

    public String getJumlah_pengendalian() {
        return jumlah_pengendalian;
    }

    public void setJumlah_pengendalian(String jumlah_pengendalian) {
        this.jumlah_pengendalian = jumlah_pengendalian;
    }

    public String getTotal_intensitas() {
        return total_intensitas;
    }

    public void setTotal_intensitas(String total_intensitas) {
        this.total_intensitas = total_intensitas;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getId_hasil() {
        return id_hasil;
    }

    public void setId_hasil(String id_hasil) {
        this.id_hasil = id_hasil;
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

    public String getBlok() {
        return blok;
    }

    public void setBlok(String blok) {
        this.blok = blok;
    }

    public String getLuas_hamparan() {
        return luas_hamparan;
    }

    public void setLuas_hamparan(String luas_hamparan) {
        this.luas_hamparan = luas_hamparan;
    }

    public String getLuas_diamati() {
        return luas_diamati;
    }

    public void setLuas_diamati(String luas_diamati) {
        this.luas_diamati = luas_diamati;
    }

    public String getLuas_persemaian() {
        return luas_persemaian;
    }

    public void setLuas_persemaian(String luas_persemaian) {
        this.luas_persemaian = luas_persemaian;
    }

    public String getPh_tanah() {
        return ph_tanah;
    }

    public void setPh_tanah(String ph_tanah) {
        this.ph_tanah = ph_tanah;
    }

    public String getKomoditas() {
        return komoditas;
    }

    public void setKomoditas(String komoditas) {
        this.komoditas = komoditas;
    }

    public String getVarietas() {
        return varietas;
    }

    public void setVarietas(String varietas) {
        this.varietas = varietas;
    }

    public String getDari_umur() {
        return dari_umur;
    }

    public void setDari_umur(String dari_umur) {
        this.dari_umur = dari_umur;
    }

    public String getHingga_umur() {
        return hingga_umur;
    }

    public void setHingga_umur(String hingga_umur) {
        this.hingga_umur = hingga_umur;
    }

    public String getPola_tanam() {
        return pola_tanam;
    }

    public void setPola_tanam(String pola_tanam) {
        this.pola_tanam = pola_tanam;
    }

    public String getPetugas_pengamatan() {
        return petugas_pengamatan;
    }

    public void setPetugas_pengamatan(String petugas_pengamatan) {
        this.petugas_pengamatan = petugas_pengamatan;
    }

    public String getTanggal_pengamatan() {
        return tanggal_pengamatan;
    }

    public void setTanggal_pengamatan(String tanggal_pengamatan) {
        this.tanggal_pengamatan = tanggal_pengamatan;
    }
}
