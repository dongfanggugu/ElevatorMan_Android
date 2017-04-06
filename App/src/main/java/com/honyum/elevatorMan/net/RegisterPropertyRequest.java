package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

public class RegisterPropertyRequest extends RequestBean {

    private RegisterProReqBody body;

    public RegisterProReqBody getBody() {
        return body;
    }

    public void setBody(RegisterProReqBody body) {
        this.body = body;
    }

    public class RegisterProReqBody extends RequestBody {

        private String name;

        private String manager;

        private String tel;

        private String address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getManager() {
            return manager;
        }

        public void setManager(String manager) {
            this.manager = manager;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
