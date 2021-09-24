package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.Call;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.CallBackend.CallManager;
import com.example.myapplication.CallBackend.Constants;
import com.example.myapplication.DTO.PersonDTO;
import com.example.myapplication.InnerDB.Querys;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityCallBinding;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Predicate;

public class CallActivity extends AppCompatActivity {

    String TAG = "CallActivity";

    ImageView iv_person_call;
    TextView tv_name_call;
    TextView tv_phone_call;
    TextView tv_state_call;
    TextView tv_time_call;
    ImageView iv_dissmiss_call;
    View v_interval_icons_call;
    ImageView ic_accept_call;

    Timer callTimer;
    TimerTask timerTask;
    // 0 : Ringing(초기값), 1: 통화 중
    int callStatus = 0;

    int min = 0;
    int sec = 0;

    //생성된 모든 Obseravle을 라이프사이클에 맞춰 해제할 수 있다.
    //메모리누수를 막는 기능으로, onStop시에 clear를 하므로 메모리누수 방지
    //clear()의 경우 계속 dispsable을 받을 수 있지만, dispose()의 경우는 불가
    private CompositeDisposable disposable;
    private String phone;
    private CallManager callManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
//        ActivityCallBinding binding = ActivityCallBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        callManager = new CallManager();
        disposable = new CompositeDisposable();
        phone = Objects.requireNonNull(getIntent().getData().getSchemeSpecificPart());

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | 
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );

        iv_person_call = findViewById(R.id.iv_person_call);
        tv_name_call = findViewById(R.id.tv_name_call);
        tv_phone_call = findViewById(R.id.tv_phone_call);
        tv_state_call = findViewById(R.id.tv_state_call);
        tv_time_call = findViewById(R.id.tv_time_call);
        iv_dissmiss_call = findViewById(R.id.iv_dissmiss_call);
        v_interval_icons_call = findViewById(R.id.v_interval_icons_call);
        ic_accept_call = findViewById(R.id.ic_accept_call);

        iv_dissmiss_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callManager.hangup();
            }
        });
        ic_accept_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callManager.answer();
            }
        });
    } //onCreate

    @Override
    protected void onStart() {
        super.onStart();
//        assert updateUi(-1) != null;
        disposable.add(
                CallManager.state
                        //onNext 이벤트 처리 (발행에 대한 처리)
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Throwable {
                                updateUi(integer);
                            }
                        }));


        disposable.add(
                CallManager.state
                        .filter(new Predicate<Integer>() {
                            @Override
                            public boolean test(Integer integer) throws Exception {
                                return integer == Call.STATE_DISCONNECTED;
                            }
                        })
                        .delay(1, TimeUnit.SECONDS)
                        .firstElement()
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer)throws Throwable{
                                finish();
                            }
                        }));
    }

    //<? super Integer> : reyey() -observable에서 onError 발생시, subscribe()함수를 재호출하여 재구독
    //Consumer : 단일 인수를 받고 결과를 반환하지 않는다.
    @SuppressLint("SetTextI18n")
    private Consumer<? super Integer> updateUi(Integer state) {
        //이름 전화번호 상태를 각각 입력
        getCallerName(phone);
        tv_state_call.setText(Constants.asString(state));
        tv_phone_call.setText(phone);

        //[통화버튼] 수신 대기중인 상태면 보여주고, 아니라면 보여주지 않는다.
        if (state == Call.STATE_RINGING) {
            ic_accept_call.setVisibility(View.VISIBLE);
            v_interval_icons_call.setVisibility(View.VISIBLE);
        } else {
            ic_accept_call.setVisibility(View.GONE);
            v_interval_icons_call.setVisibility(View.GONE);
        }

        //[통화시간] 통화가 활성화되면 보여주고 타이머를 가동한다.
        //통화가 종료된다면, 타이머를 멈춘다
        if (state == Call.STATE_ACTIVE) {
            tv_time_call.setVisibility(View.VISIBLE);
            callTimer();
        }
        if (state == Call.STATE_DISCONNECTING) {
            if (callTimer != null){
                callTimer.cancel();
            }
        }

        //[통화종료버튼] 수신대기중 또는 발신대기중 또는 통화중이라면 보여준다.
        //위의 세가지 조건이 아니라면 보여주지 않는다.
        Integer[] stateArr = {Call.STATE_DIALING, Call.STATE_RINGING, Call.STATE_ACTIVE};
        for(int i = 0; i < stateArr.length; i++) {
            if (stateArr[i] == state) {
                iv_dissmiss_call.setVisibility(View.VISIBLE);
                v_interval_icons_call.setVisibility(View.VISIBLE);
                return null;
            }
        }
        iv_dissmiss_call.setVisibility(View.GONE);
        v_interval_icons_call.setVisibility(View.GONE);

        return null;
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

    @Override
    protected void onStop() {
        super.onStop();
        disposable.clear();
    }

    public static void start(Context context, Call call) {
        Intent intent = new Intent(context, CallActivity.class)
                //새로운 태스크를 생성하여 그 태스크안에 엑티비티를 추가.
                //동일한 affinity 가 있다면 그 task 에 새 액티비티를 포함시킴
                //동일한 affinity 가 없다면 새로운 task 생성
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(call.getDetails().getHandle());
        context.startActivity(intent);
    }

}