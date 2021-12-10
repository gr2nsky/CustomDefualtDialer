package com.example.myapplication.Work;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.myapplication.Common.Persons;
import com.example.myapplication.DTO.PersonDTO;
import com.example.myapplication.InnerDB.Querys;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Yoon
 * @created 2021-09-27
 * 콘텐츠 프로바이더를 사용해 연락처의 데이터를 불러와 sqlite에 저장합니다.
 */

    /*
        1. Resolver 가져오기(데이터베이스 열어주기)
            전화번호부에 구현된 ContentProvider 를 통해 데이터를 가져올 수 있는데,
            이를 가져오기 위해 ContentResolver를 사용한다.
        2. 전화번호가 저장되어있는 테이블 uri를 가져온다.
        3. 테이블의 데이터를 가져온다.
        4. ContentResolver로 provider에 커서를 사용하여 질의 후 반환받는다.

        -> uri마다 불러올 수 있는 정보가 다르다.
           불러오려는 uri종류가 3가지 (phone, structuredPost, Eall)이므로,
           주소록에서 각 항목의 id를 확인해서 id의 정보로 조회하는 방법을 사용한다.
     */

//    private boolean task2(){
//        boolean result = false;
//
//        ContentResolver resolver = con.getContentResolver();
//
//        final String[] projection = new String[] {
//                ContactsContract.RawContacts.CONTACT_ID,
//                ContactsContract.RawContacts.DELETED
//        };
//        //selection == null이면 모든 속성을 가져온다.
//        final Cursor rawContacts = resolver.query(ContactsContract.RawContacts.CONTENT_URI,
//                projection, null, null, null);
//        final int contactIdColumnIndex = rawContacts.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID);
//        final int deletedColumnIndex = rawContacts.getColumnIndex(ContactsContract.RawContacts.DELETED);
//
//        if(rawContacts.moveToNext()){
//            while (rawContacts.isAfterLast() == false){
//                final int contactId = rawContacts.getInt(contactIdColumnIndex);
//                final boolean deleted = rawContacts.getInt(deletedColumnIndex) == 1;
//                if(deleted == false){
//                    Log.d(TAG, "test : " + String.valueOf(contactId));
//                    getContactInfo(contactId);
//                }
//            }
//        }
//
//        return result;
//    }
//
//    private void getContactInfo(int contactId){
//
//    }





public class LoadDeviceContactsTask extends AsyncTask<Void, Void, Boolean>{
    String TAG = "LoadDeviceContacts";
    ProgressDialog dialog;
    Context con;

    public LoadDeviceContactsTask(Context con) {
        this.con = con;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(con);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("연락처를 기기와 동기화하고 있습니다...");
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return task();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        dialog.dismiss();
    }

    private boolean task(){
        Log.d(TAG, "do LoadDeviceContacts task");
        ContentResolver resolver = con.getContentResolver();
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = null;
        boolean result = false;
        ArrayList<PersonDTO> dPerson = new ArrayList<>();

        //[수정요함] uri에 따라 가져오는 값이 다르므로, 이후에 id검색 -> id로 각 uri 쿼리 넣어서 불러오도록 해야 함
        try{
            String[] projection = {
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
//                    ContactsContract.CommonDataKinds.Email.DATA1,
//                    ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS,
//                    ContactsContract.CommonDataKinds.Note.NOTE
            };

            cursor = resolver.query(phoneUri, projection, null, null, null);
            if(cursor != null){
                while(cursor.moveToNext()){
                    int nameIndex = cursor.getColumnIndex(projection[0]);
                    int numberIndex = cursor.getColumnIndex(projection[1]);
//                    int emailIndex = cursor.getColumnIndex(projection[2]);
//                    int cityIndex = cursor.getColumnIndex(projection[3]);

                    // 4.2 해당 index 를 사용해서 실제 값을 가져온다.
                    String name = cursor.getString(nameIndex);
                    String phone = cursor.getString(numberIndex);
//                    String email = cursor.getString(emailIndex);
//                    String city = cursor.getString(cityIndex);
                    Log.d(TAG, "name : " + name + ", phone : " + phone);

                    PersonDTO person = new PersonDTO(name, phone);
                    dPerson.add(person);
                }


                if(!syncContacts(dPerson)){
                    Log.d(TAG, "syncContacts fail");
                    return false;
                }
            }
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "error : " + e.toString());
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
        Log.d(TAG, "end LoadDeviceContacts task");
        return result;
    }

    //Date타입 추가 확인요함
    private boolean syncContacts(ArrayList<PersonDTO> dPersons){
        Querys querys = new Querys(con);
        int pasteToken = 0;
        int indexCousor = 0;

        ArrayList<PersonDTO> sqlitePersons = new ArrayList<>();
        sqlitePersons.addAll(Persons.getPersons().getList());

        Collections.sort(dPersons);
        Collections.sort(sqlitePersons);

        for(PersonDTO dPerson : dPersons){
            int i = indexCousor;
            for(i = 0; i < sqlitePersons.size(); i++){
                if (dPerson.getName().equals(sqlitePersons.get(i).getName())){
                    if(dPerson.getPhoneNumber().equals(sqlitePersons.get(i).getPhoneNumber())){
                        indexCousor = i;
                        pasteToken = 1;
                        break;
                    }
                }
            }
            if(pasteToken == 0){
                querys.insertPersonByUser(dPerson);
                Log.d(TAG, "추가합니다. index = " + i);
                Log.d(TAG, dPerson.pringAll());
            }
            pasteToken = 0;
        }

        return true;
    }


}