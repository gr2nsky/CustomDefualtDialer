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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.Common.Persons;
import com.example.myapplication.DTO.PersonDTO;
import com.example.myapplication.R;
import com.example.myapplication.Work.DeletePerson;
import com.example.myapplication.Work.InsertPerson;
import com.example.myapplication.Work.ModifyPerson;

import java.util.ArrayList;

public class PersonDetailActivity extends AppCompatActivity{

    String TAG = "PersonDetailActivity";

    ImageView iv_person_detail;
    EditText et_name_person_detail;
    EditText et_phone_person_detail;
    String imagePath;
    EditText et_email_person_detail;
    EditText et_residence_person_detail;
    EditText et_memo_person_detail;
    LinearLayout ll_positive_person_detail;
    LinearLayout ll_negative_person_detail;
    ImageView iv_positive_person_detail;
    ImageView iv_negative_person_detail;
    TextView tv_positive_person_detail;
    TextView tv_negative_person_detail;

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
            simpleDialog("경고", "데이터를 로드하는데 실패하였습니다.", 1);
        }
        ArrayList<PersonDTO> pList = Persons.getPersons().getList();
        person = pList.get(personPosition);
        Log.d(TAG, "[person]" + person.pringAll());

        //glide 붙이기 전까진 이미지는 기본 이미지
        iv_person_detail = findViewById(R.id.iv_person_detail);
        et_name_person_detail = findViewById(R.id.et_name_person_detail);
        et_phone_person_detail = findViewById(R.id.et_phone_person_detail);
        et_email_person_detail = findViewById(R.id.et_email_person_detail);
        et_residence_person_detail = findViewById(R.id.et_residence_person_detail);
        et_memo_person_detail = findViewById(R.id.et_memo_person_detail);
        ll_positive_person_detail = findViewById(R.id.ll_positive_person_detail);
        ll_negative_person_detail = findViewById(R.id.ll_negative_person_detail);
        iv_positive_person_detail = findViewById(R.id.iv_positive_person_detail);
        iv_negative_person_detail = findViewById(R.id.iv_negative_person_detail);
        tv_positive_person_detail = findViewById(R.id.tv_positive_person_detail);
        tv_negative_person_detail = findViewById(R.id.tv_negative_person_detail);

        setPersonInfo();

        ll_positive_person_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editingToken == 0){
                    modifyModeStart();
                } else {
                    multiBtnDialog("알림", "변경사항을 저장하시겠습니까?");
                }
            }
        });
        ll_negative_person_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editingToken == 0){
                    multiBtnDialog("경고", "연락처를 정말 삭제하시겠습니까?");
                } else {
                    modifyModeClose();
                }
            }
        });


    } //onCreate

    private void setPersonInfo(){
        et_name_person_detail.setText(person.getName());
        et_phone_person_detail.setText(person.getPhoneNumber());
        et_email_person_detail.setText(person.getEmail());
        if(person.getEmail() != null && person.getEmail().equals("null")){
            et_email_person_detail.setText("");
        }

        et_residence_person_detail.setText(person.getResidence());
        if(person.getResidence() != null && person.getResidence().equals("null")){
            et_residence_person_detail.setText("");
        }


        if (person.getMemo() != null && !person.getMemo().equals("null") && !person.getMemo().equals("")){
            String memo = person.getMemo();
            memo = memo.replace("__[EnterSpace]__", "\n");
            person.setMemo(memo);
            et_memo_person_detail.setText(person.getMemo());
        } else {
            et_memo_person_detail.setText("");
        }
    }


    private void modifyModeStart(){
        editingToken = 1;

        et_name_person_detail.setBackgroundResource(R.drawable.edittext_custom);
        et_phone_person_detail.setBackgroundResource(R.drawable.edittext_custom);
        et_email_person_detail.setBackgroundResource(R.drawable.edittext_custom);
        et_residence_person_detail.setBackgroundResource(R.drawable.edittext_custom);
        et_memo_person_detail.setBackgroundResource(R.drawable.edittext_custom);
        iv_positive_person_detail.setImageResource(R.drawable.icon_check);
        iv_negative_person_detail.setImageResource(R.drawable.icon_cancel);
        tv_positive_person_detail.setText("확인");
        tv_negative_person_detail.setText("취소");

        editTextEnableSet(1);
    }

    private void modifyModeClose(){
        editingToken = 0;

        et_name_person_detail.setText(person.getName());
        et_phone_person_detail.setText(person.getPhoneNumber());
        et_email_person_detail.setText(person.getEmail());
        et_residence_person_detail.setText(person.getResidence());
        et_memo_person_detail.setText(person.getMemo());

        et_name_person_detail.setBackgroundResource(R.color.white);
        et_phone_person_detail.setBackgroundResource(R.color.white);
        et_email_person_detail.setBackgroundResource(R.color.white);
        et_residence_person_detail.setBackgroundResource(R.color.white);
        et_memo_person_detail.setBackgroundResource(R.color.white);
        iv_positive_person_detail.setImageResource(R.drawable.icon_pen);
        iv_negative_person_detail.setImageResource(R.drawable.icon_delete);
        tv_positive_person_detail.setText("수정");
        tv_negative_person_detail.setText("삭제");

        editTextEnableSet(0);
    }

    public void modifyPerson(){
        PersonDTO oldPerson = person;

        String name = et_name_person_detail.getText().toString();
        String phone = et_phone_person_detail.getText().toString();
        String email = et_email_person_detail.getText().toString();
        String residence = et_residence_person_detail.getText().toString();
        String memo = et_memo_person_detail.getText().toString();

        if(name.trim().equals("") || name == null){
            simpleDialog("경고", "이름을 입력해 주세요.", 0);
            return;
        }else if(phone.trim().equals("") || phone == null){
            simpleDialog("경고", "전화번호를 입력해 주세요.", 0);
            return;
        }

        PersonDTO modifiedPerson = new PersonDTO(person.getNo(), name, phone);
        //[수정요함]
        if (imagePath != null){
            modifiedPerson.setImagePath(imagePath);
        }
        if (!email.equals("")){
            modifiedPerson.setEmail(email);
        }
        if (!residence.equals("")){
            modifiedPerson.setResidence(residence);
        }
        if (!memo.equals("")){
            memo = memo.replace("\n", "__[EnterSpace]__");
            modifiedPerson.setMemo(memo);
        }

        Log.d(TAG, "[modifiedPerson]" + modifiedPerson.pringAll());
        ModifyPerson modifyPerson = new ModifyPerson(PersonDetailActivity.this, modifiedPerson);
        try{
            boolean result = modifyPerson.execute().get();
            if (result){
                Persons persons = Persons.getPersons();
                persons.modify(oldPerson, modifiedPerson);
                person = modifiedPerson;
                simpleDialog("알림", "연락처 수정에 성공하였습니다.", 0);
                setPersonInfo();
            } else {
                simpleDialog("경고", "연락처 수정에 실패하였습니다.", 0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        modifyModeClose();
    }

    public void deletePerson(){
        DeletePerson deletePerson = new DeletePerson(PersonDetailActivity.this, person);
        try{
            boolean result = deletePerson.execute().get();
            if (result){
                Persons persons = Persons.getPersons();
                persons.remove(person);
                simpleDialog("알림", "연락처 삭제가 수행되었습니다.", 1);
            } else {
                simpleDialog("경고", "연락처 삭제에 실패하였습니다.", 0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //editText 수정여부 0비활성화 1활성화
    private void editTextEnableSet(int token){
        if (token == 0){
            et_name_person_detail.setEnabled(false);
            et_phone_person_detail.setEnabled(false);
            et_email_person_detail.setEnabled(false);
            et_residence_person_detail.setEnabled(false);
            et_memo_person_detail.setEnabled(false);
        } else {
            et_name_person_detail.setEnabled(true);
            et_phone_person_detail.setEnabled(true);
            et_email_person_detail.setEnabled(true);
            et_residence_person_detail.setEnabled(true);
            et_memo_person_detail.setEnabled(true);
        }
    }

    public void simpleDialog(String title, String message, int backToken) {
        AlertDialog.Builder backBtnDialogBuilder = new AlertDialog.Builder(PersonDetailActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (backToken == 1){
                            finishAndRemoveTask();
                        }
                    }
                });
        AlertDialog backBtnDialog = backBtnDialogBuilder.create();
        backBtnDialog.show();
    }
    public void multiBtnDialog(String title, String message) {

        AlertDialog.Builder backBtnDialogBuilder = new AlertDialog.Builder(PersonDetailActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (editingToken == 0){
                            deletePerson();
                        } else {
                            modifyPerson();
                        }
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
}