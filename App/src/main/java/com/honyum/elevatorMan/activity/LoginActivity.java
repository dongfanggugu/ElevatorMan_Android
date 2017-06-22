package com.honyum.elevatorMan.activity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.example.viewtest.*;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.MainPage1Activity;
import com.honyum.elevatorMan.activity.common.MainpageActivity;
import com.honyum.elevatorMan.activity.common.ResetPasswordActivity;
import com.honyum.elevatorMan.activity.company.MainPageActivity;
import com.honyum.elevatorMan.activity.maintenance_1.MaintenanceActivity;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.SysActivityManager;
import com.honyum.elevatorMan.constant.Constant;
import com.honyum.elevatorMan.data.City;
import com.honyum.elevatorMan.data.CityInfo;
import com.honyum.elevatorMan.data.Province;
import com.honyum.elevatorMan.net.LoginRequest;
import com.honyum.elevatorMan.net.LoginRequest.LoginReqBody;
import com.honyum.elevatorMan.net.LoginResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.EncryptUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends BaseFragmentActivity {

    /**
     * begin:用来处理长按事件的标记参数
     **/

    private int mDownFlag = 0;

    private boolean mIsReleased = false;

    private boolean mIsMoved = false;

    private int mLastMotionX = 0;

    private int mLastMotionY = 0;

    private static final int TOUCH_SLOP = 20;

    /**
     * end:用来处理长按事件的标记参数
     **/

    private TextView tvRegion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SysActivityManager.getInstance().addActivity(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 获取程序的UID
     */
    private int getUID() {
        int uid = 0;
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ApplicationInfo appInfo = getApplicationInfo();
        List<ActivityManager.RunningAppProcessInfo> run = manager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo info : run) {
            if (info.processName != null && info.processName.equals(appInfo.processName)) {
                uid = info.uid;
            }
        }
        return uid;
    }

    /**
     * 初始化view
     */
    private void initView() {
        findViewById(R.id.wei_bao).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MaintenanceActivity.class));
            }
        });

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        EditText userName = (EditText) findViewById(R.id.et_user);

        tvRegion = (TextView) findViewById(R.id.tv_region);

        ((TextView) findViewById(R.id.tv_version)).setText(getString(R.string.app_version));

        initRegionChoice();

        userName.setText(getConfig().getUserName());

        String errorCode = getConfig().getErrorCode();
        if (errorCode.equals("-3") || errorCode.equals("-2")) {
            showToast("登陆超时，请重新登陆!");
            getConfig().setErrorCode("");
        }

        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                //startActivity(new Intent(LoginActivity.this, com.example.netsdktest.LoginActivity.class));

