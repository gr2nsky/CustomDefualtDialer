package com.example.myapplication.Adapter;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Yoon
 * @created 2021-09-09
 */
public interface PhoneBookListItemItemHelperListener {
    void onItemSwipe(int position, int listener, RecyclerView.ViewHolder viewHolder);
}
