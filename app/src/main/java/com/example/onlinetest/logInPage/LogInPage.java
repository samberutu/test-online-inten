package com.example.onlinetest.logInPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinetest.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LogInPage extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    Button masuk;
    private TextView email,password;
    //login menggunakan google
    private FirebaseAuth mAuth;
    private FirebaseAuth mAuth_email_pass;
    private GoogleSignInClient mGoogleSignInClient;
    private static int rc_sign_in = 9001;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!= null){
            Intent intent = new Intent(getApplicationContext(),Beranda.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        //login Menggunankan akun google
        mAuth = FirebaseAuth.getInstance();
        mAuth_email_pass = FirebaseAuth.getInstance();
        //createRequest();
        // login menggunakan email
        email = findViewById(R.id.etUserNameLogin);
        password = findViewById(R.id.etPasswordLogin);

        masuk =findViewById(R.id.btnMasukLogIn);
        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_user = email.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (TextUtils.isEmpty(email_user)){
                    email.setError("Tidak Boleh Kosong");
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    password.setError("Tidak Boleh Kosong");
                    return;
                }

                mAuth_email_pass.signInWithEmailAndPassword(email_user, pass)
                        .addOnCompleteListener(LogInPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(getApplicationContext(), Beranda.class);
                                    startActivity(intent);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LogInPage.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    // ...
                                }

                                // ...
                            }
                        });

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
