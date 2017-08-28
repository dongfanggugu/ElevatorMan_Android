package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseBody;
import com.honyum.elevatorMan.net.base.ResponseHead;

/**
 * Created by Star on 2017/8/11.
 */

public class PersonResponse extends Response {

    private ResponseHead head;

    private PersonCountBody body;


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
    public static PersonResponse getPersonResponse(String json) {
        return (PersonResponse) parseFromJson(PersonResponse.class, json);
    }

    public PersonCountBody getBody() {
        return body;
    }

    public void setBody(PersonCountBody body) {
        this.body = body;
    }

    public class PersonCountBody extends ResponseBody {
        private String count;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
