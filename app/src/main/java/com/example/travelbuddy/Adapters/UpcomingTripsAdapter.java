package com.example.travelbuddy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.PastTripItem;
import com.example.travelbuddy.PastTripsAdapter;
import com.example.travelbuddy.PastTripsModel;
import com.example.travelbuddy.R;
import com.example.travelbuddy.UpcomingTripsItem;
import com.example.travelbuddy.trips_model.UpcomingTripsModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UpcomingTripsAdapter extends RecyclerView.Adapter<com.example.travelbuddy.Adapters.UpcomingTripsAdapter.MyViewHolder> {
    Context context;
    ArrayList<UpcomingTripsModel> list;
    View snack_view;
    private int mRecentlyDeletedItemPosition;
    private UpcomingTripsModel mRecentlyDeletedItem;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView src_dest, date_vis;
        public CardView cv;

        public MyViewHolder(View view) {
            super(view);
            src_dest = (TextView) view.findViewById(R.id.src_dest_up);
            date_vis = view.findViewById(R.id.date_up);
            cv = view.findViewById(R.id.card_view_up);
        }
    }

    public UpcomingTripsAdapter(Context context, ArrayList<UpcomingTripsModel> list, View snack_view) {
        this.context = context;
        this.list = list;
        this.snack_view=snack_view;
    }

    @NonNull

    public com.example.travelbuddy.Adapters.UpcomingTripsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.up_trips_cv, viewGroup, false);
        return new com.example.travelbuddy.Adapters.UpcomingTripsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.travelbuddy.Adapters.UpcomingTripsAdapter.MyViewHolder myViewHolder, int i) {
        final int pos=i;
        myViewHolder.src_dest.setText(list.get(i).getSrc() + " - " + list.get(i).getDest());
        myViewHolder.date_vis.setText(list.get(i).getStdate() + " - " + list.get(i).getEndate());
        myViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpcomingTripsItem.class);
                intent.putExtra("data",list.get(pos));
                context.startActivity(intent);
            }
        });

    }

    public int getItemCount() {
        return list.size();
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItem = list.get(position);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("past_trips").child(mRecentlyDeletedItem.getUid());
        mRecentlyDeletedItemPosition = position;
        list.remove(position);
        mDatabase.getRef().removeValue();
        //notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        Snackbar snackbar = Snackbar.make(snack_view, "Deleted",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.example.travelbuddy.Adapters.UpcomingTripsAdapter.this.undoDelete();
            }
        });
        snackbar.show();
    }

    private void undoDelete() {

        mDatabase= FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("past_trips");
        mDatabase.child(mRecentlyDeletedItem.getUid()).setValue(mRecentlyDeletedItem);
        list.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        //notifyItemInserted(mRecentlyDeletedItemPosition);
    }

}