package com.honyum.elevatorMan.activity.worker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.adapter.FixPaymentAdapter;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.data.FixComponent;
import com.honyum.elevatorMan.data.FixInfo;
import com.honyum.elevatorMan.net.FixNextTimeRequest;
import com.honyum.elevatorMan.net.FixPaymentRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.honyum.elevatorMan.net.base.NetConstant.RSP_CODE_SUC_0;

/**
 * Created by Star on 2017/6/13.  订单页面
 */

public class FixPaymentActivity extends BaseActivityWraper implements ListItemCallback<FixComponent> {


    @BindView(R.id.rlv_pay_list)
    ListView rlvPayList;
    @BindView(R.id.tv_componentname)
    TextView tvComponentname;
    @BindView(R.id.tv_moneycount)
    TextView tvMoneycount;
    @BindView(R.id.iv_remove_item)
    ImageView ivRemoveItem;
    @BindView(R.id.tv_fix_payment)
    TextView tvFixPayment;
    @BindView(R.id.tv_add_item)
    TextView tvAddItem;

    private FixPaymentAdapter mFixPaymentAdapter;

    private FixInfo mFixInfo;

    private List<FixComponent> datas  = new ArrayList<FixComponent>();


    @Override
    public String getTitleString() {
        return getString(R.string.paymentsubmit);
    }

    @Override
    protected void initView() {
        ivRemoveItem.setVisibility(View.GONE);
        mFixInfo = getIntent("Info");
        mFixPaymentAdapter = new FixPaymentAdapter(this,datas);
        rlvPayList.setAdapter(mFixPaymentAdapter);
        tvComponentname.setTextColor(getResources().getColor(R.color.color_list_indexred));
        tvComponentname.setText(R.string.count);
        tvMoneycount.setText("¥0.0");
    }
    //算钱
    private double countMoney()
    {
        double result = 0.0;
        for (int i = 0 ; i<datas.size();i++)
        {
            result +=datas.get(i).getPrice();
        }
        return result;

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_pay_list;
    }


    @OnClick({R.id.tv_fix_payment,R.id.tv_add_item})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_fix_payment:
                requestAddPriceDetails();
                break;
            case R.id.tv_add_item:
                showAddDialog();
                break;

        }
    }
    public void showAddDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(FixPaymentActivity.this);

        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(FixPaymentActivity.this).inflate(R.layout.dia_addpayment, null);
        //    设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view);

        final EditText fee = (EditText)view.findViewById(R.id.et_fee);
        final EditText name = (EditText)view.findViewById(R.id.et_name);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                if(isNumber(fee.getText().toString().trim())) {
                    datas.add(new FixComponent().setName(name.getText().toString().trim()).
                            setPrice(Double.valueOf(fee.getText().toString().trim())).
                            setRepairOrderId(mFixInfo.getId()));
                    tvMoneycount.setText(countMoney() + "");
                    mFixPaymentAdapter.notifyDataSetChanged();
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void requestAddPriceDetails() {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_FIX_PAY_ADD,
                getRequestBean(getConfig().getUserId(), getConfig().getToken())) {
            @Override
            protected void onResponse(NetTask task, String result) {
                Response response = Response.getResponse(result);
                if (response.getHead() != null && response.getHead().getRspCode().equals(RSP_CODE_SUC_0)) {
                    showAppToast(getString(R.string.sucess));
                    finish();
                }
            }
        };
        addTask(task);
    }
    /**
     * 判断字符串是否是数字
     */
    public  boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }
    /**
     * 判断字符串是否是整数
     */
    public  boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是浮点数
     */
    public  boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private RequestBean getRequestBean(String userId, String token) {

        FixPaymentRequest request = new FixPaymentRequest();
        request.setHead(new NewRequestHead().setaccessToken(token).setuserId(userId));
        request.setBody(datas);
        return request;
    }

    @Override
    public void performItemCallback(FixComponent data) {
        datas.remove(data);
        tvMoneycount.setText("¥"+countMoney());
        mFixPaymentAdapter.notifyDataSetChanged();

    }

}
