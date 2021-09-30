package com.example.myapplication.Activity;
/**
 * @author Yoon
 * @created 2021-09-08
 */
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.myapplication.Adapter.NavigationDrawerListener;
import com.example.myapplication.Adapter.PhoneBookListAdapter;
import com.example.myapplication.Adapter.PhoneBookListItemTouchHelper;
import com.example.myapplication.Common.CDialog;
import com.example.myapplication.Common.Persons;
import com.example.myapplication.Adapter.NavigationDrawerAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Work.SelectAllPersons;

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
    ImageView iv_backendTask_phone_boook;
    RecyclerView list_view_phone_book;
    TextView tv_replace_list_view_phone_book;

    RecyclerView.LayoutManager layoutManager;
    PhoneBookListAdapter phoneBookListAdapter;
    ItemTouchHelper itemTouchHelper;

    DrawerLayout drawerLayout;
    ListView drawerList = null;
    Button drawer_cancel_phone_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_book);

        iv_backendTask_phone_boook = findViewById(R.id.iv_backendTask_phone_boook);
        search_view_phone_book = findViewById(R.id.search_view_phone_book);
        iv_add_phone_book = findViewById(R.id.iv_add_phone_book);
        list_view_phone_book = findViewById(R.id.list_view_phone_book);
        list_view_phone_book.addItemDecoration(new DividerItemDecoration(PhoneBookActivity.this, 1));
        tv_replace_list_view_phone_book = findViewById(R.id.tv_replace_list_view_phone_book);
        search_view_phone_book.setImeOptions(EditorInfo.IME_ACTION_DONE);
        search_view_phone_book.setOnQueryTextListener(searchViewTextListener);

        iv_add_phone_book.setOnClickListener(view -> {
            Intent intent = new Intent(PhoneBookActivity.this, PersonInputActivity.class);
            startActivity(intent);
        });
        iv_backendTask_phone_boook.setOnClickListener(view -> {
            drawerLayout.openDrawer(Gravity.LEFT);
        });
        selectAllPerson();

        //drawer nav
        drawerLayout = findViewById(R.id.drawer);
        drawer_cancel_phone_book = findViewById(R.id.drawer_cancel_phone_book);
        drawer_cancel_phone_book.setOnClickListener(view -> {
            drawerLayout.closeDrawer(Gravity.LEFT);
        });
        setNavDrawer();
    } //onCreate

    private void setAdapter(){
        if (Persons.getPersons().getList().size() < 1) {
            tv_replace_list_view_phone_book.setVisibility(View.VISIBLE);
            return;
        } else {
            tv_replace_list_view_phone_book.setVisibility(View.GONE);
        }

        //리사이클러뷰에 리사이클러뷰 어댑터 부착
        layoutManager = new LinearLayoutManager(this);
        list_view_phone_book.setLayoutManager(layoutManager);
        phoneBookListAdapter = new PhoneBookListAdapter(PhoneBookActivity.this, Persons.getPersons().getList());
        list_view_phone_book.setAdapter(phoneBookListAdapter);
        //리사이클러뷰에 ItemTouch를 부착해 스와이프 동작 구현
        itemTouchHelper = new ItemTouchHelper(new PhoneBookListItemTouchHelper(phoneBookListAdapter));
        itemTouchHelper.attachToRecyclerView(list_view_phone_book);
        tv_replace_list_view_phone_book.setVisibility(View.GONE);
    }

    private void setNavDrawer(){
        FragmentManager fm = getSupportFragmentManager();
        NavigationDrawerAdapter nd = new NavigationDrawerAdapter(PhoneBookActivity.this, fm, new NavigationDrawerListener() {
            @Override
            public void taskEnd(boolean result, String title, String msg) {
                selectAllPerson();
                setAdapter();
                Persons.getPersons().sort();
                CDialog cDialog = new CDialog(PhoneBookActivity.this);
                cDialog.oneBtnJsutDisplayDialog(title, msg);
            }
        });
        drawerList = findViewById(R.id.drawer_menulist);
        drawerList.setAdapter(nd);

    }

    private void selectAllPerson(){
        SelectAllPersons selectAllPersons = new SelectAllPersons(PhoneBookActivity.this);
        try {
            boolean result = selectAllPersons.execute().get();
            if (!result){
                dbLoadError();
            }
        }catch (Exception e){
            e.printStackTrace();
            dbLoadError();
        }
    }

    public void dbLoadError() {
        AlertDialog.Builder backBtnDialogBuilder = new AlertDialog.Builder(PhoneBookActivity.this)
                .setTitle("경고")
                .setMessage("저장된 연락처를 불러올 수 없습니다.")
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

    //추가, 입력, 삭제는 다른 화면에서 하고 돌아오므로 onResume에 갱신
    @Override
    protected void onResume() {
        super.onResume();
        if (phoneBookListAdapter == null){
            selectAllPerson();
            Persons.getPersons().sort();
            setAdapter();
        }
        if (phoneBookListAdapter != null){
            phoneBookListAdapter.renewalSearchedListBase(Persons.getPersons().getList());
            Persons.getPersons().sort();
            phoneBookListAdapter.notifyDataSetChanged();
            phoneBookListAdapter.getFilter().filter(search_view_phone_book.getQuery());
        }
    }

    //SearchView 텍스트 입력시 이벤트
    SearchView.OnQueryTextListener searchViewTextListener = new SearchView.OnQueryTextListener() {
        //검색버튼 입력시 호출, 검색버튼이 없으므로 사용하지 않음
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }
        //텍스트 입력/수정시에 호출
        @Override
        public boolean onQueryTextChange(String s) {
            phoneBookListAdapter.getFilter().filter(s);
            Log.d(TAG, "SearchVies Text is changed : " + s);
            return false;
        }
    };


}