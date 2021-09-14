package com.example.myapplication.CallBackend;

import android.content.Context;

import com.example.myapplication.Activity.CallActivity;

/**
 * @author Yoon
 * @created 2021-09-13
 */
public interface CallListener {

    void acceptCall();
    void dismissCall();
    void shareContext(CallActivity callActivity);


}
