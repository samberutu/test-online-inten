package com.example.onlinetest.listener;

import com.example.onlinetest.model.ModelSoal;

import java.util.List;

public interface IfirebaseDone {
    void onFirebaseLoadSuccess(List<ModelSoal> list_soal);
    void onFirebaseLoadFailed(String m);

}
