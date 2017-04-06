package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;

import java.io.Serializable;

public class UploadPicRequest extends RequestBean {

    private RequestHead head;

    private RequestBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public RequestBody getBody() {
        return body;
    }

    public void setBody(RequestBody body) {
        this.body = body;
    }

    public class RequestBody implements Serializable {

        /**
         * 本地图片文件Base64编码
         */
        private String pic;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
