package com.example.myapplication.Activity;
/**
 * @author Yoon
 * @created 2021-09-0777
 */
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";

    //Layout items
    EditText et_dial;
    ImageView btn_move_phone_book;
    ImageView btn_dial_call;
    ImageView btn_dial_remove;
    //ArrayList of dial numbers
    ArrayList<Integer> dialBtns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //통화권한 요청
        requestCallPermission();

        et_dial = findViewById(R.id.et_dial);
        btn_move_phone_book = findViewById(R.id.btn_move_phone_book);
        btn_dial_call = findViewById(R.id.btn_dial_call);
        btn_dial_remove = findViewById(R.id.btn_dial_remove);

        dialBtnAdd();
        btn_dial_call.setOnClickListener(callBtnClickListener);
        btn_dial_remove.setOnClickListener(removeBtnClickListener);

        //editText선택시 키보드는 호출하지 않게끔 설정
        et_dial.setShowSoftInputOnFocus(false);
    } //onCreate

    //Dial 추가가 너무 기므로 별도 분리하였음
    private void dialBtnAdd(){
        dialBtns = new ArrayList<>();
        dialBtns.add(R.id.btn_dial_0);
        dialBtns.add(R.id.btn_dial_1);
        dialBtns.add(R.id.btn_dial_2);
        dialBtns.add(R.id.btn_dial_3);
        dialBtns.add(R.id.btn_dial_4);
        dialBtns.add(R.id.btn_dial_5);
        dialBtns.add(R.id.btn_dial_6);
        dialBtns.add(R.id.btn_dial_7);
        dialBtns.add(R.id.btn_dial_8);
        dialBtns.add(R.id.btn_dial_9);
        dialBtns.add(R.id.btn_dial_star);
        dialBtns.add(R.id.btn_dial_shop);

        for(int btnId : dialBtns){
            TextView tv = findViewById(btnId);
            tv.setOnClickListener(numBtnClickListener);
        }
    }

    /*
        다이얼 번호가 적힌 TextView의 text를 EditText에 입력한다.
        단, EditText가 여러 글자가 선택되있는 상태라면 입력된 text로 대체한다.
     */
    View.OnClickListener numBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView tv = (TextView) view;
            String clickedVal = tv.getText().toString();

            /*
                선택영역의 시작과 끝을 구한다.
                단, 선택영역이 없다면 getSelectionStart/end는 -1을 반환하므로, 예외처리
            */
            int startOfSelection = et_dial.getSelectionStart() == -1? 0 : et_dial.getSelectionStart();
            int endOfSelection = et_dial.getSelectionEnd() == -1? 0 : et_dial.getSelectionEnd();

            //역방향 선택시 getSelectionStart가 getSelectionEnd다 작으므로 max, min울 이용한다.
            et_dial.getText().replace(Math.min(startOfSelection, endOfSelection),
                    Math.max(startOfSelection, endOfSelection), clickedVal);
        }
    };

    View.OnClickListener removeBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            /*
                선택영역의 시작과 끝을 구한다.
                단, 선택영역이 없다면 getSelectionStart/end는 -1을 반환하므로, 예외처리
            */
            int startOfSelection = et_dial.getSelectionStart() == -1? 0 : et_dial.getSelectionStart();
            int endOfSelection = et_dial.getSelectionEnd() == -1? 0 : et_dial.getSelectionEnd();
            Log.d(TAG, "s :" + startOfSelection + " e: " + endOfSelection);

            if (startOfSelection == 0 && endOfSelection == 0){
                return;
            }

            //역방향 선택시 getSelectionStart가 getSelectionEnd다 작으므로 max, min울 이용한다.
            if(startOfSelection == endOfSelection){
                et_dial.getText().replace(et_dial.getSelectionStart() - 1,
                        et_dial.getSelectionStart(), "");
            }else{
                et_dial.getText().replace(Math.min(startOfSelection, endOfSelection),
                        Math.max(startOfSelection, endOfSelection), "");
            }

        }
    };

    //통화버튼 클릭시 이멘트
    View.OnClickListener callBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String callNum = et_dial.getText().toString();
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callNum));
            startActivity(callIntent);
        }
    };

    //취소키 입력시 Dialog를 출력하여 바로 이탈하지 않도록 제어
    @Override
    public void onBackPressed() {
        AlertDialog.Builder backBtnDialogBuilder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("경고")
                .setMessage("앱을 종료하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //android의 TaskList에서도 삭제함. 단, API 21 이상, 이하는 finish로 대체
                        finishAndRemoveTask();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog backBtnDialog = backBtnDialogBuilder.create();
        backBtnDialog.show();
    }

    //통화, 내부저장소 읽고쓰기 권한 요청
    private void requestCallPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            }
            , 3);
        }
    }
}








