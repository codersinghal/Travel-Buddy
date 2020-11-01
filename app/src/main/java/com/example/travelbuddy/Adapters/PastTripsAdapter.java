package com.example.travelbuddy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.R;
import com.example.travelbuddy.trips.PastTripItem;
import com.example.travelbuddy.trips_model.PastTripsModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class PastTripsAdapter extends RecyclerView.Adapter<PastTripsAdapter.MyViewHolder> {
    private static final int PERMISSION_REQUEST_CODE =100 ;
    Context context;
    ArrayList<PastTripsModel> list;
    View snack_view;
    private int mRecentlyDeletedItemPosition;
    private PastTripsModel mRecentlyDeletedItem;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    final List<Target> targets = new ArrayList<Target>();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView src_dest;
        public CardView cv;
        public LinearLayout ll;
        public ImageView share_btn;

        public MyViewHolder(View view) {
            super(view);
            src_dest = (TextView) view.findViewById(R.id.src_dest);
            cv = view.findViewById(R.id.card_view);
            ll=view.findViewById(R.id.past_ll);
            share_btn=view.findViewById(R.id.share_btn2);
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
        myViewHolder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PastTripItem.class);
                intent.putExtra("data",list.get(pos));
                context.startActivity(intent);
            }
        });
        myViewHolder.share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(list.get(i));
            }
        });

    }

    private void share(PastTripsModel pastTripsModel) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        String send="Source - "+pastTripsModel.getSrc()+"\n"+"Destination - "+pastTripsModel.getDest()+"\n"+"Expenditure - "+pastTripsModel.getExpenditure()+"\n"+"Starting Date - "+pastTripsModel.getStdate()+"\n"+"End Date - "+pastTripsModel.getEndate()+"\n"
                +"Reviews - "+pastTripsModel.getReview()+"\n";
        sendIntent.putExtra(Intent.EXTRA_TEXT,send);
        sendIntent.putExtra(Intent.EXTRA_TITLE,"Past Trip");
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Share Via"));
    }
    public int getItemCount() {
        return list.size();
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItem = list.get(position);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("past_trips").child(mRecentlyDeletedItem.getUid());
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

        mDatabase= FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("past_trips");
        mDatabase.child(mRecentlyDeletedItem.getUid()).setValue(mRecentlyDeletedItem);
        list.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        //notifyItemInserted(mRecentlyDeletedItemPosition);
    }



}
