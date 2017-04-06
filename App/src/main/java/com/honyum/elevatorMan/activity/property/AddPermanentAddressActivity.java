package com.honyum.elevatorMan.activity.property;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.AddSelActivity;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.net.AddPropertyAddressRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestHead;

public class AddPermanentAddressActivity extends BaseFragmentActivity {

    private double addLat;

    private double addLng;

    private EditText etAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_permanent_address);

        initTitleBar("添加常驻地址", R.id.title_address, R.drawable.back_normal, backClickListener);

        initView();
    }

    private void initView() {
        final EditText etShortAdd = (EditText) findViewById(R.id.et_short_address);
        etAddress = (EditText) findViewById(R.id.et_address);

        findViewById(R.id.iv_locaiton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String add = etAddress.getText().toString();
                if (TextUtils.isEmpty(add)) {
                    showToast("请先填写详细地址");
                    return;
                }
                Intent intent = new Intent(AddPermanentAddressActivity.this, AddSelActivity.class);
                intent.putExtra("add", add);
                startActivityForResult(intent, 101);
            }
        });

        findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitAddress(etShortAdd.getText().toString(), etAddress.getText().toString());
            }
        });
    }

    private void commitAddress(String shortAdd, String address) {

        if (TextUtils.isEmpty(shortAdd) || TextUtils.isEmpty(address)) {
            showToast("请填写完整信息");
            return;
        }

        if (addLat == 0 || addLng == 0) {
            showToast("请选择详细位置");
            return;
        }

        String server = getConfig().getServer() + NetConstant.ADD_PROPERTY_ADDRESS;

        AddPropertyAddressRequest request = new AddPropertyAddressRequest();
        RequestHead head = new RequestHead();
        AddPropertyAddressRequest.AddPaReqBody body = request.new AddPaReqBody();

        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());

        body.setBranchId(getConfig().getBranchId());
        body.setShortName(shortAdd);
        body.setAddress(address);
        body.setLat(addLat);
        body.setLng(addLng);

        request.setHead(head);
        request.setBody(body);

        NetTask netTask = new NetTask(server, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                showToast("添加成功");
                onBackPressed();
            }
        };

        addTask(netTask);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        double[] latlng = data.getDoubleArrayExtra("latlng");

        addLat = latlng[0];
        addLng = latlng[1];
        etAddress.setText(data.getStringExtra("address"));
    }
}
