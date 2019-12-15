package com.expense.expenseadmin.view.activities.placeDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.expense.expenseadmin.R;
import com.expense.expenseadmin.Utilities.AppUtils;
import com.expense.expenseadmin.Utilities.NumberUtils;
import com.expense.expenseadmin.pojo.Model.LocationModel;
import com.expense.expenseadmin.view.activities.editPlace.EditActivity;
import com.expense.expenseadmin.view.adapters.LocationAdapter;
import com.expense.expenseadmin.view.adapters.MyPagerAdapter;
import com.expense.expenseadmin.pojo.Model.PlaceModel;
import com.expense.expenseadmin.pojo.PlaceImage;
import com.expense.expenseadmin.pojo.locationModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.core.utilities.Utilities;
import com.mancj.slimchart.SlimChart;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class PlaceDetails extends AppCompatActivity implements PlaceDetailsView, View.OnClickListener {

    private static final int CALL_REQUEST_CODE = 1;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.circle)
    CircleIndicator circleIndicator;

    @BindView(R.id.call_fab)
    FloatingActionButton mCallFab;

    @BindView(R.id.seekBar_likes)
    ProgressBar seekBar1;

    @BindView(R.id.seekBar_okays)
    ProgressBar seekBar2;

    @BindView(R.id.seekBar_dislikes)
    ProgressBar seekBar3;

    @BindView(R.id.likes_text)
    TextView likeText;

    @BindView(R.id.okays_text)
    TextView okayText;

    @BindView(R.id.dislikes_text)
    TextView disLikeText;

    @BindView(R.id.RV_places_location)
    RecyclerView recyclerView;

    @BindView(R.id.place_details_activity_description)
    TextView mDescription;

    @BindView(R.id.like_button)
    ImageView likeImage;

    @BindView(R.id.okay_button)
    ImageView okayImage;

    @BindView(R.id.dislike_button)
    ImageView dislikeImage;

    @BindView(R.id.toolbar_place_details)
    Toolbar toolbar;

    @BindView(R.id.toolbar_layout_place_details)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.place_details_activity_facebook)
    ImageView mFacebookIV;

    @BindView(R.id.place_details_activity_twitter)
    ImageView mTwitterIV;

    @BindView(R.id.place_details_activity_web)
    ImageView mWebsiteIV;

    private boolean liked = false;
    private boolean okay = false;
    private boolean disliked = false;
    private boolean favorite = false;

    private MyPagerAdapter myPager;
    private Location mCurrentLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private PlaceDetailsPresenter presenter;

    private ArrayList<Double> distances;
    private ArrayList<LocationModel> locationModels;

    PlaceModel place;

    PlaceDetails mCurrent;
    private static final String TAG = "PlaceDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initLocation() {
        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mCurrentLocation = location;
                                calcDistances();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            mCurrent = PlaceDetails.this;

            place = getIntent().getParcelableExtra(getString(R.string.place_intent_lbl));

            if (place != null) {
                mDescription.setText(place.getDescription());
                Log.i(TAG, "init(): description = " + place.getDescription());
            }

            initToolbar();
            initImagesRV();

            initLocation();
            initRatings();


            presenter = new PlaceDetailsPresenter(this);
            presenter.getLocationsList();
            presenter.getPlaceImagesList();
            presenter.getRatings();
            presenter.getChartRating();

            mCallFab.setOnClickListener(mCurrent);
            likeImage.setOnClickListener(this);
            okayImage.setOnClickListener(this);
            dislikeImage.setOnClickListener(this);
            mFacebookIV.setOnClickListener(this);
            mTwitterIV.setOnClickListener(this);
            mWebsiteIV.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRatings() {
        try {
            int val1 = place.getLikesCount();
            Log.i(TAG, "onGetSeekBarRating: value 1 = " + val1);
            int val2 = place.getOkayCount();
            int val3 = place.getDislikesCount();
            setSeekBars(val1, val2, val3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calcDistances() {
        try {

            Log.i(TAG, "calcDistances(): is called");
            distances = new ArrayList<>();
            locationModels = place.getLocationModels();
            if (mCurrentLocation != null) {
                Log.i(TAG, "calcDistances(): location no null");
                for (LocationModel locationModel : locationModels) {
                    double dist = distance(locationModel.getLatitude(), locationModel.getLongitude(), mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    distances.add(dist);
                    Log.i(TAG, "calcDistances(): distance = " + dist);
                }
            }
            initLocationRV();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initToolbar() {
        try {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            mCollapsingToolbarLayout.setTitle(place.getName());
            mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));
            mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
//            mCollapsingToolbarLayout.setEnabled(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mCollapsingToolbarLayout.setNestedScrollingEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = NumberUtils.round(dist, 1);
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.place_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.place_details_edit_item_menu) {
            editPlace();
            return true;
        } else if (item.getItemId() == R.id.place_details_favorite_item_menu) {
            handleFavorite(item);
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleFavorite(MenuItem item) {
        try {
            if (!favorite) {
                favorite = true;
                item.setIcon(R.drawable.ic_favorite_pink_24dp);
            } else {
                favorite = false;
                item.setIcon(R.drawable.ic_favorite_border_white_24dp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetLocations(ArrayList<locationModel> list) {
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(manager);
//        LocationAdapter adapter = new LocationAdapter(this, locationModels, distances, place.getName());
//        recyclerView.setAdapter(adapter);
    }

    private void initLocationRV() {
        try {
            Log.i(TAG, "initLocationRV(): is called");
            LinearLayoutManager manager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(manager);
            LocationAdapter adapter = new LocationAdapter(this, locationModels, distances, place.getName());
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initImagesRV() {
        try {
            myPager = new MyPagerAdapter(this, place.getImagesURL());
            viewPager.setAdapter(myPager);
            viewPager.setCurrentItem(myPager.getCount() - 1);
            circleIndicator.setViewPager(viewPager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetPlaceImages(ArrayList<PlaceImage> list) {

    }

    @Override
    public void onGetSeekBarRating(ArrayList<Integer> list) {

    }

    @Override
    public void onGetChartRating(double value) {
        SlimChart slimChart = findViewById(R.id.slimChart);

        int[] colors = new int[4];
        colors[0] = Color.rgb(185, 185, 185);//gray
        colors[1] = Color.rgb(50, 203, 0); // green
//        colors[1] = Color.rgb(253, 216, 53); // gold
        slimChart.setColors(colors);

        final float[] stats = new float[2];
        stats[0] = 100;
        stats[1] = (float) value * 10;
        slimChart.setStats(stats);
        //Play animation
        slimChart.setStartAnimationDuration(1500);
        slimChart.playStartAnimation();

        slimChart.setText(Double.toString(value));
        slimChart.setRoundEdges(true);
    }

    private void setSeekBars(int likesCount, int okayCount, int dislikesCount) {
        Log.i(TAG, "setSeekBars: value 1 = " + likesCount);
        seekBar1.setMax(100);
        seekBar2.setMax(100);
        seekBar3.setMax(100);
        String likes = likesCount + getResources().getString(R.string.likes_format);
        String okays = okayCount + getResources().getString(R.string.okay_format);
        String dislikes = dislikesCount + getResources().getString(R.string.dislikes_format);
        likeText.setText(likes);
        okayText.setText(okays);
        disLikeText.setText(dislikes);
        ValueAnimator anim1 = ValueAnimator.ofInt(0, likesCount);
        anim1.setDuration(1500);
        ValueAnimator anim2 = ValueAnimator.ofInt(0, okayCount);
        anim1.setDuration(1500);
        ValueAnimator anim3 = ValueAnimator.ofInt(0, dislikesCount);
        anim1.setDuration(1500);
        anim1.addUpdateListener(animation -> seekBar1.setProgress((Integer) animation.getAnimatedValue()));
        anim1.start();
        anim2.addUpdateListener(animation -> seekBar2.setProgress((Integer) animation.getAnimatedValue()));
        anim2.start();
        anim3.addUpdateListener(animation -> seekBar3.setProgress((Integer) animation.getAnimatedValue()));
        anim3.start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (v.equals(mCallFab)) {
            callPhoneNo();
        } else if (v.equals(likeImage)) {
            try {
                if (!liked) {
                    likeImage.setImageResource(R.drawable.ic_sentiment_satisfied_filled_24dp);
                    liked = true;
                } else {
                    liked = false;
                    likeImage.setImageResource(R.drawable.ic_sentiment_satisfied_24px);
                }

                okay = false;
                disliked = false;
                okayImage.setImageResource(R.drawable.ic_sentiment_neutral_black_24dp);
                dislikeImage.setImageResource(R.drawable.ic_sentiment_dissatisfied_24px);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v.equals(okayImage)) {
            try {
                if (!okay) {
                    okay = true;
                    okayImage.setImageResource(R.drawable.ic_sentiment_neutral_filled_24dp);
                } else {
                    okay = false;
                    okayImage.setImageResource(R.drawable.ic_sentiment_neutral_black_24dp);
                }

                liked = false;
                disliked = false;
                likeImage.setImageResource(R.drawable.ic_sentiment_satisfied_24px);
                dislikeImage.setImageResource(R.drawable.ic_sentiment_dissatisfied_24px);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v.equals(dislikeImage)) {
            try {
                if (!disliked) {
                    disliked = true;
                    dislikeImage.setImageResource(R.drawable.ic_sentiment_dissatisfied_filled_24dp);
                } else {
                    disliked = false;
                    dislikeImage.setImageResource(R.drawable.ic_sentiment_dissatisfied_24px);
                }
                liked = false;
                okay = false;
                likeImage.setImageResource(R.drawable.ic_sentiment_satisfied_24px);
                okayImage.setImageResource(R.drawable.ic_sentiment_neutral_black_24dp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v.equals(mFacebookIV)) {
            handleFacebookIV();
        } else if (v.equals(mTwitterIV)) {
            handleTwitterIV();
        } else if (v.equals(mWebsiteIV)) {
            handleWebsiteIV();
        }
    }

    private void handleWebsiteIV() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleTwitterIV() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleFacebookIV() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editPlace() {
        try {
            Intent intent = new Intent(mCurrent, EditActivity.class);
            intent.putExtra(getString(R.string.place_intent_lbl), place);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callPhoneNo() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mCurrent, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST_CODE);
            } else {
                String uri = "tel:" + place.getPhoneNumber().trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CALL_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhoneNo();
            }
        }
    }
}
