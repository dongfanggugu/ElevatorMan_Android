package com.honyum.elevatorMan.activity.knowledge;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.worker.WorkerBaseActivity;
import com.honyum.elevatorMan.data.KnowledgeInfo;
import com.honyum.elevatorMan.utils.SQLiteUtils;
import com.honyum.elevatorMan.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TitleListActivity extends WorkerBaseActivity {

    private PullToRefreshListView mListView;

    private String knType;

    private Cursor mCursor;

    private List<KnowledgeInfo> mKnowledgeList;

    private MyAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_list);
        knType = getIntent().getStringExtra("type");

        initTitleBar();
        mKnowledgeList = new ArrayList<KnowledgeInfo>();
        mAdapter = new MyAdapter(this, mKnowledgeList);
        initView();
    }


    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        setExitFlag(false);
        initTitleBar(knType, R.id.title_title_list, R.drawable.back_normal,
                backClickListener);
    }

    private void initView() {
        mListView = (PullToRefreshListView) findViewById(R.id.listview);
        mListView.setAdapter(mAdapter);
        setListener(mListView);

        if (knType.equals("搜索结果")) {

            String brand = getIntent().getStringExtra("brand");
            String keywords = getIntent().getStringExtra("keywords");
            queryBySelections(brand, keywords);
        } else {
            query(knType);
        }
    }

    private void query(final String type) {
        final SQLiteDatabase db = SQLiteUtils.opendb(this,
                "/data/data/com.honyum.elevatorMan/databases/knowledge.db");
        final String sql = "select id, title, keywords, kntype, brand from knowledge where kntype = ?";

        Thread queryThread = new Thread() {
            @Override
            public void run() {
                mCursor = db.rawQuery(sql, new String[]{type});
            }
        };
        queryThread.start();

        try {
            queryThread.join();
            while (mCursor.moveToNext()) {
                mKnowledgeList.add(new KnowledgeInfo(mCursor));
            }
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mCursor.close();
            db.close();
        }


    }

    private class MyAdapter extends BaseAdapter {

        private Context mContext;

        private List<KnowledgeInfo> mKnowledgeInfos;

        public MyAdapter(Context context, List<KnowledgeInfo> knowledgeInfos) {
            this.mContext = context;
            this.mKnowledgeInfos = knowledgeInfos;
        }
        @Override
        public int getCount() {
            return mKnowledgeInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return mKnowledgeInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = View.inflate(mContext, R.layout.layout_knowledge_item, null);
            }
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            TextView tvKeywords = (TextView) convertView.findViewById(R.id.tv_keywords);

            tvTitle.setText(mKnowledgeInfos.get(position).getTitle());
            tvKeywords.setText("关键字:" + mKnowledgeInfos.get(position).getKeyWords());

            convertView.setTag(R.id.knId, mKnowledgeInfos.get(position).getId());
            convertView.setTag(R.id.knTitle, mKnowledgeInfos.get(position).getTitle());
            convertView.setTag(R.id.keywords, mKnowledgeInfos.get(position).getKeyWords());

            return convertView;
        }
    }

    /**
     * 设置listview监听
     * @param listView
     */
    private void setListener(PullToRefreshListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String knId = (String) view.getTag(R.id.knId);
                String title = (String) view.getTag(R.id.knTitle);
                String keywords = (String) view.getTag(R.id.keywords);

                Intent intent = new Intent(TitleListActivity.this, KnContentActivity.class);
                intent.putExtra("id", knId);
                intent.putExtra("title", title);
                intent.putExtra("keywords", keywords);
                startActivity(intent);
            }
        });
    }

    /**
     * 根据关键字进行搜索
     * @param brand
     * @param keywords
     */
    private void queryBySelections(final String brand, final String keywords) {


        final SQLiteDatabase db = SQLiteUtils.opendb(this,
                "/data/data/com.honyum.elevatorMan/databases/knowledge.db");

        Thread queryThread = new Thread() {
            @Override
            public void run() {
                String sql = getSql(brand, keywords);
                mCursor = db.rawQuery(sql, null);
            }
        };

        queryThread.start();

        try {
            queryThread.join();
            while (mCursor.moveToNext()) {
                mKnowledgeList.add(new KnowledgeInfo(mCursor));
            }
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mCursor.close();
            db.close();
        }
    }

    private String getSql(String brand, String keywords) {
        String sql = "";

        List<String> selArgs = new ArrayList<String>();

        String[] keys = keywords.split(" ");
        if (keys != null && keys.length != 0) {
            for (int i = 0; i < keys.length; i++) {
                String key = keys[i];
                String selTitle = "title like '%" + key + "%'";
                String selKeywords = "keywords like '%" + key + "%'";
                selArgs.add(selTitle);
                selArgs.add(selKeywords);
            }
        }

        if (brand.equals("全部")) {
            if (0 == selArgs.size()) {
                sql = "select id, title, keywords, kntype, brand from knowledge";
            } else {
                sql  = "select id, title, keywords, kntype, brand from knowledge where " + getSel(selArgs);
            }
        } else {
            if (0 == selArgs.size()) {
                sql = "select id, title, keywords, kntype, brand from knowledge where brand like '%" + brand + "%'";
            } else {

                sql = "select id, title, keywords, kntype, brand from knowledge where brand like '%" + brand
                        + "%'"+ " and " + getSel(selArgs);
            }
        }

        Log.i("zhenhao", "sql:" + sql);

        return sql;
    }

    /**
     * 获取查询条件
     * @param selArgs
     * @return
     */
    private String getSel(List<String> selArgs) {
        String result = "";
        for (int i = 0; i < selArgs.size(); i++) {
            String sel = selArgs.get(i);
            if (i != selArgs.size() - 1) {
                result += sel + " OR ";
            } else {
                result += sel;
            }
        }

        return result;
    }
}
