package com.example.travelbuddy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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
    ImageView atm, restaurant, doctor, movie, mall, tourist;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

        atm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(obj.get_curr_lat());
                System.out.println(obj.get_curr_long());
                Toast.makeText(getContext(),obj.get_curr_lat()+" "+obj.get_curr_long(),Toast.LENGTH_SHORT).show();
            }
        });
        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"clicked",Toast.LENGTH_SHORT).show();
            }
        });
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"clicked",Toast.LENGTH_SHORT).show();
            }
        });
        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"clicked",Toast.LENGTH_SHORT).show();
            }
        });
        mall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"clicked",Toast.LENGTH_SHORT).show();
            }
        });
        tourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"clicked",Toast.LENGTH_SHORT).show();
            }
        });
        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

}
