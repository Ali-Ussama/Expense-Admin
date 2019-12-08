package com.expense.expenseadmin.view.fragments.addPlace;

import android.content.Context;

import com.expense.expenseadmin.data.firebase.ImagesFirebaseProcess;
import com.expense.expenseadmin.data.firebase.PlaceFirebaseProcess;
import com.expense.expenseadmin.data.firebase.callbacks.ImageFbListener;
import com.expense.expenseadmin.data.firebase.callbacks.PlaceFirebaseListener;
import com.expense.expenseadmin.pojo.Model.PlaceModel;

import java.io.File;
import java.util.ArrayList;

public class AddPlacePresenter implements PlaceFirebaseListener, ImageFbListener {

    private Context context;
    private PlaceFirebaseProcess placeDb;
    private ImagesFirebaseProcess imageDb;

    private ArrayList<File> imageFiles;
    ArrayList<String> imageUrls;
    private PlaceModel place;

    private AddPlaceView listener;

    public AddPlacePresenter(Context context, AddPlaceView mListener) {
        this.context = context;
        this.placeDb = new PlaceFirebaseProcess(context, this);
        this.imageDb = new ImagesFirebaseProcess(this);
        this.listener = mListener;
    }

    void createNewPlace(PlaceModel placeModel, ArrayList<File> imagesFiles) {
        try {
            imageUrls = new ArrayList<>();
            place = placeModel;
            imageFiles = imagesFiles;
            for (int i = 0; i < imageFiles.size(); i++) {
                imageDb.addPlaceImage(imageFiles.get(i), i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReadPlaceByCategory(ArrayList<PlaceModel> data) {

    }

    @Override
    public void onAddPlaceSuccess(boolean status, Throwable t) {

        if (status) {
            if (listener != null) {
                listener.onAddPlace(true);
            }
        } else if (t != null) {
            if (listener != null) {
                listener.onAddPlace(false);
            }
            t.printStackTrace();
        } else {
            if (listener != null) {
                listener.onAddPlace(false);
            }
        }
    }

    @Override
    public void onAddImageSuccess(String url, int position) {
        if (position == imageFiles.size() - 1) {
            imageUrls.add(url);
            place.setImagesURL(imageUrls);
            placeDb.addPlace(place, this);
        } else {
            imageUrls.add(url);
        }
    }

    @Override
    public void onAddImageFailure(Throwable t) {
        t.printStackTrace();
        listener.onAddPlace(false);
    }
}
