package com.example.travelbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.places_model.PlacesFinalContent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder> {
    ArrayList<PlacesFinalContent> data = new ArrayList<>();
    Context c;
    public PlaceAdapter()
    {

    }

    public PlaceAdapter(Context c,ArrayList<PlacesFinalContent> data)
    {
        this.data=data;
        this.c=c;
    }
    @NonNull
    @Override
    public PlaceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.places_view, viewGroup, false);
        return new PlaceAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.MyViewHolder holder, int position) {
       // Picasso.with(c).load(urls.get(position)).into(holder.imageView);
        holder.name.setText(data.get(position).getName());
        holder.vicinity.setText(data.get(position).getVicinity());
        if(data.get(position).getRating()==0.0)
            holder.rat_bar.setRating(3.0f);
        else
        holder.rat_bar.setRating(data.get(position).getRating().floatValue());
        if(data.get(position).isOpen())
        holder.status.setText("Open");
        else
            holder.status.setText("Close");
        if(data.get(position).getPhotoUrl()==null)
            Picasso.with(c).load(R.drawable.tourist).into(holder.place_img);
        else
        Picasso.with(c).load(data.get(position).getPhotoUrl()).into(holder.place_img);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,vicinity,status;
        ImageView place_img;
        RatingBar rat_bar;

        public MyViewHolder(View view) {
            super(view);
            name=view.findViewById(R.id.place_name);
            status=view.findViewById(R.id.open_now);
            vicinity=view.findViewById(R.id.vicinity);
            rat_bar=view.findViewById(R.id.rating_bar);
            place_img=view.findViewById(R.id.place_img);
            }
    }

}