package com.honyum.elevatorMan.data.mydata;

import java.io.Serializable;

/**
 * Created by Star on 2017/6/20.
 */

public class NHorderAndTask implements Serializable {
    private double allMoney;

    private String branchId;

    private BranchInfo branchInfo;

    private String code;


    private int discountMoney;

    private int frequency;


    private String ip;


    private String maintOrderId;


    private String mainttypeId;

    private MainttypeInfo mainttypeInfo;

    private String mainttypeName;

    private String orderid;

    private double payMoney;

    private String smallOwnerId;

    private SmallOwnerInfo smallOwnerInfo;

    private String terminal;

    private String type;

    private String villaId;

    private VillaInfo villaInfo = new VillaInfo();


    private String createTime;

    private String id;

    private String isPay;

    private MaintOrderInfo maintOrderInfo;

    private String maintUserId;

    private MaintUserInfo maintUserInfo;

    private String planTime;

    private String state;

    private String taskCode;

    public double getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(double allMoney) {
        this.allMoney = allMoney;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(int discountMoney) {
        this.discountMoney = discountMoney;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMaintOrderId() {
        return maintOrderId;
    }

    public void setMaintOrderId(String maintOrderId) {
        this.maintOrderId = maintOrderId;
    }

    public String getMainttypeId() {
        return mainttypeId;
    }

    public void setMainttypeId(String mainttypeId) {
        this.mainttypeId = mainttypeId;
    }

    public MainttypeInfo getMainttypeInfo() {
        return mainttypeInfo;
    }

    public void setMainttypeInfo(MainttypeInfo mainttypeInfo) {
        this.mainttypeInfo = mainttypeInfo;
    }

    public String getMainttypeName() {
        return mainttypeName;
    }

    public void setMainttypeName(String mainttypeName) {
        this.mainttypeName = mainttypeName;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
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

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public MaintOrderInfo getMaintOrderInfo() {
        return maintOrderInfo;
    }

    public void setMaintOrderInfo(MaintOrderInfo maintOrderInfo) {
        this.maintOrderInfo = maintOrderInfo;
    }

    public String getMaintUserId() {
        return maintUserId;
    }

    public void setMaintUserId(String maintUserId) {
        this.maintUserId = maintUserId;
    }

    public MaintUserInfo getMaintUserInfo() {
        return maintUserInfo;
    }

    public void setMaintUserInfo(MaintUserInfo maintUserInfo) {
        this.maintUserInfo = maintUserInfo;
    }

    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }
}
