package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBody;

public class AcceptAlarmReqBody extends RequestBody {

	private String alarmId;
	
	private String distance;

	public String getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	
}
