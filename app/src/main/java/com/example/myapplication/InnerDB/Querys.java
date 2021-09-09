package com.example.myapplication.InnerDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.Common.Persons;
import com.example.myapplication.DTO.PersonDTO;

import java.util.ArrayList;

/**
 * @author Yoon
 * @created 2021-09-09
 */
public class Querys {

    String TAG = "QueryForMain";
    SQLite sqLite;
    Context con;
    SQLiteDatabase db;

    public Querys(Context con) {
        this.con = con;
        sqLite = new SQLite(con);
    }

    public boolean selectAll(){
        //메모리에 유지중인 연락처 리스트 초기화
        Persons persons = Persons.getPersons();
        persons.clear();
        try{
            db = sqLite.getReadableDatabase();
            String query = "SELECT * FROM person ";

            Cursor coursor = db.rawQuery(query, null);
            while (coursor.moveToNext()){
                int no = coursor.getInt(0);
                String name = coursor.getString(1);
                String phoneNumber = coursor.getString(2);
                String imagePath = coursor.getString(3);
                String email = coursor.getString(4);
                String residence = coursor.getString(5);
                String memo = coursor.getString(6);

                PersonDTO person = new PersonDTO(no, name, phoneNumber, imagePath, email, residence, memo);
                persons.append(person);
            }
            coursor.close();
            sqLite.close();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
