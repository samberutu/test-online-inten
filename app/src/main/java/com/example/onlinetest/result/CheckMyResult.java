package com.example.onlinetest.result;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.onlinetest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.Map;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class CheckMyResult extends AppCompatActivity {
    private static final String TAG = "Check my result";
    CircularProgressIndicator circularProgress,pu,pk,pbm,ppu;
    //menambah data ke databas dengan referense auth
    public FirebaseFirestore db;
    public FirebaseAuth mAuth;
    public String userID;
    //DocumentReference documentReference;
    public String exam_code;
    public CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_my_result);

        //TOTAL
        circularProgress = findViewById(R.id.cpHasil);
        pu = findViewById(R.id.cpPu);
        pk = findViewById(R.id.cpPk);
        pbm = findViewById(R.id.cpPbm);
        ppu= findViewById(R.id.cpPpu);
        // you can set max and current progress values individually


        Intent intent = getIntent();
        exam_code = intent.getStringExtra("exam_code");

        cekResult();

        pbm.setMaxProgress(0);
        pbm.setCurrentProgress(0);
        //ppu
        ppu.setMaxProgress(0);
        ppu.setCurrentProgress(0);

    }

    public void cekResult(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        collectionReference = db.collection("user").document(userID).collection("list tryout").document(exam_code).collection("hasil");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int total = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> map = document.getData();
                        String amount = map.get("nilai").toString();
                        String key = document.getId();
                        total = total+Integer.valueOf(amount);
                        setMyResult(key,Integer.valueOf(amount));
                    }
                    //set bar total
                    setTotal(total);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void setMyResult(String session, int value){

        if (session.equals("PENALARAN-UMUM")){
            pu.setMaxProgress(10);
            pu.setCurrentProgress(value);
        }else if (session.equals("PENGETAHUA-KUANTITATIF")){
            pk.setMaxProgress(10);
            pk.setCurrentProgress(value);
        }else {
            Log.d(TAG, "setMyResult: Tidak ada");
        }

    }

    public void setTotal (int value){
        circularProgress.setMaxProgress(40);
        circularProgress.setCurrentProgress(value);
    }

}
