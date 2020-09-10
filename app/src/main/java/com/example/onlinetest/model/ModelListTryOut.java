package com.example.onlinetest.model;

import android.widget.TextView;

public class ModelListTryOut {

    private String judul,deskripsi,pelaksanaan,kode_soal;

    public ModelListTryOut(){}

    public ModelListTryOut(String judul, String deskripsi, String tgl_pelaksanaan,String kode_soal) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.pelaksanaan = tgl_pelaksanaan;
        this.kode_soal = kode_soal;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getPelaksanaan() {
        return pelaksanaan;
    }

    public String getKode_soal() {
        return kode_soal;
    }

    public void setKode_soal(String kode_soal) {
        this.kode_soal = kode_soal;
    }

    public void setPelaksanaan(String tgl_pelaksanaan) {
        this.pelaksanaan = tgl_pelaksanaan;
    }
}
