package com.example.myapplication.InnerDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Yoon
 * @created 2021-09-09
 */
public class SQLite extends SQLiteOpenHelper {

    String TAG = "SQLite";

    public SQLite(Context con){
        super(con, "Hphonebook.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            String query =
                    "CREATE TABLE IF NOT EXISTS person(" +
                            "pNo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "pName VARCHAR(20) NOT NULL, " +
                            "pPhoneNumber VARCHAR(50) NOT NULL, " +
                            "pImagePath TEXT," +
                            "pEmail VARCHAR(100), " +
                            "pResidence VARCHAR(100), " +
                            "pMemo TEXT, " +
                            "pIsChanged INTEGER DEFAULT 1," +
                            "pUpdateDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                            ")";
            db.execSQL(query);
        }catch (Exception e){
            Log.d(TAG, "######SQLite Error#####");
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {

    }
}
