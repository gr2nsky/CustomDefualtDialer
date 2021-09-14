package com.example.myapplication.Work;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.Activity.PhoneBookActivity;
import com.example.myapplication.Common.Persons;
import com.example.myapplication.DTO.PersonDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Yoon
 * @created 2021-09-14
 */
public class DBParseJSON {
    String TAG = "DBParseJSON";

    Context con;

    public DBParseJSON(Context con) {
        this.con = con;
    }

    public void uploadProcess(){
        if (!getAllData()){
            return;
        }
        JSONObject jsonObject = makeJson(Persons.getPersons().getList());
        if (jsonObject == null){
            errorDialog("경고", "JSON parsing에 실패하였습니다.");
            return;
        }
    }

    public void downloadProcess(){

    }

    private boolean getAllData(){
        SelectAllPersons selectAllPersons = new SelectAllPersons(con);
        try {
            boolean result = selectAllPersons.execute().get();
            if (!result){
                errorDialog("경고", "백업할 데이터가 존재하지 않습니다.");
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            errorDialog("경고", "데이터를 불러오는데 실패했습니다.");
            return false;
        }
        return true;
    }

    private JSONObject makeJson(ArrayList<PersonDTO> personDTOs){
        JSONObject jsonObject = new JSONObject();

        try{
            JSONArray jsonArray = new JSONArray();
            for (PersonDTO person : personDTOs){
                if(person.getIsChanged() == 0) {
                    continue;
                }
                JSONObject pJson = new JSONObject();
                pJson.put("pNo", person.getNo());
                pJson.put("pName", person.getName());
                pJson.put("pPhoneNumber", person.getPhoneNumber());
                pJson.put("pImagePath", person.getImagePath());
                pJson.put("pEmail", person.getEmail());
                pJson.put("pResidence", person.getResidence());
                pJson.put("pMemo", person.getMemo());

                jsonArray.put(pJson);
            }
            jsonObject.put("person", jsonArray);

        }catch (Exception e){
            e.printStackTrace();
            jsonObject = null;
        }

        Log.d(TAG, "DBParseJSON\n" + jsonObject.toString());
        return jsonObject;
    }

    public void errorDialog(String title, String msg) {
        AlertDialog.Builder backBtnDialogBuilder = new AlertDialog.Builder(con)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog backBtnDialog = backBtnDialogBuilder.create();
        backBtnDialog.show();
    }

}
