package com.honyum.elevatorMan.data.newdata;

import java.io.Serializable;

public class MainttypeInfo implements Serializable {
private String content;

private String id;

private String name;

private double price;

public void setContent(String content){
this.content = content;
}
public String getContent(){
return this.content;
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
public void setPrice(double price){
this.price = price;
}
public double getPrice(){
return this.price;
}

}