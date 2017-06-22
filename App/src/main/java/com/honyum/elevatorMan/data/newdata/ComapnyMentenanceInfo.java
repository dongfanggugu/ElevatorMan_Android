package com.honyum.elevatorMan.data.newdata;

import java.io.Serializable;

public class ComapnyMentenanceInfo implements Serializable {
    private double allMoney;

    private String branchId;

    private BranchInfo branchInfo;

    private String code;

    private String createTime;

    private int discountMoney;

    private int frequency;

    private String id;

    private String ip;

    private String taskCode;

    private String isPay;

    private String maintOrderId;

    private MaintOrderInfo maintOrderInfo;

    private String mainttypeId;

    private MaintUserInfo maintUserInfo;

    private String maintUserId;

    private MainttypeInfo mainttypeInfo;

    private String mainttypeName;

    private String state;


    private String orderid;

    private String planTime;

    private double payMoney;

    private String smallOwnerId;

    private SmallOwnerInfo smallOwnerInfo;

    private String terminal;

    private String type;

    private String villaId;

    private VillaInfo villaInfo;

    private String startTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    private String endTime;


    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public MaintUserInfo getMaintUserInfo() {
        return maintUserInfo;
    }

    public void setMaintUserInfo(MaintUserInfo maintUserInfo) {
        this.maintUserInfo = maintUserInfo;
    }

    public String getMaintUserId() {
        return maintUserId;
    }

    public void setMaintUserId(String maintUserId) {
        this.maintUserId = maintUserId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public void setAllMoney(double allMoney) {
        this.allMoney = allMoney;
    }

    public double getAllMoney() {
        return this.allMoney;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public void setBranchInfo(BranchInfo branchInfo) {
        this.branchInfo = branchInfo;
    }

    public BranchInfo getBranchInfo() {
        return this.branchInfo;
    }

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

    public void setDiscountMoney(int discountMoney) {
        this.discountMoney = discountMoney;
    }

    public int getDiscountMoney() {
        return this.discountMoney;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public String getIsPay() {
        return this.isPay;
    }

    public void setMaintOrderId(String maintOrderId) {
        this.maintOrderId = maintOrderId;
    }

    public String getMaintOrderId() {
        return this.maintOrderId;
    }

    public void setMaintOrderInfo(MaintOrderInfo maintOrderInfo) {
        this.maintOrderInfo = maintOrderInfo;
    }

    public MaintOrderInfo getMaintOrderInfo() {
        return this.maintOrderInfo;
    }

    public void setMainttypeId(String mainttypeId) {
        this.mainttypeId = mainttypeId;
    }

    public String getMainttypeId() {
        return this.mainttypeId;
    }

    public void setMainttypeInfo(MainttypeInfo mainttypeInfo) {
        this.mainttypeInfo = mainttypeInfo;
    }

    public MainttypeInfo getMainttypeInfo() {
        return this.mainttypeInfo;
    }

    public void setMainttypeName(String mainttypeName) {
        this.mainttypeName = mainttypeName;
    }

    public String getMainttypeName() {
        return this.mainttypeName;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrderid() {
        return this.orderid;
    }

    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
    }

    public double getPayMoney() {
        return this.payMoney;
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

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getTerminal() {
        return this.terminal;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
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