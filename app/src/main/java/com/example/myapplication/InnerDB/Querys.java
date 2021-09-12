package com.example.myapplication.InnerDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.myapplication.Common.Persons;
import com.example.myapplication.DTO.PersonDTO;

import java.sql.PreparedStatement;
import java.util.ArrayList;

/**
 * @author Yoon
 * @created 2021-09-09
 */
public class Querys {

    String TAG = "Querys";
    SQLite sqLite;
    Context con;
    SQLiteDatabase db;

    public Querys(Context con) {
        this.con = con;
        sqLite = new SQLite(con);
    }

    public ArrayList<PersonDTO> selectAll(){
        ArrayList<PersonDTO> persons = new ArrayList<>();

        try{
            db = sqLite.getReadableDatabase();
            sqLite.onCreate(db);
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
                int isChanged = coursor.getInt(7);

                PersonDTO person = new PersonDTO(no, name, phoneNumber, imagePath, email, residence, memo, isChanged);
                persons.add(person);

                Log.d(TAG, "selectAll wrok : " + no + ", " + name + ", " + phoneNumber + ", " + imagePath);
            }
            coursor.close();
            sqLite.close();
            Log.d(TAG, "selectAll done");

            return persons;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertPerson(PersonDTO person){
        try{
            db = sqLite.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("pName", person.getName());
            values.put("pPhoneNumber", person.getPhoneNumber());
            values.put("pImagePath", person.getImagePath());
            values.put("pEmail", person.getEmail());
            values.put("pResidence", person.getResidence());
            values.put("pMemo", person.getMemo());
            values.put("pIsChanged", person.getIsChanged());

            db.insert("person", null, values);

            Log.d(TAG, "insertPerson done");
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePerson(PersonDTO person){
        try{
            String selection = "pNo = ?";
            String[] selectionArgs =  { Integer.toString(person.getNo()) };
            int deleteRows = db.delete("person", selection, selectionArgs);

            if(deleteRows < 1){
                return false;
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
