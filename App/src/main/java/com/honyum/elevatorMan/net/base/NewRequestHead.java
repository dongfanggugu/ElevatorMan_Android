package com.honyum.elevatorMan.net.base;

/**
 * Created by Star on 2017/6/9.   包装RequestHead 变成链式
 */

public class NewRequestHead extends RequestHead{



    public NewRequestHead setuserId(String userId) {
        super.setUserId(userId);
        return this;
    }

    public NewRequestHead setaccessToken(String accessToken) {
        super.setAccessToken(accessToken);
        return this;
    }



}
