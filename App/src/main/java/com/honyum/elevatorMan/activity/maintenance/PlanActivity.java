package com.honyum.elevatorMan.activity.maintenance;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.worker.WorkerBaseActivity;
import com.honyum.elevatorMan.data.LiftInfo;
import com.honyum.elevatorMan.data.MainHelpData;
import com.honyum.elevatorMan.net.ReportPlanRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PlanActivity extends WorkerBaseActivity {
    private DatePicker datePicker;
    private TimePicker timePicker;
    Date date;
    String s1;
    String s2;
    private boolean isTimePass;
    private View dialogLayout;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        initTitleBar(getIntent());
        initView(getIntent());
    }


    /**
     * 初始化标题栏
     */
    private void initTitleBar(Intent intent) {
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        setExitFlag(false);

        String type = intent.getStringExtra("enter_type");
        String title = "";
        if (type.equals("add")) {
            title = getString(R.string.make_plan);
        } else if (type.equals("modify")) {
            title = getString(R.string.modify_plan);
        }
        initTitleBar(title, R.id.title_plan, R.drawable.back_normal,
                backClickListener);
    }

    /**
     * 初始化界面
     *
     * @param intent
     */
    private void initView(final Intent intent) {
        if (null == intent) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date(System.currentTimeMillis());
        String s = sdf.format(d).toString();
        final LiftInfo liftInfo = (LiftInfo) intent.getSerializableExtra("lift");
        ((TextView) findViewById(R.id.tv_lift_code)).setText(liftInfo.getNum());
        ((TextView) findViewById(R.id.tv_lift_add)).setText(liftInfo.getAddress());
        dialogLayout = LayoutInflater.from(this).inflate(R.layout.dia_datetime_layout, null);
        datePicker = (DatePicker) dialogLayout.findViewById(R.id.datePicker);
        timePicker = (TimePicker) dialogLayout.findViewById(R.id.timePicker);

        //维保日期处理
        final TextView tvPlanDate = (TextView) findViewById(R.id.tv_plan_date);
        final TextView tvPlanType = (TextView) findViewById(R.id.tv_plan_type);

        if (liftInfo.hasPlan()) {
            tvPlanDate.setText(liftInfo.getPlanMainTime());
            tvPlanType.setText(liftInfo.getPlanType());

        } else {
            tvPlanDate.setText(s);
            tvPlanType.setText("半月保");
        }
        date = new Date();

        //使用dialog组合日期和时间控件。
        findViewById(R.id.ll_date).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                timePicker.setIs24HourView(true);
                timePicker.setCurrentHour(date.getHours() + 1);
                timePicker.setCurrentMinute(0);
                int minute = timePicker.getCurrentMinute();
                s2 = "  " + (timePicker.getCurrentHour()) + ":" + (minute < 10 ? "0" + minute : minute);
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        s2 = ("  " + hourOfDay + ":" + (minute < 10 ? "0" + minute : minute));
                    }
                });
                alertDialog.show();
            }
        });
        alertDialog = new AlertDialog.Builder(this).setTitle("选择时间").setView(dialogLayout).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        s1 = (datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
                        String dateString = s1 + s2;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Date d = new Date();
                        try {
                            isTimePass = false;
                            d = sdf.parse(dateString);
                            long t = d.getTime();
                            long cl = System.currentTimeMillis();

                            if (cl > t) {
                                isTimePass = false;
                            } else {
                                isTimePass = true;
                            }
                            tvPlanDate.setText(dateString);
                            dialog.dismiss();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        }).create();
        //end 组合控件

        //日期设置后回调接口
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date date = new Date(year - 1900, monthOfYear, dayOfMonth);
                tvPlanDate.setText(Utils.dateToString(date));
            }
        };

