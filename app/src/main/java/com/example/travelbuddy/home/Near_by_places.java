package com.example.travelbuddy.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.travelbuddy.Adapters.PlaceAdapter;
import com.example.travelbuddy.ApiInterface;
import com.example.travelbuddy.R;
import com.example.travelbuddy.places_model.Icon;
import com.example.travelbuddy.places_model.PlacesBase;
import com.example.travelbuddy.places_model.PlacesFinalContent;
import com.example.travelbuddy.places_model.PlacesIntentData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Near_by_places extends AppCompatActivity {
    private static final int RADIUS = 10000;
    private RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places);
        rv=findViewById(R.id.places_rv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(mLayoutManager);
        rv.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        Intent intent=getIntent();
        PlacesIntentData dataobj= (PlacesIntentData) intent.getSerializableExtra("data");
        setTitle(dataobj.getPlace_type().toUpperCase());
        getPlaces(dataobj);

    }
    private void getPlaces(PlacesIntentData obj)
    {
        String baseurl="https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);
        Call<PlacesBase> call=service.getNearbyPlaces(obj.getPlace_type(),obj.getLatitude()+","+obj.getLongitude(),RADIUS);
        call.enqueue(new Callback<PlacesBase>() {
            @Override
            public void onResponse(Call<PlacesBase> call, Response<PlacesBase> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                System.out.println(response);
                ArrayList<PlacesFinalContent> list=new ArrayList<>();
                for(int i = 0; i < response.body().getResults().size(); i++) {
                    Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                    Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                    String placeName = response.body().getResults().get(i).getName();
                    String vicinity = response.body().getResults().get(i).getVicinity();
                    Double rating=response.body().getResults().get(i).getRating();
                    if(rating==null)
                        rating=0.0;
                    boolean open_now=false;
                    if(response.body().getResults().get(i).getOpeningHours()!=null) {
                         open_now=response.body().getResults().get(i).getOpeningHours().getOpenNow();
                    }
                    String photoUrl = null;
                    if(response.body().getResults().get(i).getPhotos()!=null&&response.body().getResults().get(i).getPhotos().size()!=0)
                    {
                        List<Icon> icon=response.body().getResults().get(i).getPhotos();
                        String photoref=icon.get(0).getPhotoReference();
                        int height=icon.get(0).getHeight();
                        int width=icon.get(0).getWidth();
                        photoUrl="https://maps.googleapis.com/maps/api/place/photo?photoreference="+photoref+"&sensor=false&maxheight="+height+"&maxwidth="+width+"&key=AIzaSyDYoQybddM6c-Daz0bHVe7h2tuyzxHmW1k";
                    }
                    PlacesFinalContent pfc=new PlacesFinalContent(lat,lng,rating,placeName,vicinity,open_now,photoUrl);
                    list.add(pfc);
                }
                PlaceAdapter adapter=new PlaceAdapter(Near_by_places.this,list);
                rv.setAdapter(adapter);
                System.out.println(list);
            }

            @Override
            public void onFailure(Call<PlacesBase> call, Throwable t) {

            }
        });
    }
}
