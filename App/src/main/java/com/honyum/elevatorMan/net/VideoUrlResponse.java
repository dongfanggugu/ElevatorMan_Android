package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseBody;

public class VideoUrlResponse extends Response {

    private VideoUrlBody body;

    public VideoUrlBody getBody() {
        return body;
    }

    public void setBody(VideoUrlBody body) {
        this.body = body;
    }

    public static class VideoUrlBody extends ResponseBody {

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        private String url;


    }

    public static VideoUrlResponse getVideoUrl(String json) {
        return (VideoUrlResponse) parseFromJson(VideoUrlResponse.class, json);
    }
}
