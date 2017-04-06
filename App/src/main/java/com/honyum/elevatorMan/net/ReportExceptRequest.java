package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

public class ReportExceptRequest extends RequestBean {

	private RequestHead head;

	private ReportExceptReqBody body;

	public RequestHead getHead() {
		return head;
	}

	public void setHead(RequestHead head) {
		this.head = head;
	}

	public ReportExceptReqBody getBody() {
		return body;
	}

	public void setBody(ReportExceptReqBody body) {
		this.body = body;
	}

	/**
	 * 请求 body
	 * 
	 * @author changhaozhang
	 * 
	 */
	public class ReportExceptReqBody extends RequestBody {

		private String alarmId;

		private String remark;

		public String getAlarmId() {
			return alarmId;
		}

		public void setAlarmId(String alarmId) {
			this.alarmId = alarmId;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

	}

}
