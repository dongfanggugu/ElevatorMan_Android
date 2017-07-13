package com.honyum.elevatorMan.activity.property;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;

public class MarketWebViewActivity extends BaseFragmentActivity {

    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_web_view);

        initView();
    }


    private View.OnClickListener rightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MarketWebViewActivity.this, LiftMessageActivity.class);
            intent.putExtra("type", getIntent().getStringExtra("type"));

            startActivity(intent);
        }
    };

    private void initView() {

        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setWebChromeClient(new WebChromeClient(){
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(MarketWebViewActivity.this);
                b.setTitle("提示");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }
        });
        String type = getIntent().getStringExtra("type");

        String url = "";

        if (type.equals("lift")) {

            initTitleBar("整梯销售",R.id.title,
                    R.drawable.back_normal, backClickListener);
            url = getConfig().getPCServer() + "/h5/indexPage";
        } else {

            initTitleBar("电梯装潢",R.id.title,
                    R.drawable.back_normal, backClickListener);

            url = getConfig().getPCServer() + "/h5/indexelevatorDecorationPage";
        }

        View titleView = findViewById(R.id.title);

        TextView right = (TextView)findViewById(R.id.tv_title_right);
        right.setText("联系我们");

        right.setOnClickListener(rightClickListener);

        Log.e("tag", "initView: "+url);
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();

        } else {
            super.onBackPressed();
        }
    }
}
