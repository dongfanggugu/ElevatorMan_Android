package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseHead;

public class ReportStateResponse extends Response {

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
	public static ReportStateResponse getReportStateRsp(String json) {
		return (ReportStateResponse) parseFromJson(ReportStateResponse.class, json);
	}
}
