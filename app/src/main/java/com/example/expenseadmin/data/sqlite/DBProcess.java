package com.example.expenseadmin.data.sqlite;

import android.content.ContentValues;
import android.content.Context;

import com.example.expenseadmin.configs.App;
import com.example.expenseadmin.pojo.Model.ImageModel;
import com.example.expenseadmin.pojo.Model.LocationModel;
import com.example.expenseadmin.pojo.Model.PlaceModel;

public class DBProcess {


    private Context context;

    public DBProcess(Context context) {
        this.context = context;
    }

    private void insertLocation(LocationModel locationModel) {

        try {
            ContentValues cv = new ContentValues();
            cv.put(DBConfig.LocationsTable.COLUMN_ID, locationModel.getId());
            cv.put(DBConfig.LocationsTable.COLUMN_PLACE_ID, locationModel.getPlaceID());
            cv.put(DBConfig.LocationsTable.COLUMN_COUNTRY, locationModel.getCountry());
            cv.put(DBConfig.LocationsTable.COLUMN_CITY, locationModel.getCity());
            cv.put(DBConfig.LocationsTable.COLUMN_STREET, locationModel.getStreet());
            cv.put(DBConfig.LocationsTable.COLUMN_LATITUDE, locationModel.getLatitude());
            cv.put(DBConfig.LocationsTable.COLUMN_LONGITUDE, locationModel.getLongitude());

            ((App) context).dbConnect().insert(DBConfig.LocationsTable.TABLE_NAME, null, cv);
            ((App) context).dbDisconnect();

        } catch (Exception e) {
            e.printStackTrace();
            ((App) context).dbDisconnect();
        }
    }

    private void insertImage(ImageModel imageModel) {

        try {
            ContentValues cv = new ContentValues();
            cv.put(DBConfig.ImagesTable.COLUMN_PLACE_ID, imageModel.getPlaceID());
            cv.put(DBConfig.ImagesTable.COLUMN_URL, imageModel.getURL());

            ((App) context).dbConnect().insert(DBConfig.ImagesTable.TABLE_NAME, null, cv);
            ((App) context).dbDisconnect();
        } catch (Exception e) {
            e.printStackTrace();
            ((App) context).dbDisconnect();
        }
    }

    private void insertPlace(PlaceModel placeModel) {

        try {
            ContentValues cv = new ContentValues();

            cv.put(DBConfig.PlacesTable.COLUMN_ID, placeModel.getId());
            cv.put(DBConfig.PlacesTable.COLUMN_Name, placeModel.getName());
            cv.put(DBConfig.PlacesTable.COLUMN_CATEGORY, placeModel.getCategory());
            cv.put(DBConfig.PlacesTable.COLUMN_DESCRIPTION, placeModel.getDescription());
            cv.put(DBConfig.PlacesTable.COLUMN_PHONE_NUMBER, placeModel.getPhoneNumber());
            cv.put(DBConfig.PlacesTable.COLUMN_FACEBOOK_URL, placeModel.getFacebookUrl());
            cv.put(DBConfig.PlacesTable.COLUMN_TWITTER_URL, placeModel.getTwitterUrl());
            cv.put(DBConfig.PlacesTable.COLUMN_WEBSITE_URL, placeModel.getWebsiteUrl());
            cv.put(DBConfig.PlacesTable.COLUMN_LIKES_COUNT, placeModel.getLikesCount());
            cv.put(DBConfig.PlacesTable.COLUMN_OKAY_COUNT, placeModel.getOkayCount());
            cv.put(DBConfig.PlacesTable.COLUMN_DISLIKES_COUNT, placeModel.getDislikesCount());

            int placeID = (int) ((App) context).dbConnect().insert(DBConfig.LocationsTable.TABLE_NAME, null, cv);
            ((App) context).dbDisconnect();

            for (LocationModel locationModel : placeModel.getLocationModels()) {
                locationModel.setPlaceID(placeID);
                insertLocation(locationModel);
            }

            for (ImageModel imageModel : placeModel.getImagesURL()) {
                imageModel.setPlaceID(placeID);
                insertImage(imageModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ((App) context).dbDisconnect();
        }
    }


}
