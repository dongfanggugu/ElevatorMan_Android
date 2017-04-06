package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseBody;
import com.honyum.elevatorMan.net.base.ResponseHead;

/**
 * Created by changhaozhang on 15/11/23.
 */
public class UploadIconResponse extends Response {

    private ResponseHead head;

    private UploadIconRspBody body;

    @Override
    public ResponseHead getHead() {
        return head;
    }

    @Override
    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public UploadIconRspBody getBody() {
        return body;
    }

    public void setBody(UploadIconRspBody body) {
        this.body = body;
    }

    /**
     * 根据返回的json字符串生成对象
     * @param json
     * @return
     */
    public static UploadIconResponse getUploadIconResponse(String json) {
        return (UploadIconResponse) parseFromJson(UploadIconResponse.class, json);
    }

    public class UploadIconRspBody extends ResponseBody {
        private String pic = "";

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
