package com.example.onlinetest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinetest.R;
import com.example.onlinetest.model.ModelSoal;

import java.util.ArrayList;

public class AdapterSoalRecyclerView extends RecyclerView.Adapter<AdapterSoalRecyclerView.MyViewHolder> {

    ArrayList<ModelSoal> list;

    public AdapterSoalRecyclerView(ArrayList<ModelSoal> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soal,parent,false);

        return new MyViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSoalRecyclerView.MyViewHolder holder, int position) {
        holder.soal.setText(list.get(position).getSoal());
        holder.a.setText(list.get(position).getA());
        holder.b.setText(list.get(position).getB());
        holder.c.setText(list.get(position).getC());
        holder.d.setText(list.get(position).getD());
        holder.e.setText(list.get(position).getE());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView soal;
        RadioButton a,b,c,d,e;

        public MyViewHolder(@NonNull final View itemView){
            super(itemView);
            //TextView
            soal = itemView.findViewById(R.id.tempatSoal);
            a = itemView.findViewById(R.id.a);
            b = itemView.findViewById(R.id.b);
            c = itemView.findViewById(R.id.c);
            d = itemView.findViewById(R.id.d);
            e = itemView.findViewById(R.id.e);


        }
    }
}
