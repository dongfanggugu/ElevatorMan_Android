package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseBody;
import com.honyum.elevatorMan.net.base.ResponseHead;

/**
 * Created by changhaozhang on 15/11/23.
 */
public class UploadImageResponse extends Response {



    private UploadImageBody body;



    public UploadImageBody getBody() {
        return body;
    }

    public void setBody(UploadImageBody body) {
        this.body = body;
    }

    /**
     * 根据返回的json字符串生成对象
     * @param json
     * @return
     */
    public static UploadImageResponse getUploadImageResponse(String json) {
        return (UploadImageResponse) parseFromJson(UploadImageResponse.class, json);
    }

    public class UploadImageBody extends ResponseBody {
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        private String url = "";


    }
}
