package com.honyum.elevatorMan.activity.common;

import android.os.Bundle;
import android.app.Activity;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;

public class AboutActivity extends BaseFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initTitleBar();
    }

    private void initTitleBar() {
        initTitleBar("关于", R.id.title_about, R.drawable.back_normal, backClickListener);
    }


}
