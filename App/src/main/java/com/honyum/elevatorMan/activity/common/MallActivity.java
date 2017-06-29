package com.honyum.elevatorMan.activity.common;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.utils.StatusBarCompat;

import static com.honyum.elevatorMan.net.base.NetConstant.YI_ZHU;

/**
 * Created by Star on 2017/6/27.
 */

public class MallActivity extends BaseFragmentActivity {

    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_mall);



        initView();
        StatusBarCompat.compat(MallActivity.this, R.color.titleblue);

    }
    private void initView() {

        webView = (WebView) findViewById(R.id.wv_mall_content);

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        webView.loadUrl(getConfig().getPCServer()+YI_ZHU+"?userId="+getConfig().getUserId());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
    }
//    @Override
//    public void onBackPressed() {
//        if (webView.canGoBack()) {
//            webView.loadUrl("javascript:goBack()");
//        } else {
//            super.onBackPressed();
//            Log.e("TAG", "onBackPressed: "+"需要返回主页" );
//        }
//    }
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    if(keyCode == KeyEvent.KEYCODE_BACK&&webView.canGoBack()){
        webView.goBack();//返回上个页面
        return true;
    }
    return super.onKeyDown(keyCode, event);//退出整个应用程序
}

}
