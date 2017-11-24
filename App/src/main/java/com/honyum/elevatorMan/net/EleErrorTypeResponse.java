package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.Response;

/**
 * Created by changhaozhang on 15/11/23.
 */
public class EleErrorTypeResponse extends Response {


    private String[] body;


    /**
     * 根据返回的json字符串生成对象
     *
     * @param json
     * @return
     */
    public static EleErrorTypeResponse getEleErrorTypeResponse(String json) {
        return (EleErrorTypeResponse) parseFromJson(EleErrorTypeResponse.class, json);
    }

    public String[] getBody() {
        return body;
    }

    public void setBody(String[] body) {
        this.body = body;
    }

}
