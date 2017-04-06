package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.net.base.RequestHead;

/**
 * Created by changhaozhang on 16/6/22.
 */
public class HomeReportRequest extends RequestBean {

    private RequestHead head;

    private HomeReportReqBody body;

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public HomeReportReqBody getBody() {
        return body;
    }

    public void setBody(HomeReportReqBody body) {
        this.body = body;
    }

    public class HomeReportReqBody extends RequestBody {

        private String family_address = "";

        private String family_province = "北京市";

        private String family_city = "北京市";

        private String family_county = "";

        private double family_lng;

        private double family_lat;

        public String getFamily_address() {
            return family_address;
        }

        public void setFamily_address(String family_address) {
            this.family_address = family_address;
        }

        public String getFamily_province() {
            return family_province;
        }

        public void setFamily_province(String family_province) {
            this.family_province = family_province;
        }

        public String getFamily_city() {
            return family_city;
        }

        public void setFamily_city(String family_city) {
            this.family_city = family_city;
        }

        public String getFamily_county() {
            return family_county;
        }

        public void setFamily_county(String family_county) {
            this.family_county = family_county;
        }

        public double getFamily_lng() {
            return family_lng;
        }

        public void setFamily_lng(double family_lng) {
            this.family_lng = family_lng;
        }

        public double getFamily_lat() {
            return family_lat;
        }

        public void setFamily_lat(double family_lat) {
            this.family_lat = family_lat;
        }
    }
}
