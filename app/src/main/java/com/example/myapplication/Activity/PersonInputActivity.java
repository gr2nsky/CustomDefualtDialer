package com.example.myapplication.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

/**
 * @author Yoon
 * @created 2021-09-10
 */
public class PersonInputActivity extends AppCompatActivity {

    ImageView iv_person_input;
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
        et_phone_person_input = findViewById(R.id.et_phone_person_input);
        et_email_person_input = findViewById(R.id.et_email_person_input);
        et_residence_person_input = findViewById(R.id.et_residence_person_input);
        et_memo_person_input = findViewById(R.id.et_memo_person_input);
        ll_positive_person_input = findViewById(R.id.ll_positive_person_input);
        ll_negative_person_input = findViewById(R.id.ll_negative_person_input);

        ll_positive_person_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        ll_negative_person_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
