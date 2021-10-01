package com.example.myapplication.Auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.Common.CommonVar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Yoon
 * @created 2021-09-28
 */
public class RequestAuthCodeTask extends AsyncTask<Void, Void, String> {

    String TAG = "RequestAuthCodeTask";

    Context con;
    String phoneNumber;
    String filePath = "requestAuthCodeTask";

    public RequestAuthCodeTask(Context con, String phoneNumber) {
        this.con = con;
        this.phoneNumber = phoneNumber;
    }

    @Override
    protected String doInBackground(Void... voids) {
        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        String result = "";

        try{
            URL url = new URL(CommonVar.rootPath + filePath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestProperty("Content-Type" , "application/x-www-form-urlencoded");
            httpURLConnection.setRequestMethod("POST");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());

            JSONObject json = new JSONObject();
            json.put("phone", phoneNumber);
            Log.d(TAG, json.toString());

            outputStreamWriter.write("device_data="+json.toString());
            outputStreamWriter.flush();

            if (httpURLConnection.getResponseCode() == httpURLConnection.HTTP_OK){

                inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8");
                bufferedReader = new BufferedReader(inputStreamReader);

                while (true) {
                    String str = bufferedReader.readLine();
                    if (str == null) break;
                    stringBuffer.append(str + "\n");
                }
                result = stringBuffer.toString().trim();
                Log.d(TAG, "result : " + stringBuffer);

            } else {
                return "networkError";
            }
        } catch (Exception e){
            e.printStackTrace();
            result = "";
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
    /*
        return values
        1. "", "networkError" : error
        2. the other values : auth code
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
