package com.example.travelbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.pref_item, null);
        FrameLayout fm=v.findViewById(R.id.fm);
        TextView textView = (TextView) v.findViewById(R.id.pref_text);
        ImageView imageView = (ImageView) v.findViewById(R.id.pref_img);
        ImageView imageView1=v.findViewById(R.id.select);
        textView.setText(list.get(position).getType());


            if(position==0)
                imageView.setImageResource(R.drawable.adventure);
            if(position==1)
                imageView.setImageResource(R.drawable.nature);
            if(position==2)
                imageView.setImageResource(R.drawable.culture);
            if(position==3)
                imageView.setImageResource(R.drawable.beach);
            if(position==4)
                Picasso.with(context).load(R.drawable.historical).into(imageView);
            if(position==5)
                imageView.setImageResource(R.drawable.hills);
            if(position==6)
                imageView.setImageResource(R.drawable.pilgrimage);
            if(position==7)
                imageView.setImageResource(R.drawable.art);

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
