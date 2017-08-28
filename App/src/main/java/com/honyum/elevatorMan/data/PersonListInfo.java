package com.honyum.elevatorMan.data;

import java.io.Serializable;

/**
 * Created by Star on 2017/8/11.
 */

public class PersonListInfo implements Serializable {


    private int age;
    private String branchId;
    private String branchName;
    private String code;
    private long createdatetime;
    private String id;
    private String isDelete;
    private int isdefault;
    private double lat;
    private double lng;
    private String loginname;
    private String name;
    private String operationCard;
    private String password;
    private String pic;
    private String remark;
    private String roleIds;
    private String roleNames;
    private int sex;
    private int state;
    private String tel;
    private String type;
    private int usertype;

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCreatedatetime(long createdatetime) {
        this.createdatetime = createdatetime;
    }

    public long getCreatedatetime() {
        return createdatetime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }

    public int getIsdefault() {
        return isdefault;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLng() {
        return lng;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOperationCard(String operationCard) {
        this.operationCard = operationCard;
    }

    public String getOperationCard() {
        return operationCard;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getSex() {
        return sex;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTel() {
        return tel;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public int getUsertype() {
        return usertype;
    }


}
