package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

/**
 * Created by Star on 2017/6/26.
 */

public class RegisterCompanyRequest extends RequestBean {

    public RegisterCompanyBody getBody() {
        return body;
    }

    public void setBody(RegisterCompanyBody body) {
        this.body = body;
    }

    private  RegisterCompanyBody body;

    public class RegisterCompanyBody extends RequestBody{
        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        private String companyName;
        private String tel;
        private String name;
        private String password;


    }
}
