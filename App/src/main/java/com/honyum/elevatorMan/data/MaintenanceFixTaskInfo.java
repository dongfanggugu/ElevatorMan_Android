package com.honyum.elevatorMan.data;

import java.io.Serializable;

public class MaintenanceFixTaskInfo implements Serializable {
    private String createTime;

    private String finishResult;

    private String id;

    private String repairOrderId;

    private RepairOrderInfo repairOrderInfo;

    private String state;

    private String workerId;

    private WorkerInfo1 workerInfo;

    private String workerName;

    private String workerTel;

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
