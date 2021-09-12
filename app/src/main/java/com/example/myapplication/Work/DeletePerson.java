package com.example.myapplication.Work;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.myapplication.Common.Persons;
import com.example.myapplication.DTO.PersonDTO;
import com.example.myapplication.InnerDB.Querys;

/**
 * @author Yoon
 * @created 2021-09-12
 */
public class DeletePerson extends AsyncTask<Void, Void, Boolean> {

    String TAG = "DeletePerson";

    Context con;
    ProgressDialog dialog;
    Persons persons;
    Querys querys;
    PersonDTO person;

    public DeletePerson(Context con, PersonDTO person) {
        this.con = con;
        persons = Persons.getPersons();
        querys = new Querys(con);
        this.person = person;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(con);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("연락처를 삭제하고 있습니다...");
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try{
            boolean result = querys.deletePerson(person);
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
