package com.example.travelbuddy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
    ArrayList<String> urls = new ArrayList<>();
    Context c;
    public ImageAdapter(Context c,ArrayList<String> urls)
    {
        this.urls=urls;
        this.c=c;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.img_card_view, viewGroup, false);
        return new ImageAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
          Picasso.with(c).load(urls.get(position)).into(holder.imageView);
          holder.fab.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Uri bmpUri = getLocalBitmapUri(holder.imageView,position);
                  if (bmpUri != null) {
                      // Construct a ShareIntent with link to image
                      Intent shareIntent = new Intent();
                      shareIntent.setAction(Intent.ACTION_SEND);
                      shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                      shareIntent.setType("image/*");
                      // Launch sharing dialog for image
                      c.startActivity(Intent.createChooser(shareIntent, "Share Image"));
                  } else {

                  }
              }
          });
    }
    public Uri getLocalBitmapUri(ImageView imageView,int position) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            long  hash = 7;
            for (int i = 0; i < urls.get(position).length(); i++) {
                hash = hash*31 + urls.get(position).charAt(i);
            }
           File file =  new File(c.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + hash + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = FileProvider.getUriForFile(c, "com.codepath.fileprovider", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cv;
        ImageView imageView;
        FloatingActionButton fab;

        public MyViewHolder(View view) {
            super(view);
            cv = view.findViewById(R.id.img_card_view);
            imageView=view.findViewById(R.id.img_cv);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            fab=view.findViewById(R.id.share_fab);
        }
    }


}
//
