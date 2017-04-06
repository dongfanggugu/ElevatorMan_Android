package com.honyum.elevatorMan.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.net.InfoModifyRequest;
import com.honyum.elevatorMan.net.PwdModifyRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.EncryptUtils;
import com.honyum.elevatorMan.utils.Utils;

public class PersonModifyActivity extends BaseFragmentActivity {

    private String mTitle = "";

    private static final int mPwdLength = 6;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_modify_activiy);
        initView(getIntent());
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar(String title) {
        initTitleBar(title, R.id.title_modify,
                R.drawable.back_normal, backClickListener);
    }

    /**
     * 初始化视图
     *
     * @param intent
     */
    private void initView(Intent intent) {
        final String category = intent.getStringExtra("category");

        final EditText editText = (EditText) findViewById(R.id.et_content);

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_sex);

        if (category.equals("name")) {
            mTitle = "姓名";
            initTitleBar(mTitle);

            editText.setText(getConfig().getName());
        } else if (category.equals("sex")) {
            findViewById(R.id.ll_edit).setVisibility(View.GONE);
            findViewById(R.id.ll_radio).setVisibility(View.VISIBLE);

            mTitle = "性别";
            initTitleBar(mTitle);

            int sex = getConfig().getSex();

            if (0 == sex) {
                ((RadioButton) findViewById(R.id.rb_female)).setChecked(true);
            } else {
                ((RadioButton) findViewById(R.id.rb_male)).setChecked(true);
            }
        } else if (category.equals("age")) {
            mTitle = "年龄";
            initTitleBar(mTitle);

            editText.setText("" + getConfig().getAge());

            //年龄输入的限制
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
            editText.addTextChangedListener(new TextWatcher() {
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
                        editText.setText(str.replaceFirst("0", ""));
                    }
                }
            });

            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);

        } else if (category.equals("tel")) {
            mTitle = "手机号码";
            initTitleBar(mTitle);

            editText.setText(getConfig().getTel());

            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        } else if (category.equals("pwd")) {
            mTitle = "密码";
            initTitleBar(mTitle);

            findViewById(R.id.ll_edit).setVisibility(View.GONE);
            findViewById(R.id.ll_pwd).setVisibility(View.VISIBLE);

        } else if (category.equals("operation")) {
            mTitle = "操作证号";
            initTitleBar(mTitle);

            editText.setText(getConfig().getOperationCard());
        } else if (category.equals("home")) {
            mTitle = "家庭住址";
            initTitleBar(mTitle);

        } else if (category.equals("work")) {
            mTitle = "工作地址";
            initTitleBar(mTitle);
        }

        /**
         * 点击确定
         */
        findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //密码修改
                if (category.equals("pwd")) {
                    EditText etOld = (EditText) findViewById(R.id.et_old);
                    EditText etNew = (EditText) findViewById(R.id.et_new);
                    EditText etConfirm = (EditText) findViewById(R.id.et_new_confirm);

                    String oldStr = etOld.getText().toString();
                    String newStr = etNew.getText().toString();
                    String confirm = etConfirm.getText().toString();

                    if (StringUtils.isEmpty(oldStr) || StringUtils.isEmpty(newStr)
                            || StringUtils.isEmpty(confirm)) {
                        showToast("密码输入不能为空，请重新输入!");
                        return;
                    }

                    if (!Utils.isSame(newStr, confirm)) {
                        showToast(getString(R.string.register_pwd_diff));
                        return;
                    }

                    if (!isObeyTheLength(newStr)) {
                        showToast(getString(R.string.register_pwd_short));
                        return;
                    }

                    //使用MD5加密
                    requestPwdModify(EncryptUtils.encryptMD5(oldStr), EncryptUtils.encryptMD5(newStr));
                    return;
                }


                //当不是性别修改时，检测输入内容不能为空
                if (!category.equals("sex")) {
                    if (StringUtils.isEmpty(editText.getText().toString())) {
                        showToast(mTitle + "不能为空，请重新输入!");
                        return;
                    }
                }


                if (category.equals("tel")) {
                    if (!Utils.isMobileNumber(editText.getText().toString())) {
                        showToast("请输入合法的手机号码!");
                        return;
                    }
                }

                getNewValue(category, editText, radioGroup);

            }
        });
    }

    /**
     * 获取个人信息修改的bean
     *
     * @param name
     * @param age
     * @param sex
     * @param operationCard
     * @param tel
     * @return
     */
    private RequestBean getInfoModifyRequestBean(String userId, String token, String name, int age,
                                                 int sex, String operationCard, String tel) {
        InfoModifyRequest request = new InfoModifyRequest();
        InfoModifyRequest.InfoModifyReqBody body = request.new InfoModifyReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(userId);
        head.setAccessToken(token);

        body.setName(name);
        body.setAge(age);
        body.setSex(sex);
        body.setOperationCard(operationCard);
        body.setTel(tel);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    /**
     * 上报修改的信息
     *
     * @param name
     * @param age
     * @param sex
     * @param operationCard
     * @param tel
     */
    private void reportModifyInfo(final String name, final int age, final int sex,
                                  final String operationCard, final String tel) {
        String server = getConfig().getServer() + NetConstant.URL_MODIFY_INFO;
        RequestBean requestBean = getInfoModifyRequestBean(getConfig().getUserId(), getConfig().getToken(),
                name, age, sex, operationCard, tel);

        NetTask netTask = new NetTask(server, requestBean) {
            @Override
            protected void onResponse(NetTask task, String result) {

                //更新本地保存的个人信息
                getConfig().setName(name);
                getConfig().setAge(age);
                getConfig().setSex(sex);
                getConfig().setOperationCard(operationCard);
                getConfig().setTel(tel);

                showToast("信息修改成功!");
                finish();
            }
        };

        addTask(netTask);
    }

    /**
     * 获取新输入的值
     *
     * @param category
     * @param editText
     */
    private void getNewValue(String category, EditText editText, RadioGroup radioGroup) {

        String name = getConfig().getName();
        int age = getConfig().getAge();
        int sex = getConfig().getSex();
        String operationCard = getConfig().getOperationCard();
        String tel = getConfig().getTel();

        String value = editText.getText().toString();
        if (category.equals("name")) {
            name = value;

        } else if (category.equals("age")) {
            age = Utils.StringToInt(value);

        } else if (category.equals("sex")) {
            int id = radioGroup.getCheckedRadioButtonId();
            if (id == R.id.rb_female) {
                sex = 0;
            } else {
                sex = 1;
            }
        } else if (category.equals("operation")) {
            operationCard = value;

        } else if (category.equals("tel")) {
            tel = value;
        }

        reportModifyInfo(name, age, sex, operationCard, tel);
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
     * 获取密码修改请求bean
     *
     * @param userId
     * @param token
     * @param oldPwd
     * @param newPwd
     * @return
     */
    private RequestBean getPwdModifyRequestBean(String userId, String token, String oldPwd, String newPwd) {
        PwdModifyRequest request = new PwdModifyRequest();
        PwdModifyRequest.PwdModifyReqBody body = request.new PwdModifyReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(userId);
        head.setAccessToken(token);

        body.setOldPwd(oldPwd);
        body.setNewPwd(newPwd);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    /**
     * 修改密码
     *
     * @param oldPwd
     * @param newPwd
     */
    private void requestPwdModify(String oldPwd, String newPwd) {
        String server = getConfig().getServer() + NetConstant.URL_PWD_MODIFY;
        RequestBean requestBean = getPwdModifyRequestBean(getConfig().getUserId(), getConfig().getToken(),
                oldPwd, newPwd);

        NetTask netTask = new NetTask(server, requestBean) {
            @Override
            protected void onResponse(NetTask task, String result) {
                showToast("密码修改成功!");
                finish();
            }

            @Override
            protected void onFailed(NetTask task, String errorCode, String errorMsg) {
                //super.onFailed(task, errorCode, errorMsg);
                if (errorCode.equals("-1")) {
                    showToast("原密码输入错误,请重新输入!");
                }
            }
        };

        addTask(netTask);
    }

}
