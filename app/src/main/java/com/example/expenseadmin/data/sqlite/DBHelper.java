package com.example.expenseadmin.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.expenseadmin.configs.App;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "EXPENSE_Admin.db";
    private static final int DB_VERSION_1 = 1;
    private static final int DB_VERSION_2 = 2;
    private static final int DB_VERSION_3 = 3;
    private static final int DB_VERSION_4 = 4;
    private static final String TAG = "DBHelper";

    private Context mContext;

    /**
     * DATABASE VERSION Changed from V3 to V4
     * By Ali Ussama on 10/9/2019
     */
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION_1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // execute Sql commands for tables.
        ArrayList<String> listSqlStatements = DBConfig.TablesSqlStatements.getAllSqlCreationStatements();
        for (String sqlStatement : listSqlStatements) {
            db.execSQL(sqlStatement);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        switch (oldVersion) {

            case 3:
                //TODO upgrade
                Log.i(TAG, "database version is changed to 4");
                upgradeVersionToV3(db);
                break;
            default:
                onCreate(db);
                break;
        }
    }

    private void upgradeVersionToV3(SQLiteDatabase db) {
        ArrayList<String> bccTablesNamesList = getAllDBTablesNames(mContext);
        if (bccTablesNamesList != null) {
            if (!bccTablesNamesList.isEmpty()) {
                for (String tableName : bccTablesNamesList) {
//                    if (tableName.matches(DBConfig.CustomerDebtBillsTable.TABLE_NAME)) {
                    db.execSQL(DBConfig.TablesSqlStatements.deleteTable(tableName));
//                    }
                }
                onCreate(db);
            }
        }
    }


    private static ArrayList<String> getAllDBTablesNames(Context context) {
        ArrayList<String> listTablesNames = null;
        try {
            String sqlStatement = "SELECT NAME FROM sqlite_master" + " WHERE type='table'";
            Cursor cursor = ((App) context.getApplicationContext()).dbConnect().rawQuery(sqlStatement, null);
            listTablesNames = new ArrayList<>();
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    listTablesNames.add(cursor.getString(0));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception ee) {
            ee.printStackTrace();
        } finally {
            ((App) context.getApplicationContext()).dbDisconnect();
        }
        if (listTablesNames != null) {
            if (!listTablesNames.isEmpty()) {
                listTablesNames.remove(0); // remove metadata table.
            }
        }
        return listTablesNames;
    }
}
