package com.example.travelbuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("past_pics").child(pastTripsModel.getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                      ArrayList<Uri> imageuris=new ArrayList<>();
                      int cnt= (int) dataSnapshot.getChildrenCount();
                      System.out.println("count is "+cnt);
                      int i=0;
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        String l = npsnapshot.getValue(String.class);
                        System.out.println(l);
                        Target target=new Target() {
                            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Bitmap bmp = bitmap;
                                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, "SomeText", null);
                                imageuris.add(Uri.parse(path));
                                targets.remove(this);
                            }
                            @Override public void onBitmapFailed(Drawable errorDrawable) {
                                System.out.println(errorDrawable);
                                System.out.println("error1");
                                targets.remove(this);
                            }
                            @Override public void onPrepareLoad(Drawable placeHolderDrawable) {
                                System.out.println(placeHolderDrawable);
                                System.out.println("error2");
                            }
                        };
                        targets.add(target);
                        Picasso.with(context).load(l).into(target);

                    }
                    System.out.println(imageuris);
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageuris);
                    shareIntent.setType("image/*");
                    context.startActivity(Intent.createChooser(shareIntent, "Share images to.."));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
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
        mDatabase.child(mRecentlyDeletedItem.getUid()).setValue(mRecentlyDeletedItem);
        list.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        //notifyItemInserted(mRecentlyDeletedItemPosition);
    }



}
