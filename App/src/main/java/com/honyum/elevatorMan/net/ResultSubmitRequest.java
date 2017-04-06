package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * 提交救援结果报表
 * @author changhaozhang
 *
 */
public class ResultSubmitRequest extends RequestBean {

	private RequestHead head;

	private ResultSubmitReqBody body;

	public RequestHead getHead() {
		return head;
	}

	public void setHead(RequestHead head) {
		this.head = head;
	}

	public ResultSubmitReqBody getBody() {
		return body;
	}

	public void setBody(ResultSubmitReqBody body) {
		this.body = body;
	}

	public class ResultSubmitReqBody extends RequestBody {

		private String alarmId;

		private String savedCount;

		private String injuredCount;

		private String other;

		public String getAlarmId() {
			return alarmId;
		}

		public void setAlarmId(String alarmId) {
			this.alarmId = alarmId;
		}

		public String getSavedCount() {
			return savedCount;
		}

		public void setSavedCount(String savedCount) {
			this.savedCount = savedCount;
		}

		public String getInjuredCount() {
			return injuredCount;
		}

		public void setInjuredCount(String injuredCount) {
			this.injuredCount = injuredCount;
		}

		public String getOther() {
			return other;
		}

		public void setOther(String other) {
			this.other = other;
		}

	}
}
