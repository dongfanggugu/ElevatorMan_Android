package com.honyum.elevatorMan.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.maintenance.MaintenanceActivity;
import com.honyum.elevatorMan.activity.maintenance.MyLiftActivity;
import com.honyum.elevatorMan.activity.maintenance.PlanActivity;
import com.honyum.elevatorMan.base.Config;
import com.honyum.elevatorMan.data.LiftInfo;
import com.honyum.elevatorMan.data.RemindDate;
import com.honyum.elevatorMan.utils.RemindUtils;
import com.honyum.elevatorMan.utils.Utils;

import java.util.Calendar;
import java.util.Date;

public class RemindReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();
        LiftInfo liftInfo = (LiftInfo) intent.getSerializableExtra("lift");

        //如果此次闹铃的用户和当前登录用户不一致，不提醒，并且不再设置下次闹铃
//        String userId = intent.getStringExtra("user_id");
//        if (!userId.equals(getUserId(context))) {
//            return;
//        }

        //设置下次提醒
        RemindUtils.setRemind(context, liftInfo, getUserId(context));

        //设置的闹铃日期，如果相差时间大于10min，则不再响闹铃
        Calendar calendar = (Calendar) intent.getSerializableExtra("date");

        long intervalMs = RemindUtils.getIntervalMs(calendar.getTime(), new Date());
        long tenMin = 1000 * 60 * 10;

        Log.i("zhenhao", "num:" + liftInfo.getNum());
        Log.i("zhenhao", "date:" + RemindUtils.calendarToStr(calendar));

        if (intervalMs > tenMin) {
            return;
        }

        if (action.equals(RemindUtils.ALARM_TYPE_MAKE_PLAN)) {


            String code = liftInfo.getNum();
            String title = "有电梯需要制定维保计划";
            String message = "电梯" + code + "需要进行制定计划";

           // SQLUtils.insertMsg(context, userId, Utils.dateToString(new Date()), message);
            Intent jumpIntent = new Intent(context, PlanActivity.class);
            jumpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            jumpIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            jumpIntent.putExtra("enter_type", "add");
            jumpIntent.putExtra("lift", liftInfo);

            sendNotify(context, title, message, code, jumpIntent);

        } else if (action.equals(RemindUtils.ALARM_TYPE_FINISH_PLAN)) {
            String code = liftInfo.getNum();
            String title = "有电梯需要完成维保";
            String message = "电梯" + code + "需要进行维保";

            // SQLUtils.insertMsg(context, userId, Utils.dateToString(new Date()), message);


            Intent jumpIntent = new Intent(context, MaintenanceActivity.class);
            jumpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            jumpIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            jumpIntent.putExtra("lift", liftInfo);

            sendNotify(context, title, message, code, jumpIntent);

        } else if (action.equals(RemindUtils.ALARM_TYPE_DEAD_LINE)) {
            String code = liftInfo.getNum();
            String title = "有电梯维保即将过期";
            String message = "电梯" + code + "即将过期";

            // SQLUtils.insertMsg(context, userId, Utils.dateToString(new Date()), message);

            Intent jumpIntent = new Intent();
            if (liftInfo.hasPlan()) {
                jumpIntent.setClass(context, MaintenanceActivity.class);
            } else {
                jumpIntent.setClass(context, PlanActivity.class);
            }

            jumpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            jumpIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            jumpIntent.putExtra("lift", liftInfo);

            sendNotify(context, title, message, code, jumpIntent);
        }
    }

    /**
     * 发送通知
     * @param context
     * @param title
     * @param message
     */
    private void sendNotify(Context context, String title, String message, String id, Intent intent) {
        int notifyId = Utils.getIntByStr(id);

        Notification notify = new Notification();
        notify.icon = R.drawable.logo;
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notify.defaults = Notification.DEFAULT_SOUND;


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        notify.setLatestEventInfo(context, title, message, pendingIntent);

        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(notifyId, notify);
    }

    /**
     * 获取用户id
     * @param context
     * @return
     */
    private String getUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return preferences.getString("user_id", "");
    }
}
