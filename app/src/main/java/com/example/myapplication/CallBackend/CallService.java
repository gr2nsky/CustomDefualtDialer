package com.example.myapplication.CallBackend;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telecom.Call;
import android.telecom.InCallService;

import com.example.myapplication.Activity.CallActivity;

public class CallService extends InCallService {

    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        call.registerCallback(callCallback);
        Intent intent = new Intent(getApplicationContext(), CallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //CallManager.updateCall(call);
    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        call.unregisterCallback(callCallback);
        //CallManager.updateCall(null);
    }

    private Call.Callback callCallback = new Call.Callback() {
        @Override
        public void onStateChanged(Call call, int state) {
            //CallManager.updateCall(call);
        }
    };

}