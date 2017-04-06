package com.honyum.elevatorMan.activity.common;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.utils.Utils;
import com.honyum.elevatorMan.view.CircleImageView;

import java.io.File;
import java.net.URI;

public class PersonBasicInfoActivity extends BaseFragmentActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_basic_info);
        initTitleBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        initTitleBar(getString(R.string.basic_info), R.id.title_basic_info,
                R.drawable.back_normal, backClickListener);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        ((TextView) findViewById(R.id.tv_user_name)).setText(getConfig().getUserName());
        ((TextView) findViewById(R.id.tv_name)).setText(getConfig().getName());
        ((TextView) findViewById(R.id.tv_sex)).setText(getConfig().getSex() == 0 ? "女" : "男");
        ((TextView) findViewById(R.id.tv_age)).setText("" + getConfig().getAge());
        ((TextView) findViewById(R.id.tv_tel)).setText(getConfig().getTel());

        loadIcon((CircleImageView) findViewById(R.id.iv_person));

        //姓名修改
        findViewById(R.id.ll_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonBasicInfoActivity.this, PersonModifyActivity.class);
                intent.putExtra("category", "name");
                startActivity(intent);
            }
        });


        //性别修改
        findViewById(R.id.ll_sex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonBasicInfoActivity.this, PersonModifyActivity.class);
                intent.putExtra("category", "sex");
                startActivity(intent);
            }
        });

        //年龄修改
        findViewById(R.id.ll_age).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonBasicInfoActivity.this, PersonModifyActivity.class);
                intent.putExtra("category", "age");
                startActivity(intent);
            }
        });

        //电话修改
        findViewById(R.id.ll_tel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonBasicInfoActivity.this, PersonModifyActivity.class);
                intent.putExtra("category", "tel");
                startActivity(intent);
            }
        });

        //头像修改
        findViewById(R.id.ll_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonBasicInfoActivity.this, PersonIconPopActivity.class);
                startActivity(intent);
            }
        });
    }

}
