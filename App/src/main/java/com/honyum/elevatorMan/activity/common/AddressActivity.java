package com.honyum.elevatorMan.activity.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.City;
import com.honyum.elevatorMan.data.CityInfo;
import com.honyum.elevatorMan.data.DistrictInfo;
import com.honyum.elevatorMan.data.DistrictList;
import com.honyum.elevatorMan.data.Province;
import com.honyum.elevatorMan.net.HomeReportRequest;
import com.honyum.elevatorMan.net.WorkPlaceReportRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.PipedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddressActivity extends BaseFragmentActivity {

    private EditText etAddress;

    private TextView tvLat;

    private TextView tvLng;

    private TextView tvDistict;

    private TextView tvCity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initTitleBar();
        initView();
    }

    private void initTitleBar() {
        initTitleBar("地址填写", R.id.title_address,
                R.drawable.back_normal, backClickListener);
    }

    private void initView() {
        etAddress = (EditText) findViewById(R.id.et_address);


        tvDistict = (TextView) findViewById(R.id.tv_zone);

        tvCity = (TextView) findViewById(R.id.tv_city);

        String category = getIntent().getStringExtra("category");
        if (category.equals("home")) {
            if (!StringUtils.isEmpty(getConfig().getHCity())) {
                tvCity.setText(getConfig().getHCity());
            }
            tvDistict.setText(getConfig().getHDistrict());
            if(!StringUtils.isEmpty(getConfig().getHAddress())) {
                etAddress.setText(getConfig().getHAddress());
            }
        } else if (category.equals("work")) {
            if (!StringUtils.isEmpty(getConfig().getWCity())) {
                tvCity.setText(getConfig().getWCity());
            }
            tvDistict.setText(getConfig().getWDistrict());
            if(!StringUtils.isEmpty(getConfig().getWAddress())) {
                etAddress.setText(getConfig().getWAddress());
            }
        }

        tvDistict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDistrictInfoFromFile();
            }
        });

        tvLat = (TextView) findViewById(R.id.tv_lat);
        tvLng = (TextView) findViewById(R.id.tv_lng);

        findViewById(R.id.ll_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String add = etAddress.getText().toString();
                if (StringUtils.isEmpty(add)) {
                    showToast("请先填写详细地址");
                    return;
                }
                Intent intent = new Intent(AddressActivity.this, AddSelActivity.class);
                intent.putExtra("add", add);
                startActivityForResult(intent, 0);
            }
        });

        findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String district = tvDistict.getText().toString();
                if (StringUtils.isEmpty(district)) {
                    showToast("请选择区域!");
                    return;
                }
                String address = etAddress.getText().toString();
                if (StringUtils.isEmpty(address)) {
                    showToast("请输入详细地址");
                    return;
                }

                String latStr = tvLat.getText().toString();
                String lngStr = tvLng.getText().toString();

                if (StringUtils.isEmpty(latStr) || StringUtils.isEmpty(lngStr)) {
                    showToast("请点击定位按钮确定您的经纬度");
                    return;
                }
                double lat = Double.parseDouble(latStr);
                double lng = Double.parseDouble(lngStr);

                String category = getIntent().getStringExtra("category");
                if (category.equals("home")) {
                    reportHome(district, address, lat, lng);
                } else if (category.equals("work")) {
                    reportWork(district, address, lat, lng);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null == data) {
            return;
        }
        double[] d = data.getDoubleArrayExtra("latlng");
        if (null == d || 0 == d.length) {
            showToast("没有获取您的经纬度，请修改详细地址为有效的地址!");
        } else {

            findViewById(R.id.ll_latlng).setVisibility(View.VISIBLE);
            tvLat.setText("" + d[0]);
            tvLng.setText("" + d[1]);
        }
    }

    /**
     * 从文件中获取城市信息
     */
    private void getDistrictInfoFromFile() {
        try {
            InputStream inputStream = getAssets().open("beijing_districts.json");
            int size = inputStream.available();

            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, "UTF-8");
            DistrictList info = DistrictList.getDistricts(json);


            View view = View.inflate(this, R.layout.layout_districts_selection, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setView(view);
            builder.setCancelable(false);
            Dialog dialog = builder.create();
            List<DistrictInfo> districtInfos = info.getDistricts();
            initDistrictsInfo(view, dialog, districtInfos);
            dialog.show();

            //设置弹出框的padding值
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            View decorView = dialogWindow.getDecorView();
            decorView.setPadding(0, 50, 0, 50);
            dialogWindow.setAttributes(lp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化城市选择的弹出框
     * @param view
     * @param dialog
     * @param districtInfos
     */
    private void initDistrictsInfo(View view, final Dialog dialog, final List<DistrictInfo> districtInfos) {



        final ListView listView = (ListView) view.findViewById(R.id.list_districts);


        //监听城市选择事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String region = ((TextView) view).getText().toString();
                tvDistict.setText(region);
                dialog.dismiss();
            }
        });




        DistrictAdapter adapter = new DistrictAdapter(this, districtInfos);
        listView.setAdapter(adapter);

        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    /**
     * 城市选择适配器
     */
    private class DistrictAdapter extends BaseAdapter {

        private Context mContext;

        private List<DistrictInfo> mList;

        public DistrictAdapter(Context context, List<DistrictInfo> cityList) {
            mContext = context;
            mList = cityList;
        }
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = View.inflate(mContext, R.layout.layout_main_help_item, null);
            }

            ((TextView) convertView).setText(mList.get(position).getName());
            convertView.setTag(mList.get(position));

            return convertView;
        }
    }

    private RequestBean getWorkRequestBean(String district, String address, double lat, double lng) {
        WorkPlaceReportRequest request = new WorkPlaceReportRequest();
        WorkPlaceReportRequest.WorkPlaceReportReqBody body = request.new WorkPlaceReportReqBody();
        RequestHead head = new RequestHead();

        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());

        body.setResident_county(district);
        body.setResident_address(address);
        body.setResident_lat(lat);
        body.setResident_lng(lng);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    private RequestBean getHomeRequestBean(String district, String address, double lat, double lng) {
        HomeReportRequest request = new HomeReportRequest();
        HomeReportRequest.HomeReportReqBody body = request.new HomeReportReqBody();
        RequestHead head = new RequestHead();

        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());

        body.setFamily_county(district);
        body.setFamily_address(address);
        body.setFamily_lat(lat);
        body.setFamily_lng(lng);

        request.setHead(head);
        request.setBody(body);

        return request;
    }


    private void reportWork(final String district, final String address, double lat, double lng) {
        String server = getConfig().getServer();
        RequestBean request = getWorkRequestBean(district, address, lat, lng);
        NetTask task = new NetTask(server + NetConstant.URL_REPORT_WORK_PLACE, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                showToast("您的工作地址更新成功!");
                getConfig().setWDistrict(district);
                getConfig().setWAddress(address);
                finish();
            }
        };

        addTask(task);
    }

    private void reportHome(final String district, final String address, double lat, double lng) {
        String server = getConfig().getServer();
        RequestBean request = getHomeRequestBean(district, address, lat, lng);
        NetTask task = new NetTask(server + NetConstant.URL_REPORT_HOME_PLACE, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                showToast("您的家庭住址更新成功!");
                getConfig().setHDistrict(district);
                getConfig().setHAddress(address);
                finish();
            }
        };

        addTask(task);
    }
}
