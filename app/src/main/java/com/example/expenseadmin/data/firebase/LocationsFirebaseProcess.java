package com.example.expenseadmin.data.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.expenseadmin.pojo.Model.LocationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class LocationsFirebaseProcess {

    FirebaseFirestore db;
    final String LOCATION_COLLECTION = "locations";
    final String TAG = "LocationsFirebase";

    public LocationsFirebaseProcess(FirebaseFirestore db) {
        this.db = db;
    }

    public LocationsFirebaseProcess() {
        this.db = FirebaseFirestore.getInstance();
    }

    public ArrayList<LocationModel> getPlaceLocations(String placeID) {
        ArrayList<LocationModel> result = new ArrayList<>();
        try {
            db.collection("Location").document(placeID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful() && task.isComplete()) {
                        Log.i(TAG, "LocationsFirebaseProcess(): onComplete(): task is successful");
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot != null) {
                            String id = (String) documentSnapshot.get("id");
                            String country = (String) documentSnapshot.get("country");
                            String city = (String) documentSnapshot.get("country");
                            String street = (String) documentSnapshot.get("country");
                            String place_id = (String) documentSnapshot.get("country");
                            GeoPoint mGeoPoint = (GeoPoint) documentSnapshot.get("latlang");
                            double lat = 0, lang = 0;
                            if (mGeoPoint != null) {
                                lat = mGeoPoint.getLatitude();
                                lang = mGeoPoint.getLongitude();
                            }
                            result.add(new LocationModel(id, place_id, country, city, street, lat, lang));
                        }
                    } else {
                        Log.i(TAG, "LocationsFirebaseProcess(): onComplete(): task is Canceled " + task.isCanceled());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
