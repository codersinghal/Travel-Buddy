package com.example.travelbuddy.recommendations;

import com.example.travelbuddy.recommendations.RecommendationData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// read csv file containing data
public class CSVFile {
    InputStream inputStream;
    Map<String, List<RecommendationData>> myMaps = new HashMap<String, List<RecommendationData>>();
    public CSVFile(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public Map<String, List<RecommendationData>> read(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",",-2);
                String[] categories=row[3].split("/",-2);
                for(int i=0;i<categories.length;i++)
                {
                    System.out.println(categories[i]);
                    if(!myMaps.containsKey(categories[i]))
                    {
                        myMaps.put(categories[i],new ArrayList<RecommendationData>());
                    }
                    String title = "";
                    for(int j=4;j<row.length;j++)
                    {
                        title=title+(row[j]+",");
                    }
                    title=title.substring(0,title.length()-1);
                    RecommendationData obj=new RecommendationData(row[1]+" "+row[2],title.toString());
                    myMaps.get(categories[i]).add(obj);
                }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return myMaps;
    }
}
