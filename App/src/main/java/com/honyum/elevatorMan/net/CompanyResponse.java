package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.CompanyInfo;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseHead;

import java.util.List;

/**
 * Created by changhaozhang on 16/1/8.
 */
public class CompanyResponse extends Response {

    private ResponseHead head;

    private List<CompanyInfo> body;

    @Override
    public ResponseHead getHead() {
        return head;
    }

    @Override
    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public List<CompanyInfo> getBody() {
        return body;
    }

    public void setBody(List<CompanyInfo> body) {
        this.body = body;
    }


    /**
     * 根据json生成CompanyResponse对象
     * @param json
     * @return
     */
    public static CompanyResponse getCompanyResponse(String json) {
        return (CompanyResponse) parseFromJson(CompanyResponse.class, json);
    }
}
