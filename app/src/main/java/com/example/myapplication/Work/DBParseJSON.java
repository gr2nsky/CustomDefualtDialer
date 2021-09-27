package com.example.myapplication.Work;

import android.app.Person;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.Activity.PhoneBookActivity;
import com.example.myapplication.Common.Persons;
import com.example.myapplication.DTO.PersonDTO;
import com.example.myapplication.InnerDB.Querys;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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

    //////////////////////////////////////////////////////////////////
    //                 upload task
    //////////////////////////////////////////////////////////////////
    /*
        1. sqlite 데이터를 select all해 Persons.list를 갱신
        2. Persons.list중, isChange가 1인 값들(최후의 백업이후로 신규 또는 수정된 값)만 배열에 담음
        3. 배열을 json으로 parsing해 PersonBackUpTask로 http communication을 aync task로 수행
        4. http communication이 정상적으로 수행되었다면,
           2의 배열의 pIsChange값을 0으로 바꾸도록 sqlite 질의
     */
    public boolean uploadProcess(){
        boolean result = false;
        ArrayList<PersonDTO> changedPerson = new ArrayList<>();

        if (!getAllData()){
            return false;
        }

        for (PersonDTO person : Persons.getPersons().getList()) {
            if (person.getIsChanged() == 0) {
                continue;
            }
            changedPerson.add(person);
        }
        JSONObject jsonObject = makeJson(changedPerson);
        if (jsonObject == null){
            errorDialog("경고", "JSON parsing에 실패하였습니다.");
            return false;
        }

        PersonBackUpTask personBackUpTask = new PersonBackUpTask(con, jsonObject);
        try{
            result = personBackUpTask.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }

        if (!result){
            errorDialog("경고", "네트워크 통신에 실패하였습니다.");
            return false;
        }

        ArrayList<String> pNoArrayList = new ArrayList<>();
        for(PersonDTO person : changedPerson){
            pNoArrayList.add(Integer.toString(person.getNo()));
        }
        String[] pNos = pNoArrayList.toArray(new String[pNoArrayList.size()]);
        Querys querys = new Querys(con);
        querys.isChangedRemove(pNos);

        return true;
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

    //////////////////////////////////////////////////////////////////
    //                 download task
    //////////////////////////////////////////////////////////////////
    /*
        1. 서버의 DB data를 json 형식의 String 타입으로 받는다.
        2. 해당 String 타입을 json 형식으로 parsing해 배열에 저장한다.
        3. SQLite에 질의하며 데이터를 넣는다
            [Server DB는 보존이 아닌 저장의 개념]
     */

    public boolean downloadProcess(){
        LoadPersonTask loadPersonTask = new LoadPersonTask(con);
        ArrayList<PersonDTO> loadedPerson = null;
        try{
            loadedPerson = loadPersonTask.execute().get();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        if(loadedPerson == null){
            errorDialog("경고", "올바른 데이터 형식을 받지 못했습니다.");
            return false;
        }
        if(loadedPerson.size() < 1){
            errorDialog("경고", "불러올 데이터가 존재하지 않습니다.");
            return false;
        }
        syncSQLiteDataFromServer(loadedPerson);
        return true;
    }

    private void syncSQLiteDataFromServer(ArrayList<PersonDTO> loadPerson){
        if (!selecltAll()){
            return;
        }
        int indexCousor = 0;
        int pasteToken = 0;
        Querys querys = new Querys(con);
        //addall의 이유 : 탐색을 하면서 sqlite에 지속적으로 추가하므로,
        //얕은복사를 하게 되면 비교하게되는 sqlitePerson의 배열이 계속 변하여 원하는 결과가 도출되지 않는다.
        ArrayList<PersonDTO> serverPersons = loadPerson;
        ArrayList<PersonDTO> sqlitePersons = new ArrayList<>();
        sqlitePersons.addAll(Persons.getPersons().getList());
        /*
            정렬기준은 Comparable을 상속받아 구현하였으므로, PersonDTO class에서 확인 가능
         */
        Collections.sort(serverPersons);
        Collections.sort(sqlitePersons);

        for(PersonDTO serverPerson : serverPersons){
            int i = indexCousor;
            for(i = 0; i < sqlitePersons.size(); i++){
                if (serverPerson.getName().equals(sqlitePersons.get(i).getName())){
                    if(serverPerson.getPhoneNumber().equals(sqlitePersons.get(i).getPhoneNumber())){
                        querys.modifyPerson(sqlitePersons.get(i), serverPerson);
                        indexCousor = i;
                        pasteToken = 1;
                        Log.d(TAG, "덮어씁니다 index = " + i +  " index cousor = " + indexCousor);
                        Log.d(TAG, serverPerson.pringAll());
                        break;
                    }
                }
            }
            if(pasteToken == 0){
                querys.insertPerson(serverPerson);
                Log.d(TAG, "추가합니다. index = " + i);
                Log.d(TAG, serverPerson.pringAll());
            }
            pasteToken = 0;
        }
    }

    private boolean selecltAll(){
        SelectAllPersons selectAllPersons = new SelectAllPersons(con);
        try {
            boolean result = selectAllPersons.execute().get();
            if (!result){
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            errorDialog("경고", "데이터를 불러오는데 실패했습니다.");
            return false;
        }
        return true;
    }

    // common
    private JSONObject makeJson(ArrayList<PersonDTO> personDTOs){
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try{
            for (PersonDTO person : personDTOs){
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

        Log.d(TAG, "DBParseJSONArray\n" + jsonArray.toString());
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
