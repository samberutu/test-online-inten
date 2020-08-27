package com.example.onlinetest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.onlinetest.adapter.AdapterSoalRecyclerView;
import com.example.onlinetest.dialog.DialogMasukTryout;
import com.example.onlinetest.model.ModelSoal;
import com.example.onlinetest.result.CheckJawaban;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //view pager
    RecyclerView recyclerView;
    AdapterSoalRecyclerView adapterSoal;
    DatabaseReference ref;
    ArrayList<ModelSoal> list;
    Context context;
    int banyak_data;
    //mengambil data dari radio button
    RadioGroup radioGroup;
    private static final String TAG = "MyActivity";
    String answer[] = new String[100];

    //menambah data ke databas dengan referense auth
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    public String user_name;

    //create countdown Timer
    TextView countdown ;
    private CountDownTimer countDownTimer;
    private long max_time = 7200000;

    //menambah tombol
    public ArrayList<Button> buttons = new ArrayList<Button>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //membuat full screen pada layout
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // memulai program
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //countDown timer
        countdown = findViewById(R.id.countDownTimer);

        //init firebase dan recyclerview
        ref = FirebaseDatabase.getInstance().getReference("soal");
        recyclerView = findViewById(R.id.recyclerViewSoal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        //membuat recyclerview seperti viewPAGER
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

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


    }

    private void btnPosisiSoal(){

        for (int i = 0 ; i < banyak_data;i++){
            final int j = i;
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("onClick: Tombol "+buttons.get(j).getId());
                    recyclerView.getLayoutManager().scrollToPosition(j);
                }
            });
        }
    }

    private void startCountDown() {
        new CountDownTimer(max_time,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                max_time = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                finishTest();
            }
        }.start();

    }

    private void updateCountDownText() {
        int minutes = (int) (max_time / 1000) / 60;
        int seconds = (int) (max_time / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        countdown.setText(timeLeftFormatted);
    }

    @Override
    protected void onStart() {
        super.onStart();

        startCountDown();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<ModelSoal> listBos= new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    listBos.add(dataSnapshot1.getValue(ModelSoal.class));
                }
                adapterSoal = new AdapterSoalRecyclerView(listBos);
                recyclerView.setAdapter(adapterSoal);
                setBanyak_data(adapterSoal.getItemCount());
                answer = adapterSoal.getAnswer();
                //membuat tombol otomatis
                createButton(adapterSoal.getItemCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(""+databaseError.toString());
            }
        });

    }


    public void btnFinishTest(View view) {

        finishTest();

        //Log.d(TAG, "jawaban : "+answer[1]);
    }

    private void finishTest() {
        String jawaban[] = getAnswer();
        DocumentReference documentReference = db.collection("jawaban").document(user_name);
        Map<String,Object> user_data = new HashMap<>();
        for(int i = 0 ;i<banyak_data;i++){
            int j = i+1;
            user_data.put(""+j , jawaban[i]);
        }
        
        documentReference.set(user_data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Data sudah Terkirim ke firebase !!!");
            }
        });

        Intent intent = new Intent(this, CheckJawaban.class);
        intent.putExtra("list_jawaban",getAnswer());
        intent.putExtra("count",banyak_data);
        startActivity(intent);
    }

    public String[] getAnswer(){

        return answer;
    }

    private void setBanyak_data(int i){
        this.banyak_data = i;
    }

    public int getBanyak_data(){
        return banyak_data;
    }

    private void createButton(int count){
        //menambahkan tobol otomatis di card
        int columns,columns_sementara,sisa,last_row,number,data_count;
        boolean genap = true;
        data_count = count;
        System.out.println("jumlah data = "+data_count);
        number = 1;
        last_row = 4 ;
        sisa = banyak_data % 4;
        columns_sementara = banyak_data - sisa;
        genap = (sisa == 0 ) ? true : false;
        columns = (columns_sementara % 4 == 0) ?  banyak_data/4 :  (banyak_data/4)+1;

        LinearLayout layout = findViewById(R.id.btnAnswer);
        for (int j=0; j< columns; j++){
            LinearLayout row = new LinearLayout(this);

            if (!genap){
                if (j == columns-1) last_row = sisa;
            }else {

            }
            for(int i = 0; i < last_row; i++){
                Button button = new Button(this);
                button.setText(""+number);
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

}