//        //点击日期时修改
//        findViewById(R.id.ll_date).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(Utils.stringToDate(tvPlanDate.getText().toString()));
//                DatePickerDialog datePickerDialog = new DatePickerDialog(PlanActivity.this, dateSetListener,
//                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
//                        calendar.get(Calendar.DATE)) {
//                    @Override
//                    protected void onStop() {
//                    }
//                };
//                DatePicker datePicker = datePickerDialog.getDatePicker();
//                datePicker.setSpinnersShown(false);
//
//                datePicker.setCalendarViewShown(true);
//                datePickerDialog.show();
//            }
//        });


        //维保计划类型选择
        findViewById(R.id.tv_plan_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popTypeSelector(tvPlanType);
            }
        });

        //维保类型维保内容帮助
        findViewById(R.id.tv_type_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = LiftInfo.stringToType(tvPlanType.getText().toString());
                Intent intent = new Intent(PlanActivity.this, MainHelpActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });


        //维保计划提交
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = tvPlanDate.getText().toString();
                String type = LiftInfo.stringToType(tvPlanType.getText().toString());

                reportPlan(getConfig().getUserId(), getConfig().getToken(), liftInfo.getId(), date,
                        type, intent.getStringExtra("enter_type"));
            }
        });
    }


    /**
     * 维保类型选择弹出框选择接口
     */
    class TypeSelector implements View.OnClickListener {

        private AlertDialog mDialog;
        private TextView mTextView;

        public TypeSelector(AlertDialog dialog, TextView textView) {
            mDialog = dialog;
            mTextView = textView;
        }

        @Override
        public void onClick(View v) {
            mDialog.dismiss();
            switch (v.getId()) {
                case R.id.ll_semi_month:
                    mTextView.setText("半月保");
                    break;
                case R.id.ll_month:
                    mTextView.setText("月保");
                    break;
                case R.id.ll_season:
                    mTextView.setText("季度保");
                    break;
                case R.id.ll_semi_year:
                    mTextView.setText("半年保");
                    break;
                case R.id.ll_year:
                    mTextView.setText("年保");
                    break;
            }
        }
    }

    /**
     * 维保类型选择
     *
     * @param textView
     */
    private void popTypeSelector(TextView textView) {
        View view = View.inflate(this, R.layout.layout_plan_type, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;


        View.OnClickListener listener = new TypeSelector(dialog, textView);

        view.findViewById(R.id.ll_semi_month).setOnClickListener(listener);
        view.findViewById(R.id.ll_month).setOnClickListener(listener);
        view.findViewById(R.id.ll_season).setOnClickListener(listener);
        view.findViewById(R.id.ll_semi_year).setOnClickListener(listener);
        view.findViewById(R.id.ll_year).setOnClickListener(listener);


        List<TextView> textViews = new ArrayList<>(5);
        textViews.add((TextView) view.findViewById(R.id.tv_half_month));
        textViews.add((TextView) view.findViewById(R.id.tv_star_month));
        textViews.add((TextView) view.findViewById(R.id.tv_star_quarter));
        textViews.add((TextView) view.findViewById(R.id.tv_star_half_year));
        textViews.add((TextView) view.findViewById(R.id.tv_star_year));


        List<ImageView> imageViews = new ArrayList<>(5);
        imageViews.add((ImageView) view.findViewById(R.id.iv_half_month));
        imageViews.add((ImageView) view.findViewById(R.id.iv_star_month));
        imageViews.add((ImageView) view.findViewById(R.id.iv_star_quarter));
        imageViews.add((ImageView) view.findViewById(R.id.iv_star_half_year));
        imageViews.add((ImageView) view.findViewById(R.id.iv_star_year));


        String selectedString = textView.getText() + "";

        int selectedIndex = 0;
        if (!TextUtils.isEmpty(selectedString)) {
            for (int i = 0; i < textViews.size(); i++) {
                if (selectedString.equals(textViews.get(i).getText() + "")) {
                    selectedIndex = i;
                    break;
                }
            }
            imageViews.get(selectedIndex).setBackgroundResource(R.drawable.star_selected);
            textViews.get(selectedIndex).setTextColor(getResources().getColor(R.color.title_bg_color));

        }
        dialog.show();
    }

    /**
     * 获取提交维保计划的bean
     *
     * @param userId
     * @param token
     * @param id
     * @param planDate
     * @param planType
     * @return
     */
    public static RequestBean getReportPlanRequest(String userId, String token, String id, String planDate,
                                                   String planType) {
        ReportPlanRequest request = new ReportPlanRequest();
        RequestHead head = new RequestHead();
        ReportPlanRequest.ReportPlanReqBody body = request.new ReportPlanReqBody();

        head.setUserId(userId);
        head.setAccessToken(token);

        body.setId(id);
        body.setPlanTime(planDate);
        body.setMainType(planType);

        request.setHead(head);
        request.setBody(body);

        return request;
    }

    /**
     * 上传维保计划
     *
     * @param userId
     * @param token
     * @param id
     * @param planDate
     * @param planType
     */
    private void reportPlan(String userId, String token, String id, String planDate,
                            String planType, String type) {

        String server = "";
        if (type.equals("add")) {

            server = getConfig().getServer() + NetConstant.URL_REPORT_PLAN;
        } else if (type.equals("modify")) {

            server = getConfig().getServer() + NetConstant.URL_MODIFY_PLAN;
        }
        NetTask netTask = new NetTask(server, getReportPlanRequest(userId, token, id, planDate,
                planType)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                showToast("维保计划提交成功，请及时到记录上传里面完成您的维保计划");
                finish();
                //Intent liftIntent = new Intent(PlanActivity.this, MyLiftActivity.class);
                //liftIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(liftIntent);
            }
        };
        addTask(netTask);
    }
}
