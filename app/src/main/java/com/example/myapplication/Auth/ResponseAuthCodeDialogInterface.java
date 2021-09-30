package com.example.myapplication.Auth;

import android.widget.TextView;

/**
 * @author Yoon
 * @created 2021-09-28
 */
public interface ResponseAuthCodeDialogInterface {

    String onPositiveClick(String inputedAuthCode);
    String onRequestNewAuthClick(ResponseAuthCodeDialog responseAuthCodeDialog);
}
