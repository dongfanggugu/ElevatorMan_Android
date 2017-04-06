package com.honyum.elevatorMan.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by chang on 2016/3/4.
 */
public class SQLiteUtils {

    private static final String TAG = "SQLiteUtils";

    /**
     * 打开db文件
     * @param context
     * @param dbpath
     * @return
     */
    public static SQLiteDatabase opendb(Context context, String dbpath) {
        SQLiteDatabase db = context.openOrCreateDatabase(dbpath, Context.MODE_PRIVATE, null);
        return db;
    }

    /**
     * 判断表是否存在
     * @param db
     * @param table
     * @return
     */
    public static boolean isTableExist(SQLiteDatabase db, String table) {
        if (!db.isOpen()) {
            Log.e(TAG, "db is not open");
            return false;
        }

        boolean result  = false;

        String sql = "select name from sqlite_master where type='table'";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
            while(cursor.moveToNext()) {
                String name = cursor.getString(0);
                if (name.equals(table)) {
                    result = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return result;
    }
}
