package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * 物业报警
 * @author changhaozhang
 *
 */
public class ReportAlarmRequest extends RequestBean {
	
	private RequestHead head ;
	
	private ReportAlarmReqBody body;
	
	

	public RequestHead getHead() {
		return head;
	}



	public void setHead(RequestHead head) {
		this.head = head;
	}



	public ReportAlarmReqBody getBody() {
		return body;
	}



	public void setBody(ReportAlarmReqBody body) {
		this.body = body;
	}



	public class ReportAlarmReqBody extends RequestBody {
		
		private String liftId;	//电梯id
		
		private String isInjure;	//人伤情况
		
		private String remark;	//其他描述

		public String getLiftId() {
			return liftId;
		}

		public void setLiftId(String liftId) {
			this.liftId = liftId;
		}

		public String getIsInjure() {
			return isInjure;
		}

		public void setIsInjure(String isInjure) {
			this.isInjure = isInjure;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}
		
		
	} 
}
