package com.example.myapplication.InnerDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Yoon
 * @created 2021-09-09
 */
public class SQLite extends SQLiteOpenHelper {

    public SQLite(Context con){
        super(con, "Hphonebook.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE person(" +
                "pNo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "pName VARCHAR(20) NOT NULL, " +
                "pPhoneNumber VARCHAR(50) NOT NULL, " +
                "pEmail VARCHAR(100), " +
                "pResidence VARCHAR(100), " +
                "pMemo TEXT" +
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {

    }
}
