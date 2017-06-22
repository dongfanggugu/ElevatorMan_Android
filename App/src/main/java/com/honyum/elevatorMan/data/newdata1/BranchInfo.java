package com.honyum.elevatorMan.data.newdata1;

import java.io.Serializable;

public class BranchInfo implements Serializable{
private String address;

private String branchtype;

private String code;

private long createdatetime;

private String icon;

private String id;

private int integral;

private String isAgent;

private String isDelete;

private String isJoinVilla;

private String name;

private int seq;

private String showState;

private String state;

private String tel;

public void setAddress(String address){
this.address = address;
}
public String getAddress(){
return this.address;
}
public void setBranchtype(String branchtype){
this.branchtype = branchtype;
}
public String getBranchtype(){
return this.branchtype;
}
public void setCode(String code){
this.code = code;
}
public String getCode(){
return this.code;
}
public void setCreatedatetime(long createdatetime){
this.createdatetime = createdatetime;
}
public long getCreatedatetime(){
return this.createdatetime;
}
public void setIcon(String icon){
this.icon = icon;
}
public String getIcon(){
return this.icon;
}
public void setId(String id){
this.id = id;
}
public String getId(){
return this.id;
}
public void setIntegral(int integral){
this.integral = integral;
}
public int getIntegral(){
return this.integral;
}
public void setIsAgent(String isAgent){
this.isAgent = isAgent;
}
public String getIsAgent(){
return this.isAgent;
}
public void setIsDelete(String isDelete){
this.isDelete = isDelete;
}
public String getIsDelete(){
return this.isDelete;
}
public void setIsJoinVilla(String isJoinVilla){
this.isJoinVilla = isJoinVilla;
}
public String getIsJoinVilla(){
return this.isJoinVilla;
}
public void setName(String name){
this.name = name;
}
public String getName(){
return this.name;
}
public void setSeq(int seq){
this.seq = seq;
}
public int getSeq(){
return this.seq;
}
public void setShowState(String showState){
this.showState = showState;
}
public String getShowState(){
return this.showState;
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

}