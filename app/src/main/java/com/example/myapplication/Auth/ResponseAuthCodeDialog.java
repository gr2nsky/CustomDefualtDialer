package com.example.myapplication.Auth;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.databinding.DialogResponseAuthCodeBinding;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Yoon
 * @created 2021-09-28
 */
public class ResponseAuthCodeDialog extends DialogFragment {

    private DialogResponseAuthCodeBinding binding;
    int requestCodeClickedCount = 0;

    private ResponseAuthCodeDialogInterface dialogInterface;
    private String authCode = "";
    private Timer timer = null;
    private int min = 0;
    private int second = 0;
    private boolean isAtuhInputTimeVaild = false;

    public ResponseAuthCodeDialog(ResponseAuthCodeDialogInterface dialogInterface) {
        this.dialogInterface = dialogInterface;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogResponseAuthCodeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        binding.tvOk.setOnClickListener(view -> {
            String getResponse = "";
            String inputedAuthCode = binding.etAuthCodeInput.getText().toString();
            /*
                return values
                    1. "" : error
                    2. "networkError" : networkError
                    3. "true" : user inputed valid auth code
                    4. "false " : user inputed invalid auth code
             */
            getResponse = dialogInterface.onPositiveClick(inputedAuthCode);
            switch (getResponse){
                case "true":
                    Toast.makeText(getContext(), "인증 완료", Toast.LENGTH_SHORT).show();
                    dismiss();
                    return;
                case "false":
                    binding.tvErrMsg.setVisibility(View.VISIBLE);
                    binding.tvErrMsg.setVisibility(View.VISIBLE);
                    return;
                case "networkError":
                    Toast.makeText(getContext(), "네트워크 에러", Toast.LENGTH_SHORT).show();
                    return;
                default:
                    //에러 처리
                    return;
            }

        });

        binding.tvCancle.setOnClickListener(view -> {
            dismiss();
        });

        binding.tvRequestNewAuthCode.setOnClickListener(view -> {
            binding.tvOk.setEnabled(true);
            String getResponse = "";
            if(requestCodeClickedCount == 0){
                requestCodeClickedCount ++;
                binding.tvRequestNewAuthCode.setText("인증번호 새로 받기");
            }
            /*
                return values
                    1. "", "networkError" : error
                    2. the other values : auth code
             */
            getResponse = dialogInterface.onRequestNewAuthClick(this);
            switch (getResponse){
                case "":
                    break;
                case "networkError":
                    break;
                default:
                    timerRun();
                    setAuthCode(getResponse);
                    break;
            }
        });
    }

    public void setAuthCode(String authCode){
        this.authCode = authCode;
        binding.tvAuthCodeHint.setText(authCode);
    }

    public String getAuthCode(){
        return authCode;
    }

    ///////////////////////////////////////////////////////////
    //                   about timer                         //
    ///////////////////////////////////////////////////////////
    public void timerRun(){
        isAtuhInputTimeVaild = true;
        min = 3;
        second = 0;
        binding.tvAuthCodeTimer.setText("03:00");

        if(timer != null){
            timer.cancel();
        }

        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                setTime();
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    private void setTime(){
        second -= 1;
        if(second < 0){
            min -= 1;
            second = 59;
        }
        if(min < 0){
            isAtuhInputTimeVaild = false;
            binding.tvAuthCodeTimer.setText("시간초과");
            binding.tvOk.setEnabled(false);
            timerStop();
        }
        String secondStr = second < 10 ? "0"+second : Integer.toString(second);
        binding.tvAuthCodeTimer.setText("0"+min+":"+secondStr);
    }

    public void timerStop(){
        if(timer != null){
            timer.cancel();
        }
    }
}
