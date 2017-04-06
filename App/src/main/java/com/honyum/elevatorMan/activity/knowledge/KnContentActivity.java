package com.honyum.elevatorMan.activity.knowledge;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.worker.WorkerBaseActivity;
import com.honyum.elevatorMan.data.KnowledgeInfo;
import com.honyum.elevatorMan.utils.SQLiteUtils;
import com.honyum.elevatorMan.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class KnContentActivity extends WorkerBaseActivity {

    private String mContent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kn_content);
        initTitleBar();
        initView(getIntent());
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        setExitFlag(false);
        initTitleBar(getString(R.string.lift_knowledge), R.id.title_content, R.drawable.back_normal,
                backClickListener);
    }

    private void initView(Intent intent) {

//        TextView tvTitle = (TextView) findViewById(R.id.tv_kn_title);
//        TextView tvKeywords = (TextView) findViewById(R.id.tv_keywords);
//        TextView tvContent = (TextView) findViewById(R.id.tv_content);
//
//        Log.i("zhenhao", "title:" + intent.getStringExtra("title"));
//        tvTitle.setText(intent.getStringExtra("title"));
//        tvKeywords.setText(intent.getStringExtra("keywords"));
//
//        query(intent.getStringExtra("id"));
//        tvContent.setText(mContent);
        //tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        //tv.setText(Html.fromHtml(mContent));
        WebView webView = (WebView) findViewById(R.id.wv_content);
        query(intent.getStringExtra("id"));
        //Log.i("zhenhao", "mContent:" + mContent);
        //mContent = "<!Doctype html><html xmlns=http://www.w3.org/1999/xhtml>" + mContent + "</html>";
        //System.out.print("mContent:" + mContent);
        //webView.loadData(mContent, "text/html", "utf-8");

        webView.loadDataWithBaseURL(null, mContent, "text/html", "utf-8", null);
    }


    private void query(final String id) {
        final SQLiteDatabase db = SQLiteUtils.opendb(this,
                "/data/data/com.honyum.elevatorMan/databases/knowledge.db");
        final String sql = "select content from knowledge where id = ?";


        Thread queryThread = new Thread() {
            @Override
            public void run() {
                Cursor cursor = db.rawQuery(sql, new String[]{id});
                while (cursor.moveToNext()) {
                    mContent = cursor.getString(cursor.getColumnIndex("content"));
                }
                cursor.close();
            }
        };
        queryThread.start();

        try {
            queryThread.join();
            if (!StringUtils.isEmpty(mContent)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

    }
}
