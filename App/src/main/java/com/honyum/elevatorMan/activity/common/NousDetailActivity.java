package com.honyum.elevatorMan.activity.common;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;

public class NousDetailActivity extends BaseFragmentActivity {

    private String title;
    private String content;
    private WebView web_nous_detail;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nous_detail);

        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        title = bundle.getString("kntype");
        content = bundle.getString("content");
        initTitleBar(title, R.id.title,
                R.drawable.back_normal, backClickListener);
        //initTitleBar(R.id.title, title, R.mipmap.back, backClickListener, 0, null);

        web_nous_detail = (WebView) findViewById(R.id.web_nous_detail);
        web_nous_detail.getSettings().setJavaScriptEnabled(true);
        Log.e("content", content);
        web_nous_detail.loadDataWithBaseURL(null,content,"text/html","utf-8",null);
    }

}
