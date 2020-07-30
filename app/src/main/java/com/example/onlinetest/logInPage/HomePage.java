package com.example.onlinetest.logInPage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.onlinetest.R;

public class HomePage extends AppCompatActivity {

    Button masuk,daftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        masuk = findViewById(R.id.btnSignIn);
        daftar = findViewById(R.id.btnSignUp);
        final ImageView logoInten = findViewById(R.id.logoInten);

        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LogInPage.class);

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomePage.this,logoInten,"logoInten");

                startActivity(intent,options.toBundle());
            }
        });
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterPage.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomePage.this,logoInten,"logoInten");

                startActivity(intent,options.toBundle());
            }
        });


    }
}
