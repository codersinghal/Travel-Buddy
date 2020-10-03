package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;
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
    private ValueEventListener valueListener;
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
        Toast.makeText(PastTripItem.this,"on create called",Toast.LENGTH_SHORT).show();
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
            src_dest.setText(ptm.getSrc()+" To "+ptm.getDest());
            stdate.setText(ptm.getStdate());
            endate.setText(ptm.getEndate());
            review.setText(ptm.getReview());
            expenditure.setText(ptm.getExpenditure());

        }
        if (obj!=null) {

            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            mRef=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("past_pics").child(ptm.getUid());
            valueListener=mRef.addValueEventListener(new ValueEventListener() {
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
            PastTripsModel addobj = new PastTripsModel(srcs[0].trim(), srcs[1].trim(), review.getText().toString(), stdate.getText().toString(), endate.getText().toString(), expenditure.getText().toString());
            if (obj == null) {
                String this_item_uid = mDatabase.push().getKey();
                addobj.setUID(this_item_uid);
                mDatabase.child(this_item_uid).setValue(addobj);
                mRef=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("past_pics").child(this_item_uid);
                uploadImage(this_item_uid);
            } else {
                String this_item_uid = ptm.getUid();
                mRef=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("past_pics").child(ptm.getUid());
                if(valueListener!=null)
                    mRef.removeEventListener(valueListener);
                uploadImage(this_item_uid);
                addobj.setUID(ptm.getUid());
                mDatabase.child(ptm.getUid()).setValue(addobj);
            }
        }
        if (id == R.id.addimg) {
            if(checkPermissionREAD_EXTERNAL_STORAGE(this))
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
                File file=new File(SiliCompressor.with(this).compress(FileUtils.getPath(this,url),new File(this.getCacheDir(),"temp")));
                Uri uri=Uri.fromFile(file);
                ref = storageReference.child(user.getUid() + "/past_trips/" + this_item_uid+"/"+uri);
                final StorageReference finalRef = ref;
                ref.putFile(uri)
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
                                            file.delete();

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
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot
                                        .getTotalByteCount();
                                progressDialog.setMessage("Saving " + (int) progress + "%");
                            }
                        });
            }

        }

    }

//    private Uri compressFile(Uri url) {
//        if(url!=null)
//        {
//            System.out.println(url);
//            File file=new File(SiliCompressor.with(this).compress(FileUtils.getPath(this,url),new File(this.getCacheDir(),"temp")));
//            Uri uri=Uri.fromFile(file);
//            return uri;
//        }
//        return null;
//    }
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }


    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(PastTripItem.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
}
