package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseBody;
import com.honyum.elevatorMan.net.base.ResponseHead;

public class ReportAlarmResponse extends Response {

	private ResponseHead head;
	
	private ReportAlarmRspBody body;
	
	
	
	public ResponseHead getHead() {
		return head;
	}



	public void setHead(ResponseHead head) {
		this.head = head;
	}



	public ReportAlarmRspBody getBody() {
		return body;
	}



	public void setBody(ReportAlarmRspBody body) {
		this.body = body;
	}


	/**
	 * 根据json生成对象
	 * @param json
	 * @return
	 */
	public static ReportAlarmResponse getReportAlarmResponse(String json) {
		return (ReportAlarmResponse) parseFromJson(ReportAlarmResponse.class, json);
	}



	public class ReportAlarmRspBody extends ResponseBody {
		
		private String id;	//返回的报警事件的id

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		
	}
}
