package com.example.myapplication.Auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.example.myapplication.Common.CommonVar;
import com.example.myapplication.DTO.PersonDTO;

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
 * @created 2021-09-28
 *     Server에 단말기의 uuid와 전화번호를 넘겨서 결과값을 받는다.
 *     1. 새 유저 -> "new"
 *     2. 이미 등록된 유저
 *         2.1 해당 단말의 uuid와 서버에 등록된 uuid가 같음 -> "pass"
 *         2.2 해당 단말의 uuid와 서버에 등록된 uuid가 다름 -> "key값 전송"
 *     3. Error
 *         3.1 네트워크 오류 -> "networkError"
 */
public class CheckValidUserDevice extends AsyncTask<Void, Void, String> {

    String TAG = "CheckValidUserDevice";

    Context con;
    String uuid;
    String phoneNumber;
    ProgressDialog dialog;

    String filePath = "CheckValidUserDevice";

    public CheckValidUserDevice(Context con, String uuid, String phoneNumber) {
        this.con = con;
        this.uuid = uuid;
        this.phoneNumber = phoneNumber;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(con);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("사용자를 확인하고 있습니다");
        dialog.show();
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
            json.put("uuid", uuid);
            json.put("phone", phoneNumber);
            Log.d(TAG, json.toString());

            outputStreamWriter.write("device_data="+json.toString());
            outputStreamWriter.flush();

            if (httpURLConnection.getResponseCode() == httpURLConnection.HTTP_OK){

                inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8");
                bufferedReader = new BufferedReader(inputStreamReader);

                while (true) {
                    String str = bufferedReader.readLine();
                    if (str == null) {
                        break;
                    }
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

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        dialog.dismiss();
    }

}
