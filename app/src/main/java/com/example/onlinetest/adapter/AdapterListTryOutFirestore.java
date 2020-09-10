package com.example.onlinetest.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinetest.R;
import com.example.onlinetest.model.ModelListTryOut;
import com.example.onlinetest.result.CheckMyResult;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterListTryOutFirestore extends FirestoreRecyclerAdapter<ModelListTryOut,AdapterListTryOutFirestore.MyViewHolder> {

    private static final String TAG = "Adapter list tryout";

    public AdapterListTryOutFirestore(@NonNull FirestoreRecyclerOptions<ModelListTryOut> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ModelListTryOut model) {
        holder.judul.setText(model.getJudul());
        holder.deskripsi.setText(model.getDeskripsi());
        holder.pelaksanaan.setText(model.getPelaksanaan());
        holder.kode_soal.setText(model.getKode_soal());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_try_out,parent,false);
        return new AdapterListTryOutFirestore.MyViewHolder(view) ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView judul,deskripsi,pelaksanaan,kode_soal;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            judul = itemView.findViewById(R.id.tvJudul);
            deskripsi = itemView.findViewById(R.id.tvDeskripsi);
            pelaksanaan = itemView.findViewById(R.id.tvPelaksanaan);
            kode_soal =itemView.findViewById(R.id.tvExamCode);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), CheckMyResult.class);
                    intent.putExtra("exam_code",kode_soal.getText().toString());
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}