//                String user = "admin";
//
//                String password = "888888";
//
//                String ip = "223.223.199.57";
//
//                int port = 18101;
//
//                Intent intent = new Intent(LoginActivity.this, com.chorstar.video.LiftVideoActivity.class);
//
//                intent.putExtra("user", user);
//                intent.putExtra("password", password);
//                intent.putExtra("ip", ip);
//                intent.putExtra("port", port);
//
//                startActivity(intent);
                String userName = ((EditText) findViewById(R.id.et_user)).getText().toString();
                String password = ((EditText) findViewById(R.id.et_pwd)).getText().toString();
                if (StringUtils.isEmpty(userName)) {
                    showToast(getString(R.string.user_null));
                    return;
                }

                if (StringUtils.isEmpty(password)) {
                    showToast(getString(R.string.pwd_null));
                    return;
                }

                login(userName, password);
            }

        });


        //注册
        findViewById(R.id.tv_register).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterStepOneActivity.class));

                //startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

        //忘记密码
        findViewById(R.id.tv_reset_pwd).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        //长按logo 5秒后弹出窗口，选择内部服务器
        findViewById(R.id.login_logo).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x = (int) event.getX();
                int y = (int) event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mLastMotionX = x;
                        mLastMotionY = y;
                        mDownFlag++;
                        mIsReleased = false;
                        mIsMoved = false;
                        mHandler.postDelayed(mLongPressRunnable, 5000);
                        break;

                    case MotionEvent.ACTION_UP:
                        mIsReleased = true;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (mIsMoved) {
                            break;
                        }

                        if (Math.abs(mLastMotionX - x) > TOUCH_SLOP
                                || Math.abs(mLastMotionY - y) > TOUCH_SLOP) {
                            mIsMoved = true;
                        }
                        break;
                }
                return true;
            }
        });
    }

    //长按的回掉处理
    Runnable mLongPressRunnable = new Runnable() {
        @Override
        public void run() {
            mDownFlag--;
            if (mDownFlag > 0 || mIsMoved || mIsReleased) {
                return;
            }

            View view = View.inflate(LoginActivity.this, R.layout.layout_company_list, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                    .setView(view);
            builder.setCancelable(false);
            Dialog dialog = builder.create();
            initInnerServerView(view, dialog);
            dialog.show();
        }
    };

    /**
     * 初始化服务器
     */
    private void initRegionChoice() {

        //设置城市为北京，并且不可以修改
        //getConfig().setRegion(Constant.BEIJING);
        String region = getConfig().getRegion();
        tvRegion.setText(region);

        tvRegion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                getCityInfoFromFile();
            }
        });

    }

    /**
     * 登录请求bean
     *
     * @return
     */
    private RequestBean getLoginReq(String userName, String password) {
        LoginRequest request = new LoginRequest();
        LoginReqBody body = request.new LoginReqBody();
        RequestHead head = new RequestHead();

        body.setUserName(userName);
        body.setPassword(EncryptUtils.encryptMD5(password));

        request.setBody(body);
        request.setHead(head);

        return request;
    }

    /**
     * 登录
     */
    private void login(String userName, String password) {
        getConfig().setUserName(userName);

        NetTask task = new NetTask(getConfig().getServer() + NetConstant.URL_LOG_IN,
                getLoginReq(userName, password)) {

            @Override
            protected void onResponse(NetTask task, String result) {
                // TODO Auto-generated method stub
                Log.i("zhenhao", "result:" + result);
                LoginResponse response = LoginResponse.getLoginResonse(result);

                String token = response.getHead().getAccessToken();

                setUserInfo(token, response.getBody());

                if (response.getBody().getType().equals(Constant.PROPERTY)) {
                    startProperty(true);
                }
                else if(response.getBody().getType().equals(Constant.COMPANY))
                {
                    startActivity(new Intent(LoginActivity.this, MainPageActivity.class));
                }
                    else {
                    //startWorker(getIntent() == null ? null : getIntent().getStringExtra("alarm_id"));
                    startActivity(new Intent(LoginActivity.this, MainPage1Activity.class));
                }
            }

        };
        addTask(task);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //从注册返回时刷新地区显示
        if (tvRegion != null) {
            tvRegion.setText(getConfig().getRegion());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 处理登录页面的返回事件，退出整个应用
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SysActivityManager.getInstance().exit();
    }

    /**
     * 从文件中获取城市信息
     */
    private void getCityInfoFromFile() {
        try {
            InputStream inputStream = getAssets().open("city_json.json");
            int size = inputStream.available();

            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, "UTF-8");
            CityInfo cityInfo = CityInfo.getCityInfoFromJson(json);


            View view = View.inflate(LoginActivity.this, R.layout.layout_company_list, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                    .setView(view);
            builder.setCancelable(false);
            Dialog dialog = builder.create();
            List<Province> provinceList = cityInfo.getProvinceList();
            Collections.sort(provinceList);
            initProvinceInfo(view, dialog, provinceList);
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
     * 初始化省份选择的弹出框
     *
     * @param view
     * @param dialog
     * @param provinceList
     */
    private void initProvinceInfo(View view, final Dialog dialog, final List<Province> provinceList) {
        TextView titleView = (TextView) view.findViewById(R.id.tv_title);
        titleView.setText("选择省份");

        final List<Province> adapterList = new ArrayList<Province>();


        final ListView listView = (ListView) view.findViewById(R.id.list_company);


        //监听省份选择
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();

                Province selectProvince = (Province) view.getTag();
                String title = selectProvince.getProname();
                List<City> cityList = selectProvince.getCitys();
                Collections.sort(cityList);

                View cityView = View.inflate(LoginActivity.this,
                        R.layout.layout_company_list, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                        .setView(cityView);
                Dialog customDialog = builder.create();
                initCityInfo(title, cityView, customDialog, cityList);

                customDialog.show();
            }
        });


        for (Province province : provinceList) {
            adapterList.add(province);
        }

        final ProvinceAdapter mAdapter = new ProvinceAdapter(this, adapterList);
        listView.setAdapter(mAdapter);

        final EditText filter = (EditText) view.findViewById(R.id.et_query);
        filter.setHint("请输入省份名称");
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                adapterList.clear();

                String curString = s.toString().trim();

                if (StringUtils.isEmpty(curString)) {
                    for (Province province : provinceList) {
                        adapterList.add(province);
                    }

                } else {
                    for (Province province : provinceList) {
                        if (province.getProname().contains(curString)) {
                            adapterList.add(province);
                        }
                    }
                }

                mAdapter.notifyDataSetChanged();
            }
        });


        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    /**
     * 省份选择适配器
     */
    private class ProvinceAdapter extends BaseAdapter {

        private Context mContext;

        private List<Province> mProvinceList;

        public ProvinceAdapter(Context context, List<Province> provinceList) {
            mContext = context;
            mProvinceList = provinceList;
        }

        @Override
        public int getCount() {
            return mProvinceList.size();
        }

        @Override
        public Object getItem(int position) {
            return mProvinceList.get(position);
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

            ((TextView) convertView).setText(mProvinceList.get(position).getProname());
            convertView.setTag(mProvinceList.get(position));

            return convertView;
        }
    }


    /**
     * 初始化城市选择的弹出框
     *
     * @param view
     * @param dialog
     * @param cityList
     */
    private void initCityInfo(String title, View view, final Dialog dialog, final List<City> cityList) {
        TextView titleView = (TextView) view.findViewById(R.id.tv_title);
        titleView.setText(title);

        final List<City> adapterList = new ArrayList<City>();


        final ListView listView = (ListView) view.findViewById(R.id.list_company);


        //监听城市选择事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String region = ((TextView) view).getText().toString();
                getConfig().setRegion(region);
                tvRegion.setText(region);
                dialog.dismiss();
            }
        });


        for (City city : cityList) {
            adapterList.add(city);
        }

        final CityAdapter mAdapter = new CityAdapter(this, adapterList);
        listView.setAdapter(mAdapter);

        final EditText filter = (EditText) view.findViewById(R.id.et_query);
        filter.setHint("请输入城市名称");
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                adapterList.clear();

                String curString = s.toString().trim();

                if (StringUtils.isEmpty(curString)) {
                    for (City city : cityList) {
                        adapterList.add(city);
                    }

                } else {
                    for (City city : cityList) {
                        if (city.getCityname().contains(curString)) {
                            adapterList.add(city);
                        }
                    }
                }

                mAdapter.notifyDataSetChanged();
            }
        });


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
    private class CityAdapter extends BaseAdapter {

        private Context mContext;

        private List<City> mCityList;

        public CityAdapter(Context context, List<City> cityList) {
            mContext = context;
            mCityList = cityList;
        }

        @Override
        public int getCount() {
            return mCityList.size();
        }

        @Override
        public Object getItem(int position) {
            return mCityList.get(position);
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

            ((TextView) convertView).setText(mCityList.get(position).getCityname());
            convertView.setTag(mCityList.get(position));

            return convertView;
        }
    }

    /**
     * 初始化内部服务器选择框
     *
     * @param view
     * @param dialog
     */
    private void initInnerServerView(View view, final Dialog dialog) {
        view.findViewById(R.id.et_query).setVisibility(View.GONE);
        List<String> innerServer = new ArrayList<String>() {
            {
                add(Constant.SHOW);
                add(Constant.MA);
                add(Constant.AZURE);
                add(Constant.CHINA);
                add(Constant.ZHANG);
                add(Constant.SHANGHAI);
                add(Constant.BEIJING);
            }
        };

        ListView listView = (ListView) view.findViewById(R.id.list_company);
        listView.setAdapter(new InnerAdapter(this, innerServer));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String region = (String) view.getTag();
                getConfig().setRegion(region);
                tvRegion.setText(region);
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.tv_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private class InnerAdapter extends BaseAdapter {

        private Context mContext;

        private List<String> mList;

        public InnerAdapter(Context context, List<String> list) {
            mContext = context;
            mList = list;
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

            ((TextView) convertView).setText(mList.get(position));
            convertView.setTag(mList.get(position));
            return convertView;
        }
    }

}