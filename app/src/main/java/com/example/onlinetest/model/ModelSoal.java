package com.example.onlinetest.model;

public class ModelSoal {
    private String soal,a,b,c,d,e;
    public int selectedAnswerPosition = -1;
    public boolean isAnswered =false;
    public String imgA,imgB,imgC,imgD,imgE,imgSoal;
    public ModelSoal(){};

    public ModelSoal(String soal, String a, String b, String c, String d, String e,String imgA,String imgB,String imgC,String imgD,String imgE,String imgSoal) {
        this.soal = soal;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.imgA = imgA;
        this.imgB = imgB;
        this.imgC = imgC;
        this.imgD = imgD;
        this.imgE = imgE;
        this.imgSoal = imgSoal;
    }

    public String getSoal() {
        return soal;
    }

    public void setSoal(String soal) {
        this.soal = soal;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public int getSelectedAnswerPosition() {
        return selectedAnswerPosition;
    }

    public void setSelectedAnswerPosition(int selectedAnswerPosition) {
        this.selectedAnswerPosition = selectedAnswerPosition;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public String getImgA() {
        return imgA;
    }

    public void setImgA(String imgA) {
        this.imgA = imgA;
    }

    public String getImgB() {
        return imgB;
    }

    public void setImgB(String imgB) {
        this.imgB = imgB;
    }

    public String getImgC() {
        return imgC;
    }

    public void setImgC(String imgC) {
        this.imgC = imgC;
    }

    public String getImgD() {
        return imgD;
    }

    public void setImgD(String imgD) {
        this.imgD = imgD;
    }

    public String getImgE() {
        return imgE;
    }

    public void setImgE(String imgE) {
        this.imgE = imgE;
    }

    public String getImgSoal() {
        return imgSoal;
    }

    public void setImgSoal(String imgSoal) {
        this.imgSoal = imgSoal;
    }
}
