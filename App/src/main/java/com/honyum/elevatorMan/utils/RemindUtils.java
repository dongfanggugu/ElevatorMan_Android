package com.honyum.elevatorMan.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.data.LiftInfo;
import com.honyum.elevatorMan.data.RemindDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by changhaozhang on 15/9/14.
 */
public class RemindUtils {

    //制定计划的提醒
    public static final String ALARM_TYPE_MAKE_PLAN = "com.chorstar.alarm.make_plan";

    //完成计划任务的提醒
    public static final String ALARM_TYPE_FINISH_PLAN = "com.chorstar.alarm.finish_plan";

    //维保过期的提醒
    public static final String ALARM_TYPE_DEAD_LINE = "com.chorstar.alarm.dead_line";

    //提醒时间点，默认上午10:00:00
    private static int mRemindTime = 10;


    public static List<PendingIntent> mRemindList = new ArrayList<PendingIntent>();


    /**
     * 获取两个时间间隔的毫秒数
     * @param preDate
     * @param nowDate
     * @return
     */
    public static long getIntervalMs(Date preDate, Date nowDate) {

        if (preDate.after(nowDate)) {
            return 0;
        }
        long preMs = preDate.getTime();
        long nowMs = nowDate.getTime();
        return nowMs - preMs;
    }

    /**
     * 设置时间为 yyyy-mm-dd 00：00：00.000
     * @param date
     */
    public static Calendar initCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 将日历的日期设置为10:00:00 + minute
     * @param calendar
     * @param minute
     */
    public static void initCalendarTime(Calendar calendar, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * 将日历的日期设置为10:00:00
     * @param calendar
     */
    public static void initCalendarTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * 返回Date的Calendar的实例
     * @param date
     */
    public static Calendar getCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 根据Calendar返回字符串
     * @param calendar
     * @return
     */
    public static String calendarToStr(Calendar calendar) {
        Date date = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }


    /**
     * 设置计划提醒
     *
     * @param liftInfo
     */
    public static void setRemind(Context context, LiftInfo liftInfo, String userId) {

        //避免闹钟同时响铃，根据id设置一个分钟数
        int minute = Utils.getIntByStr(liftInfo.getId()) % 10;

        if (liftInfo.hasPlan()) {   //完成计划提醒
            Date nextDate = Utils.stringToDate(liftInfo.getPlanMainTime());
            Date date = new Date();
            int days = Utils.getIntervalDays(date, nextDate);

            if (days >= 3) {    //当距离维保计划日期大于等于3天时，设置提前两天的提醒闹钟
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(nextDate);
                calendar.add(Calendar.DAY_OF_MONTH, -2);

                RemindUtils.initCalendarTime(calendar, minute);
                setAlarm(context, liftInfo, calendar, ALARM_TYPE_FINISH_PLAN, userId);

            } else if (days < 3) {  //当计划过期时，如果还没有完成计划，依然进行提醒
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);

                if (hour < mRemindTime) {
                    RemindUtils.initCalendarTime(calendar, minute);
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    RemindUtils.initCalendarTime(calendar, minute);
                }
                setAlarm(context, liftInfo, calendar, ALARM_TYPE_FINISH_PLAN, userId);
            }
        } else {    //制定计划提醒
            Date lastDate = Utils.stringToDate(liftInfo.getLastMainTime());
            Date date = new Date();
            int days = Utils.getIntervalDays(lastDate, date);

            //当上次维保时间到今天小于3天时，设置距离上次维保3天后的提醒
            if (days < 3) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(lastDate);
                calendar.add(Calendar.DAY_OF_MONTH, 3);

                RemindUtils.initCalendarTime(calendar);
                setAlarm(context, liftInfo, calendar, ALARM_TYPE_MAKE_PLAN, userId);

            } else if (days >= 3) { //当大于等于三天并且小于等于12天(此时提醒过期的提醒进行提醒，制定计划不再提醒)

                //没到达10:00:00时设置当天的提醒，超过10:00:00时设置第二天的提醒
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);

                if (hour < mRemindTime) {
                    RemindUtils.initCalendarTime(calendar, minute);
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    RemindUtils.initCalendarTime(calendar, minute);
                }

                setAlarm(context, liftInfo, calendar, ALARM_TYPE_MAKE_PLAN, userId);
            }
        }

        //维保过期提醒闹钟
        {
            //如果上次维保时间为空，不设置维保过期提醒
            if (StringUtils.isEmpty(liftInfo.getLastMainTime())) {
                return;
            }
            Date lastDate = Utils.stringToDate(liftInfo.getLastMainTime());
            Calendar deadLineCalendar  = Calendar.getInstance();
            deadLineCalendar.setTime(lastDate);
            deadLineCalendar.add(Calendar.DAY_OF_MONTH, 15);

            int days = Utils.getIntervalDays(new Date(), deadLineCalendar.getTime());

            if (days >= 3) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(deadLineCalendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH, -2);

                RemindUtils.initCalendarTime(calendar, 10);
                setAlarm(context, liftInfo, calendar, ALARM_TYPE_DEAD_LINE, userId);

            } else if (days >= 0){
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);

                if (hour < mRemindTime) {
                    RemindUtils.initCalendarTime(calendar, 10);
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    RemindUtils.initCalendarTime(calendar, 10);
                }
                setAlarm(context, liftInfo, calendar, ALARM_TYPE_DEAD_LINE, userId);
            }

        }

    }

    /**
     * 设置定时闹钟
     *
     * @param liftInfo
     * @param calendar
     * @param action
     */

    private static void setAlarm(Context context, LiftInfo liftInfo, Calendar calendar, String action,
                                 String userId) {

        String code = liftInfo.getNum();
        Intent intent = new Intent();
        intent.setAction(action);


        intent.putExtra("lift", liftInfo);
        intent.putExtra("date", calendar);
        intent.putExtra("user_id", userId);

        Log.i("zhenhao", "remind date:" + calendarToStr(calendar));
        //设置定时任务id，同一个电梯的同一类型任务id相同
        int senderId = Utils.getIntByStr(code);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, senderId, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        mRemindList.add(pendingIntent);
        long millionSeconds = RemindUtils.getIntervalMs(new Date(), calendar.getTime());
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + millionSeconds, pendingIntent);
    }
}
