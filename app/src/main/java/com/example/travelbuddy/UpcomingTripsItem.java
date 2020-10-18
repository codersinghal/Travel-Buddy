package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.travelbuddy.trips_model.UpcomingTripsModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import static androidx.core.view.ViewCompat.setNestedScrollingEnabled;

public class UpcomingTripsItem extends AppCompatActivity {


    private EditText src,dest;
    private ListView things_lv,places_lv;
    private EditText budget;
    private Object obj=null;
    private FloatingActionButton fab1,fab2;
    private UpcomingTripsModel utm;
    private ArrayAdapter adapter_things,adapter_places;
    private ArrayList<String> things_to_carry,places_to_visit;
    private String[] places;
    private String[] things;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_trips_item);
        src=findViewById(R.id.up_src);
        dest=findViewById(R.id.up_dest);
        things_lv=findViewById((R.id.things_to_carry));
        places_lv=findViewById(R.id.places_to_visit);
        budget=findViewById(R.id.budget);
        fab1=findViewById(R.id.things_fab);
        fab2=findViewById(R.id.places_fab);
        Intent intent = getIntent();
        if (intent.hasExtra("data"))
            obj = intent.getSerializableExtra("data");
        things_to_carry=new ArrayList<>();
        places_to_visit=new ArrayList<>();
        if(obj!=null)
        {
            utm= (UpcomingTripsModel) obj;
            src.setText(utm.getSrc());
            dest.setText(utm.getDest());
            budget.setText(utm.getBudget());
            things_to_carry=utm.getThings_to_carry();
            places_to_visit=utm.getPlaces_to_visit();
            if(things_to_carry!=null)
            things=things_to_carry.toArray(new String[0]);
            if(places_to_visit!=null)
            places=places_to_visit.toArray(new String[0]);
            if(things!=null)
            adapter_things = new ArrayAdapter<String>(this,
                    R.layout.listview_layout, things);
            if(places!=null)
            adapter_places=new ArrayAdapter<String>(this, R.layout.listview_layout,places);
            if(things!=null)
            things_lv.setAdapter(adapter_things);
            if(places!=null)
            places_lv.setAdapter(adapter_places);
        }
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpcomingTripsItem.this);
                builder.setTitle("Add a Thing to carry");

// Set up the input
                final EditText input = new EditText(UpcomingTripsItem.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String new_thing = input.getText().toString();
                        if(things_to_carry==null)
                            things_to_carry=new ArrayList<>();
                        things_to_carry.add(new_thing);
                        dialog.dismiss();
                        setThings();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpcomingTripsItem.this);
                builder.setTitle("Add a Place to visit");

// Set up the input
                final EditText input = new EditText(UpcomingTripsItem.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String new_place = input.getText().toString();
                        if(places_to_visit==null)
                            places_to_visit=new ArrayList<>();
                        places_to_visit.add(new_place);
                        dialog.dismiss();
                        setPlaces();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


            }

        });



    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.prefmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.done)
        {
            if(src.getText()==null) {
                src.setError("Please enter source");
                finish();
            }
            if(dest.getText()==null)
            {
                dest.setError("Please enter destination");
                finish();
            }
            auth=FirebaseAuth.getInstance();
            user=auth.getCurrentUser();
            mDatabase=FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("upcoming_trips");
            if(obj==null)
            {
                String key=mDatabase.push().getKey();
                mDatabase=mDatabase.child(key);
                UpcomingTripsModel new_obj=new UpcomingTripsModel(src.getText().toString(),dest.getText().toString(),budget.getText().toString(),things_to_carry,places_to_visit,"stdate","endate",key);
                mDatabase.setValue(new_obj);
            }
            else
            {
                mDatabase=mDatabase.child(utm.getUid());
                UpcomingTripsModel new_obj=new UpcomingTripsModel(src.getText().toString(),dest.getText().toString(),budget.getText().toString(),things_to_carry,places_to_visit,"stdate","endate",utm.getUid());
                mDatabase.setValue(new_obj);
            }

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setPlaces() {

        places= places_to_visit.toArray(new String[0]);
        adapter_places=new ArrayAdapter<String>(this, R.layout.listview_layout,places);
        places_lv.setAdapter(adapter_places);
    }

    private void setThings() {
        things= things_to_carry.toArray(new String[0]);
        adapter_things=new ArrayAdapter<String>(this, R.layout.listview_layout,things);
        things_lv.setAdapter(adapter_things);
    }
}
