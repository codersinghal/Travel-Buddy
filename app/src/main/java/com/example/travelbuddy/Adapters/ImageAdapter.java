package com.example.travelbuddy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
    ArrayList<String> urls = new ArrayList<>();
    Context c;
    public ImageAdapter(Context c,ArrayList<String> urls)
    {
        this.urls=urls;
        this.c=c;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.img_card_view, viewGroup, false);
        return new ImageAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
          Picasso.with(c).load(urls.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cv;
        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            cv = view.findViewById(R.id.img_card_view);
            imageView=view.findViewById(R.id.img_cv);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }


}
//
