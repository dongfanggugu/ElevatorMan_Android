package com.honyum.elevatorMan.data;

import java.io.Serializable;

public class AlarmProcessInfo implements Serializable{
private String confirmName;

private String confirmTime;

private String id;

private String isConfirm;

private String recordTime;

private String state;

private String tel;

private String userName;

public void setConfirmName(String confirmName){
this.confirmName = confirmName;
}
public String getConfirmName(){
return this.confirmName;
}
public void setConfirmTime(String confirmTime){
this.confirmTime = confirmTime;
}
public String getConfirmTime(){
return this.confirmTime;
}
public void setId(String id){
this.id = id;
}
public String getId(){
return this.id;
}
public void setIsConfirm(String isConfirm){
this.isConfirm = isConfirm;
}
public String getIsConfirm(){
return this.isConfirm;
}
public void setRecordTime(String recordTime){
this.recordTime = recordTime;
}
public String getRecordTime(){
return this.recordTime;
}
public void setState(String state){
this.state = state;
}
public String getState(){
return this.state;
}
public void setTel(String tel){
this.tel = tel;
}
public String getTel(){
return this.tel;
}
public void setUserName(String userName){
this.userName = userName;
}
public String getUserName(){
return this.userName;
}

}
