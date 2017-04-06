package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseHead;

public class ReportLocationResponse extends Response {

	private ResponseHead head;

	public ResponseHead getHead() {
		return head;
	}

	public void setHead(ResponseHead head) {
		this.head = head;
	}
	
	
	/**
	 * 根据json返回对象
	 * @param json
	 * @return
	 */
	public static ReportLocationResponse getReportLocation(String json) {
		
		return (ReportLocationResponse) parseFromJson(ReportLocationResponse.class, json);
	}
}
