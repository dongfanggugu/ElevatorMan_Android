package com.honyum.elevatorMan.data;

import java.io.Serializable;

/**
 * Created by Star on 2017/6/15.
 */

public class AlarmInfo1  implements Serializable{
    private String alarmId;

    private String alarmTime;

    private String buildingCode;

    private String communityAddress;

    private String communityId;

    private String communityName;

    private int injureCount;

    private String isMisinformation;

    private double lat;

    private String liftNum;

    private double lng;

    private int savedCount;

    private String state;

    private String type;

    private String unitCode;

    public void setAlarmId(String alarmId){
        this.alarmId = alarmId;
    }
    public String getAlarmId(){
        return this.alarmId;
    }
    public void setAlarmTime(String alarmTime){
        this.alarmTime = alarmTime;
    }
    public String getAlarmTime(){
        return this.alarmTime;
    }
    public void setBuildingCode(String buildingCode){
        this.buildingCode = buildingCode;
    }
    public String getBuildingCode(){
        return this.buildingCode;
    }
    public void setCommunityAddress(String communityAddress){
        this.communityAddress = communityAddress;
    }
    public String getCommunityAddress(){
        return this.communityAddress;
    }
    public void setCommunityId(String communityId){
        this.communityId = communityId;
    }
    public String getCommunityId(){
        return this.communityId;
    }
    public void setCommunityName(String communityName){
        this.communityName = communityName;
    }
    public String getCommunityName(){
        return this.communityName;
    }
    public void setInjureCount(int injureCount){
        this.injureCount = injureCount;
    }
    public int getInjureCount(){
        return this.injureCount;
    }
    public void setIsMisinformation(String isMisinformation){
        this.isMisinformation = isMisinformation;
    }
    public String getIsMisinformation(){
        return this.isMisinformation;
    }
    public void setLat(double lat){
        this.lat = lat;
    }
    public double getLat(){
        return this.lat;
    }
    public void setLiftNum(String liftNum){
        this.liftNum = liftNum;
    }
    public String getLiftNum(){
        return this.liftNum;
    }
    public void setLng(double lng){
        this.lng = lng;
    }
    public double getLng(){
        return this.lng;
    }
    public void setSavedCount(int savedCount){
        this.savedCount = savedCount;
    }
    public int getSavedCount(){
        return this.savedCount;
    }
    public void setState(String state){
        this.state = state;
    }
    public String getState(){
        return this.state;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setUnitCode(String unitCode){
        this.unitCode = unitCode;
    }
    public String getUnitCode(){
        return this.unitCode;
    }

}
