package com.honyum.elevatorMan.net;

import java.util.List;

import com.honyum.elevatorMan.data.WorkerInfo;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseHead;

public class AlarmStateResponse extends Response {

	private ResponseHead head;

	private List<WorkerInfo> body;

	public ResponseHead getHead() {
		return head;
	}

	public void setHead(ResponseHead head) {
		this.head = head;
	}

	public List<WorkerInfo> getBody() {
		return body;
	}

	public void setBody(List<WorkerInfo> body) {
		this.body = body;
	}

	/**
	 * 根据json生成对象
	 * 
	 * @param json
	 * @return
	 */
	public static AlarmStateResponse getAlarmState(String json) {
		return (AlarmStateResponse) parseFromJson(AlarmStateResponse.class,
				json);
	}
}
