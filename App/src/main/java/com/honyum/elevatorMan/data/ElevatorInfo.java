package com.honyum.elevatorMan.data;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Star on 2017/8/22.
 */

public class ElevatorInfo extends DataSupport implements Serializable{


    private String eleId;
    private String userId;
    private String projectName;
    private double x;
    private double y;
    private String signImage;
    private String useImage;
    private String portImage;

    public String getEleId() {
        return eleId;
    }

    public void setEleId(String eleId) {
        this.eleId = eleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getSignImage() {
        return signImage;
    }

    public void setSignImage(String signImage) {
        this.signImage = signImage;
    }

    public String getUseImage() {
        return useImage;
    }

    public void setUseImage(String useImage) {
        this.useImage = useImage;
    }

    public String getPortImage() {
        return portImage;
    }

    public void setPortImage(String portImage) {
        this.portImage = portImage;
    }


}
