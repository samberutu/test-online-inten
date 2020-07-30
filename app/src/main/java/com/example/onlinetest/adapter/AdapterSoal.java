package com.example.onlinetest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.onlinetest.R;
import com.example.onlinetest.model.ModelSoal;

import java.util.List;

public class AdapterSoal extends PagerAdapter {

    Context context;
    List<ModelSoal> list_soal;
    LayoutInflater inflater;

    public AdapterSoal(Context context, List<ModelSoal> list_soal) {
        this.context = context;
        this.list_soal = list_soal;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list_soal.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object );
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final View view = inflater.inflate(R.layout.soal,container,false);

        //TextView
        TextView soal = view.findViewById(R.id.tempatSoal);
        RadioButton a = view.findViewById(R.id.a);
        RadioButton b = view.findViewById(R.id.b);
        RadioButton c = view.findViewById(R.id.c);
        RadioButton d = view.findViewById(R.id.d);
        RadioButton e = view.findViewById(R.id.e);
        //set ke layout
        soal.setText(list_soal.get(position).getSoal());
        a.setText(list_soal.get(position).getA());
        b.setText(list_soal.get(position).getB());
        c.setText(list_soal.get(position).getC());
        d.setText(list_soal.get(position).getD());
        e.setText(list_soal.get(position).getE());

        container.addView(view);
        return view;
    }
}
