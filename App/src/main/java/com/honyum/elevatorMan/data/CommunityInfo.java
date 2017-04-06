package com.honyum.elevatorMan.data;

/**
 * 报警地址信息
 * @author chang
 *
 */
public class CommunityInfo {

	private String address;
	
	private String id;
	
	private String lat;		//纬度
	
	private String lng;		//经度
	
	private String name;

	private String propertyUtel;

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

    public String getPropertyUtel() {
        return propertyUtel;
    }

    public void setPropertyUtel(String propertyUtel) {
        this.propertyUtel = propertyUtel;
    }
}
