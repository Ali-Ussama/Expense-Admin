package com.expense.expenseadmin.view.fragments.addPlace;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.expense.expenseadmin.R;
import com.expense.expenseadmin.view.activities.Home.HomeActivity;
import com.expense.expenseadmin.view.activities.mapActivity.MapsActivity;
import com.expense.expenseadmin.view.adapters.AddLocationsRecAdapter;
import com.expense.expenseadmin.view.adapters.AddPhotosRecAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPlaceFragment extends Fragment implements View.OnClickListener , OnMapReadyCallback, GoogleMap.OnMapClickListener{

    @BindView(R.id.fragment_add_place_add_location_fab)
    FloatingActionButton addLocationFab;

    @BindView(R.id.fragment_add_place_add_photo_fab)
    FloatingActionButton addPhoto;

    @BindView(R.id.fragment_add_place_select_category_spinner)
    Spinner categorySpinner;

    @BindView(R.id.fragment_add_place_title_edit_text)
    EditText nameET;

    @BindView(R.id.fragment_add_place_phone_number_text)
    EditText phoneNoET;

    @BindView(R.id.fragment_add_place_website_edit_text)
    EditText websiteET;

    @BindView(R.id.fragment_add_place_facebook_url_et)
    EditText facebookUrlET;

    @BindView(R.id.fragment_add_place_twitter_edit_text)
    EditText twitterUrlET;

    @BindView(R.id.fragment_add_place_photos_rv)
    RecyclerView mPhotosRV;

    @BindView(R.id.fragment_add_place_locations_rv)
    RecyclerView mLocationsRV;

    @BindView(R.id.fragment_add_place_post_ad_button)
    Button mCreatePlaceButton;

    @BindView(R.id.add_place_locations_bottom_sheet)
    CardView mBottomSheetLayout;

    @BindView(R.id.map_activity_add_location)
    Button mConfirmLocation;

    private static final String TAG = "MapsActivity";
    private static final int ACCESS_LOCATION = 1;
    private GoogleMap mMap;
    private MapsActivity mCurrent;

    BottomSheetBehavior bottomSheetBehavior;

    private AddPhotosRecAdapter mAddPhotosRecAdapter;
    private AddLocationsRecAdapter mAddLocationsRecAdapter;


    public AddPlaceFragment() {
        // Required empty public constructor
    }

    private HomeActivity getParentActivity() {

        return (HomeActivity) getActivity();

    }

    public static AddPlaceFragment newInstance() {

        return new AddPlaceFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_add_place, container, false);

            init(view);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return view;
    }

    private void init(View view) {
        try {
            ButterKnife.bind(this, view);


            initPhotosRV();

            initLocationsRV();

            initBottomSheet();

            addLocationFab.setOnClickListener(this);
            mConfirmLocation.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBottomSheet() {
        try {

            bottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initLocationsRV() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getParentActivity(), RecyclerView.HORIZONTAL, false);
            mAddLocationsRecAdapter = new AddLocationsRecAdapter(getParentActivity());
            mLocationsRV.setLayoutManager(layoutManager);
            mLocationsRV.setAdapter(mAddLocationsRecAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPhotosRV() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getParentActivity(), RecyclerView.HORIZONTAL, false);
            mAddPhotosRecAdapter = new AddPhotosRecAdapter();
            mPhotosRV.setLayoutManager(layoutManager);
            mPhotosRV.setAdapter(mAddPhotosRecAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(addLocationFab)) {
            try {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v.equals(mConfirmLocation)) {
            try {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
