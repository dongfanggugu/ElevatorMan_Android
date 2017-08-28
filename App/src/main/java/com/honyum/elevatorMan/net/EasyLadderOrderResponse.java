package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.AlarmInfo;
import com.honyum.elevatorMan.data.CommunityInfo;
import com.honyum.elevatorMan.data.EasyLadderOrderInfo;
import com.honyum.elevatorMan.data.Elevator;
import com.honyum.elevatorMan.data.MaintRecInfo;
import com.honyum.elevatorMan.data.WorkerInfo;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseBody;
import com.honyum.elevatorMan.net.base.ResponseHead;

import java.util.List;

public class EasyLadderOrderResponse extends Response {

	public List<EasyLadderOrderInfo> getBody() {
		return body;
	}

	public void setBody(List<EasyLadderOrderInfo> body) {
		this.body = body;
	}

	private List<EasyLadderOrderInfo> body;


	/**
	 * 根据json生成对象
	 * @param json
	 * @return
	 */
	public static EasyLadderOrderResponse getEasyLadderOrderResponse(String json) {
		return (EasyLadderOrderResponse) parseFromJson(EasyLadderOrderResponse.class, json);
	}
}
