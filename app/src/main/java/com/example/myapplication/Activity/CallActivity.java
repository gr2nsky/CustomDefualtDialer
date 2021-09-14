package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.CallBackend.CallListener;
import com.example.myapplication.Common.Persons;
import com.example.myapplication.Common.ShareInterface;
import com.example.myapplication.DTO.PersonDTO;
import com.example.myapplication.InnerDB.Querys;
import com.example.myapplication.R;

import java.util.Timer;
import java.util.TimerTask;

public class CallActivity extends AppCompatActivity {

    String TAG = "CallActivity";

    ImageView iv_person_call;
    TextView tv_name_call;
    TextView tv_phone_call;
    TextView tv_time_call;
    ImageView iv_dissmiss_call;
    View v_interval_icons_call;
    ImageView ic_accept_call;

    CallListener callListener;

    Timer callTimer;
    TimerTask timerTask;
    // 0 : Ringing(초기값), 1: 통화 중
    int callStatus = 0;

    int min = 0;
    int sec = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        iv_person_call = findViewById(R.id.iv_person_call);
        tv_name_call = findViewById(R.id.tv_name_call);
        tv_phone_call = findViewById(R.id.tv_phone_call);
        tv_time_call = findViewById(R.id.tv_time_call);
        iv_dissmiss_call = findViewById(R.id.iv_dissmiss_call);
        v_interval_icons_call = findViewById(R.id.v_interval_icons_call);
        ic_accept_call = findViewById(R.id.ic_accept_call);

        Intent passedIntent = getIntent();
        processIntent(passedIntent);

        callListener = ShareInterface.callListener;
        if(callListener == null){
            finishAndRemoveTask();
        }
        callListener.shareContext(this);

        iv_dissmiss_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callStatus = 1;
                changeLayout();
            }
        });
        ic_accept_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callListener.acceptCall();
                changeLayout();
            }
        });
    } //onCreate

    private void processIntent(Intent intent){
        if(intent != null){
            int endedCall = intent.getIntExtra("stateToken", 0);
            if(endedCall != 0){
                callStatus = 1;
                changeLayout();
                return;
            }

            String phone = intent.getStringExtra("phone");
            tv_phone_call.setText(phone);
            getCallerName(phone);
        }
    }

    //다른 작업에 방해되지 않도록 동기 처리
    private void getCallerName(String phone){
        String caller = null;

        Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Querys querys = new Querys(CallActivity.this);
                        PersonDTO person = querys.selectByPhoneNo(phone);
                        if (person == null){
                            tv_name_call.setText("미등록 번호");
                        } else {
                            tv_name_call.setText(person.getName());
                        }
                    }
                });
            }
        });
        thread.start();
    }

    //call activity가 task 최상위일 때, 한번 더 startActivity를
    //BroadCast가 호출했을 때 호출됨.
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
    }

    public void changeLayout(){
        //통화 수락
        if (callStatus == 0) {
            callStatus = 1;
            callTimer();
            ic_accept_call.setVisibility(View.GONE);
            v_interval_icons_call.setVisibility(View.GONE);
            return;
        }

        //통화 종료
        callStatus = 0;
        callListener.dismissCall();
        if (callTimer != null){
            callTimer.cancel();
        }
        tv_time_call.setText("통화종료");
        layoutDelayFinish();

    }
    private void layoutDelayFinish(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finishAndRemoveTask();
            }
        }, 2000);
    }

    private void callTimer(){
        min = 0;
        sec = 0;
        tv_time_call.setText("00:00");

        callTimer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                sec += 1;
                if(sec == 60){
                    sec = 0;
                    min += 1;
                }
                String minStr = min < 10 ? "0" + min : Integer.toString(min);
                String secStr = sec < 10 ? "0" + sec : Integer.toString(sec);

                tv_time_call.setText(minStr + " : " + secStr);
            }
        };

        callTimer.schedule(timerTask, 0, 1000);
    }

}