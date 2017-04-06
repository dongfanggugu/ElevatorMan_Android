package com.honyum.elevatorMan.data;

public class Employee {
	private String id = "0000";
	private String name = "李志";
	private String telephone = "13212321112";
	private String address = "";
	private String info = "高级技工";

	private double latitude = 39.915168;
	private double longitude = 116.403875;
	private boolean responsible = false;
	private boolean center = false;
	
	private double distance = 0.0;
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public boolean isCenter() {
		return center;
	}

	public void setCenter(boolean center) {
		this.center = center;
	}

	public boolean isResponsible() {
		return responsible;
	}

	public void setResponsible(boolean responsible) {
		this.responsible = responsible;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	private void dataGenerator() {
		int random = (int) (Math.random() * 50);
		this.address = String.valueOf(random);

		double baseLat = 39.915168;
		double baseLng = 116.403875;

		this.latitude = baseLat + (Math.random() / 10);
		this.longitude = baseLng + (Math.random() / 10);
		this.id = String.valueOf(random);
	}

	/**
	 * 构造函数
	 */
	public Employee() {
		dataGenerator();
	}

	public Employee(double lat, double lng, boolean center, String address) {
		this.latitude = lat;
		this.longitude = lng;
		this.center = center;
		this.address = address;
	}	

}