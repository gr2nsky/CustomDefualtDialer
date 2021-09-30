package com.example.myapplication.Auth;

import static android.content.Context.TELEPHONY_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Common.CDialog;

import java.util.UUID;

/**
 * @author Yoon
 * @created 2021-09-28
 *     1. 전화번호와 UUID server에 넘김
 *         1-1. 사용하던 단말인 경우
 *         1-2. 처음 접속한 단말인 경우
 *     2. 인증번호를 서버에 넘김
 *         2-1. 인증번호가 틀린 경우
 *         2-2. 인증번호가 맞는 경우
 */
public class UserAuthProcess {

    String TAG = "UserAuthProcess";

    Context con;
    FragmentManager fm;

    SharedPreferences prefs;
    CDialog cDialog;

    String uuid = "";
    String phoneNumber = "";

    private boolean isVailedUserDevice = false;

    public UserAuthProcess(Context con, FragmentManager fm) {
        this.con = con;
        this.fm = fm;
        prefs = con.getSharedPreferences("AUTH", con.MODE_PRIVATE);
        cDialog = new CDialog(con);
    }
    //////////////////////////////////////////////////////////////////////////
    //        phoneNumber & UUID를 서버에 전송해 대조하여 결과값을 받는 과정       //
    //////////////////////////////////////////////////////////////////////////
/*
    리턴값 종류
        1. 새 유저 -> "new"
        2. 이미 등록된 유저
            2.1 해당 단말의 uuid와 서버에 등록된 uuid가 같음 -> "pass"
            2.2 해당 단말의 uuid와 서버에 등록된 uuid가 다름 -> "key값 전송"
    Error
        1 권한 없음
        2 네트워크 오류
 */
    public String isEnableUserCheckProcess(){
        uuid = getUUID();
        Log.d(TAG, "uuid : " + uuid);
        phoneNumber = getDevicePhoneNumber();
        Log.d(TAG, "phoneNumber : " + phoneNumber);
        String requestResult = "";

        //권한이 없어 전화번호를 얻어오지 못 함
        if(phoneNumber.equals("")){
            cDialog.oneBtnJsutDisplayDialog("경고", "권환을 허용해 주세요.");
            return;
        }
        
        CheckValidUserDevice checkValidUserDevice  = new CheckValidUserDevice(con, uuid, phoneNumber);
        try{
            requestResult = checkValidUserDevice.execute().get();
            Log.d(TAG, "requestResult : " + requestResult);
        }catch (Exception e){
            e.printStackTrace();
        }

        /*
            #Return vars.
            true : uuid - phone이 유효하거나 신규 회원
            need_auth : uuid - phone 불일치, authcode dialog 실행해야 함
            new_false : 신규 회원가입 실패
            networkError : 네트워크 에러
        */
        switch (requestResult){
            case "true":
                isVailedUserDevice = true;
                break;
            case "need_auth":
                responseAuthCodePocess();
                break;
            case "new_false":
                cDialog.oneBtnJsutDisplayDialog("경고", "계정 등록 에러");
                isVailedUserDevice = false;
                break;
            case "networkError":
                cDialog.oneBtnJsutDisplayDialog("경고", "네트워크 Error");
                isVailedUserDevice = false;
                break;
            default:
                cDialog.oneBtnJsutDisplayDialog("경고", "확인되지 않은 오류입니다.\n다시 실행해주세요.");
                isVailedUserDevice = false;
        }

        return isVailedUserDevice;
    }

    //UUID 생성 및 확인
    private String getUUID() {
        String uuid = prefs.getString("UUID", "");

        if (uuid.equals("")) {
            makeUUID();
            return getUUID();
        }
        return uuid;
    }

    private void makeUUID() {
        String uuid = UUID.randomUUID().toString();

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("UUID", uuid);
        editor.commit();
    }

    //PhoneNum 획득
    private String getDevicePhoneNumber() {
        TelephonyManager telManager = (TelephonyManager) con.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(con, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        String PhoneNum = telManager.getLine1Number();
        if(PhoneNum.startsWith("+82")){
            PhoneNum = PhoneNum.replace("+82", "0");
        }
        return PhoneNum;
    }

    //////////////////////////////////////////////////////////////////////////
    //                    기존 유저가 새 단말이거나 새로 설치할 경우               //
    //////////////////////////////////////////////////////////////////////////
    /*
        1. Auth code를 입력할 dialog 출력
            - 버튼은 두개, 확인 또는 취소
            - 입력 칸 하단에 '새로받기' touchable textview 위치
            - dialog 출력과 동시에 우선 카운트 (3m 시작)
            - 새로받기 후에는 count 새로 시작
        2. 올바른 Auth code를 입력한 경우
            - true 반환 및 toast 출력
        3. 틀린 경우
            - toast로 틀렸다고 출력, 이외엔 그대로 유지
            - auth code editText 근처에 틀렸다고 표시 해줄 것
        4. 취소 키 입력
            - false 반환 및 모든 작업 종료
     */
    public void responseAuthCodePocess(){
        ResponseAuthCodeDialog authCodeDialog = new ResponseAuthCodeDialog(new ResponseAuthCodeDialogInterface() {
            @Override
            public String onPositiveClick(String inputedAuthCode) {
                String getResponse = "";

                CheckValidAuthCodeTask checkValidAuthCodeTask = new CheckValidAuthCodeTask(con, phoneNumber, inputedAuthCode);
                try{
                    getResponse = checkValidAuthCodeTask.execute().get();
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (getResponse.equals("true")){
                    isVailedUserDevice = true;
                }
                return getResponse;
            }
            @Override
            public String onRequestNewAuthClick(ResponseAuthCodeDialog responseAuthCodeDialog) {
                String getResponse = "";
                RequestAuthCodeTask requestAuthCodeTask = new RequestAuthCodeTask(con, phoneNumber);
                try {
                    getResponse = requestAuthCodeTask.execute().get();
                } catch (Exception e){
                    e.printStackTrace();
                }
                return getResponse;
            }
        });
        authCodeDialog.show(fm, "authDialog");
    }

    public boolean getIsAtuhInputTimeVaild(){
        return isVailedUserDevice;
    }
}








































