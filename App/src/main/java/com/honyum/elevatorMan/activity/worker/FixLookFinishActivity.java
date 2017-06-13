package com.honyum.elevatorMan.activity.worker;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.FixInfo;
import com.honyum.elevatorMan.data.FixTaskInfo;
import com.honyum.elevatorMan.net.FixWorkFinishRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.Response;

import butterknife.BindView;
import butterknife.OnClick;

import static com.honyum.elevatorMan.net.base.NetConstant.FIX_PAYMENT_END;
import static com.honyum.elevatorMan.net.base.NetConstant.RSP_CODE_SUC_0;

/**
 * Created by Star on 2017/6/12.
 */

public class FixLookFinishActivity extends BaseActivityWraper {

    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.iv_image1)
    ImageView ivImage1;
    @BindView(R.id.iv_image2)
    ImageView ivImage2;
    @BindView(R.id.iv_image3)
    ImageView ivImage3;
    @BindView(R.id.iv_image4)
    ImageView ivImage4;
    @BindView(R.id.tv_fix_complete)
    TextView tvFixComplete;
    @BindView(R.id.tv_fix_complete1)
    TextView tvFixComplete1;
    private FixTaskInfo mFixTaskInfo;
    private String currId;
    private String currImage;
    private FixInfo mFixInfo;

    @Override
    public String getTitleString() {
        return getString(R.string.fix_look_finish);
    }

    @Override
    protected void initView() {
        mFixTaskInfo = getIntent("Info");
        mFixInfo = getIntent("Fixdata");
        currId = mFixTaskInfo.getId();
        if (mFixInfo.getState().equals(FIX_PAYMENT_END)) {
            tvFixComplete1.setVisibility(View.VISIBLE);
            tvFixComplete.setVisibility(View.GONE);
        } else {
            tvFixComplete.setVisibility(View.VISIBLE);
            tvFixComplete1.setVisibility(View.GONE);
        }


    }

    private void requestFixLookFinish(String state) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_FIX_FINISH,
                getRequestBean(getConfig().getUserId(), getConfig().getToken(), state)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                Response response = Response.getResponse(result);
                if (response.getHead() != null && response.getHead().getRspCode().equals(RSP_CODE_SUC_0)) {
                    showAppToast(getString(R.string.sucess));
                    finish();
                }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());
            }
            //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());
        };
        addTask(task);
    }

    private RequestBean getRequestBean(String userId, String token, String state) {

        FixWorkFinishRequest request = new FixWorkFinishRequest();
        request.setHead(new NewRequestHead().setaccessToken(token).setuserId(userId));
        //TODO 这里的上传图片没有做
        request.setBody(request.new FixWorkFinishBody().setRepairOrderProcessId(currId).setState(state).setFinishResult(etRemark.getText().toString()).setPictures(currImage));
        return request;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_fix_result;
    }


    @OnClick({R.id.iv_image1, R.id.iv_image2, R.id.iv_image3, R.id.iv_image4, R.id.tv_fix_complete, R.id.tv_fix_complete1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_image1:
                break;
            case R.id.iv_image2:
                break;
            case R.id.iv_image3:
                break;
            case R.id.iv_image4:
                break;
            case R.id.tv_fix_complete:
                requestFixLookFinish(NetConstant.FIX_LOOK_FINISHED);
                break;
            case R.id.tv_fix_complete1:
                requestFixLookFinish(NetConstant.FIX_FIX_FINISH);
                break;
        }
    }
}
