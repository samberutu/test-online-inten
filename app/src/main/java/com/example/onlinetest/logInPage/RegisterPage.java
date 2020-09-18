package com.example.onlinetest.logInPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinetest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {

    Button daftar;
    private FirebaseAuth mAuth;
    TextView mEmail,mPassword,mName,mPhone_number;
    private static String TAG = "registrasi";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redister_page);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        //pendefisian
        daftar = findViewById(R.id.btnDaftarReg);
        mEmail = findViewById(R.id.etEmailReg);
        mPassword = findViewById(R.id.etPasswordReg);
        mName = findViewById(R.id.etNamaLengkap);
        mPhone_number = findViewById(R.id.etPhoneNumber);
        final ImageView logoInten = findViewById(R.id.logoInten);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        /*
        //membuat animasi latar belakang
        LinearLayout constraintLayout = findViewById(R.id.registrasiAkun);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
*/
        daftar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String name = mName.getText().toString();
                final String phone_number = mPhone_number.getText().toString();

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Tidak Boleh Kosong");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Tidak Boleh Kosong");
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String userID = mAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = db.collection("user").document(userID);
                                    Map<String,Object> user_data = new HashMap<>();
                                    user_data.put("user_name",name);
                                    user_data.put("user_phone_number",phone_number);
                                    user_data.put("user_email",email);
                                    documentReference.set(user_data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "onSuccess: berhasil menyimpan data di firestore");
                                        }
                                    });

                                    Intent intent = new Intent(getApplicationContext(), Beranda.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                }

                                // ...
                            }
                        });
            }
        });
    }
}
