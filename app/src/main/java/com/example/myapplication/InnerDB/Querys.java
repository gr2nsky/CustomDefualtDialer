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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
                String timeStampStr = coursor.getString(8);

                PersonDTO person = new PersonDTO(no, name, phoneNumber, imagePath, email, residence, memo, isChanged, timeStampStr);
                persons.add(person);

                Log.d(TAG, "selectAll wrok : " + no + ", " + name + ", " + phoneNumber + ", " + imagePath + ", " + timeStampStr);
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

    public PersonDTO selectByPhoneNo(String phoneNo){
        PersonDTO person = null;
        String[] selectionArgs = { phoneNo };

        try{
            db = sqLite.getReadableDatabase();
            sqLite.onCreate(db);
            String query = "SELECT * FROM person WHERE pPhoneNumber = ?";

            Cursor coursor = db.rawQuery(query, selectionArgs);
            if (coursor.moveToNext()){
                int no = coursor.getInt(0);
                String name = coursor.getString(1);
                String phoneNumber = coursor.getString(2);
                String imagePath = coursor.getString(3);
                String email = coursor.getString(4);
                String residence = coursor.getString(5);
                String memo = coursor.getString(6);
                int isChanged = coursor.getInt(7);
                String timeStampStr = coursor.getString(8);

                person = new PersonDTO(no, name, phoneNumber, imagePath, email, residence, memo, isChanged, timeStampStr);

                Log.d(TAG, "selectByPhoneNo wrok : " + no + ", " + name + ", " + phoneNumber + ", " + imagePath + ", " + timeStampStr);
            }
            coursor.close();
            sqLite.close();
            Log.d(TAG, "selectByPhoneNo done");

            return person;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Date타입 추가 확인요함
    public boolean insertPersonByUser(PersonDTO person){
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

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String getTime = dateFormat.format(date);

            values.put("pUpdateDate", getTime);

            long newRowId = db.insert("person", null, values);
            if (newRowId == -1){
                return false;
            }

            Log.d(TAG, "insertPersonByUser done");
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //Date타입 추가 확인요함
    public boolean insertPersonByServer(PersonDTO person){
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
            values.put("pUpdateDate", person.getUpdateDate());

            long newRowId = db.insert("person", null, values);
            if (newRowId == -1){
                return false;
            }

            Log.d(TAG, "insertPersonByServer done");
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePerson(PersonDTO person){
        try{
            db = sqLite.getWritableDatabase();

            String selection = "pNo = ?";
            String[] selectionArgs =  { Integer.toString(person.getNo()) };

            int deleteRows = db.delete("person", selection, selectionArgs);
            if(deleteRows < 1){
                return false;
            }
            Log.d(TAG, "deletePerson done");
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //Date타입 추가 확인요함
    public boolean modifyPersonByUser(PersonDTO person){
        try {
            db = sqLite.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("pName", person.getName());
            values.put("pPhoneNumber", person.getPhoneNumber());
            values.put("pImagePath", person.getImagePath());
            values.put("pEmail", person.getEmail());
            values.put("pResidence", person.getResidence());
            values.put("pMemo", person.getMemo());
            values.put("pIsChanged", "1");

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String getTime = dateFormat.format(date);

            values.put("pUpdateDate", getTime);

            String selection = "pNo = ?";
            String[] selectionArgs =  { Integer.toString(person.getNo()) };

            int count = db.update(
                    "person",
                    values,
                    selection,
                    selectionArgs
            );
            if (count < 1){
                return false;
            }

            Log.d(TAG, "modifyPersonByUser done");
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //Date타입 추가 확인요함
    public boolean modifyPersonByServer(PersonDTO sqlitePerson, PersonDTO serverPerson){
        try {
            db = sqLite.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("pName", serverPerson.getName());
            values.put("pPhoneNumber", serverPerson.getPhoneNumber());
            values.put("pImagePath", serverPerson.getImagePath());
            values.put("pEmail", serverPerson.getEmail());
            values.put("pResidence", serverPerson.getResidence());
            values.put("pMemo", serverPerson.getMemo());
            values.put("pIsChanged", "0");
            values.put("pUpdateDate", serverPerson.getUpdateDate());

            String selection = "pNo = ?";
            String[] selectionArgs =  { Integer.toString(sqlitePerson.getNo()) };

            int count = db.update(
                    "person",
                    values,
                    selection,
                    selectionArgs
            );
            if (count < 1){
                return false;
            }

            Log.d(TAG, "modifyPersonByServer done");
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean isChangedRemove(){
        try {

            db = sqLite.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("pIsChanged", "0");

            int count = db.update(
                    "person",
                    values,
                    null,
                    null
            );

            Log.d(TAG, "isChangedRemove done");
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
