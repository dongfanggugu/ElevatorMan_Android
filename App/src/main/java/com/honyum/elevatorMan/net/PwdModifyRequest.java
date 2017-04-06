package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * Created by changhaozhang on 15/11/18.
 */
public class PwdModifyRequest extends RequestBean {

    private RequestHead head;

    private PwdModifyReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public PwdModifyReqBody getBody() {
        return body;
    }

    public void setBody(PwdModifyReqBody body) {
        this.body = body;
    }

    public class PwdModifyReqBody extends RequestBody {

        private String oldPwd;

        private String newPwd;

        public String getOldPwd() {
            return oldPwd;
        }

        public void setOldPwd(String oldPwd) {
            this.oldPwd = oldPwd;
        }

        public String getNewPwd() {
            return newPwd;
        }

        public void setNewPwd(String newPwd) {
            this.newPwd = newPwd;
        }
    }
}
