package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

public class ReportLocationRequest extends RequestBean {

	private RequestHead head;

	private ReportLocationReqBody body;

	public RequestHead getHead() {
		return head;
	}

	public void setHead(RequestHead head) {
		this.head = head;
	}

	public ReportLocationReqBody getBody() {
		return body;
	}

	public void setBody(ReportLocationReqBody body) {
		this.body = body;
	}

	public class ReportLocationReqBody extends RequestBody {

		private double lat; // 纬度

		private double lng; // 精度

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}

	}

}
