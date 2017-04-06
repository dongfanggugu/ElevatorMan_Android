package com.honyum.elevatorMan.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.LoginActivity;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.constant.Constant;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends BaseFragmentActivity {

    private ViewPager mViewPager;

    public int[] guides = new int[] {R.drawable.nav1, R.drawable.nav2, R.drawable.nav3,
            R.drawable.nav_4, R.drawable.nav_5, R.drawable.nav_6, R.drawable.nav_7};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        initView();
    }


    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_nav);

        List<View> viewList = new ArrayList<View>();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int index : guides) {
            View item = inflater.inflate(R.layout.layout_nav_guide, null);
            item.setBackgroundResource(index);
            viewList.add(item);
        }

        MyAdapter adapter = new MyAdapter(viewList);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(adapter);
        mViewPager.setCurrentItem(0);
    }

    class MyAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        private List<View> mViewList;

        public MyAdapter(List<View> viewList) {
            this.mViewList = viewList;
        }


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == mViewList.size() - 1) {
                View view = mViewList.get(position);
                Button btn = (Button) view.findViewById(R.id.button1);
                btn.setVisibility(View.VISIBLE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityByToken();
                    }
                });
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position), 0);
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }
    }


    /**
     * 根据保存的信息跳转页面
     */
    private void startActivityByToken() {
        String userId = getConfig().getUserId();
        String token = getConfig().getToken();
        String role = getConfig().getRole();
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(token)
                || StringUtils.isEmpty(role)) {
            startLogin("");

        } else if (role.equals(Constant.WORKER)) {
            startWorker("");

        } else if (role.equals(Constant.PROPERTY)) {
            startProperty(false);
        }
        finish();
    }

    /**
     * 跳转到登陆页面
     */
    private void startLogin(String alarmId) {

        Intent intent = new Intent(NavigationActivity.this, LoginActivity.class);

        if (!StringUtils.isEmpty(alarmId)) {
            intent.putExtra("alarm_id", alarmId);
            intent.setAction(Constant.ACTION_ALARM_RECEIVED);
        }
        startActivity(intent);
    }
}
