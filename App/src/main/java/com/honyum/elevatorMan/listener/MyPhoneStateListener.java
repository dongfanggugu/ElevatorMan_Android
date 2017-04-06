package com.honyum.elevatorMan.listener;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;


/**
 * Created by 李有鬼 on 2017/1/11 0011
 */

public class MyPhoneStateListener extends PhoneStateListener {

    private Context context;

    private String tel;

    private OnCallStateListener onCallStateListener;

    public MyPhoneStateListener() {
    }

    public MyPhoneStateListener(Context context, String tel, OnCallStateListener onCallStateListener) {
        this.context = context;
        this.tel = tel;
        this.onCallStateListener = onCallStateListener;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        if (!tel.equals(incomingNumber)) {
            return;
        }

        Log.d("AAA", "通话状态===>>>  " + state);

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:  //挂断|空闲
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:   //拨出
                onCallStateListener.onCallStateListener();
                break;
            case TelephonyManager.CALL_STATE_RINGING:   //来电响铃
                break;
        }
    }
}
