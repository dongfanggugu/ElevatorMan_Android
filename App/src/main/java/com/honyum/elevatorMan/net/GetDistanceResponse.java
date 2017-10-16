package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseBody;

public class GetDistanceResponse extends Response {

    private GetDistanceBody body;

    public GetDistanceBody getBody() {
        return body;
    }

    public void setBody(GetDistanceBody body) {
        this.body = body;
    }

    public class GetDistanceBody extends ResponseBody {


        private long reachDistance;


        public long getReachDistance() {
            return reachDistance;
        }

        public void setReachDistance(long reachDistance) {
            this.reachDistance = reachDistance;
        }
    }

    public static GetDistanceResponse getGetDistance(String json) {
        return (GetDistanceResponse) parseFromJson(GetDistanceResponse.class, json);
    }
}
