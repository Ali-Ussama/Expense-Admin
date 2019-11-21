package com.example.expenseadmin.data.firebase;

import androidx.annotation.Nullable;

import com.example.expenseadmin.pojo.Model.ImageModel;
import com.example.expenseadmin.pojo.Model.LocationModel;
import com.example.expenseadmin.pojo.Model.PlaceModel;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaceFirebaseProcess {

    FirebaseFirestore db;
    String categories_collection = "Categories";
    String restaurant_document = "Restaurant";
    private LocationsFirebaseProcess locationsFirebase;
    private ImagesFirebaseProcess imagesFirebase;

    public PlaceFirebaseProcess() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        this.locationsFirebase = new LocationsFirebaseProcess(db);
        this.imagesFirebase = new ImagesFirebaseProcess();
    }

    public ArrayList<PlaceModel> getPlacesByCategory(String category, String document_name, String subCollection) {
        ArrayList<PlaceModel> placeModels = new ArrayList<>();
        try {
            db.collection(category).document(document_name).collection(subCollection).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e == null) {
                        try {
                            if (queryDocumentSnapshots != null) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                    HashMap<String, Object> data = (HashMap<String, Object>) queryDocumentSnapshot.getData();

                                    ArrayList<LocationModel> locationModels = locationsFirebase.getPlaceLocations(queryDocumentSnapshot.getId());
//                                    ArrayList<ImageModel> image = imagesFirebase;
                                    placeModels.add(new PlaceModel(
                                            0,
                                            (String) data.get("name"),
                                            (String) data.get("category"),
                                            (String) data.get("phone"),
                                            (String) data.get("description"),
                                            (String) data.get("facebookURL"),
                                            (String) data.get("twitterURL"),
                                            (String) data.get("websiteURL"),
                                            locationModels,
                                            0,
                                            0,
                                            0,
                                            new ArrayList<ImageModel>()));

                                }
                            }
                        } catch (Exception ex) {
                            e.printStackTrace();
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return placeModels;
    }
}
