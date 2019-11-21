package com.example.expenseadmin.configs;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.example.expenseadmin.data.sqlite.DBHelper;

import java.util.Locale;

public class App extends Application {

    private final String TAG = App.class.getName();
    //    public CurrentLoggedUser mLoggedUser = null;
    private SQLiteDatabase mDataBase = null;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            applyUsDefaultLang();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        //MultiDex.install(base);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Log.v(TAG, "Configuration changed," + newConfig.toString());
        try {
            applyUsDefaultLang();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void applyUsDefaultLang() {
        try {
            Locale.setDefault(Locale.US); // to show numbers in english not arabic.
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.v(TAG, ex.getMessage());
        }
    }

    public void setCurrentLoggedUser(long inventLocId, String username/*, String password*/) {
        try {
//            mLoggedUser = new CurrentLoggedUser(inventLocId, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SQLiteDatabase dbConnect() {
        try {
            if (mDataBase == null) {
                DBHelper helper = new DBHelper(getApplicationContext());
                mDataBase = helper.getWritableDatabase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDataBase;
    }

    public void dbDisconnect() {
        try {
            if (mDataBase != null) mDataBase.close();
            mDataBase = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
