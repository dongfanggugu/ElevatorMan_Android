package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * Created by changhaozhang on 15/11/23.
 */
public class UploadIconRequest extends RequestBean {

    private RequestHead head;

    private UploadIconReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public UploadIconReqBody getBody() {
        return body;
    }

    public void setBody(UploadIconReqBody body) {
        this.body = body;
    }

    public class UploadIconReqBody extends RequestBody {

        private String pic;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
