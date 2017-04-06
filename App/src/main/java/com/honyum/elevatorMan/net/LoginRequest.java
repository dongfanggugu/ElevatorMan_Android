/**
 * 登录请求
 */
package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

public class LoginRequest extends RequestBean {

    private RequestHead head;

    private LoginReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public LoginReqBody getBody() {
        return body;
    }

    public void setBody(LoginReqBody body) {
        this.body = body;
    }

    public class LoginReqBody extends RequestBody {

        private String userName;

        private String password;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}