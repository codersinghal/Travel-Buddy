package com.example.travelbuddy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.travelbuddy.places_model.PlacesIntentData;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface
 * to handle interaction events.
 * Use the {@link BottomSheet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomSheet extends BottomSheetDialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MainActivity obj;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView atm, restaurant, doctor, movie, mall, tourist,weather,airport,gas;
  //  private OnFragmentInteractionListener mListener;

    public BottomSheet() {
        // Required empty public constructor
    }
    public BottomSheet(MainActivity obj)
    {
        this.obj=obj;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BottomSheet.
     */
    // TODO: Rename and change types and number of parameters
    public static BottomSheet newInstance(String param1, String param2) {
        BottomSheet fragment = new BottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview;
        rootview= inflater.inflate(R.layout.fragment_bottom_sheet2, container, false);
        atm=rootview.findViewById(R.id.atm);
        restaurant=rootview.findViewById(R.id.restaurant);
        doctor=rootview.findViewById(R.id.doctor);
        movie=rootview.findViewById(R.id.movies);
        mall=rootview.findViewById(R.id.malls);
        tourist=rootview.findViewById(R.id.tourist);
        weather=rootview.findViewById(R.id.weather);
        airport=rootview.findViewById(R.id.airports);
        gas=rootview.findViewById(R.id.gas_stations);
        final PlacesIntentData dataobj=new PlacesIntentData(obj.get_curr_lat(),obj.get_curr_long(),null);
        atm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Near_by_places.class);
                dataobj.setPlace_type("atm");
                intent.putExtra("data",dataobj);
                startActivity(intent);
            }
        });
        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Near_by_places.class);
                dataobj.setPlace_type("restaurant");
                intent.putExtra("data",dataobj);
                startActivity(intent);
            }
        });
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Near_by_places.class);
                dataobj.setPlace_type("hospital");
                intent.putExtra("data",dataobj);
                startActivity(intent);
            }
        });
        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Near_by_places.class);
                dataobj.setPlace_type("movie_theater");
                intent.putExtra("data",dataobj);
                startActivity(intent);
            }
        });
        mall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Near_by_places.class);
                dataobj.setPlace_type("shopping_mall");
                intent.putExtra("data",dataobj);
                startActivity(intent);
            }
        });
        tourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Near_by_places.class);
                dataobj.setPlace_type("tourist_attraction");
                intent.putExtra("data",dataobj);
                startActivity(intent);
            }
        });
        airport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Near_by_places.class);
                dataobj.setPlace_type("airport");
                intent.putExtra("data",dataobj);
                startActivity(intent);
            }
        });
        gas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Near_by_places.class);
                dataobj.setPlace_type("gas_station");
                intent.putExtra("data",dataobj);
                startActivity(intent);
            }
        });
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Weather.class);
                intent.putExtra("data",new LatLng(dataobj.getLatitude(),dataobj.getLongitude()));
                startActivity(intent);
            }
        });

        return rootview;
    }


}
