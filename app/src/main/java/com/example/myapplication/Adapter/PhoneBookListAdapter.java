package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.DTO.PersonDTO;
import com.example.myapplication.R;

import java.util.ArrayList;

/**
 * @author Yoon
 * @created 2021-09-08
 */
public class PhoneBookListAdapter extends BaseAdapter {

    Context con;
    ArrayList<PersonDTO> persons;

    public PhoneBookListAdapter(Context con, ArrayList<PersonDTO> personDTOS){
        this.con = con;
        this.persons = personDTOS;
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public Object getItem(int i) {
        return persons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // ViewHolder를 사용해 view를 재활용하는것이 효율적이나,
    // application의 규모가 작고, 이후 복수삭제기능 구현시에도 매번 inflater해주는것이 용이해 아래와같이 사용용
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(con).inflate(R.layout.list_item_phone_book, null);

        ImageView iv_person_phone_book_list_item = view.findViewById(R.id.iv_person_phone_book_list_item);
        TextView tv_name_phone_book_list_item = view.findViewById(R.id.tv_name_phone_book_list_item);
        ImageView iv_call_phone_book_list_item = view.findViewById(R.id.iv_call_phone_book_list_item);

        PersonDTO person = persons.get(i);
        //[수정요함] 이미지 작업의 경우 glide를 사용해 server의 image를 불러올 것
        tv_name_phone_book_list_item.setText(person.getName());

        iv_call_phone_book_list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + person.getPhoneNumber()));
                con.startActivity(callIntent);
            }
        });


        return view;
    }
}
