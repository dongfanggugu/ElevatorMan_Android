package com.honyum.elevatorMan.data;

import java.io.Serializable;

/**
 * Created by Star on 2017/6/15.
 */

public class MaintRecInfo implements Serializable {
    private String address;

    private String brand;

    private String communityName;

    private String coummunityId;

    private String elevatorId;

    private String elevatorNum;

    private String lastMainTime;

    private double lat;

    private double lng;

    private String maintTime = "";

    private String maintType;

    private String planTime;

    private String propertyFlg;

    private String userId;

    private String userName;

    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return this.address;
    }
    public void setBrand(String brand){
        this.brand = brand;
    }
    public String getBrand(){
        return this.brand;
    }
    public void setCommunityName(String communityName){
        this.communityName = communityName;
    }
    public String getCommunityName(){
        return this.communityName;
    }
    public void setCoummunityId(String coummunityId){
        this.coummunityId = coummunityId;
    }
    public String getCoummunityId(){
        return this.coummunityId;
    }
    public void setElevatorId(String elevatorId){
        this.elevatorId = elevatorId;
    }
    public String getElevatorId(){
        return this.elevatorId;
    }
    public void setElevatorNum(String elevatorNum){
        this.elevatorNum = elevatorNum;
    }
    public String getElevatorNum(){
        return this.elevatorNum;
    }
    public void setLastMainTime(String lastMainTime){
        this.lastMainTime = lastMainTime;
    }
    public String getLastMainTime(){
        return this.lastMainTime;
    }
    public void setLat(double lat){
        this.lat = lat;
    }
    public double getLat(){
        return this.lat;
    }
    public void setLng(double lng){
        this.lng = lng;
    }
    public double getLng(){
        return this.lng;
    }
    public void setMaintTime(String maintTime){
        this.maintTime = maintTime;
    }
    public String getMaintTime(){
        return this.maintTime;
    }
    public void setMaintType(String maintType){
        this.maintType = maintType;
    }
    public String getMaintType(){
        return this.maintType;
    }
    public void setPlanTime(String planTime){
        this.planTime = planTime;
    }
    public String getPlanTime(){
        return this.planTime;
    }
    public void setPropertyFlg(String propertyFlg){
        this.propertyFlg = propertyFlg;
    }
    public String getPropertyFlg(){
        return this.propertyFlg;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
    public String getUserId(){
        return this.userId;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getUserName(){
        return this.userName;
    }
}
