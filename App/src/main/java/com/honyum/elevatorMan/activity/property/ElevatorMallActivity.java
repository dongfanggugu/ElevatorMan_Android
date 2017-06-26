package com.honyum.elevatorMan.activity.property;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;


public class ElevatorMallActivity extends BaseFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elevator_mall);
        initView();
    }

    private void initView() {
        initTitleBar("电梯商城",R.id.title,
                R.drawable.back_normal, backClickListener);

        findViewById(R.id.ll_ztxs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ElevatorMallActivity.this, MarketWebViewActivity.class);
                intent.putExtra("type", "lift");
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_dtpj).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ElevatorMallActivity.this, EMdtpjActivity.class));
            }
        });

        findViewById(R.id.ll_dtzh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ElevatorMallActivity.this, MarketWebViewActivity.class);
                intent.putExtra("type", "decorate");
                startActivity(intent);
            }
        });
    }
}
