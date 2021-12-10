package com.example.myapplication.CallBackend;

import android.telecom.Call;
import android.telecom.InCallService;
import android.util.Log;
import com.example.myapplication.Activity.CallActivity;

/**
 * @author Yoon
 * @created 2021-09-24
 */
public class CallService extends InCallService {
    String TAG = "CallService";

    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        Log.v(TAG, "call : " + call);
        new CallManager().setCall(call);
        CallActivity.start(this, call);
    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        new CallManager().setCall(call);
    }
}
