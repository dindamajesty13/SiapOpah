package com.majesty.siapopa.model;

public class ModelPemberitahuan {
    String id_laporan, title, msg, laporan, id, tanggal, kecamatan, desa, blok, approval_kab, approval_satpel, approval_prov, bukti_foto, id_hasil;


    public ModelPemberitahuan() {
    }

    public ModelPemberitahuan(String id_hasil, String id_laporan, String title, String msg, String laporan, String id, String tanggal, String kecamatan, String desa, String blok, String approval_kab, String approval_satpel, String approval_prov, String bukti_foto) {
        this.id_laporan = id_laporan;
        this.title = title;
        this.msg = msg;
        this.laporan = laporan;
        this.id = id;
        this.tanggal = tanggal;
        this.kecamatan = kecamatan;
        this.desa = desa;
        this.blok = blok;
        this.approval_kab = approval_kab;
        this.approval_satpel = approval_satpel;
        this.approval_prov = approval_prov;
        this.bukti_foto = bukti_foto;
        this.id_hasil = id_hasil;
    }

    public String getId_hasil() {
        return id_hasil;
    }

    public void setId_hasil(String id_hasil) {
        this.id_hasil = id_hasil;
    }

    public String getBukti_foto() {
        return bukti_foto;
    }

    public void setBukti_foto(String bukti_foto) {
        this.bukti_foto = bukti_foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLaporan() {
        return laporan;
    }

    public void setLaporan(String laporan) {
        this.laporan = laporan;
    }

    public String getId_laporan() {
        return id_laporan;
    }

    public void setId_laporan(String id_laporan) {
        this.id_laporan = id_laporan;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
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

    public String getApproval_prov() {
        return approval_prov;
    }

    public void setApproval_prov(String approval_prov) {
        this.approval_prov = approval_prov;
    }
}
