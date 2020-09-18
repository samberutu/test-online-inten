package com.example.onlinetest.logInPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinetest.R;
import com.example.onlinetest.adapter.AdapterListTryOutFirestore;
import com.example.onlinetest.adapter.AdapterSoalFirestore;
import com.example.onlinetest.dialog.DialogMasukTryout;
import com.example.onlinetest.model.ModelListTryOut;
import com.example.onlinetest.model.ModelSoal;
import com.example.onlinetest.result.CheckMyResult;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class Beranda extends AppCompatActivity {

    private static final String TAG = "beranda";
    ImageButton btn_menu_siswa;
    TextView nama,sekolah;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Handler handler = new Handler();
    private DocumentReference documentReference_user;
    private CollectionReference documentReference_list_try_out;
    public String userID;

    //view pager
    RecyclerView recyclerView;
    AdapterListTryOutFirestore adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        //memberi nilai pada firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        //mendapat data dari yang login
        nama = findViewById(R.id.namaSiswa);
        sekolah = findViewById(R.id.sekolahSiswa);

        //init firebase dan recyclerview
        recyclerView = findViewById(R.id.recyclerViewListTo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //membuat recyclerview seperti viewPAGER
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        setUpRecyclerView();

        documentReference_user = db.collection("user").document(userID);
        documentReference_user.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
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
        //menampilkan recyclerview list try out


    }

    public void setUpRecyclerView(){
        documentReference_list_try_out = db.collection("user").document(userID).collection("list tryout");
        Query query = documentReference_list_try_out.orderBy("pelaksanaan",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<ModelListTryOut> options = new FirestoreRecyclerOptions.Builder<ModelListTryOut>()
                .setQuery(query, ModelListTryOut.class)
                .build();
        adapter = new AdapterListTryOutFirestore(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {

    }

    public void masukKelas(View view) {
        DialogMasukTryout dialogMasukKelas = new DialogMasukTryout(getApplicationContext());
        dialogMasukKelas.show(getSupportFragmentManager(), "Masuk kode Kelas");
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
