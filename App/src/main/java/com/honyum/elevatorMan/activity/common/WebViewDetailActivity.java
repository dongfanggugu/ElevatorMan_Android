package com.honyum.elevatorMan.activity.common;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;

public class WebViewDetailActivity extends BaseFragmentActivity {

    private String title;
    private String content;
    private WebView web_nous_detail;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail);

        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        title = bundle.getString("kntype");
        content = bundle.getString("content");
        web_nous_detail = (WebView) findViewById(R.id.web_nous_detail);
        web_nous_detail.getSettings().setJavaScriptEnabled(true);
        Log.e("content", content);
        web_nous_detail.loadUrl(getConfig().getPCServer()+content);

        initTitleBar(title, R.id.title,
                R.drawable.back_normal, backClickListener);
        //initTitleBar(R.id.title, title, R.mipmap.back, backClickListener, 0, null);
        backClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(web_nous_detail.canGoBack())
                {
                    web_nous_detail.goBack();
                }
                else
                {
                    finish();
                }
            }
        };


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK&&web_nous_detail.canGoBack()){
            web_nous_detail.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);//退出整个应用程序
    }

}
