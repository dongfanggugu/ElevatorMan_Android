package com.honyum.elevatorMan.data.mydata;

import java.io.Serializable;

public class NHFixAndTask implements Serializable {
    private String address;

    private String branchId;

    private BranchInfo branchInfo;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    private String picture;

    private String brand;

    private String code;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public BranchInfo getBranchInfo() {
        return branchInfo;
    }

    public void setBranchInfo(BranchInfo branchInfo) {
        this.branchInfo = branchInfo;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getIsPayment() {
        return isPayment;
    }

    public void setIsPayment(String isPayment) {
        this.isPayment = isPayment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(int payMoney) {
        this.payMoney = payMoney;
    }

    public String getPhenomenon() {
        return phenomenon;
    }

    public void setPhenomenon(String phenomenon) {
        this.phenomenon = phenomenon;
    }

    public String getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(String repairTime) {
        this.repairTime = repairTime;
    }

    public String getRepairTypeId() {
        return repairTypeId;
    }

    public void setRepairTypeId(String repairTypeId) {
        this.repairTypeId = repairTypeId;
    }

    public RepairTypeInfo getRepairTypeInfo() {
        return repairTypeInfo;
    }

    public void setRepairTypeInfo(RepairTypeInfo repairTypeInfo) {
        this.repairTypeInfo = repairTypeInfo;
    }

    public String getSmallOwnerId() {
        return smallOwnerId;
    }

    public void setSmallOwnerId(String smallOwnerId) {
        this.smallOwnerId = smallOwnerId;
    }

    public SmallOwnerInfo getSmallOwnerInfo() {
        return smallOwnerInfo;
    }

    public void setSmallOwnerInfo(SmallOwnerInfo smallOwnerInfo) {
        this.smallOwnerInfo = smallOwnerInfo;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getVillaId() {
        return villaId;
    }

    public void setVillaId(String villaId) {
        this.villaId = villaId;
    }

    public VillaInfo getVillaInfo() {
        return villaInfo;
    }

    public void setVillaInfo(VillaInfo villaInfo) {
        this.villaInfo = villaInfo;
    }

    private String isDelete;

    private String isPayment;

    private String name;

    private int payMoney;

    private String phenomenon;

    private String repairTime;

    private String repairTypeId;

    private RepairTypeInfo repairTypeInfo = new RepairTypeInfo();

    private String smallOwnerId;

    private SmallOwnerInfo smallOwnerInfo;


    private String tel;

    private String villaId;

    private VillaInfo villaInfo = new VillaInfo();
    private String createTime;

    private String id;

    private String planTime;

    private String repairOrderId;

    private RepairOrderInfo repairOrderInfo = new RepairOrderInfo();

    private String state;

    private String taskCode;

    private String workerId;

    private WorkerInfo workerInfo;

    private String workerName;



    private String workerTel;

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public String getPlanTime() {
        return this.planTime;
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

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getTaskCode() {
        return this.taskCode;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getWorkerId() {
        return this.workerId;
    }

    public void setWorkerInfo(WorkerInfo workerInfo) {
        this.workerInfo = workerInfo;
    }

    public WorkerInfo getWorkerInfo() {
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
