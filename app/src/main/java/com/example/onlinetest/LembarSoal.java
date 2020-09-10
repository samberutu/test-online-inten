package com.example.onlinetest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.onlinetest.adapter.AdapterSoalFirestore;
import com.example.onlinetest.model.ModelSoal;
import com.example.onlinetest.result.CheckJawaban;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LembarSoal extends AppCompatActivity {

    private static final String TAG = "coba firestore ";
    //view pager
    RecyclerView recyclerView;
    AdapterSoalFirestore adapter;

    //menambah data ke databas dengan referense auth
    public FirebaseFirestore db;
    public FirebaseAuth mAuth;
    private String user_name;
    //DocumentReference documentReference;
    public String exam_code="kode_soal";
    int session = 0;
    CollectionReference collectionReference;

    //create countdown Timer
    TextView countdown;
    private long max_time = 7200000;

    //another property
    public String session_name[] = new String[]{
            "PENALARAN-UMUM",
            "PENGETAHUA-KUANTITATIF"};
    private int data_count = 10 ;
    String answer[] = new String[100];
    public ArrayList<Button> buttons = new ArrayList<Button>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lembar_soal);
        //Intent
        Intent intent = getIntent();
        //exam_code = intent.getStringExtra("exam code");

        //bagian db
        db =FirebaseFirestore.getInstance();
        collectionReference = db.collection("soal").document(exam_code).collection("PENALARAN-UMUM").document("soal").collection("soal");

        //countDown timer
        countdown = findViewById(R.id.countDownTimer);

        //init firebase dan recyclerview
        recyclerView = findViewById(R.id.recyclerViewSoal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //membuat recyclerview seperti viewPAGER
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        startCountDown();
        setUpRecyclerView();
        //menambah data ke databas dengan referense auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("user").document(mAuth.getCurrentUser().getUid());
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                user_name = value.getString("user_name");
            }
        });

        //membuat tombol otomatis
        createButton();

    }

    private void setUpRecyclerView(){
        Query query = collectionReference.orderBy("soal", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<ModelSoal> options = new FirestoreRecyclerOptions.Builder<ModelSoal>()
                .setQuery(query, ModelSoal.class)
                .build();
        adapter = new AdapterSoalFirestore(options);
        recyclerView.setAdapter(adapter);
        answer = adapter.getAnswer();
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //adapter.stopListening();

    }

    private void startCountDown() {
        new CountDownTimer(max_time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                max_time = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                //finishTest();
                Log.d(TAG, "onFinish: selesai !!");
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (max_time / 1000) / 60;
        int seconds = (int) (max_time / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        countdown.setText(timeLeftFormatted);
    }

    private void createButton(){
        //menambahkan tobol otomatis di card
        int last_row = 4,number =1;
        LinearLayout layout = findViewById(R.id.btnAnswer);

        for (int j = 0; j < 3; j++) {
            LinearLayout row = new LinearLayout(this);
            if (j == 2) last_row = 2;
            for (int i = 0; i < last_row; i++) {
                Button button = new Button(this);
                button.setText("" + number);
                button.setId(number);
                buttons.add(button);
                row.addView(button);
                number++;
            }
            row.setGravity(Gravity.CENTER);
            layout.addView(row);
        }
        btnPosisiSoal();
    }

    private void btnPosisiSoal(){
        for (int i = 0; i < data_count; i++) {
            final int j = i;
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("onClick: Tombol " + buttons.get(j).getId());
                    recyclerView.getLayoutManager().scrollToPosition(j);
                }
            });
        }
    }

    public void btnFinishTest(View view) {
        if (session == 0){
            //nextStep();
            sentYourAnswers();
            db =FirebaseFirestore.getInstance();
            session++;
            collectionReference = db.collection("soal").document(exam_code).collection(session_name[session]);
            setUpRecyclerView();
        }else {
            finishTest();
        }

    }

    public void sentYourAnswers(){
        String jawaban[] = getAnswer();
        DocumentReference documentReference = db.collection("user")
                .document(mAuth.getCurrentUser()
                .getUid())
                .collection("list tryout")
                .document(exam_code).collection("jawaban")
                .document(session_name[session]);
        Map<String, Object> user_data = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            int j = i + 1;
            user_data.put("" + j, jawaban[i]);
        }

        documentReference.set(user_data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Data sudah Terkirim ke firebase !!!");
            }
        });
    }

    private void finishTest() {
        String jawaban[] = getAnswer();
        DocumentReference documentReference = db.collection("user")
                .document(mAuth.getCurrentUser().getUid())
                .collection("list tryout")
                .document(exam_code)
                .collection("jawaban")
                .document(session_name[session]);
        Map<String, Object> user_data = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            int j = i + 1;
            user_data.put("" + j, jawaban[i]);
        }

        documentReference.set(user_data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Data sudah Terkirim ke firebase !!!");
            }
        });


        Intent intent = new Intent(this, CheckJawaban.class);
        intent.putExtra("list_jawaban", getAnswer());
        intent.putExtra("count", data_count);
        startActivity(intent);
    }

    public String[] getAnswer() {

        return answer;
    }
}
