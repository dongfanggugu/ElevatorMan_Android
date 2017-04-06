package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.Point;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseHead;

import java.util.List;

/**
 * Created by changhaozhang on 15/8/18.
 */
public class WorkersResponse extends Response {

    private ResponseHead head;

    private List<Point> body;



    @Override
    public ResponseHead getHead() {
        return head;
    }

    @Override
    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public List<Point> getBody() {
        return body;
    }

    public void setBody(List<Point> body) {
        this.body = body;
    }

    /**
     * 根据json字符串返回对象ß
     * @param json
     * @return
     */
    public static WorkersResponse getWorkersResponse(String json) {
        return (WorkersResponse) parseFromJson(WorkersResponse.class, json);
    }
}
