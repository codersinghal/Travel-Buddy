package com.example.travelbuddy.recommendations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.travelbuddy.R;
import com.example.travelbuddy.recommendations.RecAdapData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VerticlePagerAdapter extends PagerAdapter {

    List<RecAdapData> ls;
    Context mContext;
    LayoutInflater mLayoutInflater;

    public VerticlePagerAdapter(Context context, List<RecAdapData> ls) {
        mContext = context;
        this.ls=ls;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.recomm_data, container, false);

        TextView extract = (TextView) itemView.findViewById(R.id.textView);
        TextView title=itemView.findViewById(R.id.rec_title);
        TextView type=itemView.findViewById(R.id.place_type);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            type.setText(ls.get(position).getType().toUpperCase());
            title.setText(ls.get(position).getTitle());
            extract.setText(ls.get(position).getExtract());
        Picasso.with(mContext).load("https://source.unsplash.com/400x300/?"+"city,"+ls.get(position).getType()+ls.get(position).getTitle()).into(imageView);
            //imageView.setImageResource(R.drawable.nature);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
