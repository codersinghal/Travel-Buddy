package com.example.travelbuddy.Adapters;

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
import java.util.List;

public class SimpleListAdapter extends ArrayAdapter<String> {

    ArrayList<String> objects;
    Context context;
    public SimpleListAdapter(@NonNull Context context, @NonNull ArrayList<String> objects) {
        super(context,R.layout.listview_layout,objects);
        this.context=context;
        this.objects=objects;
    }
    public View getView(int position, View view, ViewGroup parent) {

        //LayoutInflater inflater=getContext().getLayoutInflater();
        System.out.println("reached here");
        View rowView= LayoutInflater.from(context).inflate(R.layout.listview_layout,parent,false);
        TextView item=rowView.findViewById(R.id.list_item);
        item.setText(objects.get(position));
        return rowView;
    };
}
