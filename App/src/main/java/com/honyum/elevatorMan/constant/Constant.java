package com.honyum.elevatorMan.constant;

import java.util.HashMap;
import java.util.Map;

public class Constant {



    /**
     * 用户角色
     */

    /**
     * 用户角色
     */
    //维修工
    public static final String WORKER = "3";

    //物业
    public static final String PROPERTY = "2";

    //维修公司
    public static final String COMPANY = "1";

    /**
     * 消息广播
     */
    //定位完成
    public static final String ACTION_LOCATION_COMPLETE = "com.action.location_complete";

    //接收到报警信息
    public static final String ACTION_ALARM_RECEIVED = "WORKER_ALARM";

    //接收到指派信息
    public static final String ACTION_ALARM_ASSIGNED = "WORKER_CHOSEN_TRUE";

    public static final String ACTION_ALARM_UNASSIGNED = "WORKER_CHOSEN_FALSE";

    //定时任务执行时发送消息
    public static final String ACTION_TIMER_START = "com.action.timer_service";

    //已经指派完维修工，发送给物业
    public static final String ACTION_WORKER_ASSIGNED = "PROPERTY_ASSIGNED";

    //维修工已经到达
    public static final String ACTION_WORKER_ARRIVED = "PROPERTY_ARRIVED";

    //救援已完成
    public static final String ACTION_ALARM_COMPLETE = "PROPERTY_COMPLETED";

    //物业收到报警通知
    public static final String ACTION_ALARM_PROPERTY = "PROPERTY_ALARM";

    //维修工维保计划被拒绝
    public static final String ACTION_MAIN_REJECT = "MAIN_DENIED";

    //报警撤销
    public static final String ACTION_ALARM_CANCEL = "ALARM_CANCEL";


    /**
     * 报警任务状态
     */

    //报警事件发生
    public static final String ALARM_STATE_START = "0";

    //报警事件已经分配给维修工进行处理
    public static final String ALARM_STATE_ASSIGNED = "1";

    //报警事件开始进行现场救援
    public static final String ALARM_STATE_ARRIVED = "2";

    //报警事件现场救援完成
    public static final String ALARM_STATE_COMPLETE = "3";

    //报警任务通过物业确认
    public static final String ALARM_STATE_CONFIRM = "5";


    /**
     * 维修工状态
     */

    //接收到报警信息
    public static final String WORKER_STATE_RECEIVED = "-1";

    //维修工接收报警
    public static final String WORKER_STATE_ACCEPT = "0";

    //维修工被指派参与救援任务，开始出发
    public static final String WORKER_STATE_START = "1";

    //维修工到达救援任务现场
    public static final String WORKER_STATE_ARRIVED = "2";

    //维修工完成救援任务
    public static final String WORKER_STATE_COMPLETE = "3";


    /**
     * 处理服务器列表
     */

    public static final String BEIJING = "北京";

    public static final String SHANGHAI = "上海";

    public static final String MA = "马晓明";

    public static final String SHOW = "演示";

    public static final String AZURE = "Azure";

    public static final String CHINA = "全国";

    public static final String ZHANG = "张明锁";

    public static final Map<String, String> SERVER_LIST = new HashMap<String, String>() {
        {
            put(BEIJING, "http://211.147.152.6:8080/lift/mobile");

            put(SHANGHAI, "http://47.93.123.2:8080/lift/mobile");

            put(MA, "http://119.57.248.130/mobile");

            put(SHOW, "http://182.92.177.247:8080/lift/mobile");

            put(AZURE, "http://smartydt-lift-beijing.chinacloudapp.cn:8080/lift/mobile");

            put(CHINA, "http://123.57.10.16:8080/lift/mobile");

            put(ZHANG, "http://192.168.0.82:8080/lift/mobile");
        }
    };

}