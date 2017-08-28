package com.honyum.elevatorMan.activity.common;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.data.InsuranceInfo;
import com.honyum.elevatorMan.net.InsuranceCommitRequest;
import com.honyum.elevatorMan.net.InsuranceUserInfoResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Star on 2017/8/15.
 */

public class InsuranceBuyActivity extends BaseActivityWraper {


    private Intent returnIntent;

    public static final int IDCARD = 1;
    public static final int POLITICAL = 2;
    public static final int GRADE = 3;
    public static final int MAJOR = 4;

    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.tv_idcard)
    TextView mTvIdcard;
    @BindView(R.id.tv_change_id)
    TextView mTvChangeId;
    @BindView(R.id.tv_political)
    TextView mTvPolitical;
    @BindView(R.id.tv_change_political)
    TextView mTvChangePolitical;
    @BindView(R.id.tv_edu_grade)
    TextView mTvEduGrade;
    @BindView(R.id.tv_change_degrade)
    TextView mTvChangeDegrade;
    @BindView(R.id.tv_major)
    TextView mTvMajor;
    @BindView(R.id.tv_change_major)
    TextView mTvChangeMajor;
    @BindView(R.id.tv_submit)
    TextView tv_submit;

    @Override
    public String getTitleString() {
        return "我要买保险";
    }

    @Override
    protected void initView() {

        getUserInfoById();
    }

    private void editInsuranceByUser(InsuranceInfo ii) {
        String serverUrl = getConfig().getServer() + NetConstant.EDIT_INSURANCE;
        InsuranceCommitRequest bean = new InsuranceCommitRequest();
        bean.setHead(new NewRequestHead().setuserId(getConfig().getUserId()).setaccessToken(getConfig().getToken()));
        bean.setBody(ii);
        NetTask netTask = new NetTask(serverUrl, bean) {
            @Override
            protected void onResponse(NetTask task, String result) {
                showToast("提交成功！");
                finish();
            }
        };
        addTask(netTask);
    }

    private void getUserInfoById() {
        String serverUrl = getConfig().getServer() + NetConstant.GET_PERSON_INFO;
        RequestBean bean = new RequestBean();
        bean.setHead(new NewRequestHead().setuserId(getConfig().getUserId()).setaccessToken(getConfig().getToken()));
        NetTask netTask = new NetTask(serverUrl, bean) {
            @Override
            protected void onResponse(NetTask task, String result) {

                InsuranceUserInfoResponse ir = InsuranceUserInfoResponse.getInsuranceUserInfoResponse(result);
                if (ir.getBody() != null) {
                    mTvName.setText(ir.getBody().getName());
                    mTvSex.setText(ir.getBody().getSex().equals("1")?"男":"女");
                    mTvPhone.setText(ir.getBody().getTel());
                }


            }
        };
        addTask(netTask);
    }

    private void setFormData(TextView v) {

        v.setText(returnIntent.getStringExtra("returndata"));
        returnIntent = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        returnIntent = data;
        if (resultCode == RESULT_CANCELED)
        {
            return;
        }
        switch (requestCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case IDCARD:
                setFormData(mTvIdcard);
                break;
            case POLITICAL:
                setFormData(mTvPolitical);
                break;
            case GRADE:
                setFormData(mTvEduGrade);
                break;
            case MAJOR:
                setFormData(mTvMajor);
                break;


        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_buy_insurance;
    }

    public void toAnotherActivity(String title, int index, boolean select) {
        Intent it = new Intent(this, AddInsuranceDataActivity.class);
        it.putExtra("title", title);
        it.putExtra("type", index);
        it.putExtra("isSelect", select);
        startActivityForResult(it, index);
    }


    @OnClick({R.id.tv_change_id, R.id.tv_change_political, R.id.tv_change_degrade, R.id.tv_change_major, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_change_id:
                toAnotherActivity("修改身份证", IDCARD, false);
                break;
            case R.id.tv_change_political:
                toAnotherActivity("修改政治面貌", POLITICAL, true);
                break;
            case R.id.tv_change_degrade:
                toAnotherActivity("修改学历", GRADE, true);
                break;
            case R.id.tv_change_major:
                toAnotherActivity("修改专业", MAJOR, false);
                break;
            case R.id.tv_submit:

                if ((isNotEmpty(mTvName.getText() + "", mTvPhone.getText() + "", mTvSex.getText() + "", mTvIdcard.getText() + "", mTvPolitical.getText() + "", mTvEduGrade.getText() + "", mTvMajor.getText() + "")))

                {
                    InsuranceInfo ii = new InsuranceInfo();
                    ii.setEducation(mTvEduGrade.getText() + "");
                    ii.setIdentityCard(mTvIdcard.getText()+"");
                    ii.setMajor(mTvMajor.getText()+"");
                    ii.setName(mTvName.getText() + "");
                    ii.setPolitical(mTvPolitical.getText() + "");
                    ii.setSex(mTvSex.getText() + "");
                    ii.setTel(mTvPhone.getText() + "");
                    editInsuranceByUser(ii);
                    break;
                }
                else
                {
                    showToast("请填写完整的个人信息！");
                }

        }
    }

    public static boolean isNotEmpty(String... s) {
        for (String s1 : s) {

            boolean b = s1 != null && s1.length() != 0;
            if (!b)
                return false;

        }
        return true;
    }
}
