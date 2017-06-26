package com.honyum.elevatorMan.activity.company;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.FixTaskInfo;
import com.honyum.elevatorMan.data.mydata.NHFixAndTask;
import com.honyum.elevatorMan.view.MyListView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Star on 2017/6/15.
 */

public class NHFixDetailActivity extends BaseActivityWraper implements ListItemCallback<FixTaskInfo> {
    @BindView(R.id.tv_order_id)
    TextView tvOrderId;
    @BindView(R.id.tv_ordertime)
    TextView tvOrdertime;
    @BindView(R.id.tv_apptime)
    TextView tvApptime;
    @BindView(R.id.tv_breaktype)
    TextView tvBreaktype;
    @BindView(R.id.tv_breakdes)
    TextView tvBreakdes;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.iv_image1)
    ImageView ivImage1;
    @BindView(R.id.tv_vill_address)
    TextView tvVillAddress;
    @BindView(R.id.tv_eve_brand)
    TextView tvEveBrand;
    @BindView(R.id.eve_weight)
    TextView eveWeight;
    @BindView(R.id.eve_landing)
    TextView eveLanding;
    @BindView(R.id.tv_nexttime)
    TextView tvNexttime;
    @BindView(R.id.tv_paybill)
    TextView tvPaybill;
    @BindView(R.id.tv_look_eva)
    TextView tvLookEva;
    @BindView(R.id.fix_rec)
    MyListView fixRec;
    @BindView(R.id.rl_fix_title)
    RelativeLayout rlFixTitle;
    @BindView(R.id.ll_full_screen)
    LinearLayout llFullScreen;
    @BindView(R.id.iv_overview)
    ImageView ivOverview;
    @BindView(R.id.iv_image2)
    ImageView ivImage2;
    @BindView(R.id.iv_image3)
    ImageView ivImage3;
    @BindView(R.id.iv_image4)
    ImageView ivImage4;
    @BindView(R.id.line)
    View line;

    private NHFixAndTask mCompanyRepairInfo;
//    private List<FixTaskInfo> mFixTasks;
//    private FixTaskDetailListAdapter mFixTaskDetailListAdapter;

    @Override
    public String getTitleString() {
        return "维修信息";
    }

    @Override
    protected void onResume() {
        super.onResume();
        //requestFixTaskListInfo();
    }

    /**
     * 查看预览信息
     *
     * @param image1
     */

    private void showPreviewImage(ImageView image1) {
        String filePath = (String) image1.getTag(R.id.file_path);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        ivOverview.setImageBitmap(bitmap);
        llFullScreen.setVisibility(View.VISIBLE);
        llFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llFullScreen.setVisibility(View.GONE);
            }
        });
        image1.setDrawingCacheEnabled(false);
        image1.setDrawingCacheEnabled(true);
    }

    @Override
    protected void initView() {
        ivImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviewImage(ivImage1);
            }
        });
        mCompanyRepairInfo = getIntent("Info");
