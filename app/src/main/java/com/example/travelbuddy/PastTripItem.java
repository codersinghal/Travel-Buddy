package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static java.security.AccessController.getContext;

public class PastTripItem extends AppCompatActivity {
    private PastTripsModel ptm;
    private RecyclerView rv_img;
    Object obj = null;
    private FirebaseAuth auth;
    private ImageAdapter adapter;
    private FirebaseUser user;
    private DatabaseReference mDatabase,mRef;
    Calendar mycalendar;
    int flag = 0;
    final int PICK_IMAGE_REQUEST = 71;
    private EditText src_dest, stdate, endate, review, expenditure;
    private ArrayList<Uri> filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_trip_item);
        src_dest=findViewById(R.id.src_to_dest);
        stdate = findViewById(R.id.stdate);
        endate = findViewById(R.id.endate);
        review = findViewById(R.id.review);
        rv_img = findViewById(R.id.recyclerviewimg);
        expenditure = findViewById(R.id.expenditure);
        LinearLayoutManager layoutManager = new LinearLayoutManager(PastTripItem.this, LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        rv_img.setLayoutManager(layoutManager);
        final int radius = getResources().getDimensionPixelSize(R.dimen.radius);
        final int dotsHeight = getResources().getDimensionPixelSize(R.dimen.dots_height);
        final int color = ContextCompat.getColor(PastTripItem.this,R.color.colorPrimary);
        rv_img.addItemDecoration(new DotsIndicatorDecoration(radius, radius * 4, dotsHeight, color, color));
        snapHelper.attachToRecyclerView(rv_img);
        filePath =new ArrayList<Uri>();
        dateView();
        Intent intent = getIntent();
        if (intent.hasExtra("data"))
            obj = intent.getSerializableExtra("data");
        if (obj != null) {
            ptm = (PastTripsModel) obj;
            src_dest.setText(ptm.getSrc()+"  To  "+ptm.getDest());
            stdate.setText(ptm.getStdate());
            endate.setText(ptm.getEndate());
            review.setText(ptm.getReview());
            expenditure.setText(ptm.getExpenditure());

        }
        if (obj!=null) {

            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            mRef=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("past_pics").child(ptm.getUid());
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<String> send=new ArrayList<>();
                    if (dataSnapshot.exists()) {
                        send.clear();
                        for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                            String l = npsnapshot.getValue(String.class);
                            send.add(l);
                        }
                        System.out.println("setting adapter");
                        adapter=new ImageAdapter(PastTripItem.this,send );
                        rv_img.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("past_trips");
            String srcs[]=(src_dest.getText()).toString().split(" To ",-2);
            PastTripsModel addobj = new PastTripsModel(srcs[0], srcs[1], review.getText().toString(), stdate.getText().toString(), endate.getText().toString(), expenditure.getText().toString());
            if (obj == null) {
                String this_item_uid = mDatabase.push().getKey();
                mDatabase.child(this_item_uid).setValue(addobj);
                mRef=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("past_pics").child(this_item_uid);
                uploadImage(this_item_uid);
            } else {
                String this_item_uid = ptm.getUid();
                uploadImage(this_item_uid);
                mDatabase.child(ptm.getUid()).setValue(addobj);
            }
        }
        if (id == R.id.addimg) {
            chooseImage();
        }
        return super.onOptionsItemSelected(item);
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
                updateLabel();
            }

        };
        stdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                flag = 1;
                new DatePickerDialog(PastTripItem.this, date, mycalendar
                        .get(Calendar.YEAR), mycalendar.get(Calendar.MONTH),
                        mycalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        endate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                flag = 2;
                new DatePickerDialog(PastTripItem.this, date, mycalendar
                        .get(Calendar.YEAR), mycalendar.get(Calendar.MONTH),
                        mycalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (flag == 1)
            stdate.setText(sdf.format(mycalendar.getTime()));
        else
            endate.setText(sdf.format(mycalendar.getTime()));
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath.add(data.getData());
        }
    }

    private void uploadImage(String this_item_uid) {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Saving...");
            progressDialog.show();
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            mRef=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("past_pics").child(this_item_uid);
            StorageReference ref;
            for (Uri url : filePath) {
                ref = storageReference.child(user.getUid() + "/past_trips/" + this_item_uid+"/"+url);
                final StorageReference finalRef = ref;
                ref.putFile(url)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                finalRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        URL url = null;
                                        try {
                                            url = new URL(uri.toString());
                                            System.out.println(url);
                                            mRef.push().setValue(url.toString());

                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                Toast.makeText(PastTripItem.this, "Saved", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(PastTripItem.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                if(progress==100.0)
                                progressDialog.setMessage("Saved " + (int) progress + "%");
                            }
                        });
            }

        }

    }

}
