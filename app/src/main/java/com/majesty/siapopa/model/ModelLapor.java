package com.majesty.siapopa.model;

public class ModelLapor {
    private String id, username,kecamatan, varietas, opt, usiaTanam,
            luasTerserang, intensitas, populasi, luasWaspada, rekomendasi,
            keterangan, latitude, longitude, alamat, bukti, suratRekomendasi, signature,
            dateLapor, approval_kab, approval_satpel;

    public ModelLapor() {
    }

    public ModelLapor(String id, String username, String kecamatan, String varietas, String opt, String usiaTanam, String luasTerserang,
                        String intensitas, String populasi, String luasWaspada, String rekomendasi, String keterangan,
                        String latitude, String longitude, String alamat, String bukti,
                        String suratRekomendasi, String signature, String dateLapor, String approval_kab, String approval_satpel) {
        this.id = id;
        this.username = username;
        this.kecamatan = kecamatan;
        this.varietas = varietas;
        this.opt = opt;
        this.usiaTanam = usiaTanam;
        this.luasTerserang = luasTerserang;
        this.intensitas = intensitas;
        this.populasi = populasi;
        this.luasWaspada = luasWaspada;
        this.rekomendasi = rekomendasi;
        this.keterangan = keterangan;
        this.latitude = latitude;
        this.longitude = longitude;
        this.alamat = alamat;
        this.bukti = bukti;
        this.suratRekomendasi = suratRekomendasi;
        this.signature = signature;
        this.dateLapor = dateLapor;
        this.approval_kab = approval_kab;
        this.approval_satpel = approval_satpel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getVarietas() {
        return varietas;
    }

    public void setVarietas(String varietas) {
        this.varietas = varietas;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getUsiaTanam() {
        return usiaTanam;
    }

    public void setUsiaTanam(String usiaTanam) {
        this.usiaTanam = usiaTanam;
    }

    public String getLuasTerserang() {
        return luasTerserang;
    }

    public void setLuasTerserang(String luasTerserang) {
        this.luasTerserang = luasTerserang;
    }

    public String getIntensitas() {
        return intensitas;
    }

    public void setIntensitas(String intensitas) {
        this.intensitas = intensitas;
    }

    public String getPopulasi() {
        return populasi;
    }

    public void setPopulasi(String populasi) {
        this.populasi = populasi;
    }

    public String getLuasWaspada() {
        return luasWaspada;
    }

    public void setLuasWaspada(String luasWaspada) {
        this.luasWaspada = luasWaspada;
    }

    public String getRekomendasi() {
        return rekomendasi;
    }

    public void setRekomendasi(String rekomendasi) {
        this.rekomendasi = rekomendasi;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
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

    public String getBukti() {
        return bukti;
    }

    public void setBukti(String bukti) {
        this.bukti = bukti;
    }

    public String getSuratRekomendasi() {
        return suratRekomendasi;
    }

    public void setSuratRekomendasi(String suratRekomendasi) {
        this.suratRekomendasi = suratRekomendasi;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDateLapor() {
        return dateLapor;
    }

    public void setDateLapor(String dateLapor) {
        this.dateLapor = dateLapor;
    }

    public String getApproval_kab() {
        return approval_kab;
    }

    public void setApproval_kab(String approval_kab) {
        this.approval_kab = approval_kab;
    }

    public String getApproval_satpel() {
        return approval_satpel;
    }

    public void setApproval_satpel(String approval_satpel) {
        this.approval_satpel = approval_satpel;
    }
}
