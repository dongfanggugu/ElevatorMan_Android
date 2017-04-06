package com.honyum.elevatorMan.data;

public class AlarmNotify extends Atom {
	
	private String notifyType;  //通知类型
	
	private String alarmId;   //报警事件的id

	public String getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}

	public String getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}
	
	/**
	 * 根据json串实例化对象
	 * @param json
	 * @return
	 */
	public static AlarmNotify getAlarmNotify(String json) {
		return (AlarmNotify) parseFromJson(AlarmNotify.class, json);
	}
	
	
}