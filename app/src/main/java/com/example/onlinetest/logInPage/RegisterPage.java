package com.example.onlinetest.logInPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.onlinetest.R;

public class RegisterPage extends AppCompatActivity {

    Button daftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redister_page);

        //pendefisian
        daftar = findViewById(R.id.btnDaftarReg);
        final ImageView logoInten = findViewById(R.id.logoInten);
        /*
        //membuat animasi latar belakang
        LinearLayout constraintLayout = findViewById(R.id.registrasiAkun);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
*/
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Beranda.class);
                startActivity(intent);
            }
        });
    }
}
