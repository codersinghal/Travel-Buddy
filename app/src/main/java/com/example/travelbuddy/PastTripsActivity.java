package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class PastTripsActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE =100 ;
    private RecyclerView rv;
    private PastTripsAdapter mAdapter;
    private ArrayList<PastTripsModel> trips_list;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    AlertDialog pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_trips);
       getSupportActionBar().hide();
        if(checkPermission()==false)
        {
            requestPermission();
        }
        pb = new SpotsDialog.Builder().setContext(this).build();
        pb.show();
        setFAB();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        String uid=user.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child(uid).child("past_trips");
        rv=findViewById(R.id.past_trips_rv);
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
                        PastTripsModel l = npsnapshot.getValue(PastTripsModel.class);
                        //l.setUID(npsnapshot.getKey());
                        trips_list.add(l);
                    }
                    mAdapter = new PastTripsAdapter(PastTripsActivity.this, trips_list,findViewById(R.id.relLayout1));
                    setUpRecyclerView();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    void setFAB()
    {
        FloatingActionButton fab=findViewById(R.id.addfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PastTripsActivity.this,PastTripItem.class);
                startActivity(intent);
            }
        });
    }
    private void setUpRecyclerView() {
        rv.setAdapter(mAdapter);
        pb.dismiss();
        rv.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(rv);
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(PastTripsActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) PastTripsActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(PastTripsActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(PastTripsActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    checkPermission();
                }
                break;

        }
    }
}

