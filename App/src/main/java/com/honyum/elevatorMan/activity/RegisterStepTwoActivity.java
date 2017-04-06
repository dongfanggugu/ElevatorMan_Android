package com.honyum.elevatorMan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.PersonIconPopActivity;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.City;
import com.honyum.elevatorMan.data.CityInfo;
import com.honyum.elevatorMan.data.CompanyInfo;
import com.honyum.elevatorMan.data.MainHelpData;
import com.honyum.elevatorMan.data.Province;
import com.honyum.elevatorMan.net.CompanyResponse;
import com.honyum.elevatorMan.net.RegisterRequest;
import com.honyum.elevatorMan.net.VersionCheckRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.Utils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterStepTwoActivity extends BaseFragmentActivity {

    private ImageView imageView1;

    private ImageView imageView2;

    private ImageView imageView3;

    private final String ID = "id";

    private final String OPERATION = "operation";

    private final String MIX = "mix";

    private TextView tvCompany;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_two);
        initTitleBar();
        initView();
    }

    /**
     * 初始化标题
     */
    private void initTitleBar() {
        initTitleBar(getString(R.string.register_title), R.id.title_register, R.drawable.back_normal,
                backClickListener);
    }

    /**
     * 初始化视图
     */
    private void initView() {

        final EditText etName = (EditText) findViewById(R.id.et_name);


        final EditText etAge = (EditText) findViewById(R.id.et_age);

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_sex);
        ((RadioButton) findViewById(R.id.rb_female)).setChecked(true);



        tvCompany = (TextView) findViewById(R.id.tv_company);

        final EditText etCardId = (EditText) findViewById(R.id.et_card_id);

        final EditText etOperation = (EditText) findViewById(R.id.et_operation_code);

        final EditText etPhone = (EditText) findViewById(R.id.et_phone_num);

        imageView1 = (ImageView) findViewById(R.id.iv_image1);
        imageView1.setTag(ID);

        imageView2 = (ImageView) findViewById(R.id.iv_image2);
        imageView2.setTag(OPERATION);

        imageView3 = (ImageView) findViewById(R.id.iv_image3);
        imageView3.setTag(MIX);

        imageView1.setOnClickListener(imageViewClickListener);

        imageView2.setOnClickListener(imageViewClickListener);

        imageView3.setOnClickListener(imageViewClickListener);


        //公司选择
        tvCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCompanyInfo();
            }
        });


        //年龄输入的限制
        etAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.length() > 0 && str.startsWith("0")) {
                    etAge.setText(str.replaceFirst("0", ""));
                }
            }
        });

        //提交
        findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //姓名不能为空
                if (StringUtils.isEmpty(etName.getText().toString())) {
                    showToast(getString(R.string.register_name_null));
                    return;
                }

                //年龄不能为空
                if (StringUtils.isEmpty(etAge.getText().toString())) {
                    showToast(getString(R.string.register_age_null));
                    return;
                }

                //公司不能为空
                if (StringUtils.isEmpty(tvCompany.getText().toString())) {
                    showToast(getString(R.string.company_null));
                    return;
                }

                //身份证号不能为空
                if (StringUtils.isEmpty(etCardId.getText().toString())) {
                    showToast(getString(R.string.card_id_null));
                    return;
                }

                //验证身份证号码的合法性
                if (!Utils.isLegalCardId(etCardId.getText().toString())) {
                    showToast(getString(R.string.card_id_incorrect));
                    return;
                }

                //操作码不能为空
                if (StringUtils.isEmpty(etOperation.getText().toString())) {
                    showToast(getString(R.string.register_operation_null));
                    return;
                }

                //电话号码不能为空
                if (StringUtils.isEmpty(etPhone.getText().toString())) {
                    showToast(getString(R.string.register_phone_null));
                    return;
                }

                //电话号码需要符合正确规则
                if (!Utils.isMobileNumber(etPhone.getText().toString())) {
                    showToast(getString(R.string.register_phone_incorrect));
                    return;
                }

                //检测拍照
                String image1 = (String) imageView1.getTag(R.id.file_path);
                String image2 = (String) imageView2.getTag(R.id.file_path);
                String image3 = (String) imageView3.getTag(R.id.file_path);

                if (StringUtils.isEmpty(image1) || StringUtils.isEmpty(image2) || StringUtils.isEmpty(image3)) {
                    showToast("请拍摄完整的照片!");
                    return;
                }

                Intent intent = getIntent();
                String loginName = intent.getStringExtra("login_name");
                String password = intent.getStringExtra("pwd");
                String city = intent.getStringExtra("city");

                String name = etName.getText().toString();
                int age = Utils.StringToInt(etAge.getText().toString());

                int checkedId = radioGroup.getCheckedRadioButtonId();
                int sex = 0;
                if (checkedId == R.id.rb_male) {
                    sex = 1;
                }

                String companyName = tvCompany.getText().toString();

                String companyId = (String) tvCompany.getTag();

                String cardId = etCardId.getText().toString();

                String operationCode = etOperation.getText().toString();

                String tel = etPhone.getText().toString();

                List<String> pics = new ArrayList<String>();
                pics.add(Utils.imgToStrByBase64(image1));
                pics.add(Utils.imgToStrByBase64(image2));
                pics.add(Utils.imgToStrByBase64(image3));

                //提交到服务器
                register(loginName, password, name, sex, age, companyName, companyId, operationCode, tel, pics,
                        city, cardId);
            }
        });
    }

    /**
     * 判断两个字符串是否相同
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
     * 用户名唯一性验证
     */
    private View.OnFocusChangeListener userNameFocusChangedListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (v instanceof EditText) {
                    EditText editText = (EditText) v;
                    if(StringUtils.isEmpty(editText.getText().toString())) {
                        v.setTag(true);
                        return;
                    }
                    v.setTag(false);
                }
            }
        }
    };

    /**
     * 获取注册请求的bean
     * @param loginName
     * @param password
     * @param name
     * @param sex
     * @param age
     * @param branchName
     * @param operationCode
     * @param tel
     * @return
     */
    private RequestBean getRegisterRequest(String loginName, String password, String name, int sex,
                                           int age, String branchName, String branchId, String operationCode,
                                           String tel, String operationState, List<String> pics,
                                           String city, String cardId) {

        RegisterRequest request = new RegisterRequest();
        RegisterRequest.RegisterReqBody body = request.new RegisterReqBody();
        RequestHead head = new RequestHead();

        body.setLoginname(loginName);
        body.setPassword(password);
        body.setName(name);
        body.setSex(sex);
        body.setAge(age);
        body.setBranchName(branchName);
        body.setBranchId(branchId);
        body.setOperationCard(operationCode);
        body.setTel(tel);
        body.setOperateState(operationState);
        body.setPics(pics);
        body.setCity(city);
        body.setIdNumber(cardId);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    /**
     * 维修工注册
     * @param loginName
     * @param password
     * @param name
     * @param sex
     * @param age
     * @param branchName
     * @param operationCode
     * @param tel
     */
    private void register(String loginName, String password, String name, int sex,
                          int age, String branchName, String branchId, String operationCode, String tel, List<String> pics,
                          String city, String cardId) {
        RequestBean requestBean = getRegisterRequest(loginName, password, name, sex, age, branchName, branchId,
                operationCode, tel, "1", pics, city, cardId);
        final String server = getConfig().getServer() + NetConstant.URL_WORKER_REGISTER;

        NetTask netTask = new NetTask(server, requestBean) {
            @Override
            protected void onResponse(NetTask task, String result) {
                showToast(getString(R.string.register_success));
                Intent intent = new Intent(RegisterStepTwoActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        };
        addTask(netTask);
    }

    /**
     * 启动照片选择页面
     */
    private void popSelectWindow(String tag) {
        Intent intent = new Intent(this, PicturePickActivity.class);
        intent.putExtra("tag", tag);
        startActivityForResult(intent, 1);
    }

    /**
     * 点击拍照时
     */
    private View.OnClickListener imageViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popSelectWindow((String) v.getTag());
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }

        String tag = data.getStringExtra("tag");
        String photo = data.getStringExtra("result");

        if (tag.equals(ID)) {
            if (null == imageView1) {
                return;
            }
            setImageView(imageView1, photo, (Button) findViewById(R.id.btn_del_1));

        } else if (tag.equals(OPERATION)) {
            if (null == imageView2) {
                return;
            }
            setImageView(imageView2, photo, (Button) findViewById(R.id.btn_del_2));

        } else if (tag.equals(MIX)) {
            if (null == imageView3) {
                return;
            }
            setImageView(imageView3, photo, (Button) findViewById(R.id.btn_del_3));
        }

    }

    /**
     * 设置照片
     * @param imageView
     * @param photo
     * @param delButton
     */
    private void setImageView(final ImageView imageView, String photo, final Button delButton) {
        if (null == imageView) {
            return;
        }

        if (StringUtils.isEmpty(photo)) {
            return;
        }

        Bitmap bitmap = Utils.getBitmapBySize(photo, 90, 120);
        if (null == bitmap) {
            return;
        }
        imageView.setImageBitmap(bitmap);
        imageView.setTag(R.id.file_path, photo);
        imageView.setOnClickListener(overViewClickListener);

        delButton.setVisibility(View.VISIBLE);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delButton.setVisibility(View.GONE);

                if (imageView == imageView1) {
                    imageView.setImageResource(R.drawable.card_id);
                } else if (imageView == imageView2) {
                    imageView.setImageResource(R.drawable.operation);
                } else if (imageView == imageView3) {
                    imageView.setImageResource(R.drawable.mix);
                }
                //imageView.setImageResource(R.drawable.icon_img_original);

                imageView.setTag(R.id.file_path, "");
                imageView.setOnClickListener(imageViewClickListener);
            }
        });
    }

    /**
     * 拍照之后点击照片查看照片预览
     */
    private View.OnClickListener overViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof ImageView) {
                String filePath = (String) v.getTag(R.id.file_path);

                //如果图片为空，返回
                if (StringUtils.isEmpty(filePath)) {
                    return;
                }
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                ((ImageView) findViewById(R.id.iv_overview)).setImageBitmap(bitmap);

                final LinearLayout llFullScreen = (LinearLayout) findViewById(R.id.ll_full_screen);
                llFullScreen.setVisibility(View.VISIBLE);
                llFullScreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llFullScreen.setVisibility(View.GONE);
                    }
                });

            }
        }
    };


    /**
     * 处理维保公司选择的dialog
     * @param view
     * @param dialog
     * @param companyList
     */
    private void initCompanyView(View view, final Dialog dialog, final List<CompanyInfo> companyList) {

        final List<CompanyInfo> adapterList = new ArrayList<CompanyInfo>();


        final ListView listView = (ListView) view.findViewById(R.id.list_company);


        //监听维保公司选择事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((TextView) view).getText().toString();
                if (name.equals("其他")) {
                    dialog.dismiss();

                    View customView = View.inflate(RegisterStepTwoActivity.this,
                            R.layout.layout_company_custom, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterStepTwoActivity.this)
                            .setView(customView);
                    Dialog customDialog = builder.create();
                    initCustomCompanyDialog(customView, customDialog, companyList);

                    customDialog.show();
                } else {
                    tvCompany.setText(((TextView) view).getText().toString());
                    tvCompany.setTag(view.getTag());
                    dialog.dismiss();
                }

            }
        });


        for (CompanyInfo company : companyList) {
            adapterList.add(company);
        }

        adapterList.add(CompanyInfo.createOtherCompany());

        final CompanyAdapter mAdapter = new CompanyAdapter(this, adapterList);
        listView.setAdapter(mAdapter);

        final EditText filter = (EditText) view.findViewById(R.id.et_query);
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
                    for (CompanyInfo company : companyList) {
                        adapterList.add(company);
                    }
                    adapterList.add(CompanyInfo.createOtherCompany());

                } else {
                    for (CompanyInfo company : companyList) {
                        if (company.getName().contains(curString)) {
                            adapterList.add(company);
                        }
                    }
                    adapterList.add(CompanyInfo.createOtherCompany());
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
     * 公司选择适配器
     */
    private class CompanyAdapter extends BaseAdapter {

        private Context mContext;

        private List<CompanyInfo> mCompanyList;

        public CompanyAdapter(Context context, List<CompanyInfo> companyList) {
            mContext = context;
            mCompanyList = companyList;
        }


        @Override
        public int getCount() {
            return mCompanyList.size();
        }

        @Override
        public Object getItem(int position) {
            return mCompanyList.get(position);
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

            ((TextView) convertView).setText(mCompanyList.get(position).getName());
            convertView.setTag(mCompanyList.get(position).getId());

            return convertView;
        }
    }

    /**
     *  获取请求维保公司列表的bean
     * @return
     */
    private RequestBean getCompanyInfoBean() {

        //使用检测更新的请求bean请求维保公司列表
        VersionCheckRequest request = new VersionCheckRequest();
        RequestHead head = new RequestHead();

        request.setHead(head);

        return request;
    }

    /**
     * 获取维保公司列表
     */
    private void getCompanyInfo() {
        String sever = getConfig().getServer() + NetConstant.URL_REQUEST_COMPANY;
        RequestBean requestBean = getCompanyInfoBean();

        NetTask netTask = new NetTask(sever, requestBean) {
            @Override
            protected void onResponse(NetTask task, String result) {

                View view = View.inflate(RegisterStepTwoActivity.this, R.layout.layout_company_list, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterStepTwoActivity.this)
                        .setView(view);
                builder.setCancelable(false);
                Dialog dialog = builder.create();
                initCompanyView(view, dialog, CompanyResponse.getCompanyResponse(result).getBody());
                dialog.show();
            }
        };

        addTask(netTask);

    }


    /**
     * 处理自定义维保公司名称时的dialog
     * @param view
     * @param dialog
     * @param companyInfoList
     */
    private void initCustomCompanyDialog(View view, final Dialog dialog, final List<CompanyInfo> companyInfoList) {
        final EditText etCompany = (EditText) view.findViewById(R.id.et_company);



        //关闭dialog
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //确定按钮监听
        view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyName = etCompany.getText().toString().trim();
                if (StringUtils.isEmpty(companyName)) {
                    showToast("维保公司名称不能为空!");
                    return;
                }

                for (CompanyInfo companyInfo : companyInfoList) {
                    if (companyName.equals(companyInfo.getName())) {
                        showToast("维保公司名称已经存在，请从列表中选择!");
                        return;
                    }
                }

                tvCompany.setText(companyName);
                tvCompany.setTag("");
                dialog.dismiss();
            }
        });
    }
}
