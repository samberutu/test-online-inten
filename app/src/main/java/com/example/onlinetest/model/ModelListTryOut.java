package com.example.onlinetest.model;

import android.widget.TextView;

public class ModelListTryOut {

    private String judul,deskripsi,pelaksanaan;

    public ModelListTryOut(){}

    public ModelListTryOut(String judul, String deskripsi, String tgl_pelaksanaan) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.pelaksanaan = tgl_pelaksanaan;
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

    public void setPelaksanaan(String tgl_pelaksanaan) {
        this.pelaksanaan = tgl_pelaksanaan;
    }
}
