package com.example.myapplication.CallBackend;

import android.telecom.Call;
import android.telecom.InCallService;
import android.telecom.VideoProfile;
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

        //영상통화 판별
        int v = call.getDetails().getVideoState();
        if(VideoProfile.isTransmissionEnabled(v)||VideoProfile.isReceptionEnabled(v)){
            Log.d("VIDEO_CALL_TEST", "영상통화입니다." );
        }else{
            Log.d("VIDEO_CALL_TEST", "영상통화가 아닙니다.");
        }

        new CallManager().setCall(call);
        CallActivity.start(this, call);
    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        new CallManager().setCall(call);
    }
}
