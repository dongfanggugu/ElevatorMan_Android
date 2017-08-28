package com.honyum.elevatorMan.activity.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.net.AddPropertyAddressRequest;
import com.honyum.elevatorMan.net.HomeReportRequest;
import com.honyum.elevatorMan.net.WorkPlaceReportRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.SQLiteUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddressActivity extends BaseFragmentActivity {

    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    private TextView tv_province;
    private EditText etAddress;

    private TextView tvLat;


    private Handler handler;
    private TextView tvLng;

    private TextView tvDistict;

    private TextView tvCity;

    private String rAddress;

    private final static int PROVINCE_SELECT = 0;
    private final static int CITY_SELECT = 1;
    private final static int ZONE_SELECT = 2;
    private final static int CITY_REPLACE = 3;
    private final static int ZONE_REPLACE = 4;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initTitleBar();

        //getDistrictInfoFromFile();
        initView();
    }

    private void initTitleBar() {
        initTitleBar("地址填写", R.id.title_address,
                R.drawable.back_normal, backClickListener);
    }

    public void dealMessage(Context context, Message msg, TextView displayView) {
        View view = View.inflate(context, R.layout.layout_districts_selection, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(view);
        builder.setCancelable(false);
        Dialog dialog = builder.create();
        List<cityData> pData = (List<cityData>) msg.obj;
        if (pData != null) {
            initDistrictsInfo(view, dialog, pData, displayView);
            dialog.show();

            //设置弹出框的padding值
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            View decorView = dialogWindow.getDecorView();
            decorView.setPadding(0, 50, 0, 50);
            dialogWindow.setAttributes(lp);
        }
    }
    private void initView() {
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.arg1) {
                    case PROVINCE_SELECT:
                        tv_province.setTag(R.id.index, PROVINCE_SELECT);
                        dealMessage(AddressActivity.this, msg, tv_province);
                        break;
                    case CITY_SELECT:
                        tvCity.setTag(R.id.index, CITY_SELECT);
                        dealMessage(AddressActivity.this, msg, tvCity);
                        break;
                    case ZONE_SELECT:
                        tvDistict.setTag(R.id.index, ZONE_SELECT);
                        dealMessage(AddressActivity.this, msg, tvDistict);
                        break;
                    case CITY_REPLACE:

                        tv_province.setTag(((List<cityData>) msg.obj).get(0));
                        tvCity.callOnClick();
                        //dealMessage(AddressActivity.this, msg, tv_province);
                        break;
                    case ZONE_REPLACE:

                        tvCity.setTag(((List<cityData>) msg.obj).get(0));
                        tvDistict.callOnClick();
                        break;
                }
            }
        };

        tv_province = (TextView) findViewById(R.id.tv_province);
        etAddress = (EditText) findViewById(R.id.et_address);


        tvDistict = (TextView) findViewById(R.id.tv_zone);

        tvCity = (TextView) findViewById(R.id.tv_city);


        //lambda
        tv_province.setOnClickListener(v -> getInfoFromDataBase("T_Province", PROVINCE_SELECT, "ProSort", "ProName", null, null));

        String category = getIntent().getStringExtra("category");
        if (category.equals("home")) {
            if (!StringUtils.isEmpty(getConfig().getHProvince())) {
                tv_province.setText(getConfig().getHProvince());
            }
            if (!StringUtils.isEmpty(getConfig().getHCity())) {
                tvCity.setText(getConfig().getHCity());
            }
            if (!StringUtils.isEmpty(getConfig().getHDistrict())) {
                tvDistict.setText(getConfig().getHDistrict());
            }

            if(!StringUtils.isEmpty(getConfig().getHAddress())) {
                etAddress.setText(getConfig().getHAddress());
            }
        } else if (category.equals("work")) {
            if (!StringUtils.isEmpty(getConfig().getWProvince())) {
                tv_province.setText(getConfig().getWProvince());
            }
            if (!StringUtils.isEmpty(getConfig().getWCity())) {
                tvCity.setText(getConfig().getWCity());
            }
            if (!StringUtils.isEmpty(getConfig().getWDistrict())) {
                tvDistict.setText(getConfig().getWDistrict());
            }
            if(!StringUtils.isEmpty(getConfig().getWAddress())) {
                etAddress.setText(getConfig().getWAddress());
            }
        }

        tvCity.setOnClickListener(v -> {
            if (tv_province.getTag() != null)
                getInfoFromDataBase("T_CITY", CITY_SELECT, "CitySort", "CityName", "ProId", ((cityData) tv_province.getTag()).getId());
            else if (!StringUtils.isEmpty(tvCity.getText()+"")) {
                getInfoFromDataBase("T_CITY", CITY_REPLACE, "ProID", "CityName", "CityName", "'" + tvCity.getText().toString() + "'");

            } else {
                showToast("请先填写上级信息！");
            }
        });

        //zone
        tvDistict.setOnClickListener(v -> {
            if (tvCity.getTag() != null)
                getInfoFromDataBase("T_ZONE", ZONE_SELECT, "ZoneID", "ZoneName", "CityID", ((cityData) tvCity.getTag()).getId());
            else if (!StringUtils.isEmpty(tvDistict.getText()+"")) {
                getInfoFromDataBase("T_ZONE", ZONE_REPLACE, "CityID", "ZoneName", "ZoneName", "'" + tvDistict.getText().toString() + "'");

            } else {
                showToast("请先填写上级信息！");
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
                intent.putExtra("city", tvDistict.getText().toString());
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
                    showToast("请输入详细地址!");
                    return;
                }

                String province = tv_province.getText().toString();
                if (StringUtils.isEmpty(province)) {
                    showToast("请选择省!");
                    return;
                }
                String city = tvCity.getText().toString();
                if (StringUtils.isEmpty(city)) {
                    showToast("请选择城市!");
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
                    reportHome(district, address, city, province, lat, lng);
                } else if (category.equals("work")) {
                    reportWork(district, address, city, province, lat, lng);
                }
                else if(category.equals("wy"))
                {
                    commitAddress(province+city+district,address,lat, lng);
                }
            }
        });
    }
    private void commitAddress(String shortAdd, String address,double addLat,double addLng) {

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
        if (null == data) {
            return;
        }
        double[] d = data.getDoubleArrayExtra("latlng");
        String address = data.getStringExtra("address");

        if ((null == d || 0 == d.length)&&StringUtils.isNotEmpty(address)) {
            showToast("没有获取您的经纬度，请修改详细地址为有效的地址!");
        } else {

            findViewById(R.id.ll_latlng).setVisibility(View.VISIBLE);
            tvLat.setText("" + d[0]);
            tvLng.setText("" + d[1]);
            etAddress.setText(address);

        }
    }

    /**
     * 复制assets下数据库到data/data/packagename/databases
     *
     * @param context
     * @throws IOException
     */
    private static final String DB_PATH = "/data/data/com.chorstar.enterOwner/databases/";
    private static final String DB_NAME = "china_Province_city_zone.db";

    public void copyDBToDatabases(Context context) throws IOException {

        String outFileName = DB_PATH + DB_NAME;

        File file = new File(DB_PATH);
        if (!file.mkdirs()) {
            file.mkdirs();
        }

        if (new File(outFileName).exists()) {
            // 数据库已经存在，无需复制
            return;
        }

        InputStream myInput = context.getAssets().open(DB_NAME);
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
    /**
     * 从文件中获取城市信息
     */
    private void getInfoFromDataBase(final String table, final int arg1, final String columnId, final String columnName, String queryColumn, String queryVar) {
        try {
//            InputStream inputStream = getAssets().open("beijing_districts.json");
//            int size = inputStream.available();
//
//            byte[] buffer = new byte[size];
//            inputStream.read(buffer);
//            inputStream.close();
//
//            String json = new String(buffer, "UTF-8");
//            DistrictList info = DistrictList.getDistricts(json);
//
//
//            View view = View.inflate(this, R.layout.layout_districts_selection, null);
//            AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                    .setView(view);
//            builder.setCancelable(false);
//            Dialog dialog = builder.create();
//            List<DistrictInfo> districtInfos = info.getDistricts();
//            initDistrictsInfo(view, dialog, districtInfos);
//            dialog.show();
//
//            //设置弹出框的padding值
//            Window dialogWindow = dialog.getWindow();
//            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//            View decorView = dialogWindow.getDecorView();
//            decorView.setPadding(0, 50, 0, 50);
//            dialogWindow.setAttributes(lp);

            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        copyDBToDatabases(AddressActivity.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final SQLiteDatabase db = SQLiteUtils.opendb(AddressActivity.this,
                            DB_PATH + DB_NAME);
                    String sql = "select " + columnId + "," + columnName + " from " + table;
                    if (queryColumn != null) {
                        sql += " where " + queryColumn + "=" + queryVar;
                        //"'"+queryVar+"'";
                    }
                    Log.e("TAG", "run: " + sql);
                    // db operetions, u can use handler to send message after db.insert(yourTableName, null, value);
                    Cursor cursor = db.rawQuery(sql, null);
                    List<cityData> tp = new ArrayList<cityData>();
                    while (cursor.moveToNext()) {
                        cityData tpIn = new cityData();
                        tpIn.setName(cursor.getString(cursor.getColumnIndex(columnName)));
                        tpIn.setId(cursor.getString(cursor.getColumnIndex(columnId)));
                        tp.add(tpIn);
                    }
                    cursor.close();
                    Message message = Message.obtain();
                    message.arg1 = arg1;
                    message.obj = tp;
                    handler.sendMessage(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void getDataFromTable() {
//
//
//    }

    //adapter  item
    public class cityData {

        public cityData() {
        }

        public cityData(cityData mCityData) {
            this.id = mCityData.getId();
            this.name = mCityData.getName();
        }

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String name;
    }
    /**
     * 初始化城市选择的弹出框
     * @param view
     * @param dialog
     * @param districtInfos
     */
    private void initDistrictsInfo(View view, final Dialog dialog, final List<cityData> districtInfos, TextView tv_view) {



        final ListView listView = (ListView) view.findViewById(R.id.list_districts);


        //监听城市选择事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String region = ((TextView) view).getText().toString();
                cityData cd = new cityData((cityData) view.getTag());
                tv_view.setText(cd.getName());
                changeUI(tv_view);
                tv_view.setTag(cd);
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

    public void changeUI(TextView view) {

        switch ((int) view.getTag(R.id.index)) {
            case PROVINCE_SELECT:
                tvCity.setText("");
                tvDistict.setText("");
                etAddress.setText("");
                tvCity.setTag(null);
                tvDistict.setTag(null);
                etAddress.setTag(null);
                break;
            case CITY_SELECT:
                tvDistict.setText("");
                etAddress.setText("");
                tvDistict.setTag(null);
                etAddress.setTag(null);
                break;
            case ZONE_SELECT:
                etAddress.setText("");
                etAddress.setTag(null);
                break;
            default:
                break;
        }
    }

    /**
     * 城市选择适配器
     */
    private class DistrictAdapter extends BaseAdapter {

        private Context mContext;

        private List<cityData> mList;

        public DistrictAdapter(Context context, List<cityData> cityList) {
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

    private RequestBean getWorkRequestBean(String district, String address, final String city, final String province, double lat, double lng) {
        WorkPlaceReportRequest request = new WorkPlaceReportRequest();
        WorkPlaceReportRequest.WorkPlaceReportReqBody body = request.new WorkPlaceReportReqBody();
        RequestHead head = new RequestHead();

        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());

        body.setResident_county(district);
        body.setResident_address(address);
        body.setResident_city(city);
        body.setResident_province(province);
        body.setResident_lat(lat);
        body.setResident_lng(lng);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    private RequestBean getHomeRequestBean(String district, String address, final String city, final String province, double lat, double lng) {
        HomeReportRequest request = new HomeReportRequest();
        HomeReportRequest.HomeReportReqBody body = request.new HomeReportReqBody();
        RequestHead head = new RequestHead();

        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());

        body.setFamily_county(district);
        body.setFamily_address(address);
        body.setFamily_city(city);
        body.setFamily_province(province);

        body.setFamily_lat(lat);
        body.setFamily_lng(lng);

        request.setHead(head);
        request.setBody(body);

        return request;
    }


    private void reportWork(final String district, final String address, final String city, final String province, double lat, double lng) {
        String server = getConfig().getServer();
        RequestBean request = getWorkRequestBean(district, address, city, province, lat, lng);
        NetTask task = new NetTask(server + NetConstant.URL_REPORT_WORK_PLACE, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                showToast("您的工作地址更新成功!");
                getConfig().setWDistrict(district);
                getConfig().setWAddress(address);
                getConfig().setWCity(city);
                getConfig().setWProvince(province);

                finish();
            }
        };

        addTask(task);
    }

    private void reportHome(final String district, final String address, final String city, final String province, double lat, double lng) {
        String server = getConfig().getServer();
        RequestBean request = getHomeRequestBean(district, address, city, province, lat, lng);
        NetTask task = new NetTask(server + NetConstant.URL_REPORT_HOME_PLACE, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                showToast("您的家庭住址更新成功!");
                getConfig().setHDistrict(district);
                getConfig().setHAddress(address);
                getConfig().setHCity(city);
                getConfig().setHProvince(province);
                finish();
            }
        };

        addTask(task);
    }
}
