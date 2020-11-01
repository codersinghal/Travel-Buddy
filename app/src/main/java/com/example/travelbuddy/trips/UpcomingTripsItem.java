package com.example.travelbuddy.trips;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;
import com.example.travelbuddy.Adapters.SimpleListAdapter;
import com.example.travelbuddy.R;
import com.example.travelbuddy.trips_model.UpcomingTripsModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UpcomingTripsItem extends AppCompatActivity {


    private EditText src,dest;
    private ListView things_lv,places_lv,events_lv;
    private EditText budget;
    private Object obj=null;
    private FloatingActionButton fab1,fab2,fab3;
    private UpcomingTripsModel utm;
    private SimpleListAdapter adapter_things,adapter_places,adapter_events;
    private ArrayList<String> things_to_carry,places_to_visit,events_list;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private String currEventtext,CurrEventDate;
    private Calendar mycalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_trips_item);
        src=findViewById(R.id.up_src);
        dest=findViewById(R.id.up_dest);
        things_lv=findViewById((R.id.things_to_carry));
        places_lv=findViewById(R.id.places_to_visit);
        events_lv=findViewById(R.id.events_lv);
        things_lv.setNestedScrollingEnabled(true);
        places_lv.setNestedScrollingEnabled(true);
        events_lv.setNestedScrollingEnabled(true);
        budget=findViewById(R.id.budget);
        fab1=findViewById(R.id.things_fab);
        fab2=findViewById(R.id.places_fab);
        fab3=findViewById(R.id.events_fab);
        dateView();
        Intent intent = getIntent();
        if (intent.hasExtra("data"))
            obj = intent.getSerializableExtra("data");
        things_to_carry=new ArrayList<>();
        places_to_visit=new ArrayList<>();
        events_list=new ArrayList<>();
        if(obj!=null)
        {
            utm= (UpcomingTripsModel) obj;
            src.setText(utm.getSrc());
            dest.setText(utm.getDest());
            budget.setText(utm.getBudget());
            things_to_carry=utm.getThings_to_carry();
            places_to_visit=utm.getPlaces_to_visit();
            events_list=utm.getEvents_list();
            if(things_to_carry!=null)
            {
                adapter_things=new SimpleListAdapter(UpcomingTripsItem.this,things_to_carry);
                things_lv.setAdapter(adapter_things);
            }
            if(places_to_visit!=null)
            {
                adapter_places=new SimpleListAdapter(UpcomingTripsItem.this,places_to_visit);
                places_lv.setAdapter(adapter_places);
            }
            if(events_list!=null)
            {
                adapter_events=new SimpleListAdapter(UpcomingTripsItem.this,events_list);
                events_lv.setAdapter(adapter_events);
            }
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
            mDatabase=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("upcoming_trips");
            if(obj==null)
            {
                String key=mDatabase.push().getKey();
                mDatabase=mDatabase.child(key);
                UpcomingTripsModel new_obj=new UpcomingTripsModel(src.getText().toString(),dest.getText().toString(),budget.getText().toString(),things_to_carry,places_to_visit,"stdate","endate",key,events_list);
                mDatabase.setValue(new_obj);
            }
            else
            {
                mDatabase=mDatabase.child(utm.getUid());
                UpcomingTripsModel new_obj=new UpcomingTripsModel(src.getText().toString(),dest.getText().toString(),budget.getText().toString(),things_to_carry,places_to_visit,"stdate","endate",utm.getUid(),events_list);
                mDatabase.setValue(new_obj);
            }
            Flashbar fb=new Flashbar.Builder(this)
                    .gravity(Flashbar.Gravity.TOP)
                    .title("Updated Successfully")
                    .backgroundColorRes(R.color.quantum_pink300).duration(1500)
                    .enterAnimation(FlashAnim.with(this)
                            .animateBar()
                            .duration(750)
                            .alpha()
                            .overshoot())
                    .exitAnimation(FlashAnim.with(this)
                            .animateBar()
                            .duration(400)
                            .accelerateDecelerate())
                    .build();
            fb.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setPlaces() {

        adapter_places=new SimpleListAdapter(this,places_to_visit);
        places_lv.setAdapter(adapter_places);
    }

    private void setThings() {
        adapter_things=new SimpleListAdapter(this,things_to_carry);
        things_lv.setAdapter(adapter_things);
    }
    public void dateView() {
        mycalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                mycalendar.set(Calendar.YEAR, year);
                mycalendar.set(Calendar.MONTH, monthOfYear);
                mycalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateList();
            }

        };
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpcomingTripsItem.this);
                builder.setTitle("Add an Event");

// Set up the input
                final EditText input = new EditText(UpcomingTripsItem.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currEventtext = input.getText().toString();
                        new DatePickerDialog(UpcomingTripsItem.this, date, mycalendar
                                .get(Calendar.YEAR), mycalendar.get(Calendar.MONTH),
                                mycalendar.get(Calendar.DAY_OF_MONTH)).show();
                        dialog.dismiss();

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

    private void updateList() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        CurrEventDate=sdf.format(mycalendar.getTime());
        if(events_list==null)
            events_list=new ArrayList<>();
        events_list.add(currEventtext+"  -  "+CurrEventDate);
        adapter_events=new SimpleListAdapter(UpcomingTripsItem.this,events_list);
        events_lv.setAdapter(adapter_events);

    }
}
