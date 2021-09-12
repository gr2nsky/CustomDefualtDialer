package com.example.myapplication.Activity;
/**
 * @author Yoon
 * @created 2021-09-10
 */
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.myapplication.Common.Persons;
import com.example.myapplication.DTO.PersonDTO;
import com.example.myapplication.R;

import java.util.ArrayList;

public class PersonDetailActivity extends AppCompatActivity {

    ImageView iv_person_detail;
    EditText et_name_person_detail;
    EditText et_phone_person_detail;
    EditText et_email_person_detail;
    EditText et_residence_person_detail;
    EditText et_memo_person_detail;
    LinearLayout ll_positive_person_detail;
    LinearLayout ll_negative_person_detail;

    PersonDTO person;

    //수정중인 상태라면 1, 아니라면 0
    int editingToken = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        Intent intent = getIntent();
        int personPosition = intent.getIntExtra("personPosition", 0);
        if(personPosition == -1){
            dbLoadError();
        }
        ArrayList<PersonDTO> pList = Persons.getPersons().getList();
        person = pList.get(personPosition);

        //glide 붙이기 전까진 이미지는 기본 이미지
        iv_person_detail = findViewById(R.id.iv_person_detail);
        et_name_person_detail = findViewById(R.id.et_name_person_detail);
        et_phone_person_detail = findViewById(R.id.et_phone_person_detail);
        et_email_person_detail = findViewById(R.id.et_email_person_detail);
        et_residence_person_detail = findViewById(R.id.et_residence_person_detail);
        et_memo_person_detail = findViewById(R.id.et_memo_person_detail);
        ll_positive_person_detail = findViewById(R.id.ll_positive_person_detail);
        ll_negative_person_detail = findViewById(R.id.ll_negative_person_detail);

        setPersonInfo();

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
    } //onCreate

    private void setPersonInfo(){
        et_name_person_detail.setText(person.getName());
        et_phone_person_detail.setText(person.getPhoneNumber());
        et_email_person_detail.setText(person.getEmail());
        et_residence_person_detail.setText(person.getResidence());
        et_memo_person_detail.setText(person.getMemo());
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

    public void dbLoadError() {
        AlertDialog.Builder backBtnDialogBuilder = new AlertDialog.Builder(PersonDetailActivity.this)
                .setTitle("경고")
                .setMessage("연락처를 불러오는데 실패하였습니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //android의 TaskList에서도 삭제함. 단, API 21 이상, 이하는 finish로 대체
                        finishAndRemoveTask();
                    }
                });
        AlertDialog backBtnDialog = backBtnDialogBuilder.create();
        backBtnDialog.show();
    }
}