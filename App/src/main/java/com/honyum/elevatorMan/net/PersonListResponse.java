package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.data.PersonListInfo;
import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseHead;

import java.util.List;

/**
 * Created by Star on 2017/8/11.
 */

public class PersonListResponse extends Response {

    private ResponseHead head;

    private List<PersonListInfo> body;


    public ResponseHead getHead() {
        return head;
    }

    public void setHead(ResponseHead head) {
        this.head = head;
    }



    /**
     * 根据json生成对象
     * @param json
     * @return
     */
    public static PersonListResponse getPersonListResponse(String json) {
        return (PersonListResponse) parseFromJson(PersonListResponse.class, json);
    }

    public List<PersonListInfo> getBody() {
        return body;
    }

    public void setBody(List<PersonListInfo> body) {
        this.body = body;
    }
}
