package com.example.myapplication.Work;

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
    public void uploadProcess(){
        boolean result = false;
        ArrayList<PersonDTO> changedPerson = new ArrayList<>();

        if (!getAllData()){
            return;
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
            return;
        }

        PersonBackUpTask personBackUpTask = new PersonBackUpTask(con, jsonObject);
        try{
            result = personBackUpTask.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }

        if (!result){
            errorDialog("경고", "네트워크 통신에 실패하였습니다.");
            return;
        }

        ArrayList<String> pNoArrayList = new ArrayList<>();
        for(PersonDTO person : changedPerson){
            pNoArrayList.add(Integer.toString(person.getNo()));
        }
        String[] pNos = pNoArrayList.toArray(new String[pNoArrayList.size()]);
        Querys querys = new Querys(con);
        querys.isChangedRemove(pNos);
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

    public void downloadProcess(){
        LoadPersonTask loadPersonTask = new LoadPersonTask(con);
        String jsonStr = "";
        try{
            jsonStr = loadPersonTask.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }

        if(jsonStr.equals("")){
            errorDialog("경고", "올바른 데이터 형식을 받지 못했습니다.");
            return;
        }

        ArrayList<PersonDTO> loadPerson = makeJson(jsonStr);
        if(loadPerson.size() < 1){
            errorDialog("경고", "불러올 데이터가 존재하지 않습니다.");
            return;
        }
    }

    private void syncDatabase(ArrayList<PersonDTO> loadPerson){
        int indexCousor = 0;
        Querys querys = new Querys(con);
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

        try{
            JSONArray jsonArray = new JSONArray();
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

        Log.d(TAG, "DBParseJSON\n" + jsonObject.toString());
        return jsonObject;
    }

    private ArrayList<PersonDTO> makeJson(String jsonStr){
        ArrayList<PersonDTO> personDTOs = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("person"));

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject pJson = (JSONObject) jsonArray.get(i);

                int no = Integer.parseInt(pJson.getString("pNo"));
                String name = pJson.getString("pName");
                String phone = pJson.getString("pPhoneNumber");
                String imagePath = pJson.getString("pImagePath");
                String email = pJson.getString("pEmail");
                String residence = pJson.getString("pResidence");
                String memo = pJson.getString("pMemo");
                int isChanged = 0;

                PersonDTO person = new PersonDTO(no, name, phone, imagePath, email, residence, memo, isChanged);
                personDTOs.add(person);
            }
            jsonObject.put("person", jsonArray);

        }catch (Exception e){
            e.printStackTrace();
        }
ud
        return personDTOs;
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
