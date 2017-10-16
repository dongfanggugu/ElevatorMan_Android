package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.Response;

/**
 * Created by Star on 2017/10/16.
 */

public class SmsCodeResponse extends Response {


    private SmsCodeResponseBody body;

    public SmsCodeResponseBody getBody() {
        return body;
    }

    public void setBody(SmsCodeResponseBody body) {
        this.body = body;
    }

    public class SmsCodeResponseBody
    {
       private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static SmsCodeResponse getResponse(String json) {
        return (SmsCodeResponse) parseFromJson(SmsCodeResponse.class, json);
    }
}
