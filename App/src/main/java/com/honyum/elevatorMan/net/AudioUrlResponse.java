package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseBody;

public class AudioUrlResponse extends Response {

    private AudioUrlBody body;

    public AudioUrlBody getBody() {
        return body;
    }

    public void setBody(AudioUrlBody body) {
        this.body = body;
    }

    public static class AudioUrlBody extends ResponseBody {

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static AudioUrlResponse getAudioUrl(String json) {
        return (AudioUrlResponse) parseFromJson(AudioUrlResponse.class, json);
    }
}
