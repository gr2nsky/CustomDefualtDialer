package com.example.myapplication.Work;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.Common.CommonVar;
import com.example.myapplication.DTO.PersonDTO;
import com.example.myapplication.InnerDB.Querys;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Yoon
 * @created 2021-09-14
 */
public class LoadPersonTask extends AsyncTask<Void, Void, ArrayList<PersonDTO>> {

    String TAG = "LoadPersonTask";

    Context con;
    ProgressDialog dialog;
    String filePath = "download";

    public LoadPersonTask(Context con) {
        this.con = con;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(con);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("연락처를 받고 있습니다...");
        dialog.show();
    }

    @Override
    protected ArrayList<PersonDTO> doInBackground(Void... voids) {
        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        ArrayList<PersonDTO> result = new ArrayList<>();


        try{
            URL url = new URL(CommonVar.rootPath + filePath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestProperty("Content-Type" , "application/x-www-form-urlencoded");
            httpURLConnection.setRequestMethod("POST");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());

            outputStreamWriter.write("");
            outputStreamWriter.flush();

            if (httpURLConnection.getResponseCode() == httpURLConnection.HTTP_OK){

                inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8");
                bufferedReader = new BufferedReader(inputStreamReader);

                while (true) {
                    String str = bufferedReader.readLine();
                    if (str == null) break;
                    stringBuffer.append(str + "\n");
                }
                Log.d(TAG, "parserSelect_str: " + stringBuffer);
                result.clear();
                result.addAll(parserSelect(stringBuffer));

            }
        } catch (Exception e){
            e.printStackTrace();
            result = null;
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (inputStreamReader != null) inputStreamReader.close();
                if (inputStream != null) inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<PersonDTO> result) {
        dialog.dismiss();
        super.onPostExecute(result);
    }

    private ArrayList<PersonDTO> parserSelect(StringBuffer str){
        ArrayList<PersonDTO> persons = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(str.toString());

            for(int i=0; i < jsonArray.length(); i++ ){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String name = jsonObject.getString("pName");
                String phoneNumber = jsonObject.getString("pPhoneNumber");
                String imagePath = jsonObject.getString("pImagePath");
                String email = jsonObject.getString("pEmail");
                String residence = jsonObject.getString("pResidence");
                String memo = jsonObject.getString("pMemo");

                PersonDTO person = new PersonDTO(name, phoneNumber, imagePath, email, residence, memo);
                persons.add(person);
                Log.d(TAG, person.pringAll());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return persons;
    }
}
