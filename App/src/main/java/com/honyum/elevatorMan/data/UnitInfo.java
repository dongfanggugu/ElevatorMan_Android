package com.honyum.elevatorMan.data;

import com.honyum.elevatorMan.utils.Utils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chang on 2015/10/30.
 */
public class UnitInfo implements Serializable , Comparable {

    private String unit;

    private List<Elevator> elevatorList;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<Elevator> getElevatorList() {
        return elevatorList;
    }

    public void setElevatorList(List<Elevator> elevatorList) {
        this.elevatorList = elevatorList;
    }

    @Override
    public int compareTo(Object another) {
        UnitInfo unitInfo = (UnitInfo) another;
        String unit = unitInfo.getUnit();

        int thisNum = Utils.StringToInt(this.unit);
        int anotherNum = Utils.StringToInt(unit);

        if (thisNum != 0 && anotherNum != 0) {
            return thisNum - anotherNum;
        }
        return this.unit.compareTo(unit);
    }
}
