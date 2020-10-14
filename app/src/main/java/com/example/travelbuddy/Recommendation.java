package com.example.travelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Recommendation extends AppCompatActivity {

    private static AsyncTask<String,Void,String> threadPool[] = new AsyncTask[20];
    private String url="https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=";
    private String title,extract;
    List<RecAdapData> datalist=new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    VerticalViewPager verticalViewPager;
    List<RecommendationData> mylist=new ArrayList<RecommendationData>();
    Map<String,String> timepass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        getSupportActionBar().hide();
        verticalViewPager = (VerticalViewPager) findViewById(R.id.vPager);
        InputStream inputStream = getResources().openRawResource(R.raw.cities);
        CSVFile csvFile = new CSVFile(inputStream);
        Map<String, List<RecommendationData>> data = csvFile.read();
        timepass=new HashMap<>();
        sharedPreferences=getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        System.out.println("asli list "+data);
        for(Map.Entry<String,List<RecommendationData>> entry:data.entrySet())
        {   System.out.println(entry.getKey());

            if(sharedPreferences.getBoolean(entry.getKey(),false))
            {
                List<RecommendationData> ls=entry.getValue();
                Collections.shuffle(ls);
                System.out.println("shuffled list"+ls);
                for(int i=0;i<Math.min(2,ls.size());i++)
                {
                    mylist.add(ls.get(i));
                }
                for(int i=0;i<ls.size();i++)
                {
                    if(!timepass.containsKey(ls.get(i).getTitle()))
                    {
                        timepass.put(ls.get(i).getTitle(),"");
                    }
                    String temp=timepass.get(ls.get(i).getTitle());
                    temp=temp+entry.getKey()+",";
                    System.out.println("temp = "+temp);
                    timepass.remove(ls.get(i).getTitle());
                    timepass.put(ls.get(i).getTitle(),temp);
                }
            }
        }
        for(int i=0;i<mylist.size();i++)
        {
             threadPool[i]=new LoadData().execute(url+mylist.get(i).getTitle());
        }
        initSwipePager();
    }

    private void initSwipePager(){
    }

    public class LoadData extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... urls) {

            String data="";
            try {
                data=downloadUrl(urls[0]);
                System.out.println(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(data);
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                parseData(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseData(String result) throws JSONException {
        Object obj=new JSONObject(result);
        JSONObject myobj= (JSONObject) obj;
        JSONObject query=myobj.getJSONObject("query").getJSONObject("pages");
        Iterator<String> iter = query.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                Object value = query.get(key);
                JSONObject object=(JSONObject)value;
                title=object.getString("title");
                extract=object.getString("extract");
                datalist.add(new RecAdapData(title,extract,timepass.get(title).substring(0,timepass.get(title).length()-1)));
                System.out.println(datalist);
                verticalViewPager.setAdapter(new VerticlePagerAdapter(this,datalist));
            } catch (JSONException e) {
                // Something went wrong!
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
