package com.honyum.elevatorMan.data;

import android.database.Cursor;

import java.io.Serializable;
import java.util.List;

public class AlarmInfo implements Serializable {

    private String alarmTime = "2015-06-05 18:07:10";

    private WorkerInfo alarmUserInfo;

    private CommunityInfo communityInfo;

    private Elevator elevatorInfo;

    private List<ContactList> contactList;

    private String id;

    private String remark;

    private String state; // 救援状态 1：已出发 2:已到达 3.已完成 4.拒绝

    private String userState;    //用户状态	1：已出发 	2:已到达 3.已完成	4.意外情况 5:物业确认报警

    private String appointCount;    //推送报警信息给维修工的人数

    private String isMisinformation;  //任务取消标志 0:运行中 1:取消

    private String receivedTime;

    private String type;        //1:电梯报警  2:项目报警

    private int savedCount = 0;

    private int injureCount = 0;

    public List<ContactList> getContactList() {
        return contactList;
    }

    public void setContactList(List<ContactList> contactList) {
        this.contactList = contactList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSavedCount() {
        return savedCount;
    }

    public void setSavedCount(int savedCount) {
        this.savedCount = savedCount;
    }

    public int getInjureCount() {
        return injureCount;
    }

    public void setInjureCount(int injureCount) {
        this.injureCount = injureCount;
    }

    public String getAppointCount() {
        return appointCount;
    }

    public void setAppointCount(String appointCount) {
        this.appointCount = appointCount;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public WorkerInfo getAlarmUserInfo() {
        return alarmUserInfo;
    }

    public void setAlarmUserInfo(WorkerInfo alarmUserInfo) {
        this.alarmUserInfo = alarmUserInfo;
    }

    public CommunityInfo getCommunityInfo() {
        return communityInfo;
    }

    public void setCommunityInfo(CommunityInfo communityInfo) {
        this.communityInfo = communityInfo;
    }

    public Elevator getElevatorInfo() {
        return elevatorInfo;
    }

    public void setElevatorInfo(Elevator elevatorInfo) {
        this.elevatorInfo = elevatorInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIsMisinformation() {
        return isMisinformation;
    }

    public void setIsMisinformation(String isMisinformation) {
        this.isMisinformation = isMisinformation;
    }

    public String getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(String receivedTime) {
        this.receivedTime = receivedTime;
    }

    public static AlarmInfo generateAlarmInfo(Cursor cursor) {
        AlarmInfo alarmInfo = new AlarmInfo();
        alarmInfo.id = cursor.getString(cursor.getColumnIndex("alarm_id"));
        alarmInfo.receivedTime = cursor.getString(cursor.getColumnIndex("date"));
        alarmInfo.userState = cursor.getString(cursor.getColumnIndex("state"));
        return alarmInfo;
    }
}