package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.WorkerInfo;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseHead;

import java.io.Serializable;
import java.util.List;

public class WorkNameResponse extends Response {

	private ResponseHead head;

	private List<WorkNameResponseBody> body;

	public ResponseHead getHead() {
		return head;
	}

	public void setHead(ResponseHead head) {
		this.head = head;
	}

	public List<WorkNameResponseBody> getBody() {
		return body;
	}

	public void setBody(List<WorkNameResponseBody> body) {
		this.body = body;
	}

	public static class WorkNameResponseBody
	{
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	/**
	 * 根据json生成对象
	 * 
	 * @param json
	 * @return
	 */
	public static WorkNameResponse getResult(String json) {
		return (WorkNameResponse) parseFromJson(WorkNameResponse.class,
				json);
	}
}
