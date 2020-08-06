package com.example.onlinetest.logInPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinetest.R;
import com.example.onlinetest.dialog.DialogMasukTryout;
import com.example.onlinetest.result.CheckMyResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Beranda extends AppCompatActivity {

    ImageButton btn_menu_siswa;
    TextView nama,sekolah;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        //memberi nilai pada firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        //mendapat data dari yang login
        nama = findViewById(R.id.namaSiswa);
        sekolah = findViewById(R.id.sekolahSiswa);

        DocumentReference documentReference = db.collection("user").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                nama.setText(value.getString("user_name"));
                sekolah.setText(value.getString("user_email"));
            }
        });


        //membuat menu pada profil siswa untuk logout dan about
        btn_menu_siswa = findViewById(R.id.btnMenuSiswa);
        btn_menu_siswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(Beranda.this,btn_menu_siswa);
                popupMenu.getMenuInflater().inflate(R.menu.menu_siswa,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.about:
                                Toast.makeText(Beranda.this,"about",Toast.LENGTH_SHORT ).show();
                                break;
                            case R.id.logOut:
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getApplicationContext(),HomePage.class);
                                startActivity(intent);
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();

            }
        });
        //akhir membuat menu

    }

    @Override
    public void onBackPressed() {

    }

    public void masukKelas(View view) {
        DialogMasukTryout dialogMasukKelas = new DialogMasukTryout();
        dialogMasukKelas.show(getSupportFragmentManager(), "Masuk kode Kelas");
    }

    public void checkMyResult(View view) {
        Intent intent = new Intent(getApplicationContext(), CheckMyResult.class);
        startActivity(intent);
    }

    class AmbilData extends Thread{

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                    if(signInAccount != null){
                        nama.setText(signInAccount.getDisplayName());
                        sekolah.setText(signInAccount.getEmail());
                    }
                }
            });

        }
    }
}
