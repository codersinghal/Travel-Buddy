package com.example.travelbuddy.recommendations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.travelbuddy.home.MainActivity;
import com.example.travelbuddy.R;

import java.util.ArrayList;

public class PreferencesActivity extends AppCompatActivity {
    private ListView gv;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    PrefAdapter adapter;
    String arr[]={"adventure","nature","cultural","beaches","historical","hills","pilgrimage","art"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        gv=findViewById(R.id.prefrence_gv);
         sharedpref=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
         editor = sharedpref.edit();
        ArrayList<PrefrencesData> list=new ArrayList<>();
        for(int i=0;i<arr.length;i++)
        {
            PrefrencesData obj=new PrefrencesData();
            if(!sharedpref.contains(arr[i]))
            {
                editor.putBoolean(arr[i],false);
            }
            obj.setType(arr[i]);
            obj.setSelected(sharedpref.getBoolean(arr[i],false));
            list.add(obj);
        }
        adapter=new PrefAdapter(PreferencesActivity.this,R.layout.pref_item,list);
        gv.setAdapter(adapter);
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
            for(int i=0;i<8;i++)
            {
                editor.putBoolean(arr[i],adapter.arr[i]);
            }
            editor.putBoolean("selected",true);
            editor.commit();
            Intent intent = new Intent(PreferencesActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
