package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.PersonDetailActivity;
import com.example.myapplication.DTO.PersonDTO;
import com.example.myapplication.R;

import java.util.ArrayList;

/**
 * @author Yoon
 * @created 2021-09-08
 */

public class PhoneBookListAdapter extends RecyclerView.Adapter<PhoneBookListAdapter.ViewHolder>
    implements PhoneBookListItemItemHelperListener, Filterable {

    String TAG = "PhoneBookListAdapter";

    Context con;
    //원본 배열
    ArrayList<PersonDTO> persons;
    //검색으로 걸러지는 배열 (실제 보여주는 배열)
    ArrayList<PersonDTO> searchedList = new ArrayList<>();
    ItemFilter itemFilter = new ItemFilter();

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout swipe_item_phone_book_list;
        ImageView iv_person_phone_book_list_item;
        TextView tv_name_phone_book_list_item;
        TextView tv_phone_number_phone_book_list_item;

        ImageView iv_call_phone_book_list_item;
        ImageView iv_message_phone_book_list_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            swipe_item_phone_book_list = itemView.findViewById(R.id.swipe_item_phone_book_list);
            iv_person_phone_book_list_item = itemView.findViewById(R.id.iv_person_phone_book_list_item);
            tv_name_phone_book_list_item = itemView.findViewById(R.id.tv_name_phone_book_list_item);
            tv_phone_number_phone_book_list_item = itemView.findViewById(R.id.tv_phone_number_phone_book_list_item);
            iv_call_phone_book_list_item = itemView.findViewById(R.id.iv_call_phone_book_list_item);
            iv_message_phone_book_list_item = itemView.findViewById(R.id.iv_message_phone_book_list_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    PersonDTO person = searchedList.get(position);
                    int orignPosition = persons.indexOf(person);
                    Intent intent = new Intent(con, PersonDetailActivity.class);
                    intent.putExtra("personPosition", orignPosition);
                    con.startActivity(intent);
                }
            });
        }
    }

    public PhoneBookListAdapter(Context con, ArrayList<PersonDTO> persons) {
        this.con = con;
        this.persons = persons;
        this.searchedList.addAll(persons);
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
        PersonDTO person = searchedList.get(position);
        //[수정요함] 이미지 작업의 경우 glide를 사용해 server의 image를 불러올 것
        //holder.iv_person_phone_book_list_item
        holder.tv_name_phone_book_list_item.setText(person.getName());
        holder.tv_phone_number_phone_book_list_item.setText(person.getPhoneNumber());

    }

    @Override
    public int getItemCount() {
        return searchedList.size();
    }

    //swipe시 동작
    @Override
    public void onItemSwipe(int position, int direction, RecyclerView.ViewHolder viewHolder) {
        PersonDTO person = searchedList.get(position);

        if (direction == ItemTouchHelper.LEFT){
            Uri phoneNumberUri = Uri.parse("sms:" + person.getPhoneNumber());

            Intent msgIntent = new Intent(Intent.ACTION_SENDTO, phoneNumberUri);
            msgIntent.putExtra("sms_body", "" );
            con.startActivity(msgIntent);
        } else {
            Uri phoneNumberUri = Uri.parse("tel:" + person.getPhoneNumber());

            Intent callIntent = new Intent(Intent.ACTION_CALL, phoneNumberUri);
            con.startActivity(callIntent);
        }

        notifyItemChanged(position);
    }

    public void renewalSearchedListBase(ArrayList<PersonDTO> newList){
        searchedList.clear();
        searchedList.addAll(newList);
    }

    //-- filter
    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private class ItemFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String filterString = charSequence.toString();
            FilterResults results = new FilterResults();

            Log.d(TAG, "charSequence : " + charSequence);

            ArrayList<PersonDTO> filteredList = new ArrayList<>();

            if (filterString != null && filterString.trim().length() != 0){
                for (PersonDTO dto : persons){
                    if(dto.getPhoneNumber().contains(filterString)){
                        filteredList.add(dto);
                        Log.d(TAG, "filtered name : " + dto.getName());
                    }
                }
                results.values = filteredList;
                results.count = filteredList.size();
            } else {
                results.values = persons;
                results.count = persons.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            searchedList.clear();
            searchedList.addAll((ArrayList<PersonDTO>) filterResults.values);
            notifyDataSetChanged();
        }
    }
}
