package com.honyum.elevatorMan.activity.worker;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.FixTaskInfo;
import com.honyum.elevatorMan.utils.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Star on 2017/6/12.
 */

public class FixResultLookActivity extends BaseActivityWraper {


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
    @BindView(R.id.ll_full_screen)
    LinearLayout llFullScreen;
    @BindView(R.id.iv_overview)
    ImageView ivOverview;
    @BindView(R.id.ll_image1)
    LinearLayout llImage1;
    @BindView(R.id.ll_image2)
    LinearLayout llImage2;
    @BindView(R.id.ll_image3)
    LinearLayout llImage3;
    @BindView(R.id.ll_image4)
    LinearLayout llImage4;


    private FixTaskInfo mFixTaskInfo;


    @Override
    public String getTitleString() {
        return getString(R.string.fix_result_look);
    }

    @Override
    protected void initView() {

        tvFixComplete.setVisibility(View.GONE);
        tvFixComplete1.setVisibility(View.GONE);
        mFixTaskInfo = getIntent("Info");
        ivImage1.setDrawingCacheEnabled(true);
        ivImage2.setDrawingCacheEnabled(true);
        ivImage3.setDrawingCacheEnabled(true);
        ivImage4.setDrawingCacheEnabled(true);
        etRemark.setHint("");
        etRemark.setFocusable(false);
        etRemark.setText(mFixTaskInfo.getFinishResult());
        String[] s = mFixTaskInfo.getPictures().split(",");
        Log.e("!!!!!!!!!!!!!!!!!!", "initView: "+s[0]);
        ImageView[] imageIndex = new ImageView[]{ivImage1, ivImage2, ivImage3, ivImage4};
        LinearLayout[] lls = new LinearLayout[]{llImage1,llImage2,llImage3,llImage4};
        for (int i =0 ; i<lls.length;i++)
        {
            lls[i].setVisibility(View.GONE);
        }
        if (s != null&&!s.equals("")) {
            for (int i = 0; i < s.length; i++) {
                new GetPicture(s[i], imageIndex[i]).execute();
                lls[i].setVisibility(View.VISIBLE);
            }
        }


        // new GetPicture(mMaintenanceTaskInfo.getAfterImg(),iv_after_image).execute();

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_fix_result;
    }

    /**
     * 查看预览信息
     *
     * @param image1
     */

    private void showPreviewImage(ImageView image1) {

        String s = (String) image1.getTag(R.id.file_path);
        if(StringUtils.isNotEmpty(s))
        ivOverview.setImageBitmap(Utils.getImageFromFile(new File(s)));
        llFullScreen.setVisibility(View.VISIBLE);
        image1.setDrawingCacheEnabled(false);
        image1.setDrawingCacheEnabled(true);
    }

    @OnClick({R.id.iv_image1, R.id.iv_image2, R.id.iv_image3, R.id.iv_image4, R.id.ll_full_screen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_image1:
                showPreviewImage(ivImage1);
                break;
            case R.id.iv_image2:
                showPreviewImage(ivImage2);
                break;
            case R.id.iv_image3:
                showPreviewImage(ivImage3);
                break;
            case R.id.iv_image4:
                showPreviewImage(ivImage4);
                break;
            case R.id.ll_full_screen:
                llFullScreen.setVisibility(View.GONE);
                break;
        }
    }


}
