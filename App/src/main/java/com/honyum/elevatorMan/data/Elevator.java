package com.honyum.elevatorMan.data;

import java.io.Serializable;

/**
 * 电梯数据
 *
 * @author chang
 */
public class Elevator implements Serializable {

    private String id = "0000";     //电梯id，用于报警

    private String unitCode = "1";     //单元号: unitCode + "单元"

    private String liftNum = "ABC1102";       //电梯唯一编号

    private String savedCount = "0";

    private String injureCount = "0";

    private String buildingCode = "0";            //楼号: buildingCode + "楼"

    private String brand = "0";            //品牌

    private String elevatorType = "0";            //梯型

    private String number = "0";            //救援码

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getElevatorType() {
        return elevatorType;
    }

    public void setElevatorType(String elevatorType) {
        this.elevatorType = elevatorType;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getSavedCount() {
        return savedCount;
    }

    public void setSavedCount(String savedCount) {
        this.savedCount = savedCount;
    }

    public String getInjureCount() {
        return injureCount;
    }

    public void setInjureCount(String injureCount) {
        this.injureCount = injureCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getLiftNum() {
        return liftNum;
    }

    public void setLiftNum(String liftNum) {
        this.liftNum = liftNum;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}