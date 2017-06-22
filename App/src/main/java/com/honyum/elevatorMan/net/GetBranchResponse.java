package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.BranchInfo;
import com.honyum.elevatorMan.net.base.Response;

import java.util.List;

/**
 * Created by Star on 2017/6/15.
 */

public class GetBranchResponse extends Response {
    public List<BranchInfo> getBody() {
        return body;
    }

    public void setBody(List<BranchInfo> body) {
        this.body = body;
    }

    private List<BranchInfo> body;
    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static GetBranchResponse getGetBranchResponse(String json) {
        return (GetBranchResponse) parseFromJson(GetBranchResponse.class, json);
    }
}
