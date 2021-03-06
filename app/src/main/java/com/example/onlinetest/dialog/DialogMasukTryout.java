package com.example.onlinetest.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.onlinetest.LembarSoal;
import com.example.onlinetest.MainActivity;
import com.example.onlinetest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DialogMasukTryout extends AppCompatDialogFragment {

    //bagian firestore
    public FirebaseFirestore db;
    public String exam_code_input;
    public int document_position =0;
    private FirebaseAuth mAuth;
    public String exam_code ;
    public Context context;


    public DialogMasukTryout(Context context){
        this.context = context;
        //this.exam_code = exam_code;
    }


    public String getExam_code_input() {
        return exam_code_input;
    }

    public void setExam_code_input(String exam_code_input) {
        this.exam_code_input = exam_code_input;
    }

    private EditText kodeKelas;
    private String input_main;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_masuk_tryout,null);
        kodeKelas = view.findViewById(R.id.etMasukKelas);
        builder.setView(view)
                .setTitle("Masukkan Kode")
                .setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Intent intent = new Intent(getContext(),MainActivity.class);
                        //getContext().startActivity(intent);

                    }
                })
                .setPositiveButton("masuk", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //memberi nilai pada firebase
                        mAuth = FirebaseAuth.getInstance();
                        db = FirebaseFirestore.getInstance();

                        //dokumen untuk membaca
                        CollectionReference read_data_base = db.collection("soal");
                        read_data_base.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                            String input_code = kodeKelas.getText().toString();
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    List<String> exam_code = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        Map<String, Object> map = document.getData();
                                        String amount = map.get("kode_soal").toString();
                                        exam_code.add(amount);
                                    }
                                    checkExamCode(input_code,exam_code);
                                }else {
                                    Log.d(TAG, "onComplete: empty");
                                }
                            }
                        });



                    }

                });


        return builder.create();
    }

    private void checkExamCode(String input,List<String> db) {

        String result = "empty";
        for (int i = 0 ; i<db.size();i++){
            if (input.equals(db.get(i))){
                result = "result";
                continue;
            }
        }
        
        if (result.equals("result")){
            Log.d(TAG, "checkExamCode: user input "+input);
            checkPastExam(input);
        }else {
            Toast.makeText(context,"Kode Tidak Sesuai",Toast.LENGTH_LONG).show();
        }

    }

    public void checkPastExam(final String input){
        input_main = input;
        CollectionReference user_past_exam = db.collection("user").document(mAuth.getCurrentUser().getUid()).collection("list tryout");
        user_past_exam.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            String input_ = input_main;
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> exam_code = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> map = document.getData();
                        String amount = map.get("kode_soal").toString();
                        exam_code.add(amount);
                    }
                    finalCheckUserExamCode(input_,exam_code);

                }

            }
        });
    }

    public void finalCheckUserExamCode(String input,List<String> db){

        String result = "empty";
        for (int i = 0 ; i<db.size();i++){
            if (input.equals(db.get(i))){
                result = "result";
                continue;
            }
        }

        if (result.equals("result")){
            Toast.makeText(context,"Anda Sudah Pernah Masuk",Toast.LENGTH_LONG).show();

        }else {
            writeNewUserInExamField(input);
            Intent intent = new Intent(context,LembarSoal.class);
            intent.putExtra("exam_code",input);
            context.startActivity(intent);
        }
    }


    public void writeNewUserInExamField(String exam_code_){

        mAuth = FirebaseAuth.getInstance();
        DocumentReference documentReference = db.collection("soal").document(exam_code_).collection("user").document(mAuth.getCurrentUser().getUid());
        final Map<String, Object> data = new HashMap<>();
        data.put("email",mAuth.getCurrentUser().getEmail());
        data.put("PENALARAN-UMUM"," ");
        data.put("PENGETAHUA-KUANTITATIF"," ");

        documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot written with new ID: ");
            }
        });

    }


}
