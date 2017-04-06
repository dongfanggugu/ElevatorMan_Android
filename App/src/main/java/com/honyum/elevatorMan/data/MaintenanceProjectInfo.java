package com.honyum.elevatorMan.data;

import com.honyum.elevatorMan.net.base.ResponseBody;

public class MaintenanceProjectInfo extends ResponseBody {

    private String address;
    private String areaManager;
    private String areaTel;
    private String branchName;
    private String description;
    private String id;
    private String isDelete;
    private double lat;
    private double lng;
    private String name;
    private String projectManagera;
    private String projectManagerb;
    private String projectTela;
    private String projectTelb;
    private String repairManager;
    private String repairTel;
    private String brand;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaManager() {
        return areaManager;
    }

    public void setAreaManager(String areaManager) {
        this.areaManager = areaManager;
    }

    public String getAreaTel() {
        return areaTel;
    }

    public void setAreaTel(String areaTel) {
        this.areaTel = areaTel;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectManagera() {
        return projectManagera;
    }

    public void setProjectManagera(String projectManagera) {
        this.projectManagera = projectManagera;
    }

    public String getProjectManagerb() {
        return projectManagerb;
    }

    public void setProjectManagerb(String projectManagerb) {
        this.projectManagerb = projectManagerb;
    }

    public String getProjectTela() {
        return projectTela;
    }

    public void setProjectTela(String projectTela) {
        this.projectTela = projectTela;
    }

    public String getProjectTelb() {
        return projectTelb;
    }

    public void setProjectTelb(String projectTelb) {
        this.projectTelb = projectTelb;
    }

    public String getRepairManager() {
        return repairManager;
    }

    public void setRepairManager(String repairManager) {
        this.repairManager = repairManager;
    }

    public String getRepairTel() {
        return repairTel;
    }

    public void setRepairTel(String repairTel) {
        this.repairTel = repairTel;
    }
}
