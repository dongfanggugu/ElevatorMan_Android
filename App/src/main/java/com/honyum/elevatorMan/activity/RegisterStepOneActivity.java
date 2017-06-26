package com.honyum.elevatorMan.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.constant.Constant;
import com.honyum.elevatorMan.data.City;
import com.honyum.elevatorMan.data.CityInfo;
import com.honyum.elevatorMan.data.Province;
import com.honyum.elevatorMan.net.RegisterCompanyRequest;
import com.honyum.elevatorMan.net.RegisterPropertyRequest;
import com.honyum.elevatorMan.net.RegisterRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.EncryptUtils;
import com.honyum.elevatorMan.utils.Utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegisterStepOneActivity extends BaseFragmentActivity {

    private static final int mPwdLength = 6;

    private TextView tvCity;

    private EditText tv_phonenum;

    /**
     * begin:用来处理长按事件的标记参数
     **/

    private int mDownFlag = 0;

    private boolean mIsReleased = false;

    private boolean mIsMoved = false;

    private int mLastMotionX = 0;

    private int mLastMotionY = 0;

    private static final int TOUCH_SLOP = 20;
    private EditText et_company_name;

    /**
     * end:用来处理长按事件的标记参数
     **/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDialog();
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);

        dialog.setTitle("选择注册类型");
        dialog.setItems(new String[]{"企业管理人员","维修工注册", "物业注册", "取消"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        setContentView(R.layout.activity_register_company);
                        initView0();
                        break;
                    case 1:
                        setContentView(R.layout.activity_register);
                        initView();
                        break;
                    case 2:
                        setContentView(R.layout.activity_register_property);
                        initView1();
                        break;
                    case 3:
                        finish();
                        break;
                }

                dialog.dismiss();
            }
        });

        dialog.create().show();
    }

    private void initView0() {
        initTitleBar();



        final EditText etUserName = (EditText) findViewById(R.id.et_user_name);
        final EditText etPwd = (EditText) findViewById(R.id.et_pwd);
        final EditText etPwdConfirm = (EditText) findViewById(R.id.et_pwd_confirm);

        et_company_name = (EditText)findViewById(R.id.et_company_name);

        tv_phonenum = (EditText) findViewById(R.id.et_phonenum);
        findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerCompany(tv_phonenum.getText().toString(),
                        etUserName.getText().toString(),
                        et_company_name.getText().toString(),
                        etPwd.getText().toString(),etPwdConfirm.getText().toString());
            }
        });




    }

    private void registerCompany(final String tel, String userName, String company, String pwd, String rePwd) {
        if (TextUtils.isEmpty(tel) || TextUtils.isEmpty(userName)
                || TextUtils.isEmpty(tel) || TextUtils.isEmpty(company)||TextUtils.isEmpty(pwd)||TextUtils.isEmpty(rePwd)) {
            showToast("注册信息不能为空");
            return;
        }
        if (!Utils.isMobileNumber(tel)) {
            showToast("手机号码格式不正确");
            return;
        }
        //密码和确认密码需要一致
        if (!isSame(pwd, rePwd)) {
            showToast(getString(R.string.register_pwd_diff));
            return;
        }
        String server = getConfig().getServer() + NetConstant.REG_COMPANY;

        RegisterCompanyRequest request = new RegisterCompanyRequest();
        RequestHead head = new RequestHead();
        RegisterCompanyRequest.RegisterCompanyBody body = request.new RegisterCompanyBody();

        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());

        body.setName(userName);
        body.setCompanyName(company);
        body.setTel(tel);
        body.setPassword(EncryptUtils.encryptMD5(pwd));

        request.setHead(head);
        request.setBody(body);

        NetTask netTask = new NetTask(server, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                showSuccessDialog1(tel);
            }
        };

        addTask(netTask);
    }

    private void showSuccessDialog1(String tel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("注册成功");
        builder.setItems(new String[]{"公司账号", "用户名为：" + tel}, null);

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onBackPressed();
            }
        });

        builder.create().show();
    }

    /**
     * 注册物业
     */
    private void initView1() {

        initTitleBar();

        final EditText etName = (EditText) findViewById(R.id.et_property_name);
        final EditText etContact = (EditText) findViewById(R.id.et_property_contact);
        final EditText etTel = (EditText) findViewById(R.id.et_property_tel);
        final EditText etAddress = (EditText) findViewById(R.id.et_property_address);

        findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerProperty(etName.getText().toString(),
                        etContact.getText().toString(),
                        etTel.getText().toString(),
                        etAddress.getText().toString());
            }
        });
    }

    private void registerProperty(String name, String contact, final String tel, String address) {

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(contact)
                || TextUtils.isEmpty(tel) || TextUtils.isEmpty(address)) {
            showToast("注册信息不能为空");
            return;
        }

        if (!Utils.isMobileNumber(tel)) {
            showToast("手机号码格式不正确");
            return;
        }

        String server = getConfig().getServer() + NetConstant.REGISTER_PROPERTY;

        RegisterPropertyRequest request = new RegisterPropertyRequest();
        RequestHead head = new RequestHead();
        RegisterPropertyRequest.RegisterProReqBody body = request.new RegisterProReqBody();

        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());

        body.setName(name);
        body.setManager(contact);
        body.setTel(tel);
        body.setAddress(address);

        request.setHead(head);
        request.setBody(body);

        NetTask netTask = new NetTask(server, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                showSuccessDialog(tel);
            }
        };

        addTask(netTask);
    }

    private void showSuccessDialog(String tel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("注册成功");
        builder.setItems(new String[]{"物业账号", "用户名：" + tel, "默认密码：123456"}, null);

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onBackPressed();
            }
        });

        builder.create().show();
    }

    /**
     * 初始化标题
     */
    private void initTitleBar() {
        initTitleBar("注册", R.id.title_register, R.drawable.back_normal,
                backClickListener);
    }

    /**
     * 初始化视图
     */
    private void initView() {

        initTitleBar();

        final EditText etUserName = (EditText) findViewById(R.id.et_user_name);
        final EditText etPwd = (EditText) findViewById(R.id.et_pwd);
        final EditText etPwdConfirm = (EditText) findViewById(R.id.et_pwd_confirm);


        tvCity = (TextView) findViewById(R.id.tv_city);
        if (getConfig().getRegion().equals(Constant.BEIJING)) {
            findViewById(R.id.ll_city).setVisibility(View.GONE);
            findViewById(R.id.view_line1).setVisibility(View.GONE);
            tvCity.setText(Constant.BEIJING);
        }

        //城市选择
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCityInfoFromFile();
            }
        });

        findViewById(R.id.tv_inner).setOnTouchListener(new View.OnTouchListener() {
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

        findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //城市不能为空
                if (StringUtils.isEmpty(tvCity.getText().toString().trim())) {
                    showToast(getString(R.string.register_city_null));
                    return;
                }

                //用户名不能为空
                if (StringUtils.isEmpty(etUserName.getText().toString().trim())) {
                    showToast(getString(R.string.register_user_null));
                    return;
                }

                //密码不能为空
                if (StringUtils.isEmpty(etPwd.getText().toString().trim())) {
                    showToast(getString(R.string.register_pwd_null));
                    return;
                }

                //确认密码不能为空
                if (StringUtils.isEmpty(etPwdConfirm.getText().toString().trim())) {
                    showToast(getString(R.string.register_confirm_pwd_null));
                    return;
                }

                //密码和确认密码需要一致
                if (!isSame(etPwd.getText().toString().trim(), etPwdConfirm.getText().toString().trim())) {
                    showToast(getString(R.string.register_pwd_diff));
                    return;
                }

                //密码至少需要6位
                if (!isObeyTheLength(etPwd.getText().toString().trim())) {
                    showToast(getString(R.string.register_pwd_short));
                    return;
                }

                //提交到服务器
                String city = tvCity.getText().toString().trim();
                String userName = etUserName.getText().toString().trim();
                String password = EncryptUtils.encryptMD5(etPwd.getText().toString().trim());
                checkLoginName(city, userName, password);
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
            View view = View.inflate(RegisterStepOneActivity.this, R.layout.layout_company_list, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterStepOneActivity.this)
                    .setView(view);
            builder.setCancelable(false);
            Dialog dialog = builder.create();
            initInnerServerView(view, dialog);
            dialog.show();
        }
    };

    /**
     * 判断两个字符串是否相同
     *
     * @param str1
     * @param str2
     * @return
     */
    private boolean isSame(String str1, String str2) {
        if (0 == str1.compareTo(str2)) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串长度是否符合要求
     *
     * @param str
     * @return
     */
    private boolean isObeyTheLength(String str) {
        if (str.length() >= mPwdLength) {
            return true;
        }
        return false;
    }

    /**
     * 获取注册请求的bean
     *
     * @param loginName
     * @param password
     * @param name
     * @param sex
     * @param age
     * @param branchName
     * @param operationCode
     * @param tel
     * @param operationSate
     * @return
     */
    public static RequestBean getRegisterRequest(String loginName, String password, String name, int sex,
                                                 int age, String branchName, String operationCode,
                                                 String tel, String operationSate) {

        RegisterRequest request = new RegisterRequest();
        RegisterRequest.RegisterReqBody body = request.new RegisterReqBody();
        RequestHead head = new RequestHead();

        body.setLoginname(loginName);
        body.setPassword(password);
        body.setName(name);
        body.setSex(sex);
        body.setAge(age);
        body.setBranchName(branchName);
        body.setOperationCard(operationCode);
        body.setTel(tel);
        body.setOperateState(operationSate);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    /**
     * 维修工注册
     *
     * @param loginName
     * @param password
     */
    private void checkLoginName(final String city, final String loginName, final String password) {

        RequestBean requestBean = getRegisterRequest(loginName, "", "", 0, 0, "",
                "", "", "0");
        final String server = getConfig().getServer() + NetConstant.URL_WORKER_REGISTER;

        NetTask netTask = new NetTask(server, requestBean) {
            @Override
            protected void onResponse(NetTask task, String result) {
                Intent intent = new Intent(RegisterStepOneActivity.this, RegisterStepTwoActivity.class);
                intent.putExtra("login_name", loginName);
                intent.putExtra("pwd", password);
                intent.putExtra("city", city);
                startActivity(intent);
            }

            @Override
            protected void onFailed(NetTask task, String errorCode, String errorMsg) {

                //super.onFailed(task, errorCode, errorMsg);
                if (errorCode.equals("-1")) {
                    showToast(getString(R.string.login_name_exist));
                }
            }
        };

        addTask(netTask);
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


            View view = View.inflate(RegisterStepOneActivity.this, R.layout.layout_company_list, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterStepOneActivity.this)
                    .setView(view);
            builder.setCancelable(false);
            Dialog dialog = builder.create();
            List<Province> provinceList = cityInfo.getProvinceList();
            Collections.sort(provinceList);
            initProvinceInfo(view, dialog, provinceList);
            dialog.show();

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

                View cityView = View.inflate(RegisterStepOneActivity.this,
                        R.layout.layout_company_list, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterStepOneActivity.this)
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


        //监听维保公司选择事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String region = ((TextView) view).getText().toString();
                tvCity.setText(region);
                getConfig().setRegion(region);
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
            }
        };

        ListView listView = (ListView) view.findViewById(R.id.list_company);
        listView.setAdapter(new InnerAdapter(this, innerServer));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String region = (String) view.getTag();
                getConfig().setRegion(region);
                tvCity.setText(region);
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
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
