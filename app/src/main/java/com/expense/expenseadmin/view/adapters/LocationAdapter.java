package com.expense.expenseadmin.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expense.expenseadmin.R;
import com.expense.expenseadmin.pojo.Model.LocationModel;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyHolder> {


    private Context mContext;
    private ArrayList<LocationModel> data;

    public LocationAdapter(Context mContext, ArrayList<LocationModel> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item
                , parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.mStreet.setText(data.get(position).getStreet());
        holder.mCity.setText(data.get(position).getCity());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        TextView mStreet;
        TextView mCity;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            mStreet = itemView.findViewById(R.id.street);
            mCity = itemView.findViewById(R.id.city);
        }
    }
}

