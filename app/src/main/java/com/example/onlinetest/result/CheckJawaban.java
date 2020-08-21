package com.example.onlinetest.result;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.onlinetest.R;
import com.example.onlinetest.logInPage.Beranda;

public class CheckJawaban extends AppCompatActivity {

    private TextView all_answers;
    String answer[] = new String[100];
    Integer count;
    StringBuilder builder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_jawaban);

        all_answers = findViewById(R.id.allAnswer);//Intent
        Intent intent = getIntent();
        answer = intent.getStringArrayExtra("list_jawaban");
        count = intent.getIntExtra("count",0);

        //textVIew
        for (int i = 0;i<count;i++) {
            int j = i + 1;
            builder.append(j+". "+answer[i] + "\n");
        }

        all_answers.setText(builder.toString());

    }

    public void btnDashboard(View view) {
        Intent intent = new Intent(this, Beranda.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}
