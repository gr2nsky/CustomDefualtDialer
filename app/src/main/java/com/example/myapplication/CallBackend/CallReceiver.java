package com.example.myapplication.CallBackend;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.Activity.CallActivity;
import com.example.myapplication.Common.ShareInterface;

public class CallReceiver extends BroadcastReceiver implements CallListener {

    String TAG = "CallReceiver";
    String phoneState;
    TelecomManager telecomManager;
    Context con;
    CallActivity callActivity;

    @Override
    public void onReceive(Context context, Intent intent) {
        ShareInterface.callListener = this;
        this.con = context;
        telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                //같은 상태가 중복 호출됬을때 프로그램이 꼬이는것을 방지
                String state = extras.getString(TelephonyManager.EXTRA_STATE);
                if (state.equals(phoneState)) {
                    return;
                } else {
                    phoneState = state;
                }
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    String phoneNo = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    Log.d(TAG, phoneNo + " - current Number");
                    sendToActivity(context, phoneNo);
                } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    Log.d(TAG, "통화중");
                } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    Log.d(TAG, "통화 종료 혹은 통화벨 종료");
                }

                Log.d(TAG, "phone state : " + state);
                //Log.d(TAG, "phone currentPhonestate : " + )

            }
        }
    }

    private void sendToActivity(Context con, String str) {
        callActivity = new CallActivity();
        Intent intent = new Intent(con, CallActivity.class);
        /*
        Intent.FLAG_ACTIVITY_NEW_TASK : task 생성해 액티비티를 추가
        Intent.FLAG_ACTIVITY_SINGLE_TOP : 호출된 액티비티가 최상위에 존재한다면, 재사용
        Intent.FLAG_ACTIVITY_CLEAR_TOP : 호출된 액티비티가 스택에 존재한다면, 위의 액티비티를 모두 삭제해 최상위로 만든다.
         */
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("phone", str);
        con.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void acceptCall() {
        if (ActivityCompat.checkSelfPermission(con, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        telecomManager.acceptRingingCall();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void dismissCall() {
        if (ActivityCompat.checkSelfPermission(con, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        telecomManager.endCall();
    }
}