package com.example.onlinetest.dialog;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DialogMasukTryout extends AppCompatDialogFragment {

    //bagian firestore
    public FirebaseFirestore db;
    public String exam_code_input,exam_code_pass = "kok kosong";
    public int document_position =0;
    private FirebaseAuth mAuth;
    public String[] exam_code = new String[100];


    public DialogMasukTryout(Context context){
    }

    public DialogMasukTryout(String exam_code_input, int document_position, String[] exam_code) {
        this.exam_code_input = exam_code_input;
        this.document_position = document_position;
        this.exam_code = exam_code;
    }

    public String getExam_code_input() {
        return exam_code_input;
    }

    public void setExam_code_input(String exam_code_input) {
        this.exam_code_input = exam_code_input;
    }

    private EditText kodeKelas;
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
                        Intent intent = new Intent(getContext(),MainActivity.class);
                        getContext().startActivity(intent);

                    }
                })
                .setPositiveButton("masuk", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //memberi nilai pada firebase
                        mAuth = FirebaseAuth.getInstance();
                        db = FirebaseFirestore.getInstance();
                        //dokumen untuk menulis
                        final DocumentReference write_data_base = db.collection("user").document(mAuth.getCurrentUser().getUid()).collection("list tryout").document("kode_soal");
                        //dokumen untuk membaca
                        DocumentReference read_data_base = db.collection("soal").document("kode_soal");
                        read_data_base.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()){
                                            Map<String, Object> users = (Map<String, Object>) document.getData() ;
                                            //Log.d(TAG, "onComplete: isinya adalah : "+document.getData());
                                            //Log.d(TAG, "onComplete: isinya adalah : "+users);
                                            write_data_base.set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "onSuccess: data sudah masuk ke data base");
                                                }
                                            });
                                        }
                                    }
                            }
                        });

                        Intent intent = new Intent(getContext(),LembarSoal.class);
                        getContext().startActivity(intent);
                    }

                });


        return builder.create();
    }

    private void checkExamCode() {

        int result=0,index = 0;

        for (int j = 0 ;j<document_position;j++){
            if (exam_code_input.equals(exam_code[j])){
                result++;
                index = j;
                Log.d(TAG, "checkExamCode: "+exam_code[j]);
                break;
            }
        }

        if (result >0){
            Intent intent = new Intent(getActivity().getApplicationContext(),LembarSoal.class);
            intent.putExtra("exam code",exam_code[index]);
            startActivity(intent);
        }else {
            Toast.makeText(getContext(),"Kode Tidak Sesuai",Toast.LENGTH_SHORT);
        }

        //return null;
    }


}
