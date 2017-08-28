package com.honyum.elevatorMan.activity.common;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Star on 2017/8/15.
 */

public class AddInsuranceDataActivity extends BaseActivityWraper {


    private String returnData = "";
    private ArrayAdapter<String> adapter;
    private String title;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.sp_input_list)
    Spinner sp_input_list;
    private int currType;


    private String[] data;

    @Override
    public String getTitleString() {
        return "修改信息";
    }

    @Override
    protected void initView() {
        Intent it = getIntent();
        title = it.getStringExtra("title");
        mTvTitle.setText(title);
        currType = it.getIntExtra("type", 0);
        if (it.getBooleanExtra("isSelect", false)) {
            sp_input_list.setVisibility(View.VISIBLE);
            switch (currType) {
                case InsuranceBuyActivity.POLITICAL:
                    data = getResources().getStringArray(R.array.political_data);
                    break;
                case InsuranceBuyActivity.GRADE:
                    data = getResources().getStringArray(R.array.education_data);
                    break;
            }
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data);
            sp_input_list.setAdapter(adapter);
            sp_input_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    returnData = data[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            mEtInput.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_alter_insurance;
    }


    private void setResultData(Intent it, int index) {


        if(StringUtils.isNotEmpty(returnData)) {
            it.putExtra("returndata", returnData);
            setResult(index, it);
            finish();
        }

    }

    @OnClick(R.id.tv_submit)
    public void onViewClicked() {
        Intent it = new Intent();
        if (currType == InsuranceBuyActivity.IDCARD) {
            if (Utils.idCardValidate(mEtInput.getText().toString())) {
                returnData = mEtInput.getText().toString();
                setResultData(it, InsuranceBuyActivity.IDCARD);
            }
            else
            {
                showToast("请输入合法的身份证编号！");
            }
        } else if (currType == InsuranceBuyActivity.POLITICAL&&!returnData.equals("")) {
            setResultData(it, InsuranceBuyActivity.POLITICAL);
        } else if (currType == InsuranceBuyActivity.GRADE&&!returnData.equals("")) {
            setResultData(it, InsuranceBuyActivity.GRADE);
        } else if (currType == InsuranceBuyActivity.MAJOR&&mEtInput.getText().toString()!=null) {
            returnData = mEtInput.getText().toString();
            setResultData(it, InsuranceBuyActivity.MAJOR);
        }
    }


}
