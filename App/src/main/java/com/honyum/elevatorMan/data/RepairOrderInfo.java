package com.honyum.elevatorMan.data;

import java.io.Serializable;

public class RepairOrderInfo implements Serializable {
    private String code;

    private String createTime;

    private String evaluate;

    private String id;

    private String isDelete;

    private String isPayment;

    private int payMoney;

    private String phenomenon;

    private String repairTime;

    private String repairTypeId;

    private RepairTypeInfo repairTypeInfo;

    private String smallOwnerId;

    private SmallOwnerInfo smallOwnerInfo;

    private String state;

    private String villaId;

    private VillaInfo villaInfo;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getEvaluate() {
        return this.evaluate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getIsDelete() {
        return this.isDelete;
    }

    public void setIsPayment(String isPayment) {
        this.isPayment = isPayment;
    }

    public String getIsPayment() {
        return this.isPayment;
    }

    public void setPayMoney(int payMoney) {
        this.payMoney = payMoney;
    }

    public int getPayMoney() {
        return this.payMoney;
    }

    public void setPhenomenon(String phenomenon) {
        this.phenomenon = phenomenon;
    }

    public String getPhenomenon() {
        return this.phenomenon;
    }

    public void setRepairTime(String repairTime) {
        this.repairTime = repairTime;
    }

    public String getRepairTime() {
        return this.repairTime;
    }

    public void setRepairTypeId(String repairTypeId) {
        this.repairTypeId = repairTypeId;
    }

    public String getRepairTypeId() {
        return this.repairTypeId;
    }

    public void setRepairTypeInfo(RepairTypeInfo repairTypeInfo) {
        this.repairTypeInfo = repairTypeInfo;
    }

    public RepairTypeInfo getRepairTypeInfo() {
        return this.repairTypeInfo;
    }

    public void setSmallOwnerId(String smallOwnerId) {
        this.smallOwnerId = smallOwnerId;
    }

    public String getSmallOwnerId() {
        return this.smallOwnerId;
    }

    public void setSmallOwnerInfo(SmallOwnerInfo smallOwnerInfo) {
        this.smallOwnerInfo = smallOwnerInfo;
    }

    public SmallOwnerInfo getSmallOwnerInfo() {
        return this.smallOwnerInfo;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    public void setVillaId(String villaId) {
        this.villaId = villaId;
    }

    public String getVillaId() {
        return this.villaId;
    }

    public void setVillaInfo(VillaInfo villaInfo) {
        this.villaInfo = villaInfo;
    }

    public VillaInfo getVillaInfo() {
        return this.villaInfo;
    }
}
