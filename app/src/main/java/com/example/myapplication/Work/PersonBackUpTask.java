package com.example.myapplication.Work;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.Common.CommonVar;
import com.example.myapplication.Common.Persons;
import com.example.myapplication.DTO.PersonDTO;
import com.example.myapplication.InnerDB.Querys;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Yoon
 * @created 2021-09-14
 */
public class PersonBackUpTask extends AsyncTask<Void, Void, Boolean> {

    String TAG = "PersonBackUpTask";

    Context con;
    ProgressDialog dialog;
    Querys querys;
    String jsonObject;
    String filePath = "backup";

    public PersonBackUpTask(Context con, JSONObject jsonObject) {
        this.con = con;
        querys = new Querys(con);
        this.jsonObject = jsonObject.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(con);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("연락처를 백업하고 있습니다...");
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground stated");
        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        String result = null;


        try{
            URL url = new URL(CommonVar.rootPath + filePath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestProperty("Content-Type" , "application/x-www-form-urlencoded");
            httpURLConnection.setRequestMethod("POST");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());

            outputStreamWriter.write("person=" + jsonObject);
            outputStreamWriter.flush();

            Log.d(TAG, "http response code : " + httpURLConnection.getResponseCode());
            if (httpURLConnection.getResponseCode() == httpURLConnection.HTTP_OK){
                inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8");
                bufferedReader = new BufferedReader(inputStreamReader);
                Log.d(TAG, "bufferedReader : " + bufferedReader);
                while (true) {
                    String str = bufferedReader.readLine();
                    if (str == null) break;
                    stringBuffer.append(str + "\n");
                }
                result = stringBuffer.toString().trim();

            }
        } catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, e.toString());
            result = "false";
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (inputStreamReader != null) inputStreamReader.close();
                if (inputStream != null) inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


            if (result != null && result.equals("true")){
            return true;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        dialog.dismiss();
        super.onPostExecute(aBoolean);
    }
}
