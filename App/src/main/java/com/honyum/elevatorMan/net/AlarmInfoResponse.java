package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.AlarmInfo;
import com.honyum.elevatorMan.data.CommunityInfo;
import com.honyum.elevatorMan.data.Elevator;
import com.honyum.elevatorMan.data.WorkerInfo;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseBody;
import com.honyum.elevatorMan.net.base.ResponseHead;

public class AlarmInfoResponse extends Response {

	private ResponseHead head;
	
	private AlarmInfo body;

	public ResponseHead getHead() {
		return head;
	}

	public void setHead(ResponseHead head) {
		this.head = head;
	}

	public AlarmInfo getBody() {
		return body;
	}

	public void setBody(AlarmInfo body) {
		this.body = body;
	}
	
	/**
	 * 根据json串获取对象
	 * @param json
	 * @return
	 */
	public static AlarmInfoResponse getAlarmInfoRsp(String json) {
		return (AlarmInfoResponse) parseFromJson(AlarmInfoResponse.class, json);
	}
	
	public class AlarmInfoRspBody extends ResponseBody {

		private String alarmTime = "2015-06-05 18:07:10";

		private WorkerInfo alarmUserInfo;

		private CommunityInfo communityInfo;

		private Elevator elevatorInfo;

		private String id;

		private String remark;

		private String state; // 救援状态 1：已出发 2:已到达 3.已完成 4.拒绝

		public String getAlarmTime() {
			return alarmTime;
		}

		public void setAlarmTime(String alarmTime) {
			this.alarmTime = alarmTime;
		}

		public WorkerInfo getAlarmUserInfo() {
			return alarmUserInfo;
		}

		public void setAlarmUserInfo(WorkerInfo alarmUserInfo) {
			this.alarmUserInfo = alarmUserInfo;
		}

		public CommunityInfo getCommunityInfo() {
			return communityInfo;
		}

		public void setCommunityInfo(CommunityInfo communityInfo) {
			this.communityInfo = communityInfo;
		}

		public Elevator getElevatorInfo() {
			return elevatorInfo;
		}

		public void setElevatorInfo(Elevator elevatorInfo) {
			this.elevatorInfo = elevatorInfo;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

	}
}
