package com.chorstar.jni;

import android.util.Log;

import com.chorstar.data.JKnowledgeInfo;
import com.honyum.elevatorMan.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by changhaozhang on 16/4/5.
 */
public class ChorstarJNI {

    private final static String KNOW_DB = "/data/data/com.honyum.elevatorMan/databases/knowledge.db";

    private static native ArrayList<JKnowledgeInfo> native_getKnowledgeList(String path, String brand, String keywords);

    private static native ArrayList<String> native_getBrandList(String path);

    private static native void native_requestKnowledgeUpdate(String path, String address, String token, String userId);

    private static native boolean native_hasAlarmUnassigned(String address, String token, String userId);

    private static native boolean native_hasAlarmUnfinished(String address, String token, String userId);
    /***
     * get brand list
     * @return
     */
    public static ArrayList<String> getBrandList() {
        return native_getBrandList(KNOW_DB);
    }

    /**
     * get knoledge list by seleciton
     * @param brand
     * @param keywords
     * @return
     */
    public static ArrayList<JKnowledgeInfo> getKnowledgeList(String brand, String keywords) {
        File dbFile = new File(KNOW_DB);
        if (!dbFile.exists()) {
            return new ArrayList<JKnowledgeInfo>();
        }
        return native_getKnowledgeList(KNOW_DB, brand, keywords);
    }

    public static void requestKnowledgeUpdate(String address, String token, String userId) {
        native_requestKnowledgeUpdate(KNOW_DB, address, token, userId);
    }

    /**
     * 是否有发送但没有指派成功的报警
     * @param address
     * @param token
     * @param userId
     * @return
     */
    public static boolean hasAlarmUnassigned(String address, String token, String userId) {
        return native_hasAlarmUnassigned(address, token, userId);
    }

    /**
     * 是否有没有处理完成的报警
     * @param address
     * @param token
     * @param userId
     * @return
     */
    public static boolean hasAlarmUnfinished(String address, String token, String userId) {
        return native_hasAlarmUnfinished(address, token, userId);
    }
}
