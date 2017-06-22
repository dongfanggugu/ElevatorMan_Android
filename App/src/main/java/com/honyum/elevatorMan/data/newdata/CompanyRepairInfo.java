package com.honyum.elevatorMan.data.newdata;

import java.io.Serializable;

public class CompanyRepairInfo implements Serializable {
private String createTime;

private String id;

private String planTime;

private String repairOrderId;

private RepairOrderInfo repairOrderInfo;

private String startTime;

private String state;

private String taskCode;

private String workerId;

private WorkerInfo workerInfo;

private String workerName;

private String workerTel;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    private String picture = "";

public void setCreateTime(String createTime){
this.createTime = createTime;
}
public String getCreateTime(){
return this.createTime;
}
public void setId(String id){
this.id = id;
}
public String getId(){
return this.id;
}
public void setPlanTime(String planTime){
this.planTime = planTime;
}
public String getPlanTime(){
return this.planTime;
}
public void setRepairOrderId(String repairOrderId){
this.repairOrderId = repairOrderId;
}
public String getRepairOrderId(){
return this.repairOrderId;
}
public void setRepairOrderInfo(RepairOrderInfo repairOrderInfo){
this.repairOrderInfo = repairOrderInfo;
}
public RepairOrderInfo getRepairOrderInfo(){
return this.repairOrderInfo;
}
public void setStartTime(String startTime){
this.startTime = startTime;
}
public String getStartTime(){
return this.startTime;
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
public void setWorkerId(String workerId){
this.workerId = workerId;
}
public String getWorkerId(){
return this.workerId;
}
public void setWorkerInfo(WorkerInfo workerInfo){
this.workerInfo = workerInfo;
}
public WorkerInfo getWorkerInfo(){
return this.workerInfo;
}
public void setWorkerName(String workerName){
this.workerName = workerName;
}
public String getWorkerName(){
return this.workerName;
}
public void setWorkerTel(String workerTel){
this.workerTel = workerTel;
}
public String getWorkerTel(){
return this.workerTel;
}

}