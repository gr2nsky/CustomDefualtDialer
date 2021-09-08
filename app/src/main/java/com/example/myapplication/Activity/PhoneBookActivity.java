package com.example.myapplication.Activity;
/**
 * @author Yoon
 * @created 2021-09-08
 */
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.myapplication.Adapter.PhoneBookListAdapter;
import com.example.myapplication.DTO.PersonDTO;
import com.example.myapplication.R;

import java.util.ArrayList;

/*
    전개 순서
    1. SQLite에서 DB를 읽어온다         /* 미구현
    2. 읽어온 데이터를 persons에 저장한다.   /*임시데이터 사용 -09081406
    3. 현재 Context와 persons로 phoneBookListAdapter 인스턴스를 생성한다
       3.1 persons이 빈값이라면 phoneBookListAdapter 인스턴스 생성 작업 대신,
           tv_replace_list_view_phone_book의 visible 속성을 true로 바꾼다.
    4. phoneBookListAdapter를 list_view_phone_book에 부착한다.
 */

public class PhoneBookActivity extends AppCompatActivity {

    String TAG = "PhoneBookActivity";

    SearchView search_view_phone_book;
    ImageView iv_add_phone_book;
    RecyclerView list_view_phone_book;
    TextView tv_replace_list_view_phone_book;

    RecyclerView.LayoutManager layoutManager;
    PhoneBookListAdapter phoneBookListAdapter;

    ArrayList<PersonDTO> persons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_book);

        search_view_phone_book = findViewById(R.id.search_view_phone_book);
        iv_add_phone_book = findViewById(R.id.iv_add_phone_book);
        list_view_phone_book = findViewById(R.id.list_view_phone_book);
        tv_replace_list_view_phone_book = findViewById(R.id.tv_replace_list_view_phone_book);
        search_view_phone_book.setOnQueryTextListener(searchViewTextListener);

        //[수정요함] SQLite 구축 대신 임시 데이터 사용
        tempPersonDataAdjust();
        setAdapter();
    }

    private void setAdapter(){
        if (persons.size() < 1) {
            tv_replace_list_view_phone_book.setVisibility(View.VISIBLE);
        } else {
            layoutManager = new LinearLayoutManager(this);
            list_view_phone_book.setLayoutManager(layoutManager);
            phoneBookListAdapter = new PhoneBookListAdapter(PhoneBookActivity.this, persons);
            list_view_phone_book.setAdapter(phoneBookListAdapter);
            tv_replace_list_view_phone_book.setVisibility(View.GONE);
        }
    }
    //[수정요함] 임시 데이터 적용 메서드. SQLite DB를 탐색해 data를 불러오는 기능으로 대체
    private void tempPersonDataAdjust(){
        PersonDTO p1 = new PersonDTO(1, "윤재필", "01047339270", "", "dbswovlf2009@naver.com", "안양", "");
        PersonDTO p2 = new PersonDTO(2, "곽병민", "01044771118", "", "fireguy0420@naver.com", "안양", "");
        PersonDTO p3 = new PersonDTO(3, "이영준", "01084268537", "", "ex_xe@naver.com", "안양", "");

        persons.add(p1);
        persons.add(p2);
        persons.add(p3);
    }

    //SearchView 텍스트 입력시 이벤트
    //
    SearchView.OnQueryTextListener searchViewTextListener = new SearchView.OnQueryTextListener() {
        //검색버튼 입력시 호출, 검색버튼이 없으므로 사용하지 않음
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }
        //텍스트 입력/수정시에 호출
        @Override
        public boolean onQueryTextChange(String s) {
            Log.d(TAG, "SearchVies Text is changed : " + s);
            return false;
        }
    };
}