package com.example.myapplication.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.myapplication.Activity.PhoneBookActivity;
import com.example.myapplication.Common.CDialog;
import com.example.myapplication.R;
import com.example.myapplication.Work.DBParseJSON;
import com.example.myapplication.Work.LoadDeviceContactsTask;

/**
 * @author Yoon
 * @created 2021-09-27
 */
public class NavigationDrawerAdapter extends BaseAdapter {

    final String[] itmes = {"연락처 백업", "연락처 내려받기", "기기 연락처 동기화"};
    Context con;
    NavigationDrawerListener naviListener;

    public NavigationDrawerAdapter(Context con, NavigationDrawerListener naviListener) {
        this.con = con;
        this.naviListener = naviListener;
    }

    @Override
    public int getCount() {
        return itmes.length;
    }

    @Override
    public Object getItem(int i) {
        return itmes[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(con).inflate(R.layout.list_item_navigation_drawer, null);
        ImageView iv = v.findViewById(R.id.iv_menu_drawer_nav);
        TextView tv = v.findViewById(R.id.tv_menu_name_drawer_nav);

        iv.setImageResource(setIv(i));
        tv.setText(itmes[i]);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEvent(i);
            }
        });

        return v;
    }

    private int setIv(int i){
        switch (i){
            case 0:
                return R.drawable.icon_cloud_upload;
            case 1:
                return R.drawable.icon_cloud_download;
            case 2:
                return R.drawable.icon_device;
            default:
                return R.drawable.icon_cloud;
        }
    }

    private void setEvent(int i){
        DBParseJSON dbParseJSON = new DBParseJSON(con);
        boolean result = false;

        switch (i){
            case 0:
                result = dbParseJSON.uploadProcess();
                if(result){
                    naviListener.taskEnd(result, "알림", "연락처를 성공적으로 업로드했습니다.");
                } else {
                    naviListener.taskEnd(result, "경고", "연락처 업로드에 실패했습니다.");
                }
            case 1:
                result = dbParseJSON.downloadProcess();
                if(result){
                    naviListener.taskEnd(result, "알림", "연락처를 성공적으로 로드했습니다.");
                } else {
                    naviListener.taskEnd(result, "경고", "연락처 로드에 실패했습니다.");
                }
            case 2:
                if (ContextCompat.checkSelfPermission(
                        con, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    LoadDeviceContactsTask loadDeviceContacts = new LoadDeviceContactsTask(con);
                    try {
                        if (!loadDeviceContacts.execute().get()) {
                            CDialog cDialog = new CDialog(con);
                            cDialog.oneBtnJsutDisplayDialog("경고", "디바이스 연락처 로드에 실패했습니다.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                naviListener.taskEnd(result, "알림", "연락처를 성공적으로 업로드했습니다.");
            default:
                return;
        }
    }
}
