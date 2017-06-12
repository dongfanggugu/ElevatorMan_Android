package com.honyum.elevatorMan.data;

import java.io.Serializable;

public class VillaInfo implements Serializable {
    private String address;

    private String brand;

    private String cellName;

    private String contacts;

    private String contactsTel;

    private String createTime;

    private String id;

    private String isDelete;

    private double lat;

    private int layerAmount;

    private double lng;

    private int weight;

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public String getCellName() {
        return this.cellName;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContacts() {
        return this.contacts;
    }

    public void setContactsTel(String contactsTel) {
        this.contactsTel = contactsTel;
    }

    public String getContactsTel() {
        return this.contactsTel;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return this.createTime;
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

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLayerAmount(int layerAmount) {
        this.layerAmount = layerAmount;
    }

    public int getLayerAmount() {
        return this.layerAmount;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLng() {
        return this.lng;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return this.weight;
    }
}