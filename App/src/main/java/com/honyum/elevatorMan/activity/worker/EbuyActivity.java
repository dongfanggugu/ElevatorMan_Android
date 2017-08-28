package com.honyum.elevatorMan.activity.worker;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.MainPage1Activity;
import com.honyum.elevatorMan.activity.common.MallActivity;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.net.EmptyRequest;
import com.honyum.elevatorMan.net.VideoUrlResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;

/**
 * Created by Star on 2017/8/7.
 */

public class EbuyActivity extends BaseActivityWraper {

    private WebView mWebView;

    @Override
    public String getTitleString() {
        return "E梯配";
    }

    @Override
    protected void initView() {

        mWebView = findView(R.id.wv_content);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(new WebViewClient());
        // Log.e("content", content);
        requestEasyLadderUrl();

    }

    public void requestEasyLadderUrl() {
        String server = getConfig().getServer() + NetConstant.EASY_LADDER_URL;
        EmptyRequest er = new EmptyRequest();
        er.getHead().setUserId(getConfig().getUserId());
        er.getHead().setAccessToken(getConfig().getToken());

        NetTask netTask = new NetTask(server, er) {
            @Override
            protected void onResponse(NetTask task, String result) {
                //复用videourl类
                VideoUrlResponse vl = VideoUrlResponse.getVideoUrl(result);
                if (StringUtils.isNotEmpty(vl.getBody().getUrl()))
                    mWebView.loadUrl(vl.getBody().getUrl());

            }
        };
        addTask(netTask);


    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_etpay;
    }
}