//        if(getIntent().getExtras().getInt("Id") == 1) {
//            tvOrderId.setText(getString(R.string.order_id) + mCompanyRepairInfo.getCode());
//            tvOrdertime.setText(getString(R.string.order_time) + mCompanyRepairInfo.getCreateTime());
//            tvApptime.setText(getString(R.string.app_time) + mCompanyRepairInfo.getRepairTime());
//            tvBreaktype.setText(getString(R.string.breaktype) + mCompanyRepairInfo.getPhenomenon());
//            etRemark.setFocusable(false);
//            etRemark.setText(mCompanyRepairInfo.getRepairTypeInfo().getContent());
//            new GetPicture(mCompanyRepairInfo.getPicture(), ivImage1).execute();
//            tvVillAddress.setText(getString(R.string.vill_address) + mCompanyRepairInfo.getVillaInfo().getAddress());
//            tvEveBrand.setText("电梯品牌：" + mCompanyRepairInfo.getVillaInfo().getBrand());
//            eveWeight.setText(getString(R.string.eve_weight) + mCompanyRepairInfo.getVillaInfo().getWeight() + "KG");
//            eveLanding.setText(getString(R.string.eve_landing) + mCompanyRepairInfo.getVillaInfo().getLayerAmount() + "层");
//        }
//        else
//        {
//            tvOrderId.setText(getString(R.string.order_id) + mCompanyRepairInfo.getRepairOrderInfo().getCode());
//            tvOrdertime.setText(getString(R.string.order_time) + mCompanyRepairInfo.getRepairOrderInfo().getCreateTime());
//            tvApptime.setText(getString(R.string.app_time) + mCompanyRepairInfo.getRepairOrderInfo().getRepairTime());
//            tvBreaktype.setText(getString(R.string.breaktype) + mCompanyRepairInfo.getRepairOrderInfo().getPhenomenon());
//            etRemark.setFocusable(false);
//            etRemark.setText(mCompanyRepairInfo.getRepairTypeInfo().getContent());
//            if(mCompanyRepairInfo.getPicture()!=null)
//            {
//                String s = mCompanyRepairInfo.getPicture().split(",")[0];
//                new GetPicture(s, ivImage1).execute();
//            }
//
//            tvVillAddress.setText(getString(R.string.vill_address) + mCompanyRepairInfo.getRepairOrderInfo().getVillaInfo().getAddress());
//            tvEveBrand.setText("电梯品牌：" + mCompanyRepairInfo.getRepairOrderInfo().getVillaInfo().getBrand());
//            eveWeight.setText(getString(R.string.eve_weight) + mCompanyRepairInfo.getRepairOrderInfo().getVillaInfo().getWeight() + "KG");
//            eveLanding.setText(getString(R.string.eve_landing) + mCompanyRepairInfo.getRepairOrderInfo().getVillaInfo().getLayerAmount() + "层");
//        }
        Intent i = getIntent();
        int i1 = i.getIntExtra("Id", 0);
        if (i1 == 1) {
            tvOrderId.setText(getString(R.string.order_id) + mCompanyRepairInfo.getCode());
            tvOrdertime.setText(getString(R.string.order_time) + mCompanyRepairInfo.getCreateTime());
            tvApptime.setText(getString(R.string.app_time) + mCompanyRepairInfo.getRepairTime());
            tvBreaktype.setText(getString(R.string.breaktype) + mCompanyRepairInfo.getPhenomenon());
            etRemark.setFocusable(false);
            etRemark.setText(mCompanyRepairInfo.getRepairTypeInfo().getContent());
            ivImage1.setVisibility(View.VISIBLE);
            Log.e("TAG", "initView: "+mCompanyRepairInfo.getPicture() );
            new GetPicture(mCompanyRepairInfo.getPicture(), ivImage1).execute();
            Log.e("TAG", "initView: " + mCompanyRepairInfo.getPicture());
            tvVillAddress.setText(getString(R.string.vill_address) + mCompanyRepairInfo.getVillaInfo().getAddress());
            tvEveBrand.setText("品牌：" + mCompanyRepairInfo.getVillaInfo().getBrand());
            eveWeight.setText(getString(R.string.eve_weight) + mCompanyRepairInfo.getVillaInfo().getWeight());
            eveLanding.setText(getString(R.string.eve_landing) + mCompanyRepairInfo.getVillaInfo().getLayerAmount());
        } else {
            tvOrderId.setText(getString(R.string.order_id) + mCompanyRepairInfo.getRepairOrderInfo().getCode());
            tvOrdertime.setText(getString(R.string.order_time) + mCompanyRepairInfo.getRepairOrderInfo().getCreateTime());
            tvApptime.setText(getString(R.string.app_time) + mCompanyRepairInfo.getRepairOrderInfo().getRepairTime());
            tvBreaktype.setText(getString(R.string.breaktype) + mCompanyRepairInfo.getRepairOrderInfo().getPhenomenon());
            etRemark.setFocusable(false);
            etRemark.setText(mCompanyRepairInfo.getRepairTypeInfo().getContent());


            if (mCompanyRepairInfo.getPicture() != null) {
                String[] s = mCompanyRepairInfo.getPicture().split(",");
                ImageView[] iv = new ImageView[]{ivImage1, ivImage2, ivImage3, ivImage4};
                for (int i2 = 0; i2 < s.length; i2++) {
                    if (StringUtils.isNotEmpty(s[i2])) {
                        iv[i2].setVisibility(View.VISIBLE);
                        new GetPicture(s[i2], iv[i2]).execute();

                    }
                }

            }
            tvVillAddress.setText(getString(R.string.vill_address) + mCompanyRepairInfo.getRepairOrderInfo().getVillaInfo().getAddress());
            tvEveBrand.setText("品牌：" + mCompanyRepairInfo.getRepairOrderInfo().getVillaInfo().getBrand());
            eveWeight.setText(getString(R.string.eve_weight) + mCompanyRepairInfo.getRepairOrderInfo().getVillaInfo().getWeight());
            eveLanding.setText(getString(R.string.eve_landing) + mCompanyRepairInfo.getRepairOrderInfo().getVillaInfo().getLayerAmount());


        }
        tvNexttime.setVisibility(View.GONE);
        tvPaybill.setVisibility(View.GONE);
        tvLookEva.setVisibility(View.GONE);
        rlFixTitle.setVisibility(View.GONE);
        fixRec.setVisibility(View.GONE);
        line.setVisibility(View.GONE);


    }

//    private void fillList() {
//        mFixTaskDetailListAdapter = new FixTaskDetailListAdapter(mFixTasks, this);
//        fixRec.setAdapter(mFixTaskDetailListAdapter);
//    }
//
//    //服务方法区 需要分离出
//    private RequestBean getRequestBean(String userId, String token) {
//
//        FixTaskRequest request = new FixTaskRequest();
//        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
//        request.setBody(request.new FixTaskBody().setRepairOrderId(mCompanyRepairInfo.getId()));
//        return request;
//    }
//
//    private void requestFixTaskListInfo() {
//        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_FIX_TASK,
//                getRequestBean(getConfig().getUserId(), getConfig().getToken())) {
//            @Override
//            protected void onResponse(NetTask task, String result) {
//                FixTaskResponse response = FixTaskResponse.getFixTaskResponse(result);
//                mFixTasks = response.getBody();
//                //获取到了返回的信息
//                if (mFixTasks == null || mFixTasks.size() == 0) {
//                    //findViewById(R.id.tv_tips).setVisibility(View.VISIBLE);
//                    findViewById(R.id.fix_rec).setVisibility(View.GONE);
//                    return;
//                }
//                fillList();
//            }
//        };
//        addTask(task);
//    }

    //end 服务方法区 需要分离出
    @Override
    protected int getLayoutID() {
        return R.layout.avtivity_fix_task_detail;
    }

    @Override
    public void performItemCallback(FixTaskInfo data) {


    }



}
