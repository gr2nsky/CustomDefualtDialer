package com.example.myapplication.Common;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.Activity.MainActivity;

/**
 * @author Yoon
 * @created 2021-09-27
 */
public class CDialog {

    Context con;

    public CDialog(Context con) {
        this.con = con;
    }

    public void oneBtnJsutDisplayDialog(String title, String msg){
        AlertDialog.Builder backBtnDialogBuilder = new AlertDialog.Builder(con)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog backBtnDialog = backBtnDialogBuilder.create();
        backBtnDialog.show();
    }
}
