package com.honyum.elevatorMan.activity.property;

import android.os.Bundle;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;


public class EMdtpjActivity extends BaseFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emdtpj);
        initTitleBar("电梯配件",R.id.title,
                R.drawable.back_normal, backClickListener);
    }
}
