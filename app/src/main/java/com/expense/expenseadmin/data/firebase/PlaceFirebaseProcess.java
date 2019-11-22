package com.expense.expenseadmin.data.firebase;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.expense.expenseadmin.Utilities.PlaceColumns;
import com.expense.expenseadmin.data.firebase.callbacks.LocationFBListener;
import com.expense.expenseadmin.data.firebase.callbacks.PlaceFirebaseListener;
import com.expense.expenseadmin.data.sqlite.DBProcess;
import com.expense.expenseadmin.pojo.Model.ImageModel;
import com.expense.expenseadmin.pojo.Model.LocationModel;
import com.expense.expenseadmin.pojo.Model.PlaceModel;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaceFirebaseProcess implements LocationFBListener {

    private FirebaseFirestore db;
    private static String TAG = "PlaceFirebase";
    public static String categories_collection = "Categories";
    public static String restaurant_document = "Restaurant";
    public static String places_collection = "places";

    private LocationsFirebaseProcess locationsFirebase;
    private ImagesFirebaseProcess imagesFirebase;
    private DBProcess dbProcess;
    private Context context;
    private PlaceFirebaseListener callback;
    private ArrayList<PlaceModel> placesResult;

    public PlaceFirebaseProcess(Context context, PlaceFirebaseListener callback) {
        this.context = context;
        this.dbProcess = new DBProcess(this.context);
        this.db = FirebaseFirestore.getInstance();
        this.locationsFirebase = new LocationsFirebaseProcess(db, this);
        this.callback = callback;

    }

    public void readPlacesByCategory(String root, String category, String collection) {
        try {
            db.collection(root).document(category).collection(collection).addSnapshotListener((snapshot, e) -> {
                if (e == null) {
                    try {
                        //initializing places result list
                        placesResult = new ArrayList<>();
                        if (snapshot != null) {
                            Log.i(TAG, "readPlacesByCategory(): snapshot not null");
                            Log.i(TAG, "readPlacesByCategory(): snapshot size = " + snapshot.size());

                            for (QueryDocumentSnapshot queryDocumentSnapshot : snapshot) {
                                //initializing location result list
                                ArrayList<LocationModel> locationsResult = new ArrayList<>();
                                //initializing images result list
                                ArrayList<ImageModel> imagesResult = new ArrayList<>();

                                //reading place data from document
                                HashMap<String, Object> data = (HashMap<String, Object>) queryDocumentSnapshot.getData();
                                //reading locations data from document
                                ArrayList<Map<String, String>> locations = (ArrayList<Map<String, String>>) data.get(PlaceColumns.locationModels);
                                //reading images data from document
                                ArrayList<Map<String, String>> images = (ArrayList<Map<String, String>>) queryDocumentSnapshot.get(PlaceColumns.imagesURL);

                                Log.i(TAG, "readPlacesByCategory(): data size = " + data.size());
                                //extracting locations from result
                                extractLocations(locations, locationsResult);
                                //extracting images from result
                                extractImages(imagesResult, images);

                                //extracting place from result
                                PlaceModel placeModel = extractPlace(queryDocumentSnapshot, data, imagesResult, locationsResult);
                                //adding place result into places result list
                                placesResult.add(placeModel);

                                Log.i(TAG, "readPlacesByCategory(): places size = " + placesResult.size());

                            }
                        } else {
                            Log.i(TAG, "readPlacesByCategory(): snapshot is null");
                        }
                        //returning result to callback
                        returningPlacesByCategoryResult(callback, placesResult);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void returningPlacesByCategoryResult(PlaceFirebaseListener callback, ArrayList<PlaceModel> placeModels) {
        try {
            //checking callback nullability
            if (callback != null) {
                //sending result over callback
                callback.onReadPlaceByCategory(placeModels);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void extractLocations(ArrayList<Map<String, String>> locations, ArrayList<LocationModel> locationModels) {
        try {
            //checking if result not null or empty
            if (locations != null && !locations.isEmpty()) {
                //looping over location from FireStore
                for (Map<String, String> location : locations) {
                    Log.i(TAG, "extractLocations(): locations size = " + location.size());
                    try {
                        //extracting location into location model
                        LocationModel locationModel = extractLocation(location);
                        //adding location into list
                        locationModels.add(locationModel);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void extractImages(ArrayList<ImageModel> imageModels, ArrayList<Map<String, String>> images) {
        try {
            //checking if images not null or empty
            if (images != null && !images.isEmpty()) {
                Log.i(TAG, "extractImages(): images size = " + images.size());
                //looping over images from FireStore
                for (Map<String, String> image : images) {
                    try {
                        //extracting result into image model
                        ImageModel imageModel = extractImage(image);
                        //adding image into list
                        imageModels.add(imageModel);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                Log.i(TAG, "extractImages(): images size = 0 ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PlaceModel extractPlace(QueryDocumentSnapshot queryDocumentSnapshot, HashMap<String, Object> data, ArrayList<ImageModel> imageModels, ArrayList<LocationModel> locationModels) {
        return new PlaceModel(
                queryDocumentSnapshot.getId(),
                (String) data.get(PlaceColumns.name),
                (String) data.get(PlaceColumns.category),
                (String) data.get(PlaceColumns.phoneNumber),
                (String) data.get(PlaceColumns.description),
                (String) data.get(PlaceColumns.facebookUrl),
                (String) data.get(PlaceColumns.twitterUrl),
                (String) data.get(PlaceColumns.websiteUrl),
                Integer.parseInt(String.valueOf(data.get(PlaceColumns.likesCount))),
                Integer.parseInt(String.valueOf(data.get(PlaceColumns.okayCount))),
                Integer.parseInt(String.valueOf(data.get(PlaceColumns.dislikesCount))),
                locationModels,
                imageModels
        );
    }

    private ImageModel extractImage(Map<String, String> image) {
        return new ImageModel(
                image.get(PlaceColumns.id),
                image.get(PlaceColumns.placeID),
                image.get(PlaceColumns.url));
    }

    private LocationModel extractLocation(Map<String, String> location) {
        return new LocationModel(
                location.get(PlaceColumns.id),
                location.get(PlaceColumns.placeID),
                location.get(PlaceColumns.country),
                location.get(PlaceColumns.city),
                location.get(PlaceColumns.street),
                Double.parseDouble(String.valueOf(location.get(PlaceColumns.latitude))),
                Double.parseDouble(String.valueOf(location.get(PlaceColumns.longitude))
                ));
    }

    public void addPlaceLocations(PlaceModel placeModel) {
        db.collection(categories_collection).document(restaurant_document).collection(places_collection).add(placeModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                try {
                    if (task.isComplete())
                        Log.i(TAG, "addPlaceLocations(): task is complete");
                    if (task.isSuccessful())
                        Log.i(TAG, "addPlaceLocations(): task is successful");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.i(TAG, "addPlaceLocations(): task is Canceled");
            }
        });
    }

    @Override
    public void onReadPlaceLocation(ArrayList<LocationModel> data, Throwable t) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
