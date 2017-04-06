package com.honyum.elevatorMan.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.property.PropertyMaintenanceActivity;

public class BaseFragment extends Fragment implements PropertyMaintenanceActivity.IFilterLiftInfo {


    protected boolean mIsVisible = false;

    protected boolean mIsInitial = false;

    public int mFrom = 0;


    public Config getConfig() {
        return ((BaseFragmentActivity) getActivity()).getConfig();
    }

    public void initTitleBar(View titleView, String title,
                             int leftImg, View.OnClickListener onClickLeftListener,
                             int rightImg, View.OnClickListener onClickRightListener) {

        if (titleView == null) {
            return;
        }

        ((TextView) titleView.findViewById(R.id.tv_title)).setText(title);

        if (leftImg != 0) {
            ImageView leftButton = (ImageView) titleView
                    .findViewById(R.id.btn_back);
            leftButton.setImageResource(leftImg);
            leftButton.setVisibility(View.VISIBLE);
            leftButton.setOnClickListener(onClickLeftListener);
        }

        if (rightImg != 0) {
            ImageView rightButton = (ImageView) titleView
                    .findViewById(R.id.btn_query);
            rightButton.setImageResource(rightImg);
            rightButton.setVisibility(View.VISIBLE);
            rightButton.setOnClickListener(onClickRightListener);
        }
    }


//    /**
//     * 退出登录请求bean
//     * @return
//     */
//    private RequestBean getLogoutRequest() {
//        VersionCheckRequest request = new VersionCheckRequest();
//        RequestHead head = new RequestHead();
//
//        BaseFragmentActivity activity = (BaseFragmentActivity) getActivity();
//        head.setAccessToken(activity.getConfig().getToken());
//        head.setUserId(activity.getConfig().getUserId());
//
//        request.setHead(head);
//
//        return request;
//    }

    /**
     * 退出登录
     */
    protected void logout() {
        final Context context = getActivity();
        if (!(context instanceof BaseFragmentActivity)) {
            return;
        }
        final BaseFragmentActivity activity = (BaseFragmentActivity) getActivity();

        activity.logout();

//        NetTask netTask = new NetTask(activity.getConfig().getServer() + NetConstant.URL_LOG_OUT,
//                getLogoutRequest()) {
//            @Override
//            protected void onResponse(NetTask task, String result) {
//                activity.clearUserInfo();
//
//                //退出登录时，取消之前设置的闹钟
//                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                for (PendingIntent pendingIntent : RemindUtils.mRemindList) {
//                    manager.cancel(pendingIntent);
//                }
//                //关闭定位服务
//                Intent sIntent = new Intent(getActivity(), LocationService.class);
//                getActivity().stopService(sIntent);
//
//                //启动
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                SysActivityManager.getInstance().exit();
//            }
//
//            @Override
//            protected void onFailed(NetTask task, String errorCode, String errorMsg) {
//                //super.onFailed(task, errorCode, errorMsg);
//                activity.clearUserInfo();
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                SysActivityManager.getInstance().exit();
//            }
//        };
//        activity.addTask(netTask);

    }

    @Override
    public void onFilter(String project, String building, String unit, String liftNum) {

    }
}