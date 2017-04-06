package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

import java.util.List;

/**
 * Created by chang on 2015/10/26.
 */
public class RegisterRequest extends RequestBean {

    private RequestHead head;

    private RegisterReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public RegisterReqBody getBody() {
        return body;
    }

    public void setBody(RegisterReqBody body) {
        this.body = body;
    }

    public class RegisterReqBody extends RequestBody {

        private String loginname = "";

        private String password = "";


        private String name = "";

        private int sex = 0;   //0:女 1:男

        private int age = 0;


        private String operationCard = "";  //操作证号码

        private String tel = "";    //手机号码

        private String branchName = ""; //公司名称

        private String branchId = "";

        private String operateState = "1";  //请求的标志 0:检测用户名是否唯一   1:注册

        private List<String> pics;

        private String city;

        private String idNumber;


        public String getLoginname() {
            return loginname;
        }

        public void setLoginname(String loginname) {
            this.loginname = loginname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getOperationCard() {
            return operationCard;
        }

        public void setOperationCard(String operationCard) {
            this.operationCard = operationCard;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getOperateState() {
            return operateState;
        }

        public void setOperateState(String operateState) {
            this.operateState = operateState;
        }

        public List<String> getPics() {
            return pics;
        }

        public void setPics(List<String> pics) {
            this.pics = pics;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }
    }
}
