package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.BranchInfo;
import com.honyum.elevatorMan.data.WorkerInfo2;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;

/**
 * Created by Star on 2017/6/15.
 */

public class GetWorkerResponse extends Response {
    public List<WorkerInfo2> getBody() {
        return body;
    }

    public void setBody(List<WorkerInfo2> body) {
        this.body = body;
    }

    private List<WorkerInfo2> body;
    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static GetWorkerResponse getWorkerResponse(String json) {
        return (GetWorkerResponse) parseFromJson(GetWorkerResponse.class, json);
    }
}
