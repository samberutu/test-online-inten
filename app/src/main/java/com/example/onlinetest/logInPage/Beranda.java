package com.example.onlinetest.logInPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.onlinetest.R;
import com.example.onlinetest.dialog.DialogMasukTryout;
import com.example.onlinetest.result.CheckMyResult;

public class Beranda extends AppCompatActivity {

    ImageButton btn_menu_siswa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

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
                                Toast.makeText(Beranda.this,"keluar",Toast.LENGTH_SHORT ).show();
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
}
