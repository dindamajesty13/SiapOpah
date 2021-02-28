package com.majesty.siapopa.model;

public class DesaModel {
    String id_laporan, desa;

    public DesaModel() {
    }

    public DesaModel(String id_laporan, String desa) {
        this.id_laporan = id_laporan;
        this.desa = desa;
    }

    public String getId_laporan() {
        return id_laporan;
    }

    public void setId_laporan(String id_laporan) {
        this.id_laporan = id_laporan;
    }

    public String getDesa() {
        return desa;
    }

    public void setDesa(String desa) {
        this.desa = desa;
    }
}
