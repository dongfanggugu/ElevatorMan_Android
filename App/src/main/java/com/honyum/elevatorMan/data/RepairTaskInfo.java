package com.honyum.elevatorMan.data;

import java.io.Serializable;

/**
 * Created by 李有鬼 on 2017/1/9 0009
 */
public class RepairTaskInfo implements Serializable {

    /**
     * id : 保修单ID
     * smallOwnerId : 小业主ID
     * tel : 小业主电话
     * name : 小业主姓名
     * brand : 电梯品牌
     * model : 电梯型号
     * cellName : 小区名字
     * address : 地址
     * workerId : 维保人员ID
     * workerName : 维保人员姓名
     * workerTel : 维保人员电话
     * communityId : 项目ID
     * communityName : 项目名称
     * communityManagera : 负责人姓名
     * communityTela : 负责人电话
     * startTime : 出发时间
     * arriveTime : 到达时间
     * endTime : 完成时间
     * description : 备注
     * state : 状态 1待确认2已确认4已委派6维修中8维修完成9确认完成
     * phenomenon : 报修现象
     * createTime : 报修时间
     * userName : 处理人
     * isDelete : 是否删除 1删除
     */

    private String id;
    private String smallOwnerId;
    private String tel;
    private String name;
    private String brand;
    private String model;
    private String cellName;
    private String address;
    private String workerId;
    private String workerName;
    private String workerTel;
    private String communityId;
    private String communityName;
    private String communityManagera;
    private String communityTela;
    private String startTime;
    private String arriveTime;
    private String endTime;
    private String description;
    private String state;
    private String phenomenon;
    private String createTime;
    private String userName;
    private String isDelete;
    private String finishResult;
    private String evaluate;
    private String evaluateInfo;

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getEvaluateInfo() {
        return evaluateInfo;
    }

    public void setEvaluateInfo(String evaluateInfo) {
        this.evaluateInfo = evaluateInfo;
    }

    public String getFinishResult() {
        return finishResult;
    }

    public void setFinishResult(String finishResult) {
        this.finishResult = finishResult;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSmallOwnerId() {
        return smallOwnerId;
    }

    public void setSmallOwnerId(String smallOwnerId) {
        this.smallOwnerId = smallOwnerId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerTel() {
        return workerTel;
    }

    public void setWorkerTel(String workerTel) {
        this.workerTel = workerTel;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCommunityManagera() {
        return communityManagera;
    }

    public void setCommunityManagera(String communityManagera) {
        this.communityManagera = communityManagera;
    }

    public String getCommunityTela() {
        return communityTela;
    }

    public void setCommunityTela(String communityTela) {
        this.communityTela = communityTela;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhenomenon() {
        return phenomenon;
    }

    public void setPhenomenon(String phenomenon) {
        this.phenomenon = phenomenon;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
}
