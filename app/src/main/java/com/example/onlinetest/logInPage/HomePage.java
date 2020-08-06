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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePage extends AppCompatActivity {

    Button masuk,daftar;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!= null){
            Intent intent = new Intent(getApplicationContext(),Beranda.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //firebase auth
        mAuth = FirebaseAuth.getInstance();

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

    @Override
    public void onBackPressed() {

    }
}
