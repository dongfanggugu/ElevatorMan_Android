package com.honyum.elevatorMan.net.base;

import java.io.Serializable;

public class ResponseHead implements Serializable {

	private String rspCode;    //0:成功   -1:失败    2:token验证错误，被其他设备登陆
	
	private String rspMsg;	//返回的错误信息
	
	private String version;    //服务器应用的最新版本
	
	private String accessToken;		//登陆token

	public String getRspCode() {
		return rspCode;
	}

	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}

	public String getRspMsg() {
		return rspMsg;
	}

	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	
	
}