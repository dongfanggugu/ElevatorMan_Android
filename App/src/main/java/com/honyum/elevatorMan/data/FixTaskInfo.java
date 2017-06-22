package com.honyum.elevatorMan.data;

import java.io.Serializable;

public class FixTaskInfo implements Serializable {
    private String createTime;

    private String finishResult;

    private String id;

    private String repairOrderId;

    private RepairOrderInfo repairOrderInfo;

    private String state;

    private String workerId;

    private WorkerInfo1 workerInfo;

    private String workerName;

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
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

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    private String workerTel;
    private String pictures = "";

    private String startTime;
    private String arriveTime;

    private String taskCode;
    private String planTime;
    private String payMoney;
    private String completeTime;


    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setFinishResult(String finishResult) {
        this.finishResult = finishResult;
    }

    public String getFinishResult() {
        return this.finishResult;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setRepairOrderId(String repairOrderId) {
        this.repairOrderId = repairOrderId;
    }

    public String getRepairOrderId() {
        return this.repairOrderId;
    }

    public void setRepairOrderInfo(RepairOrderInfo repairOrderInfo) {
        this.repairOrderInfo = repairOrderInfo;
    }

    public RepairOrderInfo getRepairOrderInfo() {
        return this.repairOrderInfo;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getWorkerId() {
        return this.workerId;
    }

    public void setWorkerInfo(WorkerInfo1 workerInfo) {
        this.workerInfo = workerInfo;
    }

    public WorkerInfo1 getWorkerInfo() {
        return this.workerInfo;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerName() {
        return this.workerName;
    }

    public void setWorkerTel(String workerTel) {
        this.workerTel = workerTel;
    }

    public String getWorkerTel() {
        return this.workerTel;
    }
}
