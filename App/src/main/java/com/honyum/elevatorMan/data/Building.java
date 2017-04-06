package com.honyum.elevatorMan.data;

import com.honyum.elevatorMan.utils.Utils;

import java.io.Serializable;
import java.util.List;

public class Building implements Serializable, Comparable {
	
	private String buildingCode = "一号楼";    //楼栋编号
	
	private List<Elevator> elevatorList;		//楼栋下电梯列表

    private List<UnitInfo> unitInfoList;

	public String getBuildingCode() {
		return buildingCode;
	}

	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}

	public List<Elevator> getElevatorList() {
		return elevatorList;
	}

	public void setElevatorList(List<Elevator> elevatorList) {
		this.elevatorList = elevatorList;
	}

    public List<UnitInfo> getUnitInfoList() {
        return unitInfoList;
    }

    public void setUnitInfoList(List<UnitInfo> unitInfoList) {
        this.unitInfoList = unitInfoList;
    }

    @Override
    public int compareTo(Object another) {
        Building building = (Building) another;
        String code = building.getBuildingCode();

        int thisNum = Utils.StringToInt(this.buildingCode);
        int anotherNum = Utils.StringToInt(code);

        if (thisNum != 0 && anotherNum != 0) {
            return thisNum - anotherNum;
        }
        return this.buildingCode.compareTo(code);
    }
}