package com.honyum.elevatorMan.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class Project implements Serializable {

	private String address = "昌平区天通苑"; // 项目地址

	private String id = "000000"; // 项目id

	private String lat = "40.072634"; // 项目纬度

	private String lng = "116.431238"; // 项目经度

	private String name = "天通苑六区"; // 项目名称

	private List<Building> buildingList; // 楼栋列表

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Building> getBuildingList() {
		return buildingList;
	}

	public void setBuildingList(List<Building> buildingList) {
		this.buildingList = buildingList;
	}

}