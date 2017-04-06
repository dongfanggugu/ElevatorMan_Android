package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseBody;
import com.honyum.elevatorMan.net.base.ResponseHead;

/**
 * Created by chang on 2016/3/3.
 */
public class LiftVideoResponse extends Response {

    private ResponseHead head;

    private LiftVideoRspBody body;

    @Override
    public ResponseHead getHead() {
        return head;
    }

    @Override
    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public LiftVideoRspBody getBody() {
        return body;
    }

    public void setBody(LiftVideoRspBody body) {
        this.body = body;
    }


    /**
     * 根据返回的json字符串生成对象
     * @param json
     * @return
     */
    public static LiftVideoResponse getLiftVideoRsp(String json) {
        return (LiftVideoResponse) parseFromJson(LiftVideoResponse.class, json);
    }


    public class LiftVideoRspBody extends ResponseBody {

        private String bitStream = "1";

        private String channel = "0";

        private String deviceName = "";

        private String devicePort = "";

        private String dvrUserName = "";

        private String dvrPwd = "";

        private String id = "";

        private String protocol = "";

        private String serverIp = "";

        private String serverPort = "";

        private String streamName = "";

        private String url = "";

        public String getBitStream() {
            return bitStream;
        }

        public void setBitStream(String bitStream) {
            this.bitStream = bitStream;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDevicePort() {
            return devicePort;
        }

        public void setDevicePort(String devicePort) {
            this.devicePort = devicePort;
        }

        public String getDvrUserName() {
            return dvrUserName;
        }

        public void setDvrUserName(String dvrUserName) {
            this.dvrUserName = dvrUserName;
        }

        public String getDvrPwd() {
            return dvrPwd;
        }

        public void setDvrPwd(String dvrPwd) {
            this.dvrPwd = dvrPwd;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getServerIp() {
            return serverIp;
        }

        public void setServerIp(String serverIp) {
            this.serverIp = serverIp;
        }

        public String getServerPort() {
            return serverPort;
        }

        public void setServerPort(String serverPort) {
            this.serverPort = serverPort;
        }

        public String getStreamName() {
            return streamName;
        }

        public void setStreamName(String streamName) {
            this.streamName = streamName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
