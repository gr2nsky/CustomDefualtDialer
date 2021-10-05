package com.example.myapplication.Work;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.myapplication.Common.Persons;
import com.example.myapplication.DTO.PersonDTO;
import com.example.myapplication.InnerDB.Querys;

import java.util.ArrayList;

/**
 * @author Yoon
 * @created 2021-09-10n
 */
public class InsertPerson extends AsyncTask<Void, Void, Boolean> {

    String TAG = "InsertPerson";

    Context con;
    ProgressDialog dialog;
    Persons persons;
    Querys querys;
    PersonDTO person;
    String inputer;

    public InsertPerson(Context con, PersonDTO person, String inputer) {
        this.con = con;
        persons = Persons.getPersons();
        querys = new Querys(con);
        this.person = person;
        this.inputer = inputer;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(con);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("연락처를 추가하고 있습니다...");
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean result;
        try{
            if(inputer.equals("user")){
                result = querys.insertPersonByUser(person);
            } else {
                result = querys.insertPersonByServer(person);
            }
            if(result){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        dialog.dismiss();
        super.onPostExecute(aBoolean);
    }

}
