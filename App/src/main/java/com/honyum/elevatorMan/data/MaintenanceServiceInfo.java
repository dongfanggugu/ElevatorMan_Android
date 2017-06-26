package com.honyum.elevatorMan.data;


import java.io.Serializable;

public class MaintenanceServiceInfo implements Serializable {

    private double allMoney;

    private String branchId;

    private BranchInfo1 branchInfo;

    private String code;

    private String createTime;

    private int discountMoney;

    private String endTime;

    private int frequency;

    private String id;

    private String ip;

    private String isPay;

    private String maintOrderId;

    private MaintOrderInfo maintOrderInfo = new MaintOrderInfo();

    private String maintUserId;

    private MaintUserInfo maintUserInfo;

    private String mainttypeId;

    private MaintTypeInfo mainttypeInfo;

    private String mainttypeName;

    private String orderid;

    private double payMoney;

    private String payTime;

    private String smallOwnerId;

    private SmallOwnerInfo smallOwnerInfo;

    private String startTime;

    private String terminal;

    private String type;

    private String villaId;

    private VillaInfo villaInfo = new VillaInfo();

    public void setAllMoney(double allMoney){
        this.allMoney = allMoney;
    }
    public double getAllMoney(){
        return this.allMoney;
    }
    public void setBranchId(String branchId){
        this.branchId = branchId;
    }
    public String getBranchId(){
        return this.branchId;
    }
    public void setBranchInfo(BranchInfo1 branchInfo){
        this.branchInfo = branchInfo;
    }
    public BranchInfo1 getBranchInfo(){
        return this.branchInfo;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setCreateTime(String createTime){
        this.createTime = createTime;
    }
    public String getCreateTime(){
        return this.createTime;
    }
    public void setDiscountMoney(int discountMoney){
        this.discountMoney = discountMoney;
    }
    public int getDiscountMoney(){
        return this.discountMoney;
    }
    public void setEndTime(String endTime){
        this.endTime = endTime;
    }
    public String getEndTime(){
        return this.endTime;
    }
    public void setFrequency(int frequency){
        this.frequency = frequency;
    }
    public int getFrequency(){
        return this.frequency;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setIp(String ip){
        this.ip = ip;
    }
    public String getIp(){
        return this.ip;
    }
    public void setIsPay(String isPay){
        this.isPay = isPay;
    }
    public String getIsPay(){
        return this.isPay;
    }
    public void setMaintOrderId(String maintOrderId){
        this.maintOrderId = maintOrderId;
    }
    public String getMaintOrderId(){
        return this.maintOrderId;
    }
    public void setMaintOrderInfo(MaintOrderInfo maintOrderInfo){
        this.maintOrderInfo = maintOrderInfo;
    }
    public MaintOrderInfo getMaintOrderInfo(){
        return this.maintOrderInfo;
    }
    public void setMaintUserId(String maintUserId){
        this.maintUserId = maintUserId;
    }
    public String getMaintUserId(){
        return this.maintUserId;
    }
    public void setMaintUserInfo(MaintUserInfo maintUserInfo){
        this.maintUserInfo = maintUserInfo;
    }
    public MaintUserInfo getMaintUserInfo(){
        return this.maintUserInfo;
    }
    public void setMainttypeId(String mainttypeId){
        this.mainttypeId = mainttypeId;
    }
    public String getMainttypeId(){
        return this.mainttypeId;
    }
    public void setMainttypeInfo(MaintTypeInfo mainttypeInfo){
        this.mainttypeInfo = mainttypeInfo;
    }
    public MaintTypeInfo getMainttypeInfo(){
        return this.mainttypeInfo;
    }
    public void setMainttypeName(String mainttypeName){
        this.mainttypeName = mainttypeName;
    }
    public String getMainttypeName(){
        return this.mainttypeName;
    }
    public void setOrderid(String orderid){
        this.orderid = orderid;
    }
    public String getOrderid(){
        return this.orderid;
    }
    public void setPayMoney(double payMoney){
        this.payMoney = payMoney;
    }
    public double getPayMoney(){
        return this.payMoney;
    }
    public void setPayTime(String payTime){
        this.payTime = payTime;
    }
    public String getPayTime(){
        return this.payTime;
    }
    public void setSmallOwnerId(String smallOwnerId){
        this.smallOwnerId = smallOwnerId;
    }
    public String getSmallOwnerId(){
        return this.smallOwnerId;
    }
    public void setSmallOwnerInfo(SmallOwnerInfo smallOwnerInfo){
        this.smallOwnerInfo = smallOwnerInfo;
    }
    public SmallOwnerInfo getSmallOwnerInfo(){
        return this.smallOwnerInfo;
    }
    public void setStartTime(String startTime){
        this.startTime = startTime;
    }
    public String getStartTime(){
        return this.startTime;
    }
    public void setTerminal(String terminal){
        this.terminal = terminal;
    }
    public String getTerminal(){
        return this.terminal;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setVillaId(String villaId){
        this.villaId = villaId;
    }
    public String getVillaId(){
        return this.villaId;
    }
    public void setVillaInfo(VillaInfo villaInfo){
        this.villaInfo = villaInfo;
    }
    public VillaInfo getVillaInfo(){
        return this.villaInfo;
    }
}