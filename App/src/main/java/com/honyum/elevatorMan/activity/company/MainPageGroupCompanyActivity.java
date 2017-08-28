package com.honyum.elevatorMan.activity.company;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.MallActivity;
import com.honyum.elevatorMan.activity.common.PersonActivity;
import com.honyum.elevatorMan.base.MyApplication;
import com.honyum.elevatorMan.constant.Constant;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Star on 2017/6/27.
 */

public class MainPageGroupCompanyActivity extends ActivityGroup {
    @SuppressWarnings("unused")
    private LinearLayout bodyView, headview;
    private LinearLayout one, two, three, four;
    private ImageView iv_page;
    private ImageView iv_mine;
    private ImageView iv_order;

    private TextView tv_order;
    private TextView tv_mine;
    private TextView tv_page;
    private int flag = 0; // 通过标记跳转不同的页面，显示不同的菜单项
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maingroup);
        initMainView();
        // 显示标记页面
        showView(flag);
        one.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                flag = 0;
                showView(flag);
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                flag = 1;
                showView(flag);
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                flag = 2;
                showView(flag);
            }
        });
//        four.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                flag = 3;
//                showView(flag);
//            }
//        });

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        super.onResume();
        JPushInterface.onResume(MainPageGroupCompanyActivity.this);


    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        super.onPause();
        JPushInterface.onPause(MainPageGroupCompanyActivity.this);
    }

    /*
     * 初始化主界面
     */
    public void initMainView() {

        one = (LinearLayout) findViewById(R.id.ll_page);
        two = (LinearLayout) findViewById(R.id.ll_person1);
        three = (LinearLayout) findViewById(R.id.ll_person);
        bodyView = (LinearLayout) findViewById(R.id.body);

        iv_page = (ImageView) findViewById(R.id.iv_page);
        iv_mine = (ImageView) findViewById(R.id.iv_mine);
        iv_order = (ImageView) findViewById(R.id.iv_order);

        tv_page = (TextView)findViewById(R.id.tv_page);
        tv_mine = (TextView)findViewById(R.id.tv_mine);
        tv_order = (TextView)findViewById(R.id.tv_order);


    }
    @Override
    public void onBackPressed() {
        //方式一：将此任务转向后台
        moveTaskToBack(false);

        //方式二：返回手机的主屏幕
    /*Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addCategory(Intent.CATEGORY_HOME);
    startActivity(intent);*/
    }
    // 在主界面中显示其他界面
    public void showView(int flag) {
        switch (flag) {
            case 0:
                bodyView.removeAllViews();
                View v = getLocalActivityManager().startActivity("one",
                        new Intent(MainPageGroupCompanyActivity.this, MainPageActivity.class)).getDecorView();
                bodyView.addView(v);
                iv_page.setBackgroundResource(R.drawable.select_page);
                iv_order.setBackgroundResource(R.drawable.normal_order);
                iv_mine.setBackgroundResource(R.drawable.normal_mine);

                tv_page.setTextColor(getResources().getColor(R.color.titleblue));
                tv_order.setTextColor(getResources().getColor(R.color.frontgray));
                tv_mine.setTextColor(getResources().getColor(R.color.frontgray));



                //four.setBackgroundResource(R.drawable.frame_button_background);
                break;
            case 1:
                bodyView.removeAllViews();
                bodyView.addView(getLocalActivityManager().startActivity("two",
                        new Intent(MainPageGroupCompanyActivity.this, MallActivity.class))
                        .getDecorView());
                iv_page.setBackgroundResource(R.drawable.normal_page);
                iv_order.setBackgroundResource(R.drawable.select_order);
                iv_mine.setBackgroundResource(R.drawable.normal_mine);

                tv_page.setTextColor(getResources().getColor(R.color.frontgray));
                tv_order.setTextColor(getResources().getColor(R.color.titleblue));
                tv_mine.setTextColor(getResources().getColor(R.color.frontgray));

                break;
            case 2:
                bodyView.removeAllViews();
                Intent it = new Intent(MainPageGroupCompanyActivity.this, PersonActivity.class);
                if(((MyApplication)getApplication()).getConfig().getRole().equals(Constant.WORKER))
                    it.putExtra("isWorker",true);
                bodyView.addView(getLocalActivityManager().startActivity(
                        "three",it )
                        .getDecorView());

                iv_page.setBackgroundResource(R.drawable.normal_page);
                iv_order.setBackgroundResource(R.drawable.normal_order);
                iv_mine.setBackgroundResource(R.drawable.select_mine);

                tv_page.setTextColor(getResources().getColor(R.color.frontgray));
                tv_order.setTextColor(getResources().getColor(R.color.frontgray));
                tv_mine.setTextColor(getResources().getColor(R.color.titleblue));
                break;
//            case 3:
//                bodyView.removeAllViews();
//                bodyView.addView(getLocalActivityManager().startActivity(
//                        "four", new Intent(MainView.this, FourView.class))
//                        .getDecorView());
//                one.setBackgroundResource(R.drawable.frame_button_nopressbg);
//                two.setBackgroundResource(R.drawable.frame_button_nopressbg);
//                three.setBackgroundResource(R.drawable.frame_button_nopressbg);
//                four.setBackgroundResource(R.drawable.frame_button_background);
//                break;
            default:
                break;
        }
    }
}
