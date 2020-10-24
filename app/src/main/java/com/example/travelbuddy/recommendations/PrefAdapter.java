package com.example.travelbuddy.recommendations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.travelbuddy.R;
import com.example.travelbuddy.recommendations.PrefrencesData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PrefAdapter extends ArrayAdapter<PrefrencesData> {
    ArrayList<PrefrencesData> list=new ArrayList<>();
    Context context;
    String pics[]={"adventure","nature","culture","beach","historical","hills","pilgrimage","art"};
    boolean arr[]=new boolean[8];
    public PrefAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PrefrencesData> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
        for(int i=0;i<8;i++)
        {
            arr[i]=list.get(i).isSelected();
        }
    }
    @Override
    public int getCount() {
        return super.getCount();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.pref_item, parent,false);
        RelativeLayout fm=v.findViewById(R.id.fm);
        TextView textView = (TextView) v.findViewById(R.id.pref_text);
        ImageView imageView = (ImageView) v.findViewById(R.id.pref_img);
        ImageView imageView1=v.findViewById(R.id.select);
        textView.setText(list.get(position).getType().toUpperCase());


            if(position==0) {
                imageView.setImageResource(R.drawable.adventure);
                fm.setBackgroundColor(ContextCompat.getColor(context, R.color.adventure));
            }
            if(position==1) {
                imageView.setImageResource(R.drawable.nature);
                fm.setBackgroundColor(ContextCompat.getColor(context, R.color.nature));
            }
            if(position==2) {
                imageView.setImageResource(R.drawable.culture);
                fm.setBackgroundColor(ContextCompat.getColor(context, R.color.pilgrim));
            }
            if(position==3) {
                imageView.setImageResource(R.drawable.beach);
                fm.setBackgroundColor(ContextCompat.getColor(context, R.color.beach));
            }
            if(position==4) {
                Picasso.with(context).load(R.drawable.historical).into(imageView);
                fm.setBackgroundColor(ContextCompat.getColor(context, R.color.adventure));
            }
            if(position==5) {
                imageView.setImageResource(R.drawable.hills);
                fm.setBackgroundColor(ContextCompat.getColor(context, R.color.nature));
            }
            if(position==6) {
                imageView.setImageResource(R.drawable.pilgrimage);
                fm.setBackgroundColor(ContextCompat.getColor(context, R.color.pilgrim));
            }
            if(position==7) {
                imageView.setImageResource(R.drawable.art);
                fm.setBackgroundColor(ContextCompat.getColor(context, R.color.beach));
            }

        if(arr[position]==false)
            imageView1.setVisibility(View.GONE);
        fm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arr[position]=!arr[position];
                if(arr[position])
                    imageView1.setVisibility(View.VISIBLE);
                else
                    imageView1.setVisibility(View.GONE);
            }
        });
        return v;

    }
}
