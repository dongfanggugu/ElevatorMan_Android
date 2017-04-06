package com.honyum.elevatorMan.net.base;

import com.honyum.elevatorMan.data.Atom;


public class Response extends Atom {
	private ResponseHead head;

	public ResponseHead getHead() {
		return head;
	}

	public void setHead(ResponseHead head) {
		this.head = head;
	}
	
	public static Response getResponse(String json) {
		return (Response) parseFromJson(Response.class, json);
	}
}