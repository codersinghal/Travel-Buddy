package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.travelbuddy.Adapters.UpcomingTripsAdapter;
import com.example.travelbuddy.trips_model.UpcomingTripsModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class UpcomingTripsActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private RecyclerView rv;
    private ArrayList<UpcomingTripsModel> trips_list;
    private UpcomingTripsAdapter mAdapter;
    AlertDialog pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_trips);
        pb = new SpotsDialog.Builder().setContext(this).build();
        pb.show();
        setFAB();
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        String uid=user.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child(uid).child("upcoming_trips");
        rv=findViewById(R.id.up_trips_rv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

        trips_list=new ArrayList<>();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    trips_list.clear();
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        UpcomingTripsModel l = npsnapshot.getValue(UpcomingTripsModel.class);
                        //l.setUID(npsnapshot.getKey());
                        trips_list.add(l);
                    }
                    mAdapter = new UpcomingTripsAdapter(UpcomingTripsActivity.this, trips_list,findViewById(R.id.relLayout2));
                    setUpRecyclerView();
                }
                pb.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    void setFAB()
    {
        FloatingActionButton fab=findViewById(R.id.add_up_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UpcomingTripsActivity.this,UpcomingTripsItem.class);
                startActivity(intent);
            }
        });
    }
    private void setUpRecyclerView() {
        rv.setAdapter(mAdapter);
        pb.dismiss();
        rv.setLayoutManager(new LinearLayoutManager(this));
        //ItemTouchHelper itemTouchHelper = new
          //      ItemTouchHelper(new SwipeToDeleteCallback(mAdapter));
        //itemTouchHelper.attachToRecyclerView(rv);
    }
}
