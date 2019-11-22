package com.expense.expenseadmin.view.fragments.editPlace;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.expense.expenseadmin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPlaceFragment extends Fragment {

    public EditPlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_place, container, false);
    }
}
