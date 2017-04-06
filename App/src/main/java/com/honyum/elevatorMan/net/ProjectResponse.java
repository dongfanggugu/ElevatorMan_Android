package com.honyum.elevatorMan.net;

import java.util.List;

import com.honyum.elevatorMan.data.Project;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseHead;

public class ProjectResponse extends Response {

	private ResponseHead head;
	
	private List<Project> body;
	

	public ResponseHead getHead() {
		return head;
	}

	public void setHead(ResponseHead head) {
		this.head = head;
	}

	public List<Project> getBody() {
		return body;
	}

	public void setBody(List<Project> body) {
		this.body = body;
	}

	/**
	 * 根据json生成对象
	 * @param json
	 * @return
	 */
	public static ProjectResponse getProjectResponse(String json) {
		return (ProjectResponse) parseFromJson(ProjectResponse.class, json);
	}
}
