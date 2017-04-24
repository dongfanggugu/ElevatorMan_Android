package com.honyum.elevatorMan.activity.property;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.LiftInfo;
import com.honyum.elevatorMan.net.MainDetailRequest;
import com.honyum.elevatorMan.net.MainDetailResponse;
import com.honyum.elevatorMan.net.ReportPlanStateRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProMainDetailActivity extends PropertyBaseActivity {

    private ImageView imageView;

    private ImageView imageViewWorker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_main_detail);

        initTitleBar();
        initView(getIntent());
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        setExitFlag(false);
        initTitleBar(getString(R.string.main_detail), R.id.title_detail, R.drawable.back_normal,
                backClickListener);
    }

    /**
     * 初始化视图
     * @param intent
     */
    private void initView(Intent intent) {
        if (null == intent) {
            return;
        }

        final LiftInfo liftInfo = (LiftInfo) intent.getSerializableExtra("lift");

        imageView = (ImageView) findViewById(R.id.iv_sign);

        imageViewWorker = (ImageView) findViewById(R.id.iv_worker_sign);

        new GetOriginPicture(liftInfo.getWorkerAutograph(), imageViewWorker).execute();


        ((TextView) findViewById(R.id.tv_project)).setText(liftInfo.getCommunityName());
        ((TextView) findViewById(R.id.tv_building)).setText(liftInfo.getBuildingCode());
        ((TextView) findViewById(R.id.tv_unit)).setText(liftInfo.getUnitCode());
        ((TextView) findViewById(R.id.tv_lift_code)).setText(liftInfo.getNum());
        ((TextView) findViewById(R.id.tv_plan_date)).setText(liftInfo.getMainTime());
        ((TextView) findViewById(R.id.tv_plan_type)).setText(liftInfo.getMainTypeString());

        ImageView imageView1 = (ImageView) findViewById(R.id.iv_image1);
        ImageView imageView2 = (ImageView) findViewById(R.id.iv_image2);
        ImageView imageView3 = (ImageView) findViewById(R.id.iv_image3);

        imageView1.setOnClickListener(overViewClickListener);
        imageView2.setOnClickListener(overViewClickListener);
        imageView3.setOnClickListener(overViewClickListener);

        List<ImageView> imageViewList = new ArrayList<ImageView>();
        imageViewList.add(imageView1);
        imageViewList.add(imageView2);
        imageViewList.add(imageView3);

        requestMainDetail(getConfig().getUserId(), getConfig().getToken(), liftInfo.getMainId(), imageViewList);


        //维保查看
        if (getIntent().getStringExtra("type").equals("2")) {
            findViewById(R.id.btn_submit).setVisibility(View.GONE);
            findViewById(R.id.btn_reject).setVisibility(View.GONE);

            imageView.setVisibility(View.VISIBLE);

            new GetOriginPicture(liftInfo.getPropertyAutograph(), imageView).execute();
        }

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportPlanState(getConfig().getUserId(), getConfig().getToken(), liftInfo.getMainId(), 2);
            }
        });


        findViewById(R.id.btn_reject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProMainDetailActivity.this, MaintBackInfoActivity.class);
                intent.putExtra("main_id", liftInfo.getMainId());
                startActivityForResult(intent, 101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (101 == requestCode && 102 == resultCode) {
            finish();
        }
}

    /**
     *  获取请求维保具体信息的bean
     * @param userId
     * @param token
     * @param mainId
     * @return
     */
    private RequestBean getMainDetailRequestBean(String userId, String token, String mainId) {

        MainDetailRequest request = new MainDetailRequest();
        MainDetailRequest.MainDetailReqBody body = request.new MainDetailReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(userId);
        head.setAccessToken(token);

        body.setMainId(mainId);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    /**
     * 请求详细信息
     * @param userId
     * @param token
     * @param mainId
     */
    private void requestMainDetail(String userId, String token, String mainId,
                                   final List<ImageView> imageViewList) {

        String serve = getConfig().getServer() + NetConstant.URL_PRO_GET_MAIN_DETAIL;
        RequestBean requestBean = getMainDetailRequestBean(userId, token, mainId);

        NetTask netTask = new NetTask(serve, requestBean) {
            @Override
            protected void onResponse(NetTask task, String result) {
                MainDetailResponse response = MainDetailResponse.getMainDetailResponse(result);
                List<String> picUrlList = response.getBody().getMainPics();
                for (int i = 0; i < picUrlList.size(); i++) {
                    String url = picUrlList.get(i);
                    ImageView imageView = imageViewList.get(i);
                    new GetPicture(url, imageView).execute();
                }
            }
        };

        addTask(netTask);
    }


    /**
     * 异步获取图片
     *
     * @author chang
     */
    class GetPicture extends AsyncTask<String, Void, String> {

        private String mUrl;
        private ImageView mImageView;

        public GetPicture(String url, ImageView imageView) {
            mUrl = url;
            mImageView = imageView;
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String filePath = "";
            try {
                filePath = Utils.getImage(mUrl);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);


            if (!StringUtils.isEmpty(result)) {
                Bitmap bitmap = Utils.getBitmapBySize(result, 60, 80);
                mImageView.setImageBitmap(bitmap);
                mImageView.setTag(result);
            }
        }
    }


    /**
     * 上报计划处理结果
     * @param user_id
     * @param token
     * @param mainId
     * @param state
     * @return
     */
    private RequestBean getReportPlanStateRequestBean(String user_id, String token, String mainId,
                                                      int state) {

        ReportPlanStateRequest request = new ReportPlanStateRequest();
        ReportPlanStateRequest.ReportPlanStateReqBody body = request.new ReportPlanStateReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(user_id);
        head.setAccessToken(token);

        body.setMainId(mainId);
        body.setVerify(state);

        request.setHead(head);
        request.setBody(body);

        return  request;
    }

    /**
     * 完成确认
     * @param userId
     * @param token
     * @param mainId
     * @param state
     */
    private void reportPlanState(String userId, String token, String mainId, int state) {

        String url = getConfig().getSign();

        if (StringUtils.isEmpty(url)) {
            showToast("您还没有个人手写签名,请到个人中心->设置->我的签名中设置签名");
            return;
        }
        String server = getConfig().getServer() + NetConstant.URL_PRO_REPORT_PLAN_RESULT;
        RequestBean requestBean = getReportPlanStateRequestBean(userId, token, mainId, state);

        NetTask netTask = new NetTask(server, requestBean) {
            @Override
            protected void onResponse(NetTask task, String result) {
//                Intent intent = new Intent(ProMainDetailActivity.this, PropertyMaintenanceActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("page", 0);
//                startActivity(intent);

                showToast("维保确认成功");
                imageView.setVisibility(View.VISIBLE);

                findViewById(R.id.btn_submit).setVisibility(View.GONE);
                findViewById(R.id.btn_reject).setVisibility(View.GONE);
                String url = getConfig().getSign();

                new GetOriginPicture(url, imageView).execute();
            }
        };

        addTask(netTask);
    }


    /**
     * 拍照之后点击照片查看照片预览
     */
    private View.OnClickListener overViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof ImageView) {
                String filePath  = (String) v.getTag();
                Bitmap bitmap = Utils.getBitmapBySize(filePath, 600, 800);
                ((ImageView) findViewById(R.id.iv_overview)).setImageBitmap(bitmap);

                final LinearLayout llFullScreen = (LinearLayout) findViewById(R.id.ll_full_screen);
                llFullScreen.setVisibility(View.VISIBLE);
                llFullScreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llFullScreen.setVisibility(View.GONE);
                    }
                });

            }
        }
    };


    /**
     * 异步获取图片
     *
     * @author chang
     */
    public class GetOriginPicture extends AsyncTask<String, Void, String> {

        private String mUrl;
        private ImageView mImageView;

        public GetOriginPicture(String url, ImageView imageView) {
            mUrl = url;
            mImageView = imageView;
            //mImageView.setImageResource(R.drawable.icon_person);
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String filePath = "";
            try {
                filePath = Utils.getImage(mUrl);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (!StringUtils.isEmpty(result)) {
                //Bitmap bitmap = Utils.getBitmapBySize(result, 80, 80);

                Bitmap bitmap = Utils.getImageFromFile(new File(result));
                if (bitmap != null) {
                    mImageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
