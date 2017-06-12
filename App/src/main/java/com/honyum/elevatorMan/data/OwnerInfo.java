package com.honyum.elevatorMan.data;

import java.io.Serializable;

public class OwnerInfo implements Serializable
{
    private String createTime;

    private String id;

    private String isDelete;

    private String name;

    private String password;

    private String tel;

    public void setCreateTime(String createTime){
        this.createTime = createTime;
    }
    public String getCreateTime(){
        return this.createTime;
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
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }
    public void setTel(String tel){
        this.tel = tel;
    }
    public String getTel(){
        return this.tel;
    }
}
