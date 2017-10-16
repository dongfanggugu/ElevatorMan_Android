package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;

/**
 * Created by Star on 2017/10/16.
 */

public class SmsCodeRequest extends RequestBean {


    private SmsCodeRequestBody body;

    public SmsCodeRequestBody getBody() {
        return body;
    }

    public void setBody(SmsCodeRequestBody body) {
        this.body = body;
    }

    public class SmsCodeRequestBody {
        private String tel;

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }


}
