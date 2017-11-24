package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseBody;

public class PicUrlResponse extends Response {

    private AudioUrlBody body;

    public AudioUrlBody getBody() {
        return body;
    }

    public void setBody(AudioUrlBody body) {
        this.body = body;
    }

    public static class AudioUrlBody extends ResponseBody {

        private String pic;

        public String getUrl() {
            return pic;
        }

        public void setUrl(String url) {
            this.pic = url;
        }
    }

    public static PicUrlResponse getPicUrl(String json) {
        return (PicUrlResponse) parseFromJson(PicUrlResponse.class, json);
    }
}
