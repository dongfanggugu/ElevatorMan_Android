package com.honyum.elevatorMan.data;

import java.io.Serializable;

/**
 * Created by Star on 2017/6/13.
 */

public class FixComponent implements Serializable {

    public String getRepairOrderId() {
        return repairOrderId;
    }

    public FixComponent setRepairOrderId(String repairOrderId) {
        this.repairOrderId = repairOrderId;
        return this;
    }

    public String getName() {
        return name;
    }

    public FixComponent setName(String name) {
        this.name = name;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public FixComponent setPrice(double price) {
        this.price = price;
        return this;
    }

    private String repairOrderId;
    private String name;
    private double price;

}
