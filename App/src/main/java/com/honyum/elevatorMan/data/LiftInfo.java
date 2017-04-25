package com.honyum.elevatorMan.data;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.utils.Utils;

import java.io.Serializable;

/**
 * Created by chang on 2015/9/17.
 */
public class LiftInfo implements Serializable {

    private String id = "00226207-14ec-4cce-ae77-c7542693f930";

    private String num = "12345678901234567890";

    private String address = "北京市望京soho塔2";

    private String lastMainTime = "";

    //维保类型，hm:半月保 m:月保 s:季度保 hy:半年保 y:年保
    private String lastMainType = "";

    private String planMainTime = "";

    private String planMainType = "";

    private String workerCompany = "中建华宇";

    private String workerName = "worker";

    private String communityName = "";

    private String buildingCode = "";

    private String unitCode = "2";

    private String workerTel = "";

    private String propertyFlg = "";

    private String mainId = "";

    private String mainType = "";

    private String mainTime = "";

    //维修工签名
    private String workerAutograph = "";

    //物业签名
    private String propertyAutograph = "";

    //维保完成时间
    private String postTime = "";

    //物业确认时间 
    private String propertyFinishedTime = "";

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    private String brand = "";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLastMainTime() {
        return lastMainTime;
    }

    public void setLastMainTime(String lastMainTime) {
        this.lastMainTime = lastMainTime;
    }

    public String getLastMainType() {
        return lastMainType;
    }

    public void setLastMainType(String lastMainType) {
        this.lastMainType = lastMainType;
    }

    public String getPlanMainTime() {
        return planMainTime;
    }

    public void setPlanMainTime(String planMainTime) {
        this.planMainTime = planMainTime;
    }

    public String getPlanMainType() {
        return planMainType;
    }

    public void setPlanMainType(String planMainType) {
        this.planMainType = planMainType;
    }

    public String getWorkerCompany() {
        return workerCompany;
    }

    public void setWorkerCompany(String workerCompany) {
        this.workerCompany = workerCompany;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getWorkerTel() {
        return workerTel;
    }

    public void setWorkerTel(String workerTel) {
        this.workerTel = workerTel;
    }

    public String getPropertyFlg() {
        return propertyFlg;
    }

    public void setPropertyFlg(String propertyFlg) {
        this.propertyFlg = propertyFlg;
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public String getMainType() {
        return mainType;
    }

    public void setMainType(String mainType) {
        this.mainType = mainType;
    }

    public String getMainTime() {
        return mainTime;
    }

    public void setMainTime(String mainTime) {
        this.mainTime = mainTime;
    }

    /**
     * 是否已经制定计划
     * @return
     */
    public boolean hasPlan() {
        if (StringUtils.isEmpty(planMainTime)) {
            return false;
        }
        return true;
    }

    /**
     * 根据维保类型代码返回汉字描述
     * @param type
     * @return
     */
    public static String typeToString(String type) {
        String result = "";
        if (type.equals("hm")) {
            result = "半月保";
        } else if (type.equals("m")) {
            result = "月保";
        } else if (type.equals("s")) {
            result = "季度保";
        } else if (type.equals("hy")) {
            result = "半年保";
        } else if (type.equals("y")) {
            result = "年保";
        }
        return  result;
    }

    /**
     * 根据维保的汉字描述返回维保类型
     * @param string
     * @return
     */
    public static String stringToType(String string) {
        String type = "";
        if (string.equals("半月保")) {
            type = "hm";
        } else if (string.equals("月保")) {
            type = "m";
        } else if (string.equals("季度保")) {
            type = "s";
        } else if (string.equals("半年保")) {
            type = "hy";
        } else if (string.equals("年保")) {
            type = "y";
        }

        return type;
    }

    /**
     * 获取上次维保的汉字描述
     * @return
     */
    public String getLastType() {
        return typeToString(lastMainType);
    }

    /**
     * 获取计划维保类型的汉字描述
     * @return
     */
    public String getPlanType() {
        return typeToString(planMainType);
    }

    public String getMainTypeString() {
        return typeToString(mainType);
    }

    public String getWorkerAutograph() {
        return workerAutograph;
    }

    public void setWorkerAutograph(String workerAutograph) {
        this.workerAutograph = workerAutograph;
    }

    public String getPropertyAutograph() {
        return propertyAutograph;
    }

    public void setPropertyAutograph(String propertyAutograph) {
        this.propertyAutograph = propertyAutograph;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getPropertyFinishedTime() {
        return propertyFinishedTime;
    }

    public void setPropertyFinishedTime(String propertyFinishedTime) {
        this.propertyFinishedTime = propertyFinishedTime;
    }
}
