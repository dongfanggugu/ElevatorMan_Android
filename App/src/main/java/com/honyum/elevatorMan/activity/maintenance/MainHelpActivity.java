package com.honyum.elevatorMan.activity.maintenance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.worker.WorkerBaseActivity;
import com.honyum.elevatorMan.data.LiftInfo;
import com.honyum.elevatorMan.data.MainHelpData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainHelpActivity extends WorkerBaseActivity {

    private Map<Integer, Boolean> mStateMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_help);
        initTitleBar(getIntent());
        getMainHelpFromFile(getIntent());
    }


    /**
     * 初始化标题栏
     * @param intent
     */
    private void initTitleBar(Intent intent) {
        String title = LiftInfo.typeToString(intent.getStringExtra("type"));
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        setExitFlag(false);
        initTitleBar(title, R.id.title_main_help, R.drawable.back_normal,
                backClickListener);
    }

    /**
     * 初始化视图
     * @param helpList
     */
    private void initView(List<String> helpList) {

        //初始化状态记录map
        mStateMap = new HashMap<Integer, Boolean>();
        for (int i = 0; i < helpList.size(); i++) {
            mStateMap.put(i, false);
        }
        ListView listView = (ListView) findViewById(R.id.lv_help);
        listView.setAdapter(new MyAdapter(this, R.layout.layout_main_help_item, helpList));
        setListener(listView);
    }

    /**
     * 从文件中获取帮助
     */
    private void getMainHelpFromFile(Intent intent) {
        try {
            InputStream inputStream = getAssets().open("main_help");
            int size = inputStream.available();

            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, "UTF-8");
            MainHelpData mainHelpData = MainHelpData.getMainHelpData(json);
            initView(getHelpListByType(mainHelpData, intent.getStringExtra("type")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据类型获取帮助列表
     * @param type
     * @return
     */
    private List<String> getHelpListByType(MainHelpData mainHelpData, String type) {

        if (type.equals("hm")) {
            return mainHelpData.getSm();

        } else if (type.equals("m")) {
            return mainHelpData.getM();

        } else if (type.equals("s")) {
            return mainHelpData.getS();

        } else if (type.equals("hy")) {
            return mainHelpData.getSy();

        } else {
            return mainHelpData.getY();
        }
    }

    private class MyAdapter extends BaseAdapter {

        private Context mContext;

        private int mLayout;

        private List<String> mList;

        public MyAdapter(Context context, int layout, List<String> list) {
            mContext = context;
            mLayout = layout;
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = View.inflate(mContext, mLayout, null);
            }

            TextView textView = (TextView) convertView;
            textView.setText(mList.get(position));

            boolean selected = mStateMap.get(position);

            if (selected) {
                convertView.setBackgroundColor(getResources().getColor(R.color.item_selected));
            } else {
                convertView.setBackgroundColor(getResources().getColor(R.color.white));
            }

            return convertView;
        }
    }

    /**
     * 设置ListView选中状态的切换
     * @param listView
     */
    private void setListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                setViewBag(view, position);
            }
        });
    }



    /**
     * 设置item的背景
     * @param view
     * @param position
     */
    private void setViewBag(View view, int position) {
        boolean selected = mStateMap.get(position);

        if (selected) {
            view.setBackgroundColor(getResources().getColor(R.color.white));
            mStateMap.put(position, false);

        } else {
            view.setBackgroundColor(getResources().getColor(R.color.item_selected));
            mStateMap.put(position, true);
        }
    }
}
