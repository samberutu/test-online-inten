package com.example.onlinetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.onlinetest.adapter.AdapterSoalRecyclerView;
import com.example.onlinetest.dialog.DialogMasukTryout;
import com.example.onlinetest.model.ModelSoal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //view pager
    RecyclerView recyclerView;
    AdapterSoalRecyclerView adapterSoal;
    DatabaseReference ref;
    ArrayList<ModelSoal> list;
    Context context;
    int banyak_data;
    //LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //membuat full screen pada layout
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // memulai program
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //init firebase dan recyclerview
        ref = FirebaseDatabase.getInstance().getReference("soal");
        recyclerView = findViewById(R.id.recyclerViewSoal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        //membuat recyclerview seperti viewPAGER
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        //menambahkan tobol otomatis di card
        LinearLayout layout = findViewById(R.id.btnAnswer);
        ArrayList<Button> buttons = new ArrayList<>();
        for (int j=0; j< 1; j++){
            LinearLayout row = new LinearLayout(this);
            for(int i = 0; i < 4; i++){
                Button button = new Button(this);
                int nama = j+i+1;
                button.setText(""+nama);
                button.setId(nama);
                buttons.add(button);
                //optional: add your buttons to any layout if you want to see them in your screen
                row.addView(button);
            }
            row.setGravity(Gravity.CENTER);
            layout.addView(row);
        }



    }

    @Override
    protected void onStart() {
        super.onStart();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<ModelSoal> listBos= new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    listBos.add(dataSnapshot1.getValue(ModelSoal.class));
                }
                adapterSoal = new AdapterSoalRecyclerView(listBos);
                recyclerView.setAdapter(adapterSoal);
                banyak_data = adapterSoal.getItemCount();
                //System.out.println("Jumlah item dari basis data adalah "+adapterSoal.getItemCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("salahnya disini broo ListWisataTanggamus");
            }
        });
    }



}
