package com.honyum.elevatorMan.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.honyum.elevatorMan.data.AlarmInfo;
import com.honyum.elevatorMan.data.AlarmInfo;
import com.honyum.elevatorMan.data.RemindMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chang on 2015/7/6.
 * 本地数据的操作
 */
public class AlarmSqliteUtils {

    private static String dbName = Utils.getSdPath() + "/knowledge/chorstar.db";

    private static String tableName = "alarm_msg";

    /**
     * 初始化db文件
     *
     * @param context
     */
    public static void initSQL(Context context) {
        SQLiteDatabase db = context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS " + tableName + " (id TEXT, user_id TEXT, "
                + "alarm_id TEXT, date TEXT, state TEXT)";
        try {
            db.execSQL(sqlCreateTable);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    /**
     * 返回db对象
     *
     * @param context
     * @return
     */
    private static SQLiteDatabase openDb(Context context) {
        SQLiteDatabase db = context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
        if (tableExist(db, tableName)) {
            return db;
        } else {
            return null;
        }
    }

    /**
     * 报警信息到数据库中
     * @param context
     * @param userId
     * @param alarmId
     */
    public static void insertAlarm(Context context, String userId, String alarmId) {

        SQLiteDatabase db = openDb(context);
        if (null == db) {
            return;
        }


        try {
            ContentValues cv = new ContentValues();
            cv.put("id", System.currentTimeMillis());
            cv.put("user_id", userId);
            cv.put("alarm_id", alarmId);
            cv.put("date", System.currentTimeMillis());
            cv.put("state", "-1");
            db.insert(tableName, null, cv);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    /**
     * 接受报警，更改报警状态
     * @param context
     * @param alarmId
     */
    public static void acceptAlarm(Context context, String alarmId) {

        SQLiteDatabase db = openDb(context);
        String querySql = "select * from " + tableName + " where alarm_id = ? and "
                + "state = ?";
        String updateSql = "update " + tableName + " set state = ? where alarm_id = ?";

        Cursor queryCursor = null;

        try {
            queryCursor = db.rawQuery(querySql, new String[]{alarmId, "-1"});
            if (queryCursor.getCount() > 0) {
                db.execSQL(updateSql, new String[]{"0", alarmId}) ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            queryCursor.close();
            db.close();
        }
    }
    /**
     * 获取报警信息数据
     *
     * @param context
     * @param userId
     * @return
     */
    public static List<AlarmInfo> queryAlarm(Context context, String userId) {
        SQLiteDatabase db = openDb(context);
        if (null == db) {
            return null;
        }

        List<AlarmInfo> alarmInfoList = new ArrayList<AlarmInfo>();
        String sqlQuery = "select * from " + tableName + " where user_id = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sqlQuery, new String[]{userId});
            while (cursor.moveToNext()) {
                AlarmInfo alarmInfo = AlarmInfo.generateAlarmInfo(cursor);
                alarmInfoList.add(alarmInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return alarmInfoList;
    }

    /**
     * 删除报警记录
     *
     * @param context
     * @param alarmId
     */
    public static void deleteAlarm(Context context, String alarmId) {
        SQLiteDatabase db = openDb(context);
        if (null == db) {
            return;
        }

        try {
            db.delete(tableName, "alarm_id = ?", new String[]{alarmId});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    /**
     * 判断表是否存在
     *
     * @param db
     * @param tableName
     * @return
     */
    private static boolean tableExist(SQLiteDatabase db, String tableName) {
        if (null == db) {
            return false;
        }

        boolean exist = false;

        String sql = "SELECT * FROM sqlite_master WHERE type = 'table' AND name = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, new String[]{tableName});
            if (null == cursor || 0 == cursor.getCount()) {
                exist = false;
            } else if (cursor.getCount() > 0) {
                exist = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return exist;
    }
}
