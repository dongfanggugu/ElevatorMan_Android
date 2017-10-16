package com.honyum.elevatorMan.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.ChatActivity;
import com.honyum.elevatorMan.activity.maintenance.MyLiftActivity;
import com.honyum.elevatorMan.activity.property.AlarmTraceActivity;
import com.honyum.elevatorMan.activity.worker.WorkerActivity;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.MyApplication;
import com.honyum.elevatorMan.constant.Constant;
import com.honyum.elevatorMan.data.AlarmNotify;
import com.honyum.elevatorMan.net.NotifyFeedbackRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTaskNew;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.Utils;

import cn.jpush.android.api.JPushInterface;

public class JPushMsgReceiver extends BroadcastReceiver {

    public static final String TAG = "JPUSH";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            processNotify(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

        }
    }

    /**
     * 处理推送消息
     *
     * @param context
     * @param bundle
     */
    private void processNotify(Context context, Bundle bundle) {

        //if(bundle!=null&&bundle.getS)
        setNotification(context, bundle);
    }

    /**
     * 通知消息
     *
     * @param context
     * @param bundle
     */
    private void setNotification(Context context, Bundle bundle) {

        //通知携带的额外信息
        String extraMsg = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String msgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        Log.i("push", "extra msg:" + extraMsg);
        Log.i("push", "msg:" + msg);
        Log.i("push", "msgId:" + msgId);


        AlarmNotify alarmNotify = AlarmNotify.getAlarmNotify(extraMsg);

        //获取通知的类型
        String notifyType = alarmNotify.getNotifyType();

        Log.d("push", "notifyType ==>> " + notifyType);

        //过滤接收到的消息是否为有效消息
        if (StringUtils.isEmpty(notifyType)) {
            return;
        }

        //接收到通知后，反馈给服务器接收成功标记
        SharedPreferences preferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String region = preferences.getString("region", Constant.SHANGHAI);

        String server = Constant.SERVER_LIST.get(region);

        if (StringUtils.isEmpty(server)) {
            server = Constant.SERVER_LIST.get(Constant.CHINA);
        }
        NetTaskNew task = new NetTaskNew(context, server + NetConstant.URL_NOTIFY_FEEDBACK,
                getRequestBean(context, alarmNotify.getAlarmId(), notifyType)) {

            @Override
            protected void onReturn(NetTaskNew task, String result) {
                super.onReturn(task, result);
                Log.i("notify", "feedback successfully!");
            }
        };
        task.start();
//        if(bundle!=null&& !TextUtils.isEmpty(msg))
//        {
//            String []  s = msg.split("说:");
//            if(s[0].equals(((MyApplication)context.getApplicationContext()).getConfig().getName()))
//                return;
//        }

        //维修工维保信息被拒绝
        if (notifyType.equals(Constant.ACTION_MAIN_REJECT)) {
            sendMainPlanRejectNotify(context);
        }

        //使用角色控制维修工和物业的登录冲突
        String role = getRole(context);

        //ChatActivity.isForeground()    &&ChatActivity.isForeground()
        if (role.equals(Constant.WORKER)) {
            notifyWorker(context, alarmNotify, WorkerActivity.class, msg, msgId);
        } else if (role.equals(Constant.PROPERTY)) {
            notifyProperty(context, alarmNotify, AlarmTraceActivity.class, msg, msgId);
        }

        Log.e("push", "BaseFragmentActivity当前是否显示在聊天界面==>>>" + BaseFragmentActivity.isForeground()+ ChatActivity.isForeground()+chatMsgListener == null?"false":"true");
        Log.e("push", "ChatActivity当前是否显示在聊天界面==>>>" + ChatActivity.isForeground());
        if (BaseFragmentActivity.isForeground() && "CHAT".equals(notifyType)
                && chatMsgListener != null && ChatActivity.isForeground()) {
            chatMsgListener.chatMsgListener(alarmNotify);
        } else if ("CHAT".equals(notifyType)) {
            jumpToPage(context, alarmNotify, ChatActivity.class, msg, msgId);
        }
    }

    public interface onChatMsgListener {
        void chatMsgListener(AlarmNotify alarmNotify);
    }

    private static onChatMsgListener chatMsgListener;

    public static void setChatMsgListener(onChatMsgListener chatMsgListener) {
        JPushMsgReceiver.chatMsgListener = chatMsgListener;
    }

    /**
     * 初始化通知
     *
     * @param context
     * @param intent
     * @param notifyId
     * @param msg
     * @return
     */
    private void sendNotify(Context context, Intent intent, String notifyId,
                            String msg) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.logo);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));

        if (intent.getAction().equals(Constant.ACTION_ALARM_RECEIVED)
                || intent.getAction().equals(Constant.ACTION_ALARM_PROPERTY)) {

            builder.setSound(Uri.parse("android.resource://" + context.getPackageName()
                    + "/" + R.raw.alarm));
        } else {

            builder.setSound(Uri.parse("android.resource://" + context.getPackageName()
                    + "/" + R.raw.message));
        }

        String[] split = msg.split("说:");

        builder.setTicker(msg);
        builder.setContentTitle(split[0]);
        builder.setContentText(split[1]);

        int code = (int) (Long.parseLong(notifyId) % Integer.MAX_VALUE);
        //使用code参数标记每次传递的intent 参数都不同
        PendingIntent pendingIntent = PendingIntent.getActivity(context, code,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        builder.setAutoCancel(true);

        final NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        //同一个报警事件的推送事件使用相同的通知id
        final int mId = Utils.getIntByStr(intent.getStringExtra("alarm_id"));
        manager.notify(mId, builder.build());


//        Notification notify = new Notification();
//        notify.icon = R.drawable.logo;
//        if (intent.getAction().equals(Constant.ACTION_ALARM_RECEIVED)
//                || intent.getAction().equals(Constant.ACTION_ALARM_PROPERTY)) {
//            notify.sound = Uri.parse("android.resource://" + context.getPackageName()
//                    + "/" + R.raw.alarm);
//        } else {
//            notify.sound = Uri.parse("android.resource://" + context.getPackageName()
//                    + "/" + R.raw.message);
//        }
//
//        int code = (int) (Long.parseLong(notifyId) % Integer.MAX_VALUE);
//
//        //使用code参数标记每次传递的intent 参数都不同
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, code,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        String[] split = msg.split("说:");
//
//        notify.setLatestEventInfo(context, split[0], split[1], pendingIntent);
//        notify.fullScreenIntent = pendingIntent;
//
//        notify.flags = Notification.FLAG_AUTO_CANCEL;
//
//        NotificationManager manager = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//
//        //同一个报警事件的推送事件使用相同的通知id
//        int mId = Utils.getIntByStr(intent.getStringExtra("alarm_id"));
//        manager.notify(mId, notify);
    }

    /**
     * 获取用户角色
     *
     * @param context
     * @return
     */
    private String getRole(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return preferences.getString("role", Constant.PROPERTY);
    }

    /**
     * 获取用户id
     *
     * @param context
     * @return
     */
    private String getUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return preferences.getString("user_id", "");
    }

    /**
     * 发送通知，点击跳转到页面
     *
     * @param context
     * @param alarmNotify
     * @param cls
     * @param msg
     * @param msgId
     */
    private void jumpToPage(Context context, AlarmNotify alarmNotify, Class<?> cls,
                            String msg, String msgId) {
        Intent intent = new Intent(context, cls);
        intent.setAction(alarmNotify.getNotifyType());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("alarm_id", alarmNotify.getAlarmId());
        intent.putExtra("msg", msg);
        intent.putExtra("from", TAG);

        sendNotify(context, intent, msgId, msg);
    }

    /**
     * 当在前台时，处理接收到的推送消息
     *
     * @param context
     * @param alarmNotify
     * @param msg
     */
    private void sendToPage(Context context, AlarmNotify alarmNotify, String msg) {
        Intent intent = new Intent();
        intent.setAction(addForeAction(alarmNotify.getNotifyType()));
        intent.putExtra("alarm_id", alarmNotify.getAlarmId());
        intent.putExtra("msg", msg);
        intent.putExtra("from", TAG);
        context.sendBroadcast(intent);
    }

    /**
     * 通知维修工
     *
     * @param context
     * @param alarmNotify
     * @param cls
     * @param msg
     * @param msgId
     */
    private void notifyWorker(Context context, AlarmNotify alarmNotify, Class<?> cls,
                              String msg, String msgId) {

        //如果是报警信息，写入到数据库中，同时给服务器反馈，已经接收到报警信息
//        String type = alarmNotify.getNotifyType();
//        if (type.equals(Constant.ACTION_ALARM_RECEIVED)) {
//            AlarmSqliteUtils.insertAlarm(context, getUserId(context), alarmNotify.getAlarmId());
//        }
//
//        //如果是指派结果信息，把此条报警相关的信息从sqlite中删除
//        if (type.equals(Constant.ACTION_ALARM_ASSIGNED )
//                || type.equals(Constant.ACTION_ALARM_UNASSIGNED)) {
//            AlarmSqliteUtils.deleteAlarm(context, alarmNotify.getAlarmId());
//        }
        if (BaseFragmentActivity.isForeground()) {
            sendToPage(context, alarmNotify, msg);
        } else {
            jumpToPage(context, alarmNotify, WorkerActivity.class, msg, msgId);
        }
    }

    /**
     * 通知物业
     *
     * @param context
     * @param alarmNotify
     * @param cls
     * @param msg
     * @param msgId
     */
    private void notifyProperty(Context context, AlarmNotify alarmNotify, Class<?> cls,
                                String msg, String msgId) {
        if (BaseFragmentActivity.isForeground()) {
            sendToPage(context, alarmNotify, msg);
        } else {
            jumpToPage(context, alarmNotify, AlarmTraceActivity.class, msg, msgId);
        }
    }


    /**
     * 产生内部消息的action
     *
     * @param action
     * @return
     */
    private String addForeAction(String action) {
        if (action.contains("_FORE")) {
            return action;
        }
        return action + "_FORE";
    }

    private void sendMainPlanRejectNotify(Context context) {
        Intent intent = new Intent(context, MyLiftActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("物业拒绝了您的维保计划")
                .setContentText("物业拒绝了您的维保计划，点击查看详情")
                .setContentIntent(pendingIntent)
                .getNotification();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification);
    }

    /**
     * 获取通知反馈的请求bean
     *
     * @param context
     * @param alarmId
     * @param notifyType
     * @return
     */
    private RequestBean getRequestBean(Context context, String alarmId, String notifyType) {

        NotifyFeedbackRequest request = new NotifyFeedbackRequest();
        NotifyFeedbackRequest.NotifyFeedbackReqBody body = request.new NotifyFeedbackReqBody();
        RequestHead head = new RequestHead();

        SharedPreferences preferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        head.setUserId(preferences.getString("user_id", ""));
        head.setAccessToken(preferences.getString("token", ""));

        body.setAlarmId(alarmId);
        body.setType(notifyType);
        body.setRole(preferences.getString("role", Constant.WORKER));

        request.setHead(head);
        request.setBody(body);

        return request;
    }
}