package com.honyum.elevatorMan.data.newdata1;

import java.io.Serializable;

public class RepairTypeInfo implements Serializable {
private String content;

private String createTime;

private String id;

private String name;

private int seq;

public void setContent(String content){
this.content = content;
}
public String getContent(){
return this.content;
}
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
public void setName(String name){
this.name = name;
}
public String getName(){
return this.name;
}
public void setSeq(int seq){
this.seq = seq;
}
public int getSeq(){
return this.seq;
}

}