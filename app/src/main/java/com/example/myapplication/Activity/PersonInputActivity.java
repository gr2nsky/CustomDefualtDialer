package com.example.myapplication.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Common.Persons;
import com.example.myapplication.DTO.PersonDTO;
import com.example.myapplication.R;
import com.example.myapplication.Work.InsertPerson;

/**
 * @author Yoon
 * @created 2021-09-10
 */
public class PersonInputActivity extends AppCompatActivity {

    ImageView iv_person_input;
    String imagePath = null;
    EditText et_name_person_input;
    EditText et_phone_person_input;
    EditText et_email_person_input;
    EditText et_residence_person_input;
    EditText et_memo_person_input;

    LinearLayout ll_positive_person_input;
    LinearLayout ll_negative_person_input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_input);

        iv_person_input = findViewById(R.id.iv_person_input);
        et_name_person_input = findViewById(R.id.et_name_person_input);
        et_phone_person_input = findViewById(R.id.et_phone_person_input);
        et_email_person_input = findViewById(R.id.et_email_person_input);
        et_residence_person_input = findViewById(R.id.et_residence_person_input);
        et_memo_person_input = findViewById(R.id.et_memo_person_input);
        ll_positive_person_input = findViewById(R.id.ll_positive_person_input);
        ll_negative_person_input = findViewById(R.id.ll_negative_person_input);

        ll_positive_person_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertWork();
            }
        });
        ll_negative_person_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkExitLayout();
            }
        });
    }

    private void insertWork(){
        String name = et_name_person_input.getText().toString();
        String phone = et_phone_person_input.getText().toString();
        String email = et_email_person_input.getText().toString();
        String residence = et_residence_person_input.getText().toString();
        String memo = et_memo_person_input.getText().toString();

        if(name.trim().equals("") || name == null){
            personInputError("??????", "????????? ????????? ?????????.");
            return;
        }else if(phone.trim().equals("") || phone == null){
            personInputError("??????", "??????????????? ????????? ?????????.");
            return;
        }

        PersonDTO person = new PersonDTO(name, phone);
        //[????????????]
        if (imagePath != null){
            person.setImagePath(imagePath);
        }
        if (!email.equals("")){
            person.setEmail(email);
        }
        if (!residence.equals("")){
            person.setResidence(residence);
        }
        if (!memo.equals("")){
            memo = memo.replace("\n", "__[EnterSpace]__");
            person.setMemo(memo);
        }
        InsertPerson insertPerson = new InsertPerson(PersonInputActivity.this, person, "user");
        try{
            boolean result = insertPerson.execute().get();
            if (result){
                Persons persons = Persons.getPersons();
                persons.append(person);

                finishAndRemoveTask();
            } else {
                personInputError("??????", "????????? ??????????????????.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        checkExitLayout();
    }

    private void checkExitLayout(){
        AlertDialog.Builder backBtnDialogBuilder = new AlertDialog.Builder(PersonInputActivity.this)
                .setTitle("??????")
                .setMessage("????????? ?????????????????????????")
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //android??? TaskList????????? ?????????. ???, API 21 ??????, ????????? finish??? ??????
                        finishAndRemoveTask();
                    }
                })
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog backBtnDialog = backBtnDialogBuilder.create();
        backBtnDialog.show();
    }
    private void personInputError(String title, String msg){
        AlertDialog.Builder backBtnDialogBuilder = new AlertDialog.Builder(PersonInputActivity.this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog backBtnDialog = backBtnDialogBuilder.create();
        backBtnDialog.show();
    }
}
