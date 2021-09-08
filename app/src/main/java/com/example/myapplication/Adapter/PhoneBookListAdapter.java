package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DTO.PersonDTO;
import com.example.myapplication.R;

import java.util.ArrayList;

/**
 * @author Yoon
 * @created 2021-09-08
 */
public class PhoneBookListAdapter extends RecyclerView.Adapter<PhoneBookListAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_person_phone_book_list_item;
        TextView tv_name_phone_book_list_item;
        ImageView iv_call_phone_book_list_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_person_phone_book_list_item = itemView.findViewById(R.id.iv_person_phone_book_list_item);
            tv_name_phone_book_list_item = itemView.findViewById(R.id.tv_name_phone_book_list_item);
            iv_call_phone_book_list_item = itemView.findViewById(R.id.iv_call_phone_book_list_item);
        }
    }

    Context con;
    ArrayList<PersonDTO> persons;

    public PhoneBookListAdapter(Context con, ArrayList<PersonDTO> persons) {
        this.con = con;
        this.persons = persons;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context con = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_phone_book, parent, false);
        PhoneBookListAdapter.ViewHolder viewHolder = new PhoneBookListAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PersonDTO person = persons.get(position);
        //[수정요함] 이미지 작업의 경우 glide를 사용해 server의 image를 불러올 것
        holder.tv_name_phone_book_list_item.setText(person.getName());
        holder.iv_call_phone_book_list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + person.getPhoneNumber()));
                con.startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

}
