package com.example.myapplication.Work;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.myapplication.Common.Persons;
import com.example.myapplication.DTO.PersonDTO;
import com.example.myapplication.InnerDB.Querys;

import java.util.ArrayList;

/**
 * @author Yoon
 * @created 2021-09-09
 * asynctask는 deprecated되었지만, 대체제인 코루틴, RxJAVA를 아직 모르므로...
 */
public class SelectAllPersons extends AsyncTask<Void, Void, Boolean> {

    Context con;
    ProgressDialog dialog;
    Persons persons;
    Querys querys;

    public SelectAllPersons(Context con) {
        this.con = con;
        persons = Persons.getPersons();
        querys = new Querys(con);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(con);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("연락처를 불러오고 있습니다...");
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        ArrayList<PersonDTO> pList = querys.selectAll();
        if (pList == null) {
            return false;
        }
        //초기화 후에 일괄 적용
        Persons persons = Persons.getPersons();
        persons.clear();
        persons.setList(pList);

        dialog.dismiss();
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        dialog.dismiss();
        super.onPostExecute(aBoolean);
    }
}
