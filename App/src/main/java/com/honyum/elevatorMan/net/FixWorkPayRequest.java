package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;

import java.util.List;

/**
 * Created by Star on 2017/6/8.
 */

public class FixWorkPayRequest extends RequestBean {

    private List<MaintenanceFixWorkPayBody> body;




    public class MaintenanceFixWorkPayBody extends RequestBody {


        public String getRepairOrderId() {
            return repairOrderId;
        }

        public void setRepairOrderId(String repairOrderId) {
            this.repairOrderId = repairOrderId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getPictures() {
            return pictures;
        }

        public void setPictures(String pictures) {
            this.pictures = pictures;
        }

        private String repairOrderId;
        private String name;
        private double price;
        private String pictures;


    }

}
