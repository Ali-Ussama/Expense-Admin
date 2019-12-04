package com.expense.expenseadmin.view.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.expense.expenseadmin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddLocationsRecAdapter extends RecyclerView.Adapter<AddLocationsRecAdapter.viewHolder> {

    private static final String TAG = "AddLocationsRecAdapter";
    private int selectedPosition = -1;
    Context context;

    public AddLocationsRecAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_add_place_locations_rv_row_item, parent, false);

        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class viewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.frag_add_place_locations_rv_ri_place_ic)
        ImageView mIconImage;

        @BindView(R.id.frag_add_place_locations_rv_ri_delete_ic)
        ImageView mDeleteIcon;

        @BindView(R.id.frag_add_place_locations_rv_ri_popup_container)
        CardView detailsCardView;

        viewHolder(@NonNull View itemView) {
            super(itemView);

            try {

                ButterKnife.bind(this, itemView);
                mDeleteIcon.setOnClickListener(e -> {

                    notifyDataSetChanged();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}