package com.honyum.elevatorMan.data;

import java.io.Serializable;

public class MaintUserInfo implements Serializable {
    private int age;

    private String code;

    private long createdatetime;

    private String id;

    private String isDelete;

    private int isdefault;

    private String loginname;

    private String name;

    private String operationCard;

    private String password;

    private String pic;

    private String remark;

    private int sex;

    private int state;

    private String tel;

    private String type;

    private int usertype;

    public void setAge(int age){
        this.age = age;
    }
    public int getAge(){
        return this.age;
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
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setIsDelete(String isDelete){
        this.isDelete = isDelete;
    }
    public String getIsDelete(){
        return this.isDelete;
    }
    public void setIsdefault(int isdefault){
        this.isdefault = isdefault;
    }
    public int getIsdefault(){
        return this.isdefault;
    }
    public void setLoginname(String loginname){
        this.loginname = loginname;
    }
    public String getLoginname(){
        return this.loginname;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setOperationCard(String operationCard){
        this.operationCard = operationCard;
    }
    public String getOperationCard(){
        return this.operationCard;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }
    public void setPic(String pic){
        this.pic = pic;
    }
    public String getPic(){
        return this.pic;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
    public String getRemark(){
        return this.remark;
    }
    public void setSex(int sex){
        this.sex = sex;
    }
    public int getSex(){
        return this.sex;
    }
    public void setState(int state){
        this.state = state;
    }
    public int getState(){
        return this.state;
    }
    public void setTel(String tel){
        this.tel = tel;
    }
    public String getTel(){
        return this.tel;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setUsertype(int usertype){
        this.usertype = usertype;
    }
    public int getUsertype(){
        return this.usertype;
    }
}
