package com.example.onlinetest.logInPage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.onlinetest.R;

public class LogInPage extends AppCompatActivity {

    Button masuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);


        masuk =findViewById(R.id.btnMasukLogIn);
        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Beranda.class);
                startActivity(intent);
            }
        });
    }

    public void daftarAkun(View view) {
        final ImageView logoInten =findViewById(R.id.logoInten);
        Intent intent = new Intent(getApplicationContext(), RegisterPage.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LogInPage.this,logoInten,"logoInten");

        startActivity(intent,options.toBundle());
    }
}
