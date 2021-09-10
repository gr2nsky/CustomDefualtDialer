package com.example.myapplication.Activity;
/**
 * @author Yoon
 * @created 2021-09-10
 */
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.myapplication.R;

public class PersonDetailActivity extends AppCompatActivity {

    ImageView iv_person_detail;
    EditText et_phone_person_detail;
    EditText et_email_person_detail;
    EditText et_residence_person_detail;
    EditText et_memo_person_detail;
    LinearLayout ll_positive_person_detail;
    LinearLayout ll_negative_person_detail;

    //수정중인 상태라면 1, 아니라면 0
    int editingToken = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        iv_person_detail = findViewById(R.id.iv_person_detail);
        et_phone_person_detail = findViewById(R.id.et_phone_person_detail);
        et_email_person_detail = findViewById(R.id.et_email_person_detail);
        et_residence_person_detail = findViewById(R.id.et_residence_person_detail);
        et_memo_person_detail = findViewById(R.id.et_memo_person_detail);
        ll_positive_person_detail = findViewById(R.id.ll_positive_person_detail);
        ll_negative_person_detail = findViewById(R.id.ll_negative_person_detail);

        ll_positive_person_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editingToken == 0){
                    modifyStart();
                } else {
                    modifyEnd();
                }
            }
        });
        ll_negative_person_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePerson();
            }
        });
    }

    private void modifyStart(){

    }

    private void modifyEnd(){

    }

    private void deletePerson(){

    }

    //editText 수정여부 0비활성화 1활성화
    private void editTextEnableSet(int token){
        if (token == 0){
            et_phone_person_detail.setEnabled(false);
            et_email_person_detail.setEnabled(false);
            et_residence_person_detail.setEnabled(false);
            et_memo_person_detail.setEnabled(false);
        } else {
            et_phone_person_detail.setEnabled(true);
            et_email_person_detail.setEnabled(true);
            et_residence_person_detail.setEnabled(true);
            et_memo_person_detail.setEnabled(true);
        }
    }
}