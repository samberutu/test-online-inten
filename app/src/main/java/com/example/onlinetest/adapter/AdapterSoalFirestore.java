package com.example.onlinetest.adapter;

import android.content.Context;
import android.graphics.ColorSpace;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import com.example.onlinetest.R;
import com.example.onlinetest.model.ModelSoal;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdapterSoalFirestore extends FirestoreRecyclerAdapter<ModelSoal,AdapterSoalFirestore.MyViewHolder> {

    private int lastClickedItemPosition;
    private static final String TAG = "MyActivity";
    String answer[] = new String[10];
    RadioButton radioButton;
    public Context context;
    private List<ModelSoal> listSoal;

    public AdapterSoalFirestore(Context context,@NonNull FirestoreRecyclerOptions<ModelSoal> options) {
        super(options);
        //variabel option diubah menjadi list agar mudah untuk melakukan perubahan nilai pada model
        //sehingga mengurangi error saat menggunakan radio grub pada pilihan gamnda
        this.listSoal = options.getSnapshots();
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull final ModelSoal model) {

        ModelSoal soal = listSoal.get(position);
        holder.setQuestions(soal.getSoal());
        holder.setOption(soal,position);
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
        ImageView imgA,imgB,imgC,imgD,imgE,imgSoal;
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
            imgA=itemView.findViewById(R.id.ivA);
            imgB=itemView.findViewById(R.id.ivB);
            imgC=itemView.findViewById(R.id.ivC);
            imgD=itemView.findViewById(R.id.ivD);
            imgE=itemView.findViewById(R.id.ivE);
            imgSoal=itemView.findViewById(R.id.ivsoall);
        }

        public void setQuestions(String questions){
            soal.setText(questions);
        }

        public void setOption(final ModelSoal model, int position){
            radioGroup.setTag(position);
            a.setText(model.getA());
            b.setText(model.getB());
            c.setText(model.getC());
            d.setText(model.getD());
            e.setText(model.getE());

            //agar image view tidak kosong
            imgA.setImageDrawable(null);
            imgB.setImageDrawable(null);
            imgC.setImageDrawable(null);
            imgD.setImageDrawable(null);
            imgE.setImageDrawable(null);
            imgSoal.setImageDrawable(null);

            ImageView all_img[] = {imgA,imgB,imgC,imgD,imgE,imgSoal};
            String get_img[] = {model.getImgA(),model.getImgB(),model.getImgC(),model.getImgD(),model.getImgE(),model.getImgSoal(),};

            for(int i = 0;i<all_img.length;i++){
                try{
                    if (!get_img[i].isEmpty()) Picasso.with(context).load(get_img[i]).into(all_img[i]);
                }catch (Exception E){

                }
            }


            if (model.isAnswered){
                radioGroup.check(model.getSelectedAnswerPosition());
            }else {
                radioGroup.check(-1);
            }

            //radioButton = (RadioButton) itemView;
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    lastClickedItemPosition = getAdapterPosition();
                    int pos = (int) group.getTag();
                    ModelSoal soal__ = listSoal.get(pos);
                    soal__.isAnswered = true;
                    soal__.selectedAnswerPosition = checkedId;
                    try {
                        id = radioGroup.getCheckedRadioButtonId();
                        radioButton = group.findViewById(id);
                        ubahNilaiJawaban(radioButton.getText().toString(),lastClickedItemPosition);
                    }catch (Exception e){
                        Log.d(TAG, "belum dicek "+ e);
                    }

                }
            });

        }


    }

}
