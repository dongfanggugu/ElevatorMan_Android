package com.honyum.elevatorMan.data;

import java.io.Serializable;

/**
 * 维修任务信息bean
 */

public class MaintenanceTaskInfo implements Serializable {
    private String confirmTime;

    private String createTime;

    private String finishTime;

    private String id;

    private String isPay;

    private MaintOrderInfo maintOrderInfo;

    private String maintUserFeedback;

    private String maintUserId;

    private MaintUserInfo maintUserInfo;

    private String planTime;

    private String state;

    private String taskCode;

    public void setConfirmTime(String confirmTime){
        this.confirmTime = confirmTime;
    }
    public String getConfirmTime(){
        return this.confirmTime;
    }
    public void setCreateTime(String createTime){
        this.createTime = createTime;
    }
    public String getCreateTime(){
        return this.createTime;
    }
    public void setFinishTime(String finishTime){
        this.finishTime = finishTime;
    }
    public String getFinishTime(){
        return this.finishTime;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setIsPay(String isPay){
        this.isPay = isPay;
    }
    public String getIsPay(){
        return this.isPay;
    }
    public void setMaintOrderInfo(MaintOrderInfo maintOrderInfo){
        this.maintOrderInfo = maintOrderInfo;
    }
    public MaintOrderInfo getMaintOrderInfo(){
        return this.maintOrderInfo;
    }
    public void setMaintUserFeedback(String maintUserFeedback){
        this.maintUserFeedback = maintUserFeedback;
    }
    public String getMaintUserFeedback(){
        return this.maintUserFeedback;
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
    public void setPlanTime(String planTime){
        this.planTime = planTime;
    }
    public String getPlanTime(){
        return this.planTime;
    }
    public void setState(String state){
        this.state = state;
    }
    public String getState(){
        return this.state;
    }
    public void setTaskCode(String taskCode){
        this.taskCode = taskCode;
    }
    public String getTaskCode(){
        return this.taskCode;
    }





}
