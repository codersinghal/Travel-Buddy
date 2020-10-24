package com.example.travelbuddy.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.home.MainActivity;
import com.example.travelbuddy.R;
import com.example.travelbuddy.places_model.PlacesFinalContent;
import com.google.android.gms.maps.model.LatLng;
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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.MyViewHolder holder, int position) {
       // Picasso.with(c).load(urls.get(position)).into(holder.imageView);
        holder.name.setText(data.get(position).getName());
        holder.vicinity.setText(data.get(position).getVicinity());
        if(data.get(position).getRating()==0.0)
            holder.rat_bar.setRating(1.5f);
        else
        holder.rat_bar.setRating((data.get(position).getRating().floatValue())*3.0f/5);
        if(data.get(position).isOpen()) {

            holder.status.setTextColor(Color.parseColor("#53f53d"));
            holder.status.setText("Open Now");
        }
        else {

            holder.status.setTextColor(Color.parseColor("#f20511"));
            holder.status.setText("Closed");
        }
        if(data.get(position).getPhotoUrl()==null)
            Picasso.with(c).load(R.drawable.tourist).into(holder.place_img);
        else
        Picasso.with(c).load(data.get(position).getPhotoUrl()).into(holder.place_img);
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(c, MainActivity.class);
                intent.putExtra("routeData",new LatLng(data.get(position).getLat(),data.get(position).getLon()));
                c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,vicinity,status;
        ImageView place_img;
        RatingBar rat_bar;
        LinearLayout ll;

        public MyViewHolder(View view) {
            super(view);
            ll=view.findViewById(R.id.places_ll);
            name=view.findViewById(R.id.place_name);
            status=view.findViewById(R.id.open_now);
            vicinity=view.findViewById(R.id.vicinity);
            rat_bar=view.findViewById(R.id.rating_bar);
            place_img=view.findViewById(R.id.place_img);
            }
    }

}
