package com.example.onlinetest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinetest.R;
import com.example.onlinetest.model.ModelSoal;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class AdapterSoalFirestore extends FirestoreRecyclerAdapter<ModelSoal,AdapterSoalFirestore.MyViewHolder> {
    ArrayList<ModelSoal> list;
    // Field when we store position of last clicked item
    private int lastClickedItemPosition;
    private static final String TAG = "MyActivity";
    //List<String> answer = new ArrayList<>();
    String answer[] = new String[100];
    List<Boolean> checked = new ArrayList<>();
    RadioButton radioButton;
    AdapterSoalRecyclerView.ListSize listSize;

    public AdapterSoalFirestore(@NonNull FirestoreRecyclerOptions<ModelSoal> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ModelSoal model) {
        holder.soal.setText(model.getSoal());
        holder.a.setText(model.getA());
        holder.b.setText(model.getB());
        holder.c.setText(model.getC());
        holder.d.setText(model.getD());
        holder.e.setText(model.getE());

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soal,parent,false);
        return new MyViewHolder(view) ;
    }

    public void ubahNilaiJawaban(String value,int position){
        answer[position] = value;
    }

    public String[] getAnswer(){
        return answer;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView soal;
        RadioButton a,b,c,d,e;
        RadioGroup radioGroup;
        int id;

        public MyViewHolder(@NonNull final View itemView){
            super(itemView);
            //TextView
            soal = itemView.findViewById(R.id.tempatSoal);
            a = itemView.findViewById(R.id.a);
            b = itemView.findViewById(R.id.b);
            c = itemView.findViewById(R.id.c);
            d = itemView.findViewById(R.id.d);
            e = itemView.findViewById(R.id.e);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            //radioButton = (RadioButton) itemView;
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    lastClickedItemPosition = getAdapterPosition();
                    id = radioGroup.getCheckedRadioButtonId();
                    radioButton = group.findViewById(id);
                    ubahNilaiJawaban(radioButton.getText().toString(),getAdapterPosition());
                }
            });
        }

    }

}
