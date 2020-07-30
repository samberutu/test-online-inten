package com.example.onlinetest.result;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.onlinetest.R;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class CheckMyResult extends AppCompatActivity {

    CircularProgressIndicator circularProgress,pu,pk,pbm,ppu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_my_result);

        //TOTAL
        circularProgress = findViewById(R.id.cpHasil);
        // you can set max and current progress values individually
        circularProgress.setMaxProgress(40);
        circularProgress.setCurrentProgress(29);
        // or all at once
        //circularProgress.setProgress(9, 10);

        //pu
        pu = findViewById(R.id.cpPu);
        pu.setMaxProgress(10);
        pu.setCurrentProgress(9);
        //pu.setProgress(9,10);

        //pk
        pk = findViewById(R.id.cpPk);
        pk.setMaxProgress(10);
        pk.setCurrentProgress(7);

        //pbm
        pbm = findViewById(R.id.cpPbm);
        pbm.setMaxProgress(10);
        pbm.setCurrentProgress(7);
        //ppu
        ppu= findViewById(R.id.cpPpu);
        ppu.setMaxProgress(10);
        ppu.setCurrentProgress(6);



    }
}
