package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * Created by changhaozhang on 15/11/17.
 */
public class InfoModifyRequest extends RequestBean {

    private RequestHead head;

    private InfoModifyReqBody body;

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public void setBody(InfoModifyReqBody body) {
        this.body = body;
    }

    public RequestHead getHead() {
        return head;
    }

    public InfoModifyReqBody getBody() {
        return body;
    }

    public class InfoModifyReqBody extends RequestBody {

        private String name;

        private int age;

        private int sex;

        private String operationCard;

        private String tel;

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public void setOperationCard(String operationCard) {
            this.operationCard = operationCard;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public int getSex() {
            return sex;
        }

        public String getOperationCard() {
            return operationCard;
        }

        public String getTel() {
            return tel;
        }
    }
}
