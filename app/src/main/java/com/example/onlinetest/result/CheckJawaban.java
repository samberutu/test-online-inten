package com.example.onlinetest.result;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.onlinetest.R;
import com.example.onlinetest.logInPage.Beranda;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CheckJawaban extends AppCompatActivity {

    private static final String TAG = "CheckJawaban";
    private TextView all_answers;
    String answer[] = new String[100];
    Integer count;
    StringBuilder builder = new StringBuilder();
    //menambah data ke databas dengan referense auth
    public FirebaseFirestore db;
    public FirebaseAuth mAuth;
    public String exam_code="kode_soal";
    CollectionReference collectionReference;
    //another property
    public String session_name[] = new String[]{
            "PENALARAN-UMUM",
            "PENGETAHUA-KUANTITATIF"};
    public Map<String,Object> user_answer_ref;

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

        //method untuk memeriksa hasil ujian peserta
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        examResult();
    }

    public void btnDashboard(View view) {
        Intent intent = new Intent(this, Beranda.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {

    }

    public void examResult(){
        //method untuk memeriksa hasil ujian peserta
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        DocumentReference doc_ref_item;

        int session_ = 0;
        for (int session = 0;session<2;session++){

            final String exam_code_ = exam_code;
            doc_ref_item = db.collection("soal")
                    .document(exam_code)
                    .collection("JAWABAN")
                    .document(session_name[session]);
            final int finalSession = session;
            doc_ref_item.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    //Map<String,Object> user_answer = null;
                    if (task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()){

                            prepareResult(document.getData(), finalSession,exam_code_);

                        }
                    }
                }
            });
        }

    }

    public void prepareResult(final Map<String,Object> user_answer_ref_, int session, String exam_code){
        //final Map<String,Object> user_answer_ref = user_answer_ref_;
        final int session_ = session;
        final String exam_code_ = exam_code;
        DocumentReference doc_ref_user_answer = db.collection("user")
                .document(mAuth.getUid())
                .collection("list tryout")
                .document(exam_code)
                .collection("jawaban")
                .document(session_name[session]);

        doc_ref_user_answer.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        Log.d(TAG, "nilai session saat prepare adalah + "+session_);
                        sentUserResult(user_answer_ref_,document.getData(), session_,exam_code_);

                    }
                }
            }
        });

    }
    public void sentUserResult(Map<String,Object> user_answer_ref_, Map<String,Object> user_answer_ref_user, final int session, String exam_code){

        int user_point = 0;
        Log.d(TAG, "nilai session pada saat mengirim "+session);
        for (Map.Entry<String, Object> ref : user_answer_ref_.entrySet()){

            for (Map.Entry<String, Object> ref_user : user_answer_ref_user.entrySet()){
                if (ref.getValue().toString().equals(ref_user.getValue().toString())){
                    user_point++;
                }
            }

        }

        final  Map<String,Object> user_data = new HashMap<> ();;
        user_data.put("nilai",user_point);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        DocumentReference documentReference = db.collection("user")
                .document(mAuth.getCurrentUser().getUid())
                .collection("list tryout")
                .document(exam_code)
                .collection("hasil")
                .document(session_name[session]);
        documentReference.set(user_data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: jawaban sudah dikoreksi dan sudah masuk ke data base pengguna"+session);
            }
        });

    }

}
