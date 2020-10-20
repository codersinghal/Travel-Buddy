package com.example.travelbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.agrawalsuneet.dotsloader.loaders.CircularDotsLoader;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;


public class Weather extends AppCompatActivity {

    private ArrayList<WeatherModel> list;
    private ListView lv;
    private TextView loc,currtemp,currdesc,currdate,min_max_temp;
    private ProgressDialog progressBar;
    AlertDialog pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         pb= new SpotsDialog.Builder().setContext(this).build();
        pb.show();
        setContentView(R.layout.activity_weather);
        lv=findViewById(R.id.weather_list);
        loc=findViewById(R.id.w_loc);
        currtemp=findViewById(R.id.today_temp);
        currdate=findViewById(R.id.today_date);
        min_max_temp=findViewById(R.id.today_min_max);
        currdesc=findViewById(R.id.today_desc);
        Intent intent=getIntent();
        LatLng obj=intent.getParcelableExtra("data");
        list=new ArrayList<>();
        String url="https://api.weatherbit.io/v2.0/forecast/daily?lat="+obj.latitude+"&lon="+obj.longitude+"&key=29127a4334614bb3b8ace96483869f7c";
        DataParse dp=new DataParse();
        dp.execute(url);

    }
    private class DataParse extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String data="";
            try {
                data=downloadUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(data);
            return data;
        }
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            try {
                parseData(result);
                WeatherAdapter adapter=new WeatherAdapter(Weather.this,list);
                lv.setAdapter(adapter);
                pb.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseData(String result) throws JSONException {
        Object obj=new JSONObject(result);
        JSONObject myobj= (JSONObject) obj;
        JSONArray data=((JSONObject) obj).getJSONArray("data");
        for(int i=0;i<7;i++)
        {
            JSONObject each_obj=data.getJSONObject(i);
            String date=each_obj.getString("datetime");
            String temp=each_obj.getDouble("min_temp")+"  "+each_obj.getDouble("max_temp");
            String desc=each_obj.getJSONObject("weather").getString("description");
            String weather_icon=each_obj.getJSONObject("weather").getString("icon");
            WeatherModel wm=new WeatherModel(date,temp,desc,weather_icon);
            if(i>=1)
            list.add(wm);
            else{
                currdate.setText("Today");
                min_max_temp.setText(temp);
                currdesc.setText(desc);
                String today_temp=each_obj.getString("temp");
                currtemp.setText(today_temp);
                loc.setText(myobj.getString("city_name"));
            }
        }

    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
