package com.example.expenseadmin.data.firebase;

import com.example.expenseadmin.pojo.Model.ImageModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ImagesFirebaseProcess {

    FirebaseFirestore db;

    public ImagesFirebaseProcess(FirebaseFirestore db) {
        this.db = db;
    }

    public ImagesFirebaseProcess() {
        db = FirebaseFirestore.getInstance();
    }

    public ArrayList<ImageModel> getPlaceImages(String placeID) {
        ArrayList<ImageModel> result = new ArrayList<>();
        try {
            db.collection(placeID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
