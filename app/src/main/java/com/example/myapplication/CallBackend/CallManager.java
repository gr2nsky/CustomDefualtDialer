package com.example.myapplication.CallBackend;

import android.telecom.Call;
import android.telecom.VideoProfile;

import androidx.annotation.Nullable;

import io.reactivex.rxjava3.subjects.BehaviorSubject;

/**
 * @author Yoon
 * @created 2021-09-24
 */
public class CallManager {
    //cold observable : subscribe()호출시에만 값을 발행
    //BehaviorSubject는 구독하는 시점에서 가장 최근에 갱신된 값을 받는다.
    //통화상태가 변경되면 계속해서 onNext로 값을 변경하고 변경사항을 알리게 되는 것
    public static BehaviorSubject<Integer> state = BehaviorSubject.create();
    private static Call call;

    //IncallService에 대한 변경사항을 알리는 콜백을 정의
    private Object callback = new Call.Callback(){
        @Override
        public void onStateChanged(Call call, int newState) {
            super.onStateChanged(call, newState);
            //데이터의발행을 알림
            state.onNext(newState);
        }
    };

    public final void setCall(@Nullable Call value){
        //Call이 이미 존재한다면 새 Callback을 등록취소
        if(call != null){
            call.unregisterCallback((Call.Callback) callback);
        }
        //Call이 null (통화관련 행동이 종료되거나 없음)이고 Call이 새로 감지된다면 state에 등록
        if(value != null){
            value.registerCallback((Call.Callback) callback);
            state.onNext(value.getState());
        }

        call = value;
    }

    public void answer(){
        // assert : 후행 조건문을 정의하는데 사용, 예약어이다.
        //사전조건에 해당하는 사용방식인데, 메소드 호출시 지켜야 하는 요구사항을 입력한다.
        assert call != null;
        call.answer(VideoProfile.STATE_AUDIO_ONLY);
    }

    public void  hangup(){
        assert call != null;
        call.disconnect();
    }
}
