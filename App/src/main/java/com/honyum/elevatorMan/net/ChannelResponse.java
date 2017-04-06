package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.ChannelInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;

/**
 * Created by changhaozhang on 2017/4/6.
 */
public class ChannelResponse extends Response {

    private List<ChannelInfo> body;

    public List<ChannelInfo> getBody() {
        return body;
    }

    public void setBody(List<ChannelInfo> body) {
        this.body = body;
    }

    public static ChannelResponse getChannelResponse(String json) {
        return (ChannelResponse) parseFromJson(ChannelResponse.class, json);
    }
}
