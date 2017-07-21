package com.honyum.elevatorMan.activity.common;

import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;

/**
 * Created by Star on 2017/7/14.
 */

public class EchartActivity extends BaseActivityWraper {

    WebView wv_content;

    @Override
    public String getTitleString() {
        return "运行状态";
    }

    @Override
    protected void initView() {
        wv_content = findView(R.id.wv_content);

        wv_content.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //自动适应屏幕

        WebSettings webSettings = wv_content.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);


        wv_content.getSettings().setDomStorageEnabled(true);

       // webSettings.setPluginsEnabled(true);  //支持插件

        //支持缩放
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        //
        wv_content.loadUrl("file:///android_asset/elevatorInfo.htm");
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_chart;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv_content.canGoBack()) {
            wv_content.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
