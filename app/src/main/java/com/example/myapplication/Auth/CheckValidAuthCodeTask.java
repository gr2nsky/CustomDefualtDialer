package com.example.myapplication.Auth;

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
public class CheckValidAuthCodeTask extends AsyncTask<Void, Void, String> {

    String TAG = "CheckValidAuthCodeTask";

    Context con;
    String phoneNumber;
    String uuid;
    String authCode;
    String filePath = "checkValidAuthCodeTask";

    public CheckValidAuthCodeTask(Context con, String phoneNumber, String uuid, String authCode) {
        this.con = con;
        this.phoneNumber = phoneNumber;
        this.uuid = uuid;
        this.authCode = authCode;
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
            json.put("uuid", uuid);
            json.put("authCode", authCode);
            Log.d(TAG, "device_data="+json.toString());

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
                Log.d(TAG, "result : " + stringBuffer);
                result = stringBuffer.toString().trim();

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
            1. "" : error
            2. "networkError" : networkError
            3. "true" : user inputed valid auth code
            4. "false " : user inputed invalid auth code
     */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
