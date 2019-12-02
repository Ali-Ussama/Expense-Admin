package com.expense.expenseadmin.view.fragments.addPlace;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.expense.expenseadmin.R;
import com.expense.expenseadmin.view.activities.Home.HomeActivity;
import com.expense.expenseadmin.view.activities.mapActivity.MapActivity;
import com.google.android.gms.common.api.Api;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPlaceFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.fragment_add_place_add_location_fab)
    FloatingActionButton addLocationFab;


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

            addLocationFab.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(addLocationFab)) {
            try {
                Intent intent = new Intent(getParentActivity(), MapActivity.class);
                getParentActivity().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
