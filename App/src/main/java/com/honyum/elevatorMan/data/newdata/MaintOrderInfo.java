package com.honyum.elevatorMan.data.newdata;

import java.io.Serializable;

public class MaintOrderInfo implements Serializable {
private String code;

private String createTime;

private int frequency;

private String id;

private String isDelete;

private String isPay;

private double price;

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
public void setPrice(double price){
this.price = price;
}
public double getPrice(){
return this.price;
}

}