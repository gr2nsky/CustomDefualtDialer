package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

        et_dial = findViewById(R.id.et_dial);
        btn_move_phone_book = findViewById(R.id.btn_move_phone_book);
        btn_dial_call = findViewById(R.id.btn_dial_call);
        btn_dial_remove = findViewById(R.id.btn_dial_remove);

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
        btn_dial_remove.setOnClickListener(removeBtnClickListener);

        //editText의 커서위치는 유지하되 키보드는 호출하지 않게끔 설정
        et_dial.setTextIsSelectable(true);
        et_dial.setShowSoftInputOnFocus(false);
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

            String inputedNumStr = et_dial.getText().toString();
            if (inputedNumStr.length() < 1) {
                return;
            }

            /*
                선택영역의 시작과 끝을 구한다.
                단, 선택영역이 없다면 getSelectionStart/end는 -1을 반환하므로, 예외처리
            */
            int startOfSelection = et_dial.getSelectionStart() == -1? 0 : et_dial.getSelectionStart();
            int endOfSelection = et_dial.getSelectionEnd() == -1? 0 : et_dial.getSelectionEnd();
            Log.d(TAG, "s :" + startOfSelection + " e: " + endOfSelection);


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
}









