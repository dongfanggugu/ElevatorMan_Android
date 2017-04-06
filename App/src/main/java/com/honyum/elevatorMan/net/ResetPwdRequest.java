package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * Created by chang on 2016/1/13.
 */
public class ResetPwdRequest extends RequestBean {
    private RequestHead head;

    private ResetPwdReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public ResetPwdReqBody getBody() {
        return body;
    }

    public void setBody(ResetPwdReqBody body) {
        this.body = body;
    }

    public class ResetPwdReqBody extends RequestBody {

        private String tel = "";

        private String userName = "";

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
