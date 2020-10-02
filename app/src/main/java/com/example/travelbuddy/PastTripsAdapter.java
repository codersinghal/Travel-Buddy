package com.example.travelbuddy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PastTripsAdapter extends RecyclerView.Adapter<PastTripsAdapter.MyViewHolder> {
    Context context;
    ArrayList<PastTripsModel> list;
    View snack_view;
    private int mRecentlyDeletedItemPosition;
    private PastTripsModel mRecentlyDeletedItem;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView src_dest, date_vis;
        public CardView cv;

        public MyViewHolder(View view) {
            super(view);
            src_dest = (TextView) view.findViewById(R.id.src_dest);
            date_vis = view.findViewById(R.id.date);
            cv = view.findViewById(R.id.card_view);
        }
    }

    public PastTripsAdapter(Context context, ArrayList<PastTripsModel> list,View snack_view) {
        this.context = context;
        this.list = list;
        this.snack_view=snack_view;
    }

    @NonNull

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.past_trips_cv, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final int pos=i;
        myViewHolder.src_dest.setText(list.get(i).getSrc() + " - " + list.get(i).getDest());
        myViewHolder.date_vis.setText(list.get(i).getStdate() + " - " + list.get(i).getEndate());
        myViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PastTripItem.class);
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
                PastTripsAdapter.this.undoDelete();
            }
        });
        snackbar.show();
    }

    private void undoDelete() {

        mDatabase= FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("past_trips");
        mDatabase.push().setValue(mRecentlyDeletedItem);
        list.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        //notifyItemInserted(mRecentlyDeletedItemPosition);
    }

}
