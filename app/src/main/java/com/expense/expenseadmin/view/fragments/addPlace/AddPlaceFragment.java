package com.expense.expenseadmin.view.fragments.addPlace;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.expense.expenseadmin.R;
import com.expense.expenseadmin.view.activities.Home.HomeActivity;
import com.expense.expenseadmin.view.activities.mapActivity.MapsActivity;
import com.expense.expenseadmin.view.adapters.AddLocationsRecAdapter;
import com.expense.expenseadmin.view.adapters.AddPhotosRecAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPlaceFragment extends Fragment implements View.OnClickListener {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
            addLocationFab.setOnClickListener(this);
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
                Intent intent = new Intent(getParentActivity(), MapsActivity.class);
                getParentActivity().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
