package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseHead;

public class AcceptAlarmResponse extends Response {

	private ResponseHead head;

	public ResponseHead getHead() {
		return head;
	}

	public void setHead(ResponseHead head) {
		this.head = head;
	}
	
	/**
	 * 根据json生成对象
	 * @param json
	 * @return
	 */
	public AcceptAlarmResponse getAcceptAlarmRsp(String json) {
		return (AcceptAlarmResponse) parseFromJson(AcceptAlarmResponse.class, json);
	}
}
