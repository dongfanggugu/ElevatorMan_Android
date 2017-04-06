package com.honyum.elevatorMan.net;

import java.util.List;

import com.honyum.elevatorMan.data.AlarmInfo;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseBody;
import com.honyum.elevatorMan.net.base.ResponseHead;

/**
 * 报警事件列表
 * @author changhaozhang
 *
 */
public class AlarmListResponse extends Response {
	
	private ResponseHead head;
	
	private List<AlarmInfo> body;
	
	

	public ResponseHead getHead() {
		return head;
	}



	public void setHead(ResponseHead head) {
		this.head = head;
	}
	
	public List<AlarmInfo> getBody() {
		return body;
	}



	public void setBody(List<AlarmInfo> body) {
		this.body = body;
	}



	/**
	 * 根据json生成对象
	 * @param json
	 * @return
	 */
	public static AlarmListResponse getAlarmListResponse(String json) {
		return (AlarmListResponse) parseFromJson(AlarmListResponse.class, json);
	}



	public class AlarmListRspBody extends ResponseBody {
		
		private List<AlarmInfo> alarmInfoList;

		public List<AlarmInfo> getAlarmInfoList() {
			return alarmInfoList;
		}

		public void setAlarmInfoList(List<AlarmInfo> alarmInfoList) {
			this.alarmInfoList = alarmInfoList;
		}
	}
}
