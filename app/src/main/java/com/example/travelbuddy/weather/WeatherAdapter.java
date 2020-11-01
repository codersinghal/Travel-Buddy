package com.example.travelbuddy.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.travelbuddy.R;

import java.util.ArrayList;

public class WeatherAdapter extends ArrayAdapter<WeatherModel> {

    ArrayList<WeatherModel> objects;
    Context context;
    public WeatherAdapter(@NonNull Context context, @NonNull ArrayList<WeatherModel> objects) {
        super(context, R.layout.weather_data, objects);
        this.objects=objects;
        this.context=context;
    }

    public View getView(int position, View view, ViewGroup parent) {

        //LayoutInflater inflater=getContext().getLayoutInflater();
        System.out.println("reached here");
        View rowView=LayoutInflater.from(context).inflate(R.layout.weather_data,null,true);

        TextView date = (TextView) rowView.findViewById(R.id.date);
        TextView temp = (TextView) rowView.findViewById(R.id.temp);
        ImageView img=rowView.findViewById(R.id.weather_icon);

        date.setText(objects.get(position).getDate());
        temp.setText(objects.get(position).getTemp()+"\u2103");
        Context c = img.getContext();
        int id = context.getResources().getIdentifier(objects.get(position).getWeather_icon(), "drawable", c.getPackageName());
        img.setImageResource(id);
        return rowView;
    };
}
