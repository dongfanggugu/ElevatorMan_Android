package com.honyum.elevatorMan.data;

import java.io.Serializable;

public class MaintOrderInfo implements Serializable
{
    private String code;

    private String createTime;

    private int frequency;

    private String id;

    private String isDelete;

    private String isPay;

    private MaintypeInfo maintypeInfo;

    private String ownerId;

    private OwnerInfo ownerInfo = new OwnerInfo();

    private int price;

    private String villaId;

    private VillaInfo villaInfo;

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
    public void setIsDelete(String isDelete){
        this.isDelete = isDelete;
    }
    public String getIsDelete(){
        return this.isDelete;
    }
    public void setIsPay(String isPay){
        this.isPay = isPay;
    }
    public String getIsPay(){
        return this.isPay;
    }
    public void setMaintypeInfo(MaintypeInfo maintypeInfo){
        this.maintypeInfo = maintypeInfo;
    }
    public MaintypeInfo getMaintypeInfo(){
        return this.maintypeInfo;
    }
    public void setOwnerId(String ownerId){
        this.ownerId = ownerId;
    }
    public String getOwnerId(){
        return this.ownerId;
    }
    public void setOwnerInfo(OwnerInfo ownerInfo){
        this.ownerInfo = ownerInfo;
    }
    public OwnerInfo getOwnerInfo(){
        return this.ownerInfo;
    }
    public void setPrice(int price){
        this.price = price;
    }
    public int getPrice(){
        return this.price;
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
