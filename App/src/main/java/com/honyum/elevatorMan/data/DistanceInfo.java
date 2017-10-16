package com.honyum.elevatorMan.data;

import java.io.Serializable;

/**
 * Created by Star on 2017/9/1.
 */

public class DistanceInfo implements Serializable {

    private String reachDistance;

    public String getReachDistance() {
        return reachDistance;
    }

    public void setReachDistance(String reachDistance) {
        this.reachDistance = reachDistance;
    }
}
